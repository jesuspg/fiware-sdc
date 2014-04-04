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
