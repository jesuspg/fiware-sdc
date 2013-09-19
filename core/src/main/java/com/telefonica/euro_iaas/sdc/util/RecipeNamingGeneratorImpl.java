package com.telefonica.euro_iaas.sdc.util;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.BACKUP_APPLICATION_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.BACKUP_PRODUCT_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.INSTALL_APPLICATION_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.INSTALL_PRODUCT_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.PRODUCT_LIST_SEPARATOR;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.PRODUCT_LIST_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.RESTORE_APPLICATION_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.RESTORE_PRODUCT_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UNINSTALL_APPLICATION_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UNINSTALL_PRODUCT_RECIPE_TEMPLATE;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
/**
 * Default RecipeNamingGenerator implementation.
 * @author Sergio Arroyo
 *
 */
public class RecipeNamingGeneratorImpl implements RecipeNamingGenerator {

    private SystemPropertiesProvider propertiesProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInstallRecipe(ProductInstance product) {
        String installTemplate =
            propertiesProvider.getProperty(INSTALL_PRODUCT_RECIPE_TEMPLATE);
        return populateProductRecipe(installTemplate, product);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInstallRecipe(ApplicationInstance application) {
        String installTemplate =
            propertiesProvider.getProperty(INSTALL_APPLICATION_RECIPE_TEMPLATE);
        return populateApplicationRecipe(installTemplate, application);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUninstallRecipe(ProductInstance product) {
        String uninstallTemplate =
            propertiesProvider.getProperty(UNINSTALL_PRODUCT_RECIPE_TEMPLATE);
        return populateProductRecipe(uninstallTemplate, product);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUninstallRecipe(ApplicationInstance application) {
        String uninstallTemplate =
            propertiesProvider.getProperty(UNINSTALL_APPLICATION_RECIPE_TEMPLATE);
        return populateApplicationRecipe(uninstallTemplate, application);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackupRecipe(ProductInstance product) {
        String backupTemplate =
            propertiesProvider.getProperty(BACKUP_PRODUCT_RECIPE_TEMPLATE);
        return populateProductRecipe(backupTemplate, product);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBackupRecipe(ApplicationInstance application) {
        String backupTemplate =
            propertiesProvider.getProperty(BACKUP_APPLICATION_RECIPE_TEMPLATE);
        return populateApplicationRecipe(backupTemplate, application);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRestoreRecipe(ProductInstance product) {
        String restoreTemplate =
            propertiesProvider.getProperty(RESTORE_PRODUCT_RECIPE_TEMPLATE);
        return populateProductRecipe(restoreTemplate, product);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRestoreRecipe(ApplicationInstance application) {
        String restoreTemplate =
            propertiesProvider.getProperty(RESTORE_APPLICATION_RECIPE_TEMPLATE);
        return populateApplicationRecipe(restoreTemplate, application);
    }


    /**
     * Fill the template with product name and product version.
     * @param template the template
     * @param product the product
     * @return the filled template
     */
    private String populateProductRecipe(String template,
            ProductInstance product) {
        return MessageFormat.format(template,
                product.getProduct().getProduct().getName(),
                product.getProduct().getVersion());
    }

    /**
     * Fill the template with application type, application name and version.
     * Even the product list will be filled.
     *
     * @param template the template
     * @param application the application
     * @return the filled template
     */
    private String populateApplicationRecipe(String template,
            ApplicationInstance application) {
        return MessageFormat.format(template,
                application.getApplication().getApplication().getType(),
                application.getApplication().getApplication().getName(),
                application.getApplication().getVersion(),
                getProductList(application));
    }

    /**
     * Populate the product list filled by order.
     * @param applicationInstance
     * @return
     */
    private String getProductList(ApplicationInstance applicationInstance) {
        String productListTemplate =
            propertiesProvider.getProperty(PRODUCT_LIST_TEMPLATE);
        String productListSeparator =
            propertiesProvider.getProperty(PRODUCT_LIST_SEPARATOR);
        String result = "";
        List<ProductInstance> producs = applicationInstance
        		.getEnvironmentInstance().getProductInstances();
        Collections.sort(producs);
        for (ProductInstance pi : producs) {
            if (!result.isEmpty()) {
                result = result.concat(productListSeparator);
            }
            result = result.concat(MessageFormat.format(productListTemplate,
                    pi.getProduct().getProduct().getName(),
                    pi.getProduct().getVersion()));
        }
        return result;
    }

    /////////////I.O.C.////////////

    /**
     * @param propertiesProvider the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

}
