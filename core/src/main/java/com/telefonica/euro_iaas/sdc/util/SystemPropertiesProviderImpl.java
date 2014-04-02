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

import com.telefonica.euro_iaas.commons.properties.PropertiesProvider;

/**
 * Get the properties from the default file or from.
 * 
 * @author Sergio Arroyo
 */
public class SystemPropertiesProviderImpl implements SystemPropertiesProvider {

    private String namespace;
    private PropertiesProvider propertiesProvider;

    public Properties loadProperties() {
        return propertiesProvider.load(namespace);
    }

    /** {@inheritDoc} */
    public String getProperty(String key) {
        String value = System.getenv(key);
        if (value == null) {
            value = loadProperties().getProperty(key);
        }
        return value;
    }

    public Integer getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public void setProperties(Properties configuration) {
        propertiesProvider.store(configuration, namespace);
    }

    /**
     * <p>
     * Setter for the field <code>namespace</code>.
     * </p>
     * 
     * @param namespace
     *            the namespace to set
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(PropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

}
