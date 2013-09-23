package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance.Status;

/**
 * Provides a rest api to works with ApplicationInstances
 *
 * @author Sergio Arroyo
 *
 */
public interface ApplicationInstanceResource {

    /**
     * Install a list of application in a given host.
     *
     * @param hostname
     *            the host where the product will be installed
     * @param domain
     *            the domain where the host is
     * @param ip
     *            the ip where the machine is located
     * @param products
     *            the list of products to install
     *
     * @return the installed application.
     */
    @POST
    @Path("/action/install")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    ApplicationInstance install(@FormParam("hostname") String hostname,
            @FormParam("domain") String domain, @FormParam("ip") String ip,
            @FormParam("products") List<String> products,
            @FormParam("application") String appname);

    /**
     * Uninstall a previously installed application.
     *
     * @param applicationId
     *            the candidate to uninstall
     */
    @DELETE
    @Path("/{aId}/action/uninstall")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void uninstall(@PathParam("aId") Long applicationId);

    /**
     * Retrieve all ApplicationInstance that match with a given criteria.
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
     * @return the retrieved application instances.
     */
    @GET
    @Path("/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<ApplicationInstance> findAll(@QueryParam("page") Integer page,
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
    @Path("/{aId}")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ApplicationInstance load(@PathParam("aId") Long id)
            throws EntityNotFoundException;
}
