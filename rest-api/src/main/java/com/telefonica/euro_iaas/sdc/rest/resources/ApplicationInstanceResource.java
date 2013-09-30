package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.IncompatibleProductsException;
import com.telefonica.euro_iaas.sdc.exception.NotInstalledProductsException;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationInstanceDto;

/**
 * Provides a rest api to works with ApplicationInstances
 * 
 * @author Sergio Arroyo
 */
public interface ApplicationInstanceResource extends BaseInstallableInstanceResource {

    /**
     * Install a list of application in a given host running on the selected products.
     * 
     * @param vdc
     *            the vdc where the application will be installed.
     * @param application
     *            the application to install containing the VM, the appName and the products where the application is
     *            going to be installed
     * @param callback
     *            if not null, contains the url where the system shall notify when the task is done
     * @return the task referencing the installed application.
     * @throws IncompatibleProductsException
     *             if the selected products are not compatible with the given application
     * @throws AlreadyInstalledException
     *             if the application is running on the system
     * @throws NotInstalledProductsException
     *             if the needed products to install the application are not installed
     */
    @POST
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task install(@PathParam("vdc") String vdc, ApplicationInstanceDto application,
            @HeaderParam("callback") String callback);

    /**
     * Retrieve all ApplicationInstance that match with a given criteria.
     * 
     * @param hostname
     *            the host name where the product is installed (<i>nullable</i>)
     * @param domain
     *            the domain where the machine is (<i>nullable if hostaname is null</i>)
     * @param ip
     *            the ip of the host (<i>nullable</i>)
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @param status
     *            the status the product (<i>nullable</i>)
     * @return the retrieved application instances.
     */
    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<ApplicationInstance> findAll(@QueryParam("hostname") String hostname, @QueryParam("domain") String domain,
            @QueryParam("ip") String ip, @QueryParam("fqn") String fqn, @QueryParam("page") Integer page,
            @QueryParam("pageSize") Integer pageSize, @QueryParam("orderBy") String orderBy,
            @QueryParam("orderType") String orderType, @QueryParam("status") List<Status> status,
            @PathParam("vdc") String vdc, @QueryParam("applicationName") String applicationName);

    /**
     * Retrieve the selected application instance.
     * 
     * @param name
     *            the application name
     * @return the application instance
     */
    @GET
    @Path("/{name}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ApplicationInstance load(@PathParam("name") String name);

}
