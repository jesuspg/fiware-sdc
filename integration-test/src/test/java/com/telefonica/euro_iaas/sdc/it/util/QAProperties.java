package com.telefonica.euro_iaas.sdc.it.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * Provides a set of methods to get the parameters properties to run test.
 * 
 * @author Sergio Arroyo
 */
public class QAProperties {

    public static final String BASE_URL = "base.url";
    public static final String MIME_TYPE = "mime.type";

    public static final String MAX_TIME_WAITING = "max.time.waiting";
    public static final String WAITING_PERIOD = "waiting.period";

    public static final String VDC = "vdc";
    public static final String VM = "vm";

    private static Properties properties;

    private static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            InputStream file;
            try {
                if (System.getProperty("properties") != null) {
                    file = new FileInputStream(System.getProperty("properties"));
                } else {
                    file = QAProperties.class.getResourceAsStream("/sdc-qa.properties");
                }
                properties.load(file);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties;
    }

    /**
     * Get the property from system parameter if exists. Otherwise get from a properties file.
     * 
     * @param key
     *            key of the property.
     * @return the property
     */
    public static String getProperty(String key) {
        String prop = System.getProperty(key);
        if (prop == null) {
            prop = getProperties().getProperty(key);
        }
        return prop;
    }

    public static Long getLongProperty(String key) {
        return Long.parseLong(getProperty(key));
    }

    /**
     * @return the list of keys
     */
    public static String[] getConfigurationPropertiesKeys() {
        Set<Object> keyset = getProperties().keySet();
        String[] keys = new String[keyset.size()];
        keyset.toArray(keys);
        return keys;
    }

}
