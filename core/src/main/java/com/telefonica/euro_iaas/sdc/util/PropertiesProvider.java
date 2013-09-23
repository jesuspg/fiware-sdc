package com.telefonica.euro_iaas.sdc.util;

/**
 * <p>PropertiesProvider interface.</p>
 *
 * @author ju
 * @version $Id: $
 */
public interface PropertiesProvider {

    /**
     * Get the property for a given key.
     *
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    String getProperty(String key);
}
