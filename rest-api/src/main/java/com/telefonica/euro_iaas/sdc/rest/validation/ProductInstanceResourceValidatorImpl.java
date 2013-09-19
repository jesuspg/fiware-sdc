/**
 * 
 */
package com.telefonica.euro_iaas.sdc.rest.validation;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.security.core.context.SecurityContextHolder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.sdc.model.dto.OpenStackUser;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.rest.exception.UnauthorizedOperationException;

/**
 * @author jesus.movilla
 *
 */
public class ProductInstanceResourceValidatorImpl implements
		ProductInstanceResourceValidator {

	private SystemPropertiesProvider systemPropertiesProvider;
	
	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.sdc.rest.validation.ProductInstanceResourceValidator#validateInsert()
	 */
	public void validateInsert(ProductInstanceDto product) throws UnauthorizedOperationException {
		OpenStackUser user = getCredentials();
		
		List<String> ips = new ArrayList<String>();
		List<String> serversIds = findAllServers(user);
		
		for(int i=0; i < serversIds.size(); i++){
			String ip = findServerIP(user, serversIds.get(i));
			ips.add(ip);
		}
		
		if (!(ips.contains(product.getVm().getIp()))){
			String message = " The Server with ip " + product.getVm().getIp() +
					" does not belong to user with token " + user.getToken();
			throw new UnauthorizedOperationException(message);			
		}

	}
	
	/**
	 * Get the Credentials (FIWARE) from the Spring SecurityContext
	 */
	private OpenStackUser getCredentials() {
		if (systemPropertiesProvider.getProperty(
				SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
			return (OpenStackUser) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
		} else {
			return null;
		}
	}

	/**
	 * Obtain the serversIds associated to a certain user
	 * @param user
	 * @return
	 */
	private List<String> findAllServers (OpenStackUser user){
		//http://130.206.80.63:8774/v2/ebe6d9ec7b024361b7a3882c65a57dda/servers
		String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_NOVA_PROPERTY) + 
				systemPropertiesProvider.getProperty(SystemPropertiesProvider.VERSION_PROPERTY) +  
				user.getTenantId() + "/servers";	
		String output = getResourceOpenStack(url, user.getToken());
		
		return getServerIds(output);
		
	}
	
	/**
	 * Obtain the ip associated to a certain serverid
	 * @param user
	 * \param serverId
	 * @return
	 */
	private String findServerIP (OpenStackUser user, String serverId){
		String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_NOVA_PROPERTY) + 
				systemPropertiesProvider.getProperty(SystemPropertiesProvider.VERSION_PROPERTY) +  
				user.getTenantId() + "/servers/" + serverId;	
		String output = getResourceOpenStack(url, user.getToken());
		
		return getServerPublicIP(output);
	}
	
	public String getResourceOpenStack(String url, String token) {
		Client client = new Client();
		
		WebResource wr = client.resource(url);
		Builder builder = wr.accept("application/json");
		//headers
		builder = builder.header("X-Auth-Token", token);
			
		ClientResponse response = builder.get(ClientResponse.class);
		return response.getEntity(String.class);
	}
	
	/**
	 * Obtains the serversId frm the response of a GET call
	 * @param response
	 * @return List of serverIds
	 */
	public List<String> getServerIds(String response) {
		List<String> servers = new ArrayList<String>();
		
		JSONObject jsonNode = JSONObject.fromObject(response);
		JSONArray serversjsonArray = jsonNode.getJSONArray("servers");
		
		for (int i =0; i < serversjsonArray.size(); i++){
			JSONObject serverElement = serversjsonArray.getJSONObject(i);		
			servers.add (serverElement.getString("id"));
		}
		
		return servers;
	}
	
	/**
	 * Obtains the serverPublicIP from the response of a GET call
	 * @param response
	 * @return IP of a server
	 */
	public String getServerPublicIP(String response) {	
		List<String> ips =new ArrayList<String>();	
		
		JSONObject jsonNode = JSONObject.fromObject(response);
		JSONObject serverjsonObject = jsonNode.getJSONObject("server");
		JSONObject addressesjsonObject = serverjsonObject.getJSONObject("addresses");
		JSONArray addressesjsonArray = addressesjsonObject.getJSONArray("private");
		
		for (int i =0; i < addressesjsonArray.size(); i++){
			JSONObject addressElement = addressesjsonArray.getJSONObject(i);	
			ips.add (addressElement.getString("addr"));
		}
		
		return ips.get(1);
	}
	
	/**
	 * @param systemPropertiesProvider
	 *            the systemPropertiesProvider to set
	 */
	public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
		this.systemPropertiesProvider = systemPropertiesProvider;
	}
}
