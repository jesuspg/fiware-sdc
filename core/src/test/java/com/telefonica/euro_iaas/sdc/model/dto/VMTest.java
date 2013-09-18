package com.telefonica.euro_iaas.sdc.model.dto;

import junit.framework.TestCase;
/**
 * Unit test for Host objects.
 * @author Sergio Arroyo
 *
 */
public class VMTest extends TestCase {

    private static final String HOSTNAME = "hostname";
    private static final String DOMAIN = "domain";
    private static final String IP = "ip";

    /**
     * Test to string method
     */
    public void testToStringForHostNameAndDomain() {
        VM host = new VM(HOSTNAME, DOMAIN);
        assertEquals(HOSTNAME + DOMAIN, host.getChefClientName());

        host = new VM(IP);
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
        VM host1 = new VM(IP);
        VM host2 = new VM(IP);
        assertEquals(host1, host2);

        host1 = new VM(DOMAIN, HOSTNAME);
        host2 = new VM(DOMAIN, HOSTNAME);
        assertEquals(host1, host2);
    }
}
