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

import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

/**
 * @author jesus.movilla
 *
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
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ProductRelease insert(ProductReleaseDto productRelease) throws AlreadyExistsProductReleaseException,
                    InvalidProductReleaseException;
  
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
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<ProductRelease> findAll( @QueryParam("pName") String pName, @QueryParam("osType") String osType, @QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
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
        throws EntityNotFoundException;

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
        throws ProductReleaseNotFoundException, ProductReleaseStillInstalledException;

    /**
     * Update the ProductRelease in BBDD, the associated Recipe in chef server and the installable files in webdav.
     * 
     * @param multipart
     *            which includes
     *            <ol>
     *            <li>The ProductReleaseDto: contains the information about the product</li>
     *            <li>The cookbook: a tar file containing the whole cookbook.</li>
     *            <li>The binary files: if the product needs some files which isn't in the OS repositories, a tar file
     *            containing the structure will be deploying in a webdav.</li>
     *            </ol>
     * @throws ProductReleaseNotFoundException
     *             if the Product Release does not exists
     * @throws InvalidProductReleaseException
     *             if the Product Release is still installed on some VMs
     * @throws InvalidProductReleaseUpdateRequestException
     *             if the Product is invalid
     * @throws InvalidMultiPartRequestException
     *             fi the Multipart is Invalid
     */
    @PUT
    @Path("/{version}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes("multipart/mixed")
    ProductRelease update(MultiPart multipart) throws ProductReleaseNotFoundException, InvalidProductReleaseException,
                    InvalidProductReleaseUpdateRequestException, InvalidMultiPartRequestException;

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
        throws EntityNotFoundException;

}
