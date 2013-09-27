package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import junit.framework.TestCase;

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
