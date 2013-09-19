package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.text.MessageFormat;



import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.services.ChefNodeService;
import com.telefonica.euro_iaas.sdc.model.Task;

public class ChefNodeServiceImpl extends AbstractBaseService  
	implements ChefNodeService {


	
	/**
	 * @param client
	 * @param baseHost
	 * @param type
	 */
	public ChefNodeServiceImpl(Client client, String baseHost,
			String type) {
		setClient(client);
		setBaseHost(baseHost);
		setType(type);
	}
	
	public Task delete(String vdc, String chefNodeName) 
			throws InvalidExecutionException {
		try {
			
			String url = getBaseHost()
					+ MessageFormat.format(ClientConstants.CHEFNODE_PATH,
							vdc, chefNodeName);
			WebResource wr = getClient().resource(url);
			Builder builder = wr.accept(getType()).type(getType());
			//builder = addCallback(builder, callback);
			return builder.delete(Task.class);
					
		} catch (UniformInterfaceException e) {
			String errorMsg =" Error deleting a chefnode " + chefNodeName 
					+ " in vdc " + vdc + ". Description: " + e.getMessage();
			System.out.println (errorMsg);
			throw new InvalidExecutionException (errorMsg);
		} /*catch (IOException e) {
			String errorMsg =" Error deleting a chefnode " + chefNodeName 
					+ " in vdc " + vdc + ". Description: " + e.getMessage();
			throw new InvalidExecutionException (errorMsg);
		}*/
	}

}
