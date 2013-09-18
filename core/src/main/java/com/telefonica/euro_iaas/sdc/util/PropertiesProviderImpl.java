package com.telefonica.euro_iaas.sdc.util;

import java.io.IOException;
import java.util.Properties;
/**
 * Get the properties from the default file or from
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class PropertiesProviderImpl implements PropertiesProvider {

    private String namespace;

    private Properties loadProperties() {
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getResourceAsStream(namespace));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Can not load properties file: "
                    + namespace, e);
        }
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

    /**
     * <p>Setter for the field <code>namespace</code>.</p>
     *
     * @param namespace the namespace to set
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }


}
