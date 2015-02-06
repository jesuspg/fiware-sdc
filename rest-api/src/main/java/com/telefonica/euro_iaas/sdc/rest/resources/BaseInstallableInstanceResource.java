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
    @Path("/{name}/release/{newVersion}")
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
