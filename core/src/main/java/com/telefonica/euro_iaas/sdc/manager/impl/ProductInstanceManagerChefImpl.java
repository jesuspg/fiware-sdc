package com.telefonica.euro_iaas.sdc.manager.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.INSTALL_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UNINSTALL_RECIPE_TEMPLATE;

import java.io.File;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.IpToVM;

/**
 * Implements ProductManager using Chef to do that.
 *
 * @author Sergio Arroyo
 *
 */
public class ProductInstanceManagerChefImpl
        extends BaseInstallableInstanceManager
        implements ProductInstanceManager {

    private ProductInstanceDao productInstanceDao;
    private IpToVM ip2vm;

    private static Logger LOGGER = Logger.getLogger("ProductManagerChefImpl");

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance configure(ProductInstance productInstance,
            List<Attribute> configuration) {

        String filename = "role-"  + productInstance.getProduct().getName()
        + new Date().getTime();

        String populatedRole = populateRoleTemplate(productInstance.getVM(),
                productInstance.getProduct().getName(), configuration, filename,
                productInstance.getProduct().getName());

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
    public ProductInstance install(VM vm, Product product) {
        try {
            //we need the hostname + domain so if we haven't that information,
            // shall to get it.
            if (!vm.canWorkWithChef()) {
                vm = ip2vm.getVm(vm.getIp());
            }
            String installTemplate = propertiesProvider
            .getProperty(INSTALL_RECIPE_TEMPLATE);

            String recipe = MessageFormat.format(installTemplate, product
                    .getName());
                // tell Chef assign the product installation to a client

            assignRecipes(vm, recipe);
            executeRecipes(vm);
            unassignRecipes(vm, recipe);

            return productInstanceDao.create(
                    new ProductInstance(product, Status.INSTALLED, vm));

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
        //at least has one
        String recipe = getUninstalRecipe(productInstance);
        try {
            assignRecipes(productInstance.getVM(), recipe);

            executeRecipes(productInstance.getVM());

            unassignRecipes(productInstance.getVM(), recipe);

            productInstance.setStatus(Status.UNINSTALLED);
            productInstanceDao.update(productInstance);
        } catch (ShellCommandException e) {
            LOGGER.log(Level.SEVERE,
                    "Can not uninstall prodcut due to a unexpected error: "
                            + e.getMessage());
            throw new SdcRuntimeException(e);
        } catch (InvalidEntityException e) {
            LOGGER.log(Level.SEVERE,
                    "Can not update the ProductInstance due to: "
                            + e.getMessage());
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * Get the recipe name for a product following the rule bellow:
     * product<b>::</b>components will be something like
     * product::uninstall-componet1-...-componentN
     * @param productInstance the product instance
     * @return the recipe name.
     */
    private String getUninstalRecipe(ProductInstance productInstance) {
        //TODO Sergio Arroyo check this code when the product is refactor and
        //has the product name and the different modules.
        String productName = productInstance.getProduct().getName();
        String[] components = StringUtils.split(productName, "::");

        String uninstallTemplate = propertiesProvider
        .getProperty(UNINSTALL_RECIPE_TEMPLATE);
        String recipe = MessageFormat.format(uninstallTemplate, components[0]);
        for (int i=1; i < components.length; i++) {
            recipe = recipe + "-" + components[i];
        }

        return recipe;
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
        List<ProductInstance> products =
            productInstanceDao.findByCriteria(criteria);
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
     * @param ip2vm the ip2vm to set
     */
    public void setIp2vm(IpToVM ip2vm) {
        this.ip2vm = ip2vm;
    }
}
