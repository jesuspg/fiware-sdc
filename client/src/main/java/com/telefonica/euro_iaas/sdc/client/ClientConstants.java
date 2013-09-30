/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.client;

/**
 * Contains the uris, and other constants related to sdc-client.
 * 
 * @author Sergio Arroyo
 */
public class ClientConstants {

    public static final String BASE_PRODUCT_INSTANCE_PATH = "/vdc/{0}/productInstance";
    public static final String INSTALL_PRODUCT_INSTANCE_PATH = BASE_PRODUCT_INSTANCE_PATH + "/";
    public static final String PRODUCT_INSTANCE_PATH = BASE_PRODUCT_INSTANCE_PATH + "/{1}";
    public static final String UPGRADE_PRODUCT_INSTANCE_PATH = BASE_PRODUCT_INSTANCE_PATH + "/{1}/{2}";
    public static final String INSTALL_ARTEFACT_INSTANCE_PATH = PRODUCT_INSTANCE_PATH + "/ac";
    public static final String UNINSTALL_ARTEFACT_INSTANCE_PATH = INSTALL_ARTEFACT_INSTANCE_PATH + "/{2}";

    public static final String DUMMY_INSTALL_ARTEFACT_INSTANCE_PATH = BASE_PRODUCT_INSTANCE_PATH + "/125/ac";

    public static final String BASE_APPLICATION_INSTANCE_PATH = "/vdc/{0}/application";
    public static final String INSTALL_APPLICATION_INSTANCE_PATH = BASE_APPLICATION_INSTANCE_PATH + "/";
    public static final String APPLICATION_INSTANCE_PATH = BASE_APPLICATION_INSTANCE_PATH + "/{1}";
    public static final String UPGRADE_APPLICATION_INSTANCE_PATH = BASE_APPLICATION_INSTANCE_PATH + "/{1}/{2}";

    public static final String BASE_TASK_PATH = "/vdc/{0}/task";
    public static final String TASK_PATH = BASE_TASK_PATH + "/{1}";

    public static final String BASE_PRODUCT_PATH = "/catalog/product";
    public static final String BASE_APPLICATION_PATH = "/catalog/application";
    public static final String BASE_ENVIRONMENT_PATH = "/catalog/environment";
    public static final String BASE_ENVIRONMENTINSTANCE_PATH = "/catalog/environmentInstance";

    public static final String ACTION_ENVIRONMENT_INSTANCE_PATH = BASE_ENVIRONMENT_PATH + "/{1}";
    public static final String ACTION_ENVIRONMENTINSTANCE_INSTANCE_PATH = BASE_ENVIRONMENT_PATH + "/{1}";

    public static final String PRODUCT_PATH = BASE_PRODUCT_PATH + "/{0}/";
    public static final String APPLICATION_PATH = BASE_APPLICATION_PATH + "/{0}/";

    public static final String BASE_APPLICATION_RELEASE_PATH = BASE_APPLICATION_PATH + "/{0}/release/";
    public static final String BASE_PRODUCT_RELEASE_PATH = BASE_PRODUCT_PATH + "/{0}/release/";

    public static final String PRODUCT_RELEASE_PATH = BASE_PRODUCT_RELEASE_PATH + "{1}";
    public static final String APPLICATION_RELEASE_PATH = BASE_APPLICATION_RELEASE_PATH + "{1}";

    public static final String ALL_PRODUCT_RELEASE_PATH = BASE_PRODUCT_PATH + "/release";
    public static final String ALL_APPLICATION_RELEASE_PATH = BASE_APPLICATION_PATH + "/release";

    public static final String CHEFNODE_PATH = "/vdc/{0}/node/{1}";

    public static final String CHEFCLIENT_PATH = "/vdc/{0}/chefClient/{1}";

    public static final String CHEFCLIENTHOSTNAME_PATH = "/vdc/{0}/chefClient?hostname={1}";

}
