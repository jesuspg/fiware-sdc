package com.telefonica.euro_iaas.sdc.manager.impl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.IpToVM;

/**
 * Implements ProductManager using Chef to do that.
 *
 * @author Sergio Arroyo
 *
 */
public class ProductInstanceManagerChefImpl extends
        BaseInstallableInstanceManager implements ProductInstanceManager {

    private ProductInstanceDao productInstanceDao;
    private IpToVM ip2vm;

    private static Logger LOGGER = Logger.getLogger("ProductManagerChefImpl");

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance upgrade(ProductInstance productInstance,
            ProductRelease productRelease) throws NotTransitableException {

        List<ProductRelease> productReleases = productInstance.getProduct()
                .getTransitableReleases();

        if (!productReleases.contains(productRelease)) {
            throw new NotTransitableException();
        }
        try {
            VM vm = productInstance.getVM();
            // we need the hostname + domain so if we haven't that information,
            // shall to get it.
            if (!vm.canWorkWithChef()) {
                vm = ip2vm.getVm(vm.getIp());
            }

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

        } catch (ShellCommandException sce) {
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
            List<Attribute> configuration) {

        String filename = "role-"
                + productInstance.getProduct().getProduct().getName()
                + new Date().getTime();

        String populatedRole = populateRoleTemplate(productInstance.getVM(),
                productInstance.getProduct().getProduct().getName(),
                configuration, filename, productInstance.getProduct()
                        .getProduct().getName());
        File file = createRoleFile(populatedRole, filename);
        try {
            updateAttributes(filename, file.getAbsolutePath(),
                    productInstance.getVM());
        } catch (ShellCommandException e) {
            throw new SdcRuntimeException(e);
        }
        return productInstance;
        // don't want use this method yet
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance install(VM vm, ProductRelease product) {
        try {
            // we need the hostname + domain so if we haven't that information,
            // shall to get it.
            if (!vm.canWorkWithChef()) {
                vm = ip2vm.getVm(vm.getIp());
            }
            ProductInstance instance = new ProductInstance(product,
                    Status.INSTALLED, vm);

            String installRecipe = recipeNamingGenerator
                    .getInstallRecipe(instance);
            callChef(installRecipe, vm);
            return productInstanceDao.create(instance);

        } catch (ShellCommandException sce) {
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
     */
    @Override
    public void uninstall(ProductInstance productInstance) {
        // at least has one
        String uninstallRecipe = recipeNamingGenerator
                .getUninstallRecipe(productInstance);
        try {
            callChef(uninstallRecipe, productInstance.getVM());

            productInstance.setStatus(Status.UNINSTALLED);
            productInstanceDao.update(productInstance);
        } catch (ShellCommandException e) {
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
}
