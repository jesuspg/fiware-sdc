package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;

/**
 * Provides a rest api to works with ProductInstances
 *
 * @author Sergio Arroyo
 *
 */
public interface ProductInstanceResource {

    /**
     * Install a product in a given host.
     *
     * @param hostname
     *            the host where the product will be installed
     * @param domain
     *            the domain where the host is
     * @param ip
     *            the ip where the machine is located
     * @param product
     *            the product to install
     *
     * @return the installed product.
     */
    @POST
    @Path("/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    ProductInstance install(@FormParam("hostname") String hostname,
            @FormParam("domain") String domain, @FormParam("ip") String ip,
            @FormParam("product") String product);

    /**
     * Configure the selected product.
     *
     * @param id
     *            the product instance id
     * @param arguments
     *            the configuration properties
     *
     * @return the configured product.
     */
    @PUT
    @Path("/{pId}")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_XML)
    ProductInstance configure(@PathParam("pId") Long id,
            Attributes arguments);

    /**
     * Uninstall a previously installed product.
     *
     * @param productId
     *            the candidate to uninstall
     */
    @DELETE
    @Path("/{pId}")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void uninstall(@PathParam("pId") Long productId);

    /**
     * Retrieve all OSInstance created in the system.
     *
     * @param hostname
     *            the host name where the product is installed (<i>nullable</i>)
     * @param domain
     *            the domain where the machine is (<i>nullable</i>)
     * @param ip
     *            the ip of the host (<i>nullable</i>)
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
     * @param status the status the product (<i>nullable</i>)
     * @return the created OS instances.
     */
    @GET
    @Path("/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<ProductInstance> findAll(@QueryParam("hostname") String hostname,
            @QueryParam("domain") String domain, @QueryParam("ip") String ip,
            @QueryParam("page") Integer page,
            @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy,
            @QueryParam("orderType") String orderType,
            @QueryParam("status") Status status);

    /**
     * Retrieve the selected OSInstance.
     *
     * @param id
     *            the osInstance id
     * @return the created OS instances.
     * @throws EntityNotFoundException
     *             if the osInstance does not exists
     */
    @GET
    @Path("/{pId}")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ProductInstance load(@PathParam("pId") Long id)
            throws EntityNotFoundException;
}
