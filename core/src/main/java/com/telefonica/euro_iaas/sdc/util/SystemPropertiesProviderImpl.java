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
    @Override
    public String getProperty(String key) {
        String value = System.getenv(key);
        if (value == null) {
            value = loadProperties().getProperty(key);
        }
        return value;
    }

    @Override
    public Integer getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }

    /**
     * @ø
     */
    @Override
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
     * @param propertiesProvider the propertiesProvider to set
     */
    public void setPropertiesProvider(PropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

}
