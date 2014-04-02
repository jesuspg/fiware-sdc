/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
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

    public static final String BASE_TASK_PATH = "/vdc/{0}/task";
    public static final String TASK_PATH = BASE_TASK_PATH + "/{1}";

    public static final String BASE_PRODUCT_PATH = "/catalog/product";
    public static final String PRODUCT_PATH = BASE_PRODUCT_PATH + "/{0}/";
    public static final String PRODUCT_PATH_ATTRIBUTES = BASE_PRODUCT_PATH + "/{0}/attributes";
    public static final String PRODUCT_PATH_METADATAS = BASE_PRODUCT_PATH + "/{0}/metadatas";
    
    public static final String BASE_PRODUCT_RELEASE_PATH = BASE_PRODUCT_PATH + "/{0}/release/";
    public static final String PRODUCT_RELEASE_PATH = BASE_PRODUCT_RELEASE_PATH + "{1}";
    
    public static final String ALL_PRODUCT_RELEASE_PATH = BASE_PRODUCT_PATH + "/release";
    
    public static final String CHEFNODE_PATH = "/vdc/{0}/node/{1}";

    public static final String CHEFCLIENT_PATH = "/vdc/{0}/chefClient/{1}";

    public static final String CHEFCLIENTHOSTNAME_PATH = "/vdc/{0}/chefClient?hostname={1}";

}
