/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telefonica.euro_iaas.sdc.dao.ArtifactDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.installator.Installator;
import com.telefonica.euro_iaas.sdc.installator.impl.BaseInstallableInstanceManagerChef;
import com.telefonica.euro_iaas.sdc.manager.ArtifactManager;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ArtifactSearchCriteria;
import com.telefonica.euro_iaas.sdc.validation.ProductInstanceValidator;
import com.telefonica.fiware.commons.dao.AlreadyExistsEntityException;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.xmlsolutions.annotation.Requirement;
import com.xmlsolutions.annotation.UseCase;

/**
 * Implements ProductManager using Chef to do that.
 * 
 * @author Sergio Arroyo
 */
@UseCase(traceTo = "UC_001", status = "implemented")
@Requirement(traceTo = "BR001", status = "implemented")
public class ArtifactManagerChefImpl extends BaseInstallableInstanceManagerChef implements ArtifactManager {

    private ProductInstanceDao productInstanceDao;
    private ArtifactDao artifactDao;

    private ProductInstanceValidator validator;
    private Installator installator;

    private static Logger log = LoggerFactory.getLogger(ArtifactManagerChefImpl.class);

    /**
     * {@inheritDoc}
     * 
     * @throws InstallatorException
     */

    public ProductInstance deployArtifact(ProductInstance productInstance, Artifact artifact, String token)
            throws NodeExecutionException, FSMViolationException, InstallatorException {
        log.debug("Deploy artifact " + artifact.getName() + " in product " + productInstance.getName());
        Status previousStatus = productInstance.getStatus();
        try {
            validator.validateDeployArtifact(productInstance);
            log.debug("Validated productInsntace");
            productInstance.setStatus(Status.DEPLOYING_ARTEFACT);

            // VM vm = productInstance.getVm();

            installator.callService(productInstance, productInstance.getVm(), artifact.getAttributes(),
                    DEPLOY_ARTIFACT, token);

            log.debug("Artifact " + artifact.getName() + " installed");
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

        } catch (InstallatorException e) {
            log.error(e.getMessage());
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (RuntimeException e) { // by runtime restore the previous state
            log.error(e.getMessage());
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (NodeExecutionException e) {
            log.error(e.getMessage());
            restoreInstance(Status.ERROR, productInstance);
            throw e;
        } catch (AlreadyExistsEntityException e) {
            log.error(e.getMessage());
            throw new SdcRuntimeException(e);
        }

    }

    public ProductInstance undeployArtifact(ProductInstance productInstance, String artifactName, String token)
            throws NodeExecutionException, FSMViolationException, InstallatorException {
        log.debug("UNDeploy artifact " + artifactName + " in product " + productInstance.getName());
        Status previousStatus = productInstance.getStatus();
        try {
            validator.validateDeployArtifact(productInstance);

            productInstance.setStatus(Status.UNDEPLOYING_ARTIFACT);
            Artifact artifact = getArtefactToUninstall(productInstance, artifactName);

            VM vm = productInstance.getVm();

            installator.callService(productInstance, productInstance.getVm(), artifact.getAttributes(),
                    UNDEPLOY_ARTIFACT, token);
            log.debug("UNDeployed artifact " + artifactName + " in product " + productInstance.getName());

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

        } catch (InstallatorException e) {
            log.error(e.getMessage());
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (RuntimeException e) { // by runtime restore the previous state
            // restore the status
            log.error(e.getMessage());
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (NodeExecutionException e) {
            log.error(e.getMessage());
            restoreInstance(Status.ERROR, productInstance);
            throw e;
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
        return productInstanceDao.update(productInstance);
    }

    /**
     * Creates or find the product instance in installation operation.
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

    public void setInstallator(Installator installator) {
        this.installator = installator;
    }

}
