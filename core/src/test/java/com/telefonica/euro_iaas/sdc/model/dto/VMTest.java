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
