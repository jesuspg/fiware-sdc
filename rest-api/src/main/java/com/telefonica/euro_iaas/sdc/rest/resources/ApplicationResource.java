package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.Attribute;

/**
 * Provides a rest api to works with Applications.
 *
 * @author Sergio Arroyo
 *
 */
public interface ApplicationResource {

    /**
     * Retrieve all applications available created in the system.
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
     * @return the created OS instances.
     */
    @GET
    @Path("/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Application> findAll(@QueryParam("page") Integer page,
            @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy,
            @QueryParam("orderType") String orderType);

    /**
     * Retrieve the selected Application.
     *
     * @param name
     *            the application name
     * @return the loaded application.
     * @throws EntityNotFoundException
     *             if the osInstance does not exists
     */
    @GET
    @Path("/{appName}")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Application load(@PathParam("appName") String name)
        throws EntityNotFoundException;

    /**
     * Retrieve the available attributes for a selected application.
     *
     * @param name
     *            the application name
     * @return the attributes for the selected application.
     * @throws EntityNotFoundException
     *             if the application does not exists
     */
    @GET
    @Path("/{appName}/attributes/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Attribute> loadAttributes(@PathParam("appName") String name)
        throws EntityNotFoundException;
}
