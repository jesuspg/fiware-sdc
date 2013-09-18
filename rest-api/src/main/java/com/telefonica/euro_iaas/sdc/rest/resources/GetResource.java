package com.telefonica.euro_iaas.sdc.rest.resources;

/**
 * Interface that defines the basic operations that a resource should process
 * @author Cristian Jaramillo
 *
 */

public interface GetResource {
	/**
	 * 
	 * @param resourceID
	 * @return Depending the requested resource
	 */
	public String getResource(String resourceID) throws Exception;

}
