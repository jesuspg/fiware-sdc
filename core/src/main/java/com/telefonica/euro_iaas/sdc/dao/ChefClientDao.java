/**
 * 
 */
package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;

/**
 * @author jesus.movilla
 *
 */
public interface ChefClientDao {

	/**
	 * Delete the ChefClient from ChefServer 
	 * @param chefClientName the chefClientName to be deleted
	 * @throws CanNotCallChefException
	 *             if Chef Server returns an unexpected error code
	 */
	void deleteChefClient(String chefClientName) throws CanNotCallChefException;
	
	/**
	 * Get the ChefClient
	 * @param chefClientName
	 * @return
	 * @throws CanNotCallChefException
	 */
	ChefClient getChefClient (String chefClientName) throws CanNotCallChefException, EntityNotFoundException;
	
	/**
	 * FindAll ChefClients
	 * @return
	 * @throws CanNotCallChefException
	 */
	ChefClient chefClientfindByHostname(String hostname) throws EntityNotFoundException, CanNotCallChefException;
}
