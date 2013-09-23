package com.telefonica.euro_iaas.sdc.manager.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.INSTALL_APP_RECIPE_SEPARATOR;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.INSTALL_APP_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UNINSTALL_APP_RECIPE_TEMPLATE;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.AbstractShellCommand;

/**
 * Chef based ApplicationInstanceManager implementation.
 *
 * @author Sergio Arroyo
 *
 */
public class ApplicationInstanceManagerChefImpl extends AbstractShellCommand
        implements ApplicationInstanceManager {

    private ApplicationInstanceDao applicationInstanceDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findAll() {
        return applicationInstanceDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findByCriteria(
            ApplicationInstanceSearchCriteria criteria) {
        return applicationInstanceDao.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance install(VM vm, List<ProductInstance> products,
            Application application) {
        try {
            ApplicationInstance applicationInstance = new ApplicationInstance(
                    application, products, Status.INSTALLED);

            String recipe = getInstallRecipe(products, application);
            assignRecipes(vm, recipe);
            executeRecipes(vm);
            unassignRecipes(vm, recipe);

            return applicationInstanceDao.create(applicationInstance);

        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(
                    "Can not create application instance", e);
        } catch (AlreadyExistsEntityException e) {
            throw new SdcRuntimeException(
                    "Can not create application instance", e);
        } catch (ShellCommandException e) {
            throw new SdcRuntimeException("Can not exectue the script", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance load(Long id) throws EntityNotFoundException {
        return applicationInstanceDao.load(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall(ApplicationInstance applicationInstance) {
        try {
            String recipe = getUninstallRecipe(applicationInstance.getProducts(),
                    applicationInstance.getApplication());

            VM vm = applicationInstance.getProducts().get(0).getVM();

            assignRecipes(vm, recipe);
            executeRecipes(vm);
            unassignRecipes(vm, recipe);
            applicationInstance.setStatus(Status.UNINSTALLED);
            applicationInstanceDao.update(applicationInstance);

        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(
                    "Can not create application instance", e);
        } catch (ShellCommandException e) {
            throw new SdcRuntimeException("Can not exectue the script", e);
        }
    }

    /**
     * Get the recipe name with the criteria bellow:
     * <code>appType::appName{separator}{product1}{separator}{product2}
     * ..{separator}{productN}</code>
     * @param products the product list
     * @param application the application
     * @return the recipe template filled with the specific values.
     */
    private String getInstallRecipe(List<ProductInstance> products,
            Application application) {
        return getBaseRecipe(
                propertiesProvider.getProperty(INSTALL_APP_RECIPE_TEMPLATE),
                products, application);
    }

    /**
     * Get the recipe name with the criteria bellow:
     * <code>appType::uninstall-appName{separator}{product1}{separator}{product2}
     * ..{separator}{productN}</code>
     * @param products the product list
     * @param application the application
     * @return the recipe template filled with the specific values.
     */
    private String getUninstallRecipe(List<ProductInstance> products,
            Application application) {
        return getBaseRecipe(
                propertiesProvider.getProperty(UNINSTALL_APP_RECIPE_TEMPLATE),
                products, application);
    }


    private String getBaseRecipe(String template, List<ProductInstance> products,
            Application application) {
        String separator =
            propertiesProvider.getProperty(INSTALL_APP_RECIPE_SEPARATOR);
        String productsString = "";
        Collections.sort(products);
        for (ProductInstance product : products ) {
            //TODO Sergio Arroyo check this code when the product is refactor and
            //has the product name and the different modules.
            String productName =
                StringUtils.replace(product.getProduct().getName(), "::", "_");
            productsString = productsString + separator + productName;
        }
        return MessageFormat.format(template, application.getType(),
                application.getName(), productsString);

    }
    // ///////////// I.O.C ////////////
    /**
     * @param applicationInstanceDao
     *            the applicationInstanceDao to set
     */
    public void setApplicationInstanceDao(
            ApplicationInstanceDao applicationInstanceDao) {
        this.applicationInstanceDao = applicationInstanceDao;
    }

}
