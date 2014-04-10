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
