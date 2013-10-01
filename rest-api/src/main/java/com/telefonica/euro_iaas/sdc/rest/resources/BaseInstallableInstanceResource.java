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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;

public interface BaseInstallableInstanceResource {

    /**
     * Upgrade the selected instance version.
     * 
     * @param id
     *            the installable instance id
     * @param new version the new version to upgrade to
     * @param callback
     *            if not empty, contains the url where the result of the async operation will be sent
     * @return the task
     */
    /*
     * @PUT
     * @Path("/{id}/release/{release-id}")
     * @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
     * @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) Task upgrade(@PathParam("vdc") String vdc,
     * @PathParam("id") Long id,
     * @PathParam("newVersion") String version,
     * @HeaderParam("callback") String callback);
     */

    @PUT
    @Path("/{name}/release/{release-id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task upgrade(@PathParam("vdc") String vdc, @PathParam("name") String name, @PathParam("newVersion") String version,
            @HeaderParam("callback") String callback);

    /**
     * Configure the selected instance.
     * 
     * @param name
     *            the installable instance id
     * @param arguments
     *            the configuration properties
     * @param callback
     *            if not empty, contains the url where the result of the async operation will be sent
     * @return the task.
     */

    @PUT
    @Path("/{name}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task configure(@PathParam("vdc") String vdc, @PathParam("name") String name,
            @HeaderParam("callback") String callback, Attributes arguments);

    /**
     * Uninstall a previously installed instance.
     * 
     * @param name
     *            the installable instance id
     * @param callback
     *            if not empty, contains the url where the result of the async operation will be sent
     * @return the task.
     */

    @DELETE
    @Path("/{name}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task uninstall(@PathParam("vdc") String vdc, @PathParam("name") String name,
            @HeaderParam("callback") String callback);

}
