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

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.MultiPart;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.rest.exception.APIException;

/**
 * @author jesus.movilla
 */
public interface ProductReleaseResource {

    /**
     * Add the selected Product Release in to the SDC's catalog. If the Product already exists, then add the new
     * Release. If not, this method also creates the product.
     * 
     * @param productRelease
     *            the producRelease object
     * @return the productrelease.
     * @throws AlreadyExistsProductReleaseException
     *             if the Product Release exists
     * @throws InvalidProductReleaseException
     *             if the Product Release is invalid due to either OS, Product or Product Release
     */
    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ProductRelease insert(@PathParam("pName") String pName, ProductReleaseDto productRelease)
        throws APIException;

    /**
     * Retrieve all available versions of the given product.
     * 
     * @param osType
     *            the operating system (<i>nullable</i>)
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @return the ProductReleases.
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<ProductRelease> findAll(@PathParam("pName") String pName, @QueryParam("osType") String osType,
            @QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType);

    /**
     * Retrieve the selected Product version.
     * 
     * @param name
     *            the product name
     * @param version
     *            the concrete version
     * @return the product.
     * @throws EntityNotFoundException
     *             if the product or the version does not exists
     */
    @GET
    @Path("/{version}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ProductRelease load(@PathParam("pName") String name, @PathParam("version") String version)
        throws APIException;

    /**
     * Delete the ProductRelease in BBDD, the associated Recipe in chef server and the installable files in webdav.
     * 
     * @param name
     *            the product name
     * @param version
     *            the concrete version
     * @throws ProductReleaseNotFoundException
     *             if the Product Release does not exists
     * @throws ProductReleaseStillInstalledException
     *             if the Product Release is still installed on some VMs
     */

    @DELETE
    @Path("/{version}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void delete(@PathParam("pName") String name, @PathParam("version") String version)
        throws APIException;

    /**
     * Find all possible transitions for a concrete release. It means, the different version of a product which are
     * compatible with the given release.
     * 
     * @param name
     *            the product Name
     * @param version
     *            the product version
     * @return the transitable releases.
     * @throws EntityNotFoundException
     *             if the given release does not exists.
     */
    @GET
    @Path("/{version}/updatable")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<ProductRelease> findTransitable(@PathParam("pName") String name, @PathParam("version") String version)
         throws APIException;

}
