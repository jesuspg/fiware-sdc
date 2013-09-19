package com.telefonica.euro_iaas.sdc.util;

import java.util.Properties;

/**
 * <p>PropertiesProvider interface.</p>
 *
 * @author Sergio Arroyo
 */
public interface SystemPropertiesProvider {

    // CHEF PROPERTIES //
    public final static String EXECUTE_RECIPES_SCRIPT = "executeRecipesScript";
    public final static String UPLOAD_RECIPES_SCRIPT = "uploadRecipesScript";
    public final static String DELETE_RECIPES_SCRIPT = "deleteRecipesScript";

    public final static String COPY_APP_FILES_FROM_SERVER_TO_NODE = "copyAppFilesFromServerToNode";
    public final static String DEFAULT_APP_FILES_SOURCE_FOLDER = "defaultAppFilesSourceFolder";
    public final static String DEFAULT_APP_FILES_DESTINATION_FOLDER = "defaultAppFilesDestinationFolder";

    public final static String DEFAULT_HOST_DOMAIN = "defaultHostDomain";

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

    public final static String CHEF_DIRECTORY_COOKBOOK = "chefDirectoryCookbook";
    public final static String CHEF_CLIENT_URL_TEMPLATE = "chefClientUrlTemplate";

    //WEBDAV
    public final static String WEBDAV_BASE_URL = "webdavBaseUrl";
    public final static String WEBDAV_FILE_URL = "webdavFileUrl";
    public final static String WEBDAV_USERNAME = "webdavUsername";
    public final static String WEBDAV_PASSWD = "webdavPassword";
    public final static String WEBDAV_PRODUCT_BASEDIR = "webdavProductBaseDir";
    public final static String WEBDAV_APPLICATION_BASEDIR = "webdavApplicationBasedir";

    public final static String PRODUCT_INSTANCE_BASE_URL = "productInstanceBaseUrl";
    public final static String APPLICATION_INSTANCE_BASE_URL = "applicationInstanceBaseUrl";
    public final static String TASK_BASE_URL = "taskBaseUrl";

    public final static String UNTAR_COMMAND = "untarCommand";
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

    /**
     * Persist the configuration properties in the SystemConfiguration namespace.
     * @param configuration the properties to store∫
     */
    void setProperties(Properties configuration);

    /**
     * Find all system configuration properties.
     * @return
     */
    Properties loadProperties();

}
