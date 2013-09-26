package com.telefonica.euro_iaas.sdc.model;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

import junit.framework.TestCase;

/**
 * 
 */

/**
 * @author jesus.movilla
 * 
 */
public class ChefClientTest extends TestCase {

	ChefClient chefClient;
	String json;

	@Before
	public void setUp() throws Exception {

		chefClient = new ChefClient();

		json = "{\n"
				+ "\"chef-webui\": \"http://localhost:4000/clients/chef-webui\"\n"
				+ "\"henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal\": \"http://localhost:4000/clients/henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal\",\n"
				+ "\"chef-validator\": \"http://localhost:4000/clients/chef-validator\",\n"
				+ "\"adam.novalocal\": \"http://localhost:4000/clients/adam.novalocal\"\n"
				+ "}\"";
	}

	@Test
	public void testGetChefClientURL() throws Exception {
		String name = chefClient.getChefClientName(json,
				"henartmactmysqlInstance2-tomcat7postgres8Tier-1");
		assertEquals(name,
				"henartmactmysqlInstance2-tomcat7postgres8Tier-1.novalocal");
	}

}
