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
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for MixlibAuthenticationDigester
 * 
 * @author Sergio Arroyo
 */
public class MixlibAuthenticationDigesterTest {

    private SystemPropertiesProvider propertiesProvider;
    private Signer signer;

    @Before
    public void setUp() {
        propertiesProvider = mock(SystemPropertiesProvider.class);

        signer = mock(Signer.class);
        when(signer.sign(anyString(), (File) anyObject())).thenReturn("blahblahblah");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testDigest() throws Exception {
        MixlibAuthenticationDigesterImpl digester = new MixlibAuthenticationDigesterImpl();

        digester.setSigner(signer);

        Map<String, String> headers = digester.digest("GET", "/nodes", "", new Date(), "serch", this.getClass()
                .getResource("/private.pem").getPath());
        // make assertions
        assertEquals(7, headers.size());

    }

}
