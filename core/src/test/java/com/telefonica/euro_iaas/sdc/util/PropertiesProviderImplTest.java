package com.telefonica.euro_iaas.sdc.util;

import junit.framework.TestCase;

public class PropertiesProviderImplTest extends TestCase {

    public void testFindProperties() throws Exception {
        PropertiesProviderImpl provider = new PropertiesProviderImpl();
        provider.setNamespace("/TestSystemConfiguration.properties");

        // Does not exist env variable so return the default property.
        String value = provider.getProperty("propertyKey");
        assertEquals("propertyValue", value);
        //get form env.
        for (String key : System.getenv().keySet()) {
            value = provider.getProperty(key);
            assertEquals(System.getenv(key), value);
        }

    }


}
