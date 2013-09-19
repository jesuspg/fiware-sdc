package com.telefonica.euro_iaas.sdc.manager;

import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Defines the operacion over ChefNodes in ChefServer
 * 
 * @author Jesus M. Movilla
 * 
 */
public interface ChefNodeManager {

	/** 
	 * Delete a ChefNode in ChefServer
	 * @param chefNodeName the name of the node to be deleted from chef server
	 * @throws NodeExecutionException
	 */
	void chefNodeDelete(String vdc, String chefNodename) throws NodeExecutionException;
}
