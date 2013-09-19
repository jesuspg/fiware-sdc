package com.telefonica.euro_iaas.sdc.client;
/**
 * Contains the uris, and other constants related to sdc-client
 *
 * @author Sergio Arroyo
 *
 */
public class ClientConstants {

    public static final String BASE_PRODUCT_INSTANCE_PATH = "/vdc/{0}/product";
    public static final String INSTALL_PRODUCT_INSTANCE_PATH =
            BASE_PRODUCT_INSTANCE_PATH + "/";
    public static final String PRODUCT_INSTANCE_PATH =
            BASE_PRODUCT_INSTANCE_PATH + "/{1}";
    public static final String UPGRADE_PRODUCT_INSTANCE_PATH =
            BASE_PRODUCT_INSTANCE_PATH + "/{1}/{2}";

    public static final String BASE_APPLICATION_INSTANCE_PATH = "/vdc/{0}/application";
    public static final String INSTALL_APPLICATION_INSTANCE_PATH =
            BASE_APPLICATION_INSTANCE_PATH + "/";
    public static final String APPLICATION_INSTANCE_PATH =
            BASE_APPLICATION_INSTANCE_PATH + "/{1}";
    public static final String UPGRADE_APPLICATION_INSTANCE_PATH =
            BASE_APPLICATION_INSTANCE_PATH + "/{1}/{2}";

    public static final String BASE_TASK_PATH = "/vdc/{0}/task";
    public static final String TASK_PATH = BASE_TASK_PATH + "/{1}";

    public static final String BASE_PRODUCT_PATH = "/catalog/product";
    public static final String BASE_APPLICATION_PATH = "/catalog/application";

    public static final String PRODUCT_PATH = BASE_PRODUCT_PATH + "/{0}/";
    public static final String APPLICATION_PATH = BASE_APPLICATION_PATH + "/{0}/";

    public static final String BASE_APPLICATION_RELEASE_PATH =
            BASE_APPLICATION_PATH + "/{0}/release/";
    public static final String BASE_PRODUCT_RELEASE_PATH =
            BASE_PRODUCT_PATH + "/{0}/release/";

    public static final String PRODUCT_RELEASE_PATH =
            BASE_PRODUCT_RELEASE_PATH + "{1}";
    public static final String APPLICATION_RELEASE_PATH =
            BASE_APPLICATION_RELEASE_PATH + "{1}";

    public static final String ALL_PRODUCT_RELEASE_PATH =
            BASE_PRODUCT_PATH + "/release";
    public static final String ALL_APPLICATION_RELEASE_PATH =
            BASE_APPLICATION_PATH + "/release";


}
