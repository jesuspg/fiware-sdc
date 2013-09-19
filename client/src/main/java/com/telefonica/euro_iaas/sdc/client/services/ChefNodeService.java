package com.telefonica.euro_iaas.sdc.client.services;

import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.model.Task;


public interface ChefNodeService {

	/**
	 * Delete the product release from the catalogue
	 * 
	 * @param chefnode Name to be deleted
	 *        
	 * @return the task
	 */
	Task delete(String vdc, String chefNodeName)  throws InvalidExecutionException;
}
