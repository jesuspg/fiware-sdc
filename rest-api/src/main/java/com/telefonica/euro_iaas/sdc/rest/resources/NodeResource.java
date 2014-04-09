/**
 * (c) Copyright 2014 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ChefClientExecutionException;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

/**
 * @author alberts
 */
public interface NodeResource {

    
    /**
     * Retrieve all ChefClients available in ChefServer.
     * 
     * @return the chefclients.
     */
    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ChefClient findByHostname(@QueryParam("hostname") String hostname) throws EntityNotFoundException,
            ChefClientExecutionException;

    /**
     * Retrieve the selected ChecfClientName.
     * 
     * @param chefClientName
     *            the ChefClientName
     * @return the product.
     * @throws EntityNotFoundException
     *             if the product does not exists
     */
    @GET
    @Path("/{chefClientName}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ChefClient load(@PathParam("chefClientName") String chefClientName) throws EntityNotFoundException,
            ChefClientExecutionException;
    
    /**
     * Delete a Node from Chef/Puppet only if the client has some productInstances installed
     * 
     * @param nodename
     *            the name of the node (without domain) to be deleted from Chef/Puppet
     * @throws NodeExecutionException
     */
    @DELETE
    @Path("/{nodeName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task delete(@PathParam("vdc") String vdc, @PathParam("nodeName") String nodeName,
            @HeaderParam("callback") String callback) throws ChefClientExecutionException;
}
