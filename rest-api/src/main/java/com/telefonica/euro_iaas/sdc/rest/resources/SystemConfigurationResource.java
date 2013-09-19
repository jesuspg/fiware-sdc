package com.telefonica.euro_iaas.sdc.rest.resources;


import com.telefonica.euro_iaas.sdc.model.dto.Attributes;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

public interface SystemConfigurationResource {

	/**
	 * Find all system configuration properties.
	 * 
	 * @return the configuration properties.
	 */
	@GET
	@Path("/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	Attributes findAttributes();

	/**
	 * Update the attributes and persist the new values.
	 * 
	 * @param attributes
	 *            the attributes
	 */
	@PUT
	@Path("/")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	void updateAttributes(Attributes attributes);
}
