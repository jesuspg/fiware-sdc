package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationIncompatibleException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationInstalledException;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.IpToVM;
import com.telefonica.euro_iaas.sdc.validation.ProductInstanceValidator;
import com.xmlsolutions.annotation.Requirement;
import com.xmlsolutions.annotation.UseCase;

/**
 * Implements ProductManager using Chef to do that.
 *
 * @author Sergio Arroyo
 *
 */
@UseCase(traceTo="UC_001", status="implemented")
@Requirement(traceTo="BR001", status="implemented")
public class ProductInstanceManagerChefImpl extends
        BaseInstallableInstanceManager implements ProductInstanceManager {

    private ProductInstanceDao productInstanceDao;
    private IpToVM ip2vm;
    private ProductInstanceValidator validator;




    /**
     * {@inheritDoc}
     */
    @UseCase(traceTo="UC_001.1", status="implemented")
    @Override
    public ProductInstance install(VM vm, String vdc, ProductRelease product,
            List<Attribute> attributes) throws NodeExecutionException,
            AlreadyInstalledException {
        Status previousStatus = null;
        ProductInstance instance = null;
        try {
            // we need the hostname + domain so if we haven't that information,
            // shall to get it.
            if (!vm.canWorkWithChef()) {
                vm = ip2vm.getVm(vm.getIp());
            }
            //makes the validations
            instance = getProductToInstall(product, vm, vdc);
            previousStatus = instance.getStatus();
            //now we have the productInstance so can validate the operation
            validator.validateInstall(instance);
            instance.setStatus(Status.INSTALLING);

            if (instance.getId() != null) {
                instance = productInstanceDao.update(instance);
            } else {
                instance = productInstanceDao.create(instance);
            }

            String installRecipe = recipeNamingGenerator
                    .getInstallRecipe(instance);
            callChef(product.getProduct().getName(), installRecipe, vm,
                    attributes);
            instance.setStatus(Status.INSTALLED);
            return productInstanceDao.update(instance);

        } catch (CanNotCallChefException sce) {
            restoreInstance(previousStatus, instance);
            throw new SdcRuntimeException(sce);
        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(e);
        } catch (AlreadyExistsEntityException e) {
            throw new SdcRuntimeException(e);
        } catch (RuntimeException e) {
            //by default restore the previous state when a runtime is thrown
            restoreInstance(previousStatus, instance);
            throw new SdcRuntimeException(e);
        } catch (NodeExecutionException e) {
            restoreInstance(Status.ERROR, instance);
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     * @throws FSMViolationException
     * @throws ApplicationInstalledException
     */
    @UseCase(traceTo="UC_001.2", status="implemented")
    @Override
    public void uninstall(ProductInstance productInstance)
        throws NodeExecutionException,
        ApplicationInstalledException, FSMViolationException {
        Status previousStatus = productInstance.getStatus();
        try {
            validator.validateUninstall(productInstance);
            productInstance.setStatus(Status.UNINSTALLING);
            productInstance = productInstanceDao.update(productInstance);

            // at least has one
            String uninstallRecipe = recipeNamingGenerator
                    .getUninstallRecipe(productInstance);
            callChef(uninstallRecipe, productInstance.getVm());

            productInstance.setStatus(Status.UNINSTALLED);
            productInstanceDao.update(productInstance);
        } catch (CanNotCallChefException e) {
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(e);
        } catch (RuntimeException e) {
            //by default restore the previous state when a runtime is thrown
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (NodeExecutionException e) {
            restoreInstance(Status.ERROR, productInstance);
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance configure(ProductInstance productInstance,
            List<Attribute> configuration) throws NodeExecutionException,
            FSMViolationException {
        Status previousStatus = productInstance.getStatus();
        try {
            validator.validateConfigure(productInstance);
            productInstance.setStatus(Status.CONFIGURING);
            productInstance = productInstanceDao.update(productInstance);

            String recipe = recipeNamingGenerator.getInstallRecipe(
                    productInstance);
                callChef(productInstance.getProduct().getProduct().getName(),
                        recipe, productInstance.getVm(), configuration);
            productInstance.setStatus(Status.INSTALLED);
            return productInstanceDao.update(productInstance);

        } catch (CanNotCallChefException e) {
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (RuntimeException e) { //by runtime restore the previous state
            //restore the status
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
    @UseCase(traceTo="UC_001.4", status="implemented")
    @Override
    public ProductInstance upgrade(ProductInstance productInstance,
            ProductRelease productRelease) throws NotTransitableException,
            NodeExecutionException, FSMViolationException,
            ApplicationIncompatibleException {
        Status previousStatus = productInstance.getStatus();
        try {
            validator.validateUpdate(productInstance, productRelease);
            //update the status
            productInstance.setStatus(Status.UPGRADING);
            productInstance =  productInstanceDao.update(productInstance);

            VM vm = productInstance.getVm();

            String backupRecipe = recipeNamingGenerator
                    .getBackupRecipe(productInstance);
            callChef(backupRecipe, vm);

            String uninstallRecipe = recipeNamingGenerator
                    .getUninstallRecipe(productInstance);
            callChef(uninstallRecipe, vm);

            productInstance.setProduct(productRelease);

            String installRecipe = recipeNamingGenerator
                    .getInstallRecipe(productInstance);
            callChef(installRecipe, vm);

            String restoreRecipe = recipeNamingGenerator
                    .getRestoreRecipe(productInstance);
            callChef(restoreRecipe, vm);

            productInstance.setStatus(Status.INSTALLED);
            return productInstanceDao.update(productInstance);

        } catch (CanNotCallChefException sce) {
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(sce);
        } catch (InvalidEntityException e) {
            //don't restore the status because this exception is storing the
            //product in database so it will fail anyway
            throw new SdcRuntimeException(e);
        } catch (RuntimeException e) { //by runtime restore the previous state
            //restore the status
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (NodeExecutionException e) {
            restoreInstance(Status.ERROR, productInstance);
            throw e;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductInstance> findAll() {
        return productInstanceDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance load(String vdc, Long id)
            throws EntityNotFoundException {
        ProductInstance instance = productInstanceDao.load(id);
        if (!instance.getVdc().equals(vdc)) {
            throw new EntityNotFoundException(ProductInstance.class, "vdc", vdc);
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductInstance> findByCriteria(
            ProductInstanceSearchCriteria criteria) {
        return productInstanceDao.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance loadByCriteria(ProductInstanceSearchCriteria criteria)
            throws EntityNotFoundException, NotUniqueResultException {
        List<ProductInstance> products = productInstanceDao
                .findByCriteria(criteria);
        if (products.size() == 0) {
            throw new EntityNotFoundException(ProductInstance.class,
                    "searchCriteria", criteria.toString());
        } else if (products.size() > 1) {
            throw new NotUniqueResultException();
        }
        return products.get(0);
    }

    @Override
    public ProductInstance update(ProductInstance productInstance) {
        try {
            return productInstanceDao.update(productInstance);
        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(e);
        }
    }


    ////////// PRIVATE METHODS ///////////
    /**
     * Go to previous state when a runtime exception is thrown in any method which
     * can change the status of the product instance.
     * @param previousStatus the previous status
     * @param instance the product instance
     * @return the instance.
     */
    private ProductInstance restoreInstance(Status previousStatus,
            ProductInstance instance) {
        instance.setStatus(previousStatus);
        return update(instance);
    }

    /**
     * Creates or find the product instance in installation operation.
     * @param product
     * @param vm
     * @return
     */
    private ProductInstance getProductToInstall(ProductRelease product, VM vm,
            String vdc) {
        ProductInstance instance;
        try {
            ProductInstanceSearchCriteria criteria =
                    new ProductInstanceSearchCriteria();
            criteria.setVm(vm);
            criteria.setProductName(product.getProduct().getName());
            instance = productInstanceDao.findUniqueByCriteria(criteria);
            instance.setProduct(product);
        } catch (NotUniqueResultException e) {
            instance = new ProductInstance(product,
                    Status.UNINSTALLED, vm, vdc);
        }
        return instance;
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
     * @param ip2vm
     *            the ip2vm to set
     */
    public void setIp2vm(IpToVM ip2vm) {
        this.ip2vm = ip2vm;
    }

    /**
     * @param validator the validator to set
     */
    public void setValidator(ProductInstanceValidator validator) {
        this.validator = validator;
    }

}
