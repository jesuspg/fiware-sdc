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

package com.telefonica.euro_iaas.sdc.util;

import java.util.Properties;

import com.telefonica.euro_iaas.commons.properties.PropertiesProvider;

/**
 * Get the properties from the default file or from
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

    /**
     * @ø
     */
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
