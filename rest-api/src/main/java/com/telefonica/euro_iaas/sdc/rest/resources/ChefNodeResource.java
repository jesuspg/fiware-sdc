/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

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
     * 
     * @param chefNodename
     *            the vmname to be deleted from Chef server
     * @return the state of last Node
     * @throws NodeExecutionException
     */
    @DELETE
    @Path("/{chefNodeName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task delete(@PathParam("vdc") String vdc, @PathParam("chefNodeName") String chefNodeName,
            @HeaderParam("callback") String callback) throws NodeExecutionException;
}
