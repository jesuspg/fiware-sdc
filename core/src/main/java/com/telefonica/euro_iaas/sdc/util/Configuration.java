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

package com.telefonica.euro_iaas.sdc.util;

public interface Configuration {
	
	public final static String EXECUTE_RECIPES_SCRIPT = "executeRecipesScript";
    public final static String UPLOAD_RECIPES_SCRIPT = "/opt/sdc/scripts/uploadRecipes.sh {0}";
    public final static String DELETE_RECIPES_SCRIPT = "/opt/sdc/scripts/deleteRecipes.sh {0} {1}t";

    public final static String COPY_APP_FILES_FROM_SERVER_TO_NODE = "copyAppFilesFromServerToNode";
    final static String DEFAULT_APP_FILES_SOURCE_FOLDER = "defaultAppFilesSourceFolder";
    public final static String DEFAULT_APP_FILES_DESTINATION_FOLDER = "defaultAppFilesDestinationFolder";

    public final static String DEFAULT_HOST_DOMAIN = "defaultHostDomain";


    // //////// RECIPE TEMPLATES ///////////
    public final static String INSTALL_PRODUCT_RECIPE_TEMPLATE = "{0}::{1}_install";
    public final static String UNINSTALL_PRODUCT_RECIPE_TEMPLATE = "{0}::{1}_uninstall";

    public final static String DEPLOYAC_PRODUCT_RECIPE_TEMPLATE = "{0}::{1}_deployAC";
    public final static String UNDEPLOYAC_PRODUCT_RECIPE_TEMPLATE = "{0}::{1}_undeployAC";
    public final static String BACKUP_PRODUCT_RECIPE_TEMPLATE = "{0}::{1}_backup";
    public final static String CONFIGURE_PRODUCT_RECIPE_TEMPLATE = "{0}::{1}_install";
    public final static String RESTORE_PRODUCT_RECIPE_TEMPLATE = "{0}::{1}_restore";


    public final static String PRODUCT_LIST_TEMPLATE = "{0}-{1}";
    public final static String PRODUCT_LIST_SEPARATOR = "_";
    
  

    public final static String CHEF_SERVER_NODES_PATH = "/nodes{0}";
    public final static String CHEF_SERVER_CLIENTS_PATH = "/clients{0}";

	
    public final static String CHEF_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public final static String CHEF_TIME_ZONE = "GMT";

    public final static String CHEF_DIRECTORY_COOKBOOK = "/root/chef-repo/cookbooks/";
    public final static String CHEF_CLIENT_URL_TEMPLATE = "http://{0}:9990/sdc-client";
    

    // WEBDAV
    public final static String WEBDAV_FILE_URL = "{0}/{1}/{2}/{3}/{2}{3}-bin.tar";
    public final static String WEBDAV_USERNAME = "root";
    public final static String WEBDAV_PASSWD = "temporal";
    public final static String WEBDAV_PRODUCT_BASEDIR = "product";
    public final static String WEBDAV_APPLICATION_BASEDIR = "application";
    

    		

    public final static String PRODUCT_INSTANCE_BASE_PATH = "/rest/vdc/{4}/productInstance/{0}";
    public final static String APPLICATION_INSTANCE_BASE_PATH = "/rest/vdc/{4}/application/{0}";
    public final static String TASK_BASE_PATH = "/rest/vdc/{1}/task/{0}";
    public final static String CHEF_NODE_BASE_PATH = "/rest/vdc/{1}/node/{0}";

    public final static String UNTAR_COMMAND = "tar xvf  {0} -C {1}";
    
    long OPENSTACK_SYNCHRONIZATION_POLLING_PERIOD = 84000000;
    String VALIDATION_TIME_THRESHOLD = "84000000";
    String VERSION_PROPERTY = "v2/";

    
    

}

