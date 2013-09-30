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
