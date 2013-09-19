/**
 * 
 */
package com.telefonica.euro_iaas.sdc.client.services;

import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.model.Task;


import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

/**
 * @author jesus.movilla
 *
 */
public interface ChefClientService {

	/**
	 * Delete the ChefClient from the Chef Server
	 * 
	 * @param chefClientName to be deleted
	 *        
	 * @return the task
	 */
	Task delete(String vdc, String chefClientName)  throws InvalidExecutionException;
	
	/**
	 * Load the ChefClient
	 * @param vdc
	 * @return
	 */
	ChefClient load(String vdc, String chefClientName) throws ResourceNotFoundException;
	
	/**
	 * Load by hostname the ChefClient
	 * @param vdc
	 * @return
	 */
	ChefClient loadByHostname(String vdc, String hostname) throws ResourceNotFoundException;
}
