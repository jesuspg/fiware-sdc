package com.telefonica.euro_iaas.sdc.util;

import java.util.Properties;

/**
 * <p>
 * PropertiesProvider interface.
 * </p>
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

	// //////// RECIPE TEMPLATES ///////////
	public final static String INSTALL_PRODUCT_RECIPE_TEMPLATE = "installProductRecipeTemplate";
	public final static String UNINSTALL_PRODUCT_RECIPE_TEMPLATE = "uninstallProductRecipeTemplate";

	public final static String DEPLOYAC_PRODUCT_RECIPE_TEMPLATE = "deployACProductRecipeTemplate";
	public final static String UNDEPLOYAC_PRODUCT_RECIPE_TEMPLATE = "unDeployACProductRecipeTemplate";
	public final static String BACKUP_PRODUCT_RECIPE_TEMPLATE = "backupProductRecipeTemplate";
	public final static String CONFIGURE_PRODUCT_RECIPE_TEMPLATE = "configureProductRecipeTemplate";
	public final static String RESTORE_PRODUCT_RECIPE_TEMPLATE = "restoreProductRecipeTemplate";

	public final static String INSTALL_APPLICATION_RECIPE_TEMPLATE = "installApplicationRecipeTemplate";
	public final static String UNINSTALL_APPLICATION_RECIPE_TEMPLATE = "uninstallApplicationRecipeTemplate";
	public final static String BACKUP_APPLICATION_RECIPE_TEMPLATE = "backupApplicationRecipeTemplate";
	public final static String RESTORE_APPLICATION_RECIPE_TEMPLATE = "restoreApplicationRecipeTemplate";

	public final static String PRODUCT_LIST_TEMPLATE = "productListTemplate";
	public final static String PRODUCT_LIST_SEPARATOR = "productListSeparator";

	public final static String CHEF_SERVER_URL = "chefServerUrl";
	public final static String CHEF_SERVER_NODES_PATH = "chefServerNodesPath";
	public final static String CHEF_SERVER_CLIENTS_PATH = "chefServerClientsPath";
	public final static String CHEF_CLIENT_ID = "chefClientId";
	public final static String CHEF_CLIENT_PASS = "chefClientPass";

	public final static String CHEF_DATE_FORMAT = "chefDateFormat";
	public final static String CHEF_TIME_ZONE = "chefTimeZone";

	public final static String CHEF_DIRECTORY_COOKBOOK = "chefDirectoryCookbook";
	public final static String CHEF_CLIENT_URL_TEMPLATE = "chefClientUrlTemplate";

	// WEBDAV
	public final static String WEBDAV_BASE_URL = "webdavBaseUrl";
	public final static String WEBDAV_FILE_URL = "webdavFileUrl";
	public final static String WEBDAV_USERNAME = "webdavUsername";
	public final static String WEBDAV_PASSWD = "webdavPassword";
	public final static String WEBDAV_PRODUCT_BASEDIR = "webdavProductBaseDir";
	public final static String WEBDAV_APPLICATION_BASEDIR = "webdavApplicationBasedir";

	public final static String PRODUCT_INSTANCE_BASE_URL = "productInstanceBaseUrl";
	public final static String APPLICATION_INSTANCE_BASE_URL = "applicationInstanceBaseUrl";
	public final static String TASK_BASE_URL = "taskBaseUrl";
	public final static String CHEF_NODE_BASE_URL = "chefNodeBaseUrl";
	
	public final static String UNTAR_COMMAND = "untarCommand";

	/** The Constant KEYSTONE_URL. */
	public final static String KEYSTONE_URL = "openstack-tcloud.keystone.url";

	/** The Constant CLOUD_SYSTEM. */
	public final static String CLOUD_SYSTEM = "openstack-tcloud.cloudSystem";

	/** The Constant KEYSTONE_USER. */
	public final static String KEYSTONE_USER = "openstack-tcloud.keystone.user";

	/** The Constant KEYSTONE_PASS. */
	public final static String KEYSTONE_PASS = "openstack-tcloud.keystone.pass";

	/** The Constant KEYSTONE_TENANT. */
	public final static String KEYSTONE_TENANT = "openstack-tcloud.keystone.tenant";

	/** The Constant SYSTEM_FASTTRACK. */
	public final static String VALIDATION_TIME_THRESHOLD = "openstack-tcloud.keystone.threshold";
	
	public static final String URL_NOVA_PROPERTY = "openstack.nova.url";
	public static final String VERSION_PROPERTY = "openstack.version";
	/**
	 * Get the property for a given key.
	 * 
	 * @param key
	 *            a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	String getProperty(String key);

	/**
	 * Get the property for a given key.
	 * 
	 * @param key
	 *            a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	Integer getIntProperty(String key);

	/**
	 * Persist the configuration properties in the SystemConfiguration
	 * namespace.
	 * 
	 * @param configuration
	 *            the properties to store∫
	 */
	void setProperties(Properties configuration);

	/**
	 * Find all system configuration properties.
	 * 
	 * @return
	 */
	Properties loadProperties();

}
