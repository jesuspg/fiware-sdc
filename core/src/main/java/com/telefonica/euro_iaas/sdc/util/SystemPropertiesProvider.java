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

import java.util.Properties;

/**
 * <p>
 * PropertiesProvider interface.
 * </p>
 * 
 * @author Sergio Arroyo
 */
public interface SystemPropertiesProvider {
	
	public final static String SDC_MANAGER_URL = "sdc_manager_url";
	 
	public final static String CHEF_CLIENT_ID = "chefClientId";
    public final static String CHEF_CLIENT_PASS = "chefClientPass";

    public final static String KEYSTONE_URL = "openstack-tcloud.keystone.url";

    /** The Constant KEYSTONE_USER. */
    public final static String KEYSTONE_USER = "openstack-tcloud.keystone.user";

    /** The Constant KEYSTONE_PASS. */
    public final static String KEYSTONE_PASS = "openstack-tcloud.keystone.pass";

    /** The Constant KEYSTONE_TENANT. */
    public final static String KEYSTONE_TENANT = "openstack-tcloud.keystone.tenant";

    /** The Constant SYSTEM_FASTTRACK. */
    public final static String VALIDATION_TIME_THRESHOLD = "openstack-tcloud.keystone.threshold";


    
 // Tells if the SDC works with sdc-client installed in the nodes
    public static final String SDCCLIENT_INSTALLED_IN_NODES = "sdcclient.installed.innodes";

	public static final String CLOUD_SYSTEM = "FIWARE";


    
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
     * Persist the configuration properties in the SystemConfiguration namespace.
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
