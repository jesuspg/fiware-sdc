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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.Test;

import com.telefonica.euro_iaas.commons.properties.PropertiesProvider;

public class SystemPropertiesProviderImplTest {

    @Test
    public void shouldGetPropertyPATHFromSystemEnvironment() {
        // given

        SystemPropertiesProviderImpl systemPropertiesProvider = new SystemPropertiesProviderImpl();

        // when
        String value = systemPropertiesProvider.getProperty("PATH");

        // then
        assertNotNull(value);
    }

    @Test
    public void shouldGetPropertyFromOtherProvider() {
        // given

        SystemPropertiesProviderImpl systemPropertiesProvider = new SystemPropertiesProviderImpl();
        PropertiesProvider propertiesProvider = mock(PropertiesProvider.class);
        systemPropertiesProvider.setPropertiesProvider(propertiesProvider);
        systemPropertiesProvider.setNamespace("namespace");

        Properties properties = new Properties();
        properties.put("propertyA", "valueA");

        // when

        when(propertiesProvider.load("namespace")).thenReturn(properties);
        String value = systemPropertiesProvider.getProperty("propertyA");

        // then
        assertNotNull(value);
        assertEquals(value, "valueA");
        verify(propertiesProvider).load("namespace");
    }

    @Test
    public void shouldGetIntegerPropertyFromOtherProvider() {
        // given

        SystemPropertiesProviderImpl systemPropertiesProvider = new SystemPropertiesProviderImpl();
        PropertiesProvider propertiesProvider = mock(PropertiesProvider.class);
        systemPropertiesProvider.setPropertiesProvider(propertiesProvider);
        systemPropertiesProvider.setNamespace("namespace");

        Properties properties = new Properties();
        properties.put("propertyA", "1");

        // when

        when(propertiesProvider.load("namespace")).thenReturn(properties);
        Integer value = systemPropertiesProvider.getIntProperty("propertyA");

        // then
        assertNotNull(value);
        assertEquals(value, new Integer(1));
        verify(propertiesProvider).load("namespace");
    }

}
