package com.telefonica.euro_iaas.sdc.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.model.Task;

public interface ChefNodeResource {

	/**
	 * Delete a ChefNode from Chef Server
	 * @param chefNodename the vmname to be deleted from Chef server
	 * @return the state of last Node
	 * @throws NodeExecutionException
	 */
	@DELETE
	@Path("/{chefNodeName}")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	Task delete(@PathParam("vdc") String vdc, 
			@PathParam("chefNodeName") String chefNodeName,
			@HeaderParam("callback") String callback) throws NodeExecutionException;
}
