package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationIncompatibleException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationInstalledException;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
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
import com.xmlsolutions.annotation.UseCase;

/**
 * Implements ProductManager using Chef to do that.
 *
 * @author Sergio Arroyo
 *
 */
@UseCase(traceTo="UC_001", status="implemented")
public class ProductInstanceManagerChefImpl extends
        BaseInstallableInstanceManager implements ProductInstanceManager {

    private ProductInstanceDao productInstanceDao;
    private IpToVM ip2vm;
    private ProductInstanceValidator validator;

    private static Logger LOGGER = Logger.getLogger("ProductManagerChefImpl");

    /**
     * {@inheritDoc}
     */
    @UseCase(traceTo="UC_001.4", status="implemented")
    @Override
    public ProductInstance upgrade(ProductInstance productInstance,
            ProductRelease productRelease) throws NotTransitableException,
            NodeExecutionException, FSMViolationException,
            ApplicationIncompatibleException {
        try {
            validator.validateUpdate(productInstance, productRelease);
            VM vm = productInstance.getVM();

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

            return productInstanceDao.update(productInstance);

        } catch (CanNotCallChefException sce) {
            LOGGER.log(Level.SEVERE, sce.getMessage());
            throw new SdcRuntimeException(sce);
        } catch (InvalidEntityException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance configure(ProductInstance productInstance,
            List<Attribute> configuration) throws NodeExecutionException,
            FSMViolationException {

        validator.validateConfigure(productInstance);
        String recipe = recipeNamingGenerator.getInstallRecipe(
                productInstance);
        try {
            callChef(productInstance.getProduct().getProduct().getName(),
                    recipe, productInstance.getVM(), configuration);
        } catch (CanNotCallChefException e) {
            throw new SdcRuntimeException(e);
        }
        return productInstance;
    }

    /**
     * {@inheritDoc}
     */
    @UseCase(traceTo="UC_001.1", status="implemented")
    @Override
    public ProductInstance install(VM vm, ProductRelease product,
            List<Attribute> attributes) throws NodeExecutionException,
            AlreadyInstalledException {
        try {
            // we need the hostname + domain so if we haven't that information,
            // shall to get it.
            if (!vm.canWorkWithChef()) {
                vm = ip2vm.getVm(vm.getIp());
            }
            //makes the validations
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
                        Status.INSTALLED, vm);
            }

            //now we have the productInstance so can validate the operation
            validator.validateInstall(instance);
            instance.setStatus(Status.INSTALLED);
            String installRecipe = recipeNamingGenerator
                    .getInstallRecipe(instance);
            callChef(product.getProduct().getName(), installRecipe, vm,
                    attributes);
            if (instance.getId() != null) {
                instance = productInstanceDao.update(instance);
            } else {
                instance = productInstanceDao.create(instance);
            }
            return instance;

        } catch (CanNotCallChefException sce) {
            LOGGER.log(Level.SEVERE, sce.getMessage());
            throw new SdcRuntimeException(sce);
        } catch (InvalidEntityException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new SdcRuntimeException(e);
        } catch (AlreadyExistsEntityException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new SdcRuntimeException(e);
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
        validator.validateUninstall(productInstance);
        // at least has one
        String uninstallRecipe = recipeNamingGenerator
                .getUninstallRecipe(productInstance);
        try {
            callChef(uninstallRecipe, productInstance.getVM());

            productInstance.setStatus(Status.UNINSTALLED);
            productInstanceDao.update(productInstance);
        } catch (CanNotCallChefException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Can not uninstall prodcut due to a unexpected error: "
                            + e.getMessage());
            throw new SdcRuntimeException(e);
        } catch (InvalidEntityException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Can not update the ProductInstance due to: "
                            + e.getMessage());
            throw new SdcRuntimeException(e);
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
    public ProductInstance load(Long id) throws EntityNotFoundException {
        return productInstanceDao.load(id);
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
