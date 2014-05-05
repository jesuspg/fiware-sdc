/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
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
