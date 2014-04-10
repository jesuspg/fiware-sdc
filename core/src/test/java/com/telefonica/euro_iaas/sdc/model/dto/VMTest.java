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

package com.telefonica.euro_iaas.sdc.model.dto;

import junit.framework.TestCase;

/**
 * Unit test for Host objects.
 * 
 * @author Sergio Arroyo
 */
public class VMTest extends TestCase {

    private static final String HOSTNAME = "hostname";
    private static final String DOMAIN = "domain";
    private static final String IP = "ip";
    private static final String FQN = "fqn";

    /**
     * Test to string method
     */
    public void testToStringForHostNameAndDomain() {
        VM host = new VM(FQN, HOSTNAME, DOMAIN);
        assertEquals(HOSTNAME + DOMAIN, host.getChefClientName());

        host = new VM(FQN);
        try {
            host.getChefClientName();
            fail("IllegalStateExceptionExpected");
        } catch (IllegalStateException e) {
            // It's OK
        }
    }

    /**
     * Test to string method
     */
    public void testEquals() {
        VM host1 = new VM(FQN);
        VM host2 = new VM(FQN);
        assertEquals(host1, host2);

        host1 = new VM(FQN, DOMAIN, HOSTNAME);
        host2 = new VM(FQN, DOMAIN, HOSTNAME);
        assertEquals(host1, host2);

        host1 = new VM(FQN, IP, DOMAIN, HOSTNAME);
        host2 = new VM(FQN, IP, DOMAIN, HOSTNAME);
        assertEquals(host1, host2);
    }
}
