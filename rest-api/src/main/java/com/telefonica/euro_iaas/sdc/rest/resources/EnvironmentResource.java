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

import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsEnvironmentException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.EnvironmentNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.InvalidEnvironmentException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseInApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentDto;

/**
 * Provides a rest api to works with Environment.
 *
 * @author Jesus M. Movilla
 *
 */
public interface EnvironmentResource {

    /**
     * Add the selected Environment in to the SDC's catalog. 
     * If the Environment
     * already exists, then add the new Release. 
     *
     * @param EnvironmentDto
     * <ol>
     * <li>The EnvironmentDto: contains the information about the product</li>
     * </ol>
     * @return the environment.
     * @throws AlreadyExistsProductReleaseException if the Product Release exists
     * @throws InvalidProductReleaseException if the Product Release is invalid
     * due to either OS, Product or Product Release
     * @throws InvalidMultiPartRequestException when the MUltipart object
     * in the request is null, or its size is different to three or the type sof
     * the different parts are not ProductReleaseDto, File and File
     *
     */
    @POST
    @Path("/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Environment insert(EnvironmentDto environmentDto)
        throws AlreadyExistsEnvironmentException,
        InvalidEnvironmentException, ProductNotFoundException, 
        ProductReleaseNotFoundException;

    /**
     * Retrieve all Environments available created in the system.
     *
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query
     *            (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending
     *            (asc by default <i>nullable</i>)
     * @return the Environments.
     */
    
    @GET
    @Path("/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Environment> findAll(@QueryParam("page") Integer page,
            @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy,
            @QueryParam("orderType") String orderType);

    /**
     * Retrieve the selected Environment.
     *
     * @param name
     *            the environment name
     * @return the environment.
     * @throws EnvironmentNotFoundException
     *             if the environment does not exist
     */
    @GET
    @Path("/{envName}")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Environment load(@PathParam("envName") String name)
        throws EnvironmentNotFoundException;
    
    /**
     * Delete the Environment in BBDD, 
     * @param name
     *            the env name
     * @throws EnvironmentNotFoundException   if the Environment does not exists
     * @throws ProductReleaseStillInstalledException
     * @thorws ProductReleaseInApplicationReleaseException
     */

    @DELETE
    @Path("/{envName}")
    @Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void delete(@PathParam("envName") String envName)
    throws EnvironmentNotFoundException, ProductReleaseStillInstalledException,
    ProductReleaseInApplicationReleaseException;

    /**
     * Update the Environment in BBDD, 
     * @param EnvironmentDto
     *            the product name
     * @throws EnvironmentNotFoundException
     *             if the Environment does not exists
     *  @throws InvalidEnvironmentException
     *             if the Environment is not valid
     *  @throws ProductReleaseNotFoundException
     *             if the ProductRelease does not exists
     *  @throws ProductReleaseNotFoundException
     *             if the ProductRelease does not exists
     */

    @PUT
    @Path("/{envName}")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Environment update(EnvironmentDto environmentDto)
        throws EnvironmentNotFoundException,
        InvalidEnvironmentException, 
        ProductReleaseNotFoundException, 
        ProductNotFoundException;
}
