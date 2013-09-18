package com.telefonica.euro_iaas.sdc.util;

/**
 * <p>PropertiesProvider interface.</p>
 *
 * @author Sergio Arroyo
 */
public interface SystemPropertiesProvider {
    ////////// RECIPE TEMPLATES ///////////
    public final static String INSTALL_PRODUCT_RECIPE_TEMPLATE = "installProductRecipeTemplate";
    public final static String UNINSTALL_PRODUCT_RECIPE_TEMPLATE = "uninstallProductRecipeTemplate";
    public final static String BACKUP_PRODUCT_RECIPE_TEMPLATE = "backupProductRecipeTemplate";
    public final static String RESTORE_PRODUCT_RECIPE_TEMPLATE = "restoreProductRecipeTemplate";

    public final static String INSTALL_APPLICATION_RECIPE_TEMPLATE = "installApplicationRecipeTemplate";
    public final static String UNINSTALL_APPLICATION_RECIPE_TEMPLATE = "uninstallApplicationRecipeTemplate";
    public final static String BACKUP_APPLICATION_RECIPE_TEMPLATE = "backupApplicationRecipeTemplate";
    public final static String RESTORE_APPLICATION_RECIPE_TEMPLATE = "restoreApplicationRecipeTemplate";

    public final static String PRODUCT_LIST_TEMPLATE = "productListTemplate";
    public final static String PRODUCT_LIST_SEPARATOR = "productListSeparator";

    public final static String CHEF_SERVER_URL = "chefServerUrl";
    public final static String CHEF_SERVER_NODES_PATH = "chefServerNodesPath";
    public final static String CHEF_CLIENT_ID = "chefClientId";
    public final static String CHEF_CLIENT_PASS = "chefClientPass";

    public final static String CHEF_DATE_FORMAT = "chefDateFormat";
    public final static String CHEF_TIME_ZONE = "chefTimeZone";

    public final static String CHEF_CLIENT_URL_TEMPLATE = "chefClientUrlTemplate";

    /**
     * Get the property for a given key.
     *
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    String getProperty(String key);
    /**
     * Get the property for a given key.
     *
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    Integer getIntProperty(String key);
}
