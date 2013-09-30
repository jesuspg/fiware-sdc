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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.springframework.security.core.GrantedAuthority;

public class OpenStackUserTest extends TestCase {

    public static String USER = "user";
    public static String PASSWORD = "password";
    public static String TENANT_NAME = "tenantName";
    public static String TENANT_ID = "1234fadf3";

    /**
     * Test OpenstackUser class
     * 
     * @return
     */

    public void testOpenStackUserDto() {
        List<? extends GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        OpenStackUser user = new OpenStackUser(USER, PASSWORD, authorities);
        user.setTenantId(TENANT_ID);
        user.setTenantName(TENANT_NAME);
        assertEquals(user.getToken(), PASSWORD);
        assertEquals(user.getTenantName(), TENANT_NAME);
        assertEquals(user.getTenantId(), TENANT_ID);

    }

}
