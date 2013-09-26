/**
 * 
 */
package com.telefonica.euro_iaas.sdc.rest.validation;

import java.util.List;

import org.junit.Before;
import org.junit.Test;



import junit.framework.Assert;

/**
 * @author jesus.movilla
 *
 */
public class ProductInstanceResourceValidatorImplTest {

	private String serversResponse;
	private String serverResponse;

	
	@Before
	public void setup(){
		
		serversResponse = "{\"servers\": " +
			"[" +
				"{" +
					"\"id\": \"2e855d51-4593-41de-8239-48200859d30b\", " +
					"\"links\": [" +
						"{\"href\": \"http://130.206.80.63:8774/v2/ebe6d9ec7b024361b7a3882c65a57dda/servers/2e855d51-4593-41de-8239-48200859d30b\", \"rel\": \"self\"}, " +
						"{\"href\": \"http://130.206.80.63:8774/ebe6d9ec7b024361b7a3882c65a57dda/servers/2e855d51-4593-41de-8239-48200859d30b\", \"rel\": \"bookmark\"}], " +
					"\"name\": \"31072013-01-bp-31072013-01test-1\"" +
				"}, " +
				"{" +
					"\"id\": \"4df2d4ff-233d-42ca-b7fb-c2d7787f1ee5\", " +
					"\"links\": [" +
						"{\"href\": \"http://130.206.80.63:8774/v2/ebe6d9ec7b024361b7a3882c65a57dda/servers/4df2d4ff-233d-42ca-b7fb-c2d7787f1ee5\", \"rel\": \"self\"}, " +
						"{\"href\": \"http://130.206.80.63:8774/ebe6d9ec7b024361b7a3882c65a57dda/servers/4df2d4ff-233d-42ca-b7fb-c2d7787f1ee5\", \"rel\": \"bookmark\"}], " +
					"\"name\": \"22072013-11-bp-22072013-11test-1\"" +
				"}" +
			"]" +
		"}";
		
		
		serverResponse = "{\"server\": " +
				"{\"OS-EXT-STS:task_state\": \"active\", " +
				"\"addresses\": {\"private\": [{\"version\": 4, \"addr\": \"172.30.5.4\"}, {\"version\": 4, \"addr\": \"130.206.82.66\"}]}, " +
				"\"links\": [" +
				"	{\"href\": \"http://130.206.80.63:8774/v2/ebe6d9ec7b024361b7a3882c65a57dda/servers/2e855d51-4593-41de-8239-48200859d30b\", \"rel\": \"self\"}, " +
				"	{\"href\": \"http://130.206.80.63:8774/ebe6d9ec7b024361b7a3882c65a57dda/servers/2e855d51-4593-41de-8239-48200859d30b\", \"rel\": \"bookmark\"}], " +
				"\"image\": {\"id\": \"44dcdba3-a75d-46a3-b209-5e9035d2435e\", " +
				"\"links\": [{\"href\": \"http://130.206.80.63:8774/ebe6d9ec7b024361b7a3882c65a57dda/images/44dcdba3-a75d-46a3-b209-5e9035d2435e\", \"rel\": \"bookmark\"}]}, " +
				"\"OS-EXT-STS:vm_state\": \"active\", \"flavor\": {\"id\": \"2\", \"links\": [{\"href\": \"http://130.206.80.63:8774/ebe6d9ec7b024361b7a3882c65a57dda/flavors/2\", \"rel\": \"bookmark\"}]}, " +
				"\"id\": \"2e855d51-4593-41de-8239-48200859d30b\", \"user_id\": \"e07b345d64c24c068726988617718792\", \"OS-DCF:diskConfig\": \"MANUAL\", \"accessIPv4\": \"\", \"accessIPv6\": \"\", " +
				"\"progress\": 0, \"OS-EXT-STS:power_state\": 1, \"config_drive\": \"\", \"status\": \"ACTIVE\", \"updated\": \"2013-07-31T13:22:18Z\", \"hostId\": \"46dba47bce43aa8207ccf45cee7ea3888903141554807cd3d4cd6c5a\", " +
				"\"key_name\": \"jesusmovilla\", \"name\": \"31072013-01-bp-31072013-01test-1\", \"created\": \"2013-07-31T13:21:43Z\", \"tenant_id\": \"ebe6d9ec7b024361b7a3882c65a57dda\", \"metadata\": {}}}";

	}
	
	@Test
	public void testGetServerIds() throws Exception {
		ProductInstanceResourceValidatorImpl validator = new ProductInstanceResourceValidatorImpl();
		List<String> serverids = validator.getServerIds(serversResponse);
		Assert.assertEquals(2, serverids.size() );
		Assert.assertEquals("2e855d51-4593-41de-8239-48200859d30b", serverids.get(0) );
		Assert.assertEquals("4df2d4ff-233d-42ca-b7fb-c2d7787f1ee5", serverids.get(1) );
	}
	
	@Test
	public void testGetServerIP() throws Exception {
		ProductInstanceResourceValidatorImpl validator = new ProductInstanceResourceValidatorImpl();
		String ip = validator.getServerPublicIP(serverResponse);
		Assert.assertEquals("130.206.82.66", ip );
	}
	
}
