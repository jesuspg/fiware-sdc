package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;

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
     * @param product
     *            the concrete release of a product to install. It also contains
     *            information about the VM where the product is going to be
     *            installed
     * @param callback if not empty, contains the url where the result of the
     * async operation will be sent
     *
     * @return the installed product.
     */
    @POST
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task install(ProductInstanceDto product,
            @HeaderParam("callback") String callback)
            throws NodeExecutionException, AlreadyInstalledException;

    /**
     * Upgrade the selected product version.
     *
     * @param host the vm where the product is installed
     * @param domain the domain where is the vm
     * @param name the name of the product
     * @param new version the new version to upgrade to
     * @param callback if not empty, contains the url where the result of the
     * async operation will be sent
     *
     * @return the task
     */
    @PUT
    @Path("/{host}/{domain}/{name}/{newVersion}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task upgrade(@PathParam("host") String host,
            @PathParam("domain") String domain,
            @PathParam("name") String name,
            @PathParam("newVersion") String version,
            @HeaderParam("callback") String callback);

    /**
     * Configure the selected product.
     *
     * @param host the vm where the product is installed
     * @param domain the domain where is the vm
     * @param name the name of the product
     * @param arguments
     *            the configuration properties
     * @param callback if not empty, contains the url where the result of the
     * async operation will be sent
     *
     * @return the task.
     */
    @PUT
    @Path("/{host}/{domain}/{name}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task configure(@PathParam("host") String host,
            @PathParam("domain") String domain,
            @PathParam("name") String name,
            @HeaderParam("callback") String callback, Attributes arguments);

    /**
     * Uninstall a previously installed product.
     *
     * @param host the vm where the product is installed
     * @param domain the domain where is the vm
     * @param name the name of the product
     * @param callback if not empty, contains the url where the result of the
     * async operation will be sent
     * @return the task.
     */
    @DELETE
    @Path("/{host}/{domain}/{name}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task uninstall(@PathParam("host") String host,
            @PathParam("domain") String domain,
            @PathParam("name") String name,
            @HeaderParam("callback") String callback);

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
     *            defines if the order is ascending or descending (asc by
     *            default <i>nullable</i>)
     * @param status
     *            the status the product (<i>nullable</i>)
     * @return the product instances that match with the criteria.
     */
    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<ProductInstance> findAll(@QueryParam("hostname") String hostname,
            @QueryParam("domain") String domain, @QueryParam("ip") String ip,
            @QueryParam("page") Integer page,
            @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy,
            @QueryParam("orderType") String orderType,
            @QueryParam("status") Status status);


    /**
     * Retrieve the selected product instance.
     * @param host the vm where the product is installed
     * @param domain the domain where is the vm
     * @param name the name of the product
     * @return the product instance
     * @throws EntityNotFoundException
     */
    @GET
    @Path("/{host}/{domain}/{name}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ProductInstance load(@PathParam("host") String host,
            @PathParam("domain") String domain,
            @PathParam("name") String name)
            throws EntityNotFoundException;
}
