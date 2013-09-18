package com.telefonica.euro_iaas.sdc.util;

/**
 * <p>PropertiesProvider interface.</p>
 *
 * @author Sergio Arroyo
 */
public interface SystemPropertiesProvider {
    // CHEF PROPERTIES //
    public final static String ASSING_RECIPES_SCRIPT = "assignRecipesScript";
    public final static String UNASSING_RECIPES_SCRIPT = "unassignRecipesScript";
    public final static String EXECUTE_RECIPES_SCRIPT = "executeRecipesScript";

    public final static String COPY_APP_FILES_FROM_SERVER_TO_NODE = "copyAppFilesFromServerToNode";
    public final static String DEFAULT_APP_FILES_SOURCE_FOLDER = "defaultAppFilesSourceFolder";
    public final static String DEFAULT_APP_FILES_DESTINATION_FOLDER = "defaultAppFilesDestinationFolder";

    public final static String DEFAULT_HOST_DOMAIN = "defaultHostDomain";

    public final static String CHEF_ROLE_TEMPLATE = "chefRoleTemplate";
    public final static String CHEF_ATTRIBUTES_TEMPLATE = "chefAttributesTemplate";
    public final static String CHEF_ATTRIBUTES_LEFT_LIMITER = "chefAttributesLeftLimiter";
    public final static String CHEF_ATTRIBUTES_RIGHT_LIMITER = "chefAttributesRightLimiter";
    public final static String CHEF_ATTRIBUTES_SEPARATOR = "chefAttributesSeparator";

    public final static String UPDATE_ATTRIBUTES_SCRIPT = "updateAttributesScript";

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
