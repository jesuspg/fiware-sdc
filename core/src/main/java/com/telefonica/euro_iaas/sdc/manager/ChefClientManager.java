/**
 * 
 */
package com.telefonica.euro_iaas.sdc.manager;


import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.exception.ChefClientExecutionException;

/**
 * @author jesus.movilla
 *
 */
public interface ChefClientManager {

	/** 
	 * Delete a ChefClient in ChefServer
	 * @param chefClientname the name of the chefclient to be deleted from chef server
	 * @throws ChefClientExecutionException
	 */
	void chefClientDelete(String vdc, String chefClientname) throws ChefClientExecutionException;
	
	/** 
	 * Load a ChefClient from ChefServer
	 * @param chefClientname the name of the chefclient to be deleted from chef server
	 * @throws ChefClientExecutionException
	 * @throws EntityNotFoundException
	 */
	ChefClient chefClientload(String chefClientname) 
			throws ChefClientExecutionException, EntityNotFoundException;
	
	/**
	 * Loads the chefclient whose hostname is hostname
	 * @param hostname
	 * @return ChefClient
	 */
	ChefClient chefClientfindByHostname(String hostname) 
				throws ChefClientExecutionException, EntityNotFoundException;
}
