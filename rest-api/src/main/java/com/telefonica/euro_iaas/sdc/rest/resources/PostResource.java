package com.telefonica.euro_iaas.sdc.rest.resources;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Interface that defines the basic operations that a resource should process
 * @author Cristian Jaramillo
 *
 */

public interface PostResource {
	/**
	 * 
	 * @param formParams typical web form. Each resource should recover its own params
	 */
	public void processPetition(String resourceID, MultivaluedMap<String, String> formParams) throws Exception;
}
