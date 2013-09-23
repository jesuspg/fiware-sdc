package com.telefonica.euro_iaas.sdc.util;

/**
 * <p>PropertiesProvider interface.</p>
 *
 * @author Sergio Arroyo
 */
public interface SystemPropertiesProvider {
    // CRISTIAN TOOLS PROPERTIES //
    public final static String CLONE_IMAGE_SCRIPT = "cloneImageScript";
    public final static String WAIT_FOR_RUNNING_SCRIPT = "waitForRunningScript";
    public final static String TIME_WAITING_FOR_RUNNING = "timeWaitingForRunning";
    public final static String FREEZE_IMAGE_SCRIPT = "freezeImageScript";
    // CHEF PROPERTIES //
    public final static String ASSING_RECIPES_SCRIPT = "assignRecipesScript";
    public final static String UNASSING_RECIPES_SCRIPT = "unassignRecipesScript";
    public final static String EXECUTE_RECIPES_SCRIPT = "executeRecipesScript";
    public final static String INSTALL_RECIPE_TEMPLATE = "installRecipeTemplate";
    public final static String UNINSTALL_RECIPE_TEMPLATE = "uninstallRecipeTemplate";
    public final static String CONFIGURE_RECIPE_TEMPLATE = "configureRecipeTemplate";

    public final static String COPY_APP_FILES_FROM_SERVER_TO_NODE = "copyAppFilesFromServerToNode";
    public final static String DEFAULT_APP_FILES_SOURCE_FOLDER = "defaultAppFilesSourceFolder";
    public final static String DEFAULT_APP_FILES_DESTINATION_FOLDER = "defaultAppFilesDestinationFolder";



    public final static String INSTALL_APP_RECIPE_TEMPLATE = "installAppRecipeTemplate";
    public final static String UNINSTALL_APP_RECIPE_TEMPLATE = "uninstallAppRecipeTemplate";
    public final static String INSTALL_APP_RECIPE_SEPARATOR = "installAppRecipeSeparator";

    // //
    public final static String WEBDAV_BASE_URL = "webdavBaseUrl";

    public final static String DEFAULT_HOST_DOMAIN = "defaultHostDomain";

    public final static String CHEF_ROLE_TEMPLATE = "chefRoleTemplate";
    public final static String CHEF_ATTRIBUTES_TEMPLATE = "chefAttributesTemplate";
    public final static String CHEF_ATTRIBUTES_LEFT_LIMITER = "chefAttributesLeftLimiter";
    public final static String CHEF_ATTRIBUTES_RIGHT_LIMITER = "chefAttributesRightLimiter";
    public final static String CHEF_ATTRIBUTES_SEPARATOR = "chefAttributesSeparator";

    public final static String UPDATE_ATTRIBUTES_SCRIPT = "updateAttributesScript";
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
