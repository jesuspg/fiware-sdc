package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ArtifactDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ArtifactManager;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ArtifactSearchCriteria;
import com.telefonica.euro_iaas.sdc.validation.ProductInstanceValidator;
import com.xmlsolutions.annotation.Requirement;
import com.xmlsolutions.annotation.UseCase;

/**
 * Implements ProductManager using Chef to do that.
 * 
 * @author Sergio Arroyo
 */
@UseCase(traceTo = "UC_001", status = "implemented")
@Requirement(traceTo = "BR001", status = "implemented")
public class ArtifactManagerChefImpl extends BaseInstallableInstanceManager implements ArtifactManager {

    private ProductInstanceDao productInstanceDao;
    private ArtifactDao artifactDao;
    // private ProductDao productDao;
    // private IpToVM ip2vm;
    private ProductInstanceValidator validator;

    /**
     * {@inheritDoc}
     */

    public ProductInstance deployArtifact(ProductInstance productInstance, Artifact artifact)
            throws NodeExecutionException, FSMViolationException {
        Status previousStatus = productInstance.getStatus();
        try {
            validator.validateDeployArtifact(productInstance);

            productInstance.setStatus(Status.DEPLOYING_ARTEFACT);

            VM vm = productInstance.getVm();

            String recipe = recipeNamingGenerator.getDeployArtifactRecipe(productInstance);
            callChef(productInstance.getProductRelease().getProduct().getName(), recipe, productInstance.getVm(),
                    artifact.getAttributes());

            productInstance.setStatus(Status.INSTALLED);

            // artifact.setProductInstance(productInstance);
            try {
                artifact = artifactDao.load(artifact.getName());
            } catch (EntityNotFoundException e1) {
                artifact = artifactDao.create(new Artifact(artifact.getName(), productInstance.getVdc(),
                        productInstance, artifact.getAttributes()));
            }
            productInstance.addArtifact(artifact);
            productInstance = productInstanceDao.update(productInstance);

            return productInstance;

        } catch (CanNotCallChefException e) {
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (RuntimeException e) { // by runtime restore the previous state
            // restore the status
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (NodeExecutionException e) {
            restoreInstance(Status.ERROR, productInstance);
            throw e;
        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(e);
        } catch (AlreadyExistsEntityException e) {
            throw new SdcRuntimeException(e);
        }

    }

    public ProductInstance undeployArtifact(ProductInstance productInstance, String artifactName)
            throws NodeExecutionException, FSMViolationException {
        Status previousStatus = productInstance.getStatus();
        try {
            validator.validateDeployArtifact(productInstance);

            productInstance.setStatus(Status.UNDEPLOYING_ARTIFACT);
            Artifact artifact = getArtefactToUninstall(productInstance, artifactName);

            VM vm = productInstance.getVm();

            String recipe = recipeNamingGenerator.getUnDeployArtifactRecipe(productInstance);
            callChef(productInstance.getProductRelease().getProduct().getName(), recipe, productInstance.getVm(),
                    artifact.getAttributes());

            productInstance.setStatus(Status.INSTALLED);

            productInstance.deleteArtifact(artifact);

            // artifact = artifactDao.load(artifact.getName());
            try {
                artifact = artifactDao.load(artifact.getName());
            } catch (EntityNotFoundException e1) {

            }

            artifactDao.remove(artifact);
            productInstance = productInstanceDao.update(productInstance);
            return productInstance;

        } catch (CanNotCallChefException e) {
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (RuntimeException e) { // by runtime restore the previous state
            // restore the status
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (NodeExecutionException e) {
            restoreInstance(Status.ERROR, productInstance);
            throw e;
        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(e);
        }

    }

    /**
     * {@inheritDoc}
     */

    public List<Artifact> findAll() {
        return artifactDao.findAll();
    }

    public Artifact load(String vdc, String productInstance, String name) throws EntityNotFoundException {
        Artifact instance = artifactDao.load(name);
        if (!instance.getVdc().equals(vdc)) {
            throw new EntityNotFoundException(ProductInstance.class, "vdc", vdc);
        }
        return instance;
    }

    public List<Artifact> findByCriteria(ArtifactSearchCriteria criteria) {
        return artifactDao.findByCriteria(criteria);
    }

    public Artifact loadByCriteria(ArtifactSearchCriteria criteria) throws EntityNotFoundException,
            NotUniqueResultException {
        List<Artifact> artifact = artifactDao.findByCriteria(criteria);
        if (artifact.size() == 0) {
            throw new EntityNotFoundException(Artifact.class, "searchCriteria", criteria.toString());
        } else if (artifact.size() > 1) {
            throw new NotUniqueResultException();
        }
        return artifact.get(0);
    }

    // //////// PRIVATE METHODS ///////////
    /**
     * Go to previous state when a runtime exception is thrown in any method which can change the status of the product
     * instance.
     * 
     * @param previousStatus
     *            the previous status
     * @param instance
     *            the product instance
     * @return the instance.
     */
    private ProductInstance restoreInstance(Status previousStatus, ProductInstance instance) {
        instance.setStatus(previousStatus);
        return update(instance);
    }

    public ProductInstance update(ProductInstance productInstance) {
        try {
            return productInstanceDao.update(productInstance);
        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * Creates or find the product instance in installation operation.
     * 
     * @param product
     * @param vm
     * @return
     */
    private Artifact getArtefactToUninstall(ProductInstance productInstance, String artifactName) {
        for (Artifact artifact : productInstance.getArtifacts()) {
            if (artifact.getName().equals(artifactName))
                return artifact;
        }
        return null;
    }

    // //////////// I.O.C /////////////
    /**
     * @param productInstanceDao
     *            the productInstanceDao to set
     */
    public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }

    /**
     * @param artifactDao
     *            the artifactDao to set
     */
    public void setArtifactDao(ArtifactDao artifactDao) {
        this.artifactDao = artifactDao;
    }

    /**
     * @param validator
     *            the validator to set
     */
    public void setValidator(ProductInstanceValidator validator) {
        this.validator = validator;
    }

}
