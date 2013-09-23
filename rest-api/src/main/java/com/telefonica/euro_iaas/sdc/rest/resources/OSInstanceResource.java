package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.OSInstance;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 *  Provides a rest api to works with OS Instances
 *
 * @author Sergio Arroyo
 *
 */
public interface OSInstanceResource {

    /**
     * Get an instance of the selected OS running in the selected host
     * @param os the os name according to OS catalog available in the system
     * @param vm the host where the os will be deployed.
     * <p> <b>Note:</b> this parameter can be <code>null</code>. In this case
     * the hostname will be the osName + timestamp (i.e. debian5-110531120129)</p>
     * @return the created osInstance
     */
    @POST
    @Path("/")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    OSInstance bootOS(@FormParam("os")String os, @FormParam("vm")VM vm);


    /**
     * Creates an image file of the selecte OS running on a host.
     * <p> <b>IMPORTANT:</b> This OS will  be stopped after this operation.</p>
     * @param osId the instance id
     * @return the updated os instance
     */
    @PUT
    @Path("/{osId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    OSInstance freezeOS(@PathParam("osId")Long osId);


    /**
     * Retrieve all OSInstance created in the system.
     * @return the created OS instances.
     */
    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    List<OSInstance> findAll();

    /**
     * Retrieve the selected OSInstance.
     * @param id the osInstance id
     * @return the created OS instances.
     * @throws EntityNotFoundException if the osInstance does not exists
     */
    @GET
    @Path("/{osId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    OSInstance load(@PathParam("osId") Long id) throws EntityNotFoundException;
}
