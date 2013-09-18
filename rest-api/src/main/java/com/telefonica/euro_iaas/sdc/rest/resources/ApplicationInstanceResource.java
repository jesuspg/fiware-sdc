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

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.IncompatibleProductsException;
import com.telefonica.euro_iaas.sdc.exception.NotInstalledProductsException;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;

/**
 * Provides a rest api to works with ApplicationInstances
 *
 * @author Sergio Arroyo
 *
 */
public interface ApplicationInstanceResource {

    /**
     * Install a list of application in a given host running
     * on the selected products.
     *
     * @param application the application to install containing the VM,
     *  the appName and the products where the application is going to
     *  be installed
     *
     * @return the installed application.
     * @throws IncompatibleProductsException if the selected products are
     * not compatible with the given application
     * @throws AlreadyInstalledException if the application is running on the
     *  system
     * @throws NotInstalledProductsException if the needed products to install
     *  the application are not installed
     */
    @POST
    @Path("/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    ApplicationInstance install(ApplicationInstanceDto application)
    throws NodeExecutionException, AlreadyInstalledException,
    IncompatibleProductsException, NotInstalledProductsException;

    /**
     * Uninstall a previously installed application.
     *
     * @param applicationId
     *            the candidate to uninstall
     */
    @DELETE
    @Path("/{aId}")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void uninstall(@PathParam("aId") Long applicationId)
    throws NodeExecutionException, FSMViolationException;

    /**
     * Configure the selected application.
     *
     * @param id
     *            the application instance id
     * @param arguments
     *            the configuration properties
     *
     * @return the configured application.
     */
    @PUT
    @Path("/{aId}")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_XML)
    ApplicationInstance configure(@PathParam("aId") Long id,
            Attributes arguments)
    throws NodeExecutionException, FSMViolationException ;


    /**
     * Upgrade the selected product version.
     *
     * @param id
     *            the application instance id
     * @param new version
     *            the new version to upgrade to
     *
     * @return the configured product Instance.
     */
    @PUT
    @Path("/{aId}/{newVersion}")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ApplicationInstance upgrade(@PathParam("aId") Long id,
      @PathParam("newVersion") String version)
    throws NodeExecutionException, IncompatibleProductsException,
    FSMViolationException ;

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
            @QueryParam("status") List<Status> status);

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
