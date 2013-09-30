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

/**
 * Provides a rest api to works with EnvironmentInstance.
 * 
 * @author Jesus M. Movilla
 */
public interface EnvironmentInstanceResource {

    /**
     * Add the selected EnvironmentInstance in to the SDC's catalog. If the Environment already exists, then add the new
     * Release.
     * 
     * @param EnvironmentInstanceDto
     *            <ol>
     *            <li>The EnvironmentInstanceDto: contains the information about the product</li>
     *            </ol>
     * @return the environmentInstance.
     * @throws AlreadyExistsProductInstanceException
     *             if the Product Release exists
     * @throws InvalidProductInstanceException
     *             if the Product Instance is invalid
     * @throws EnvironmentNotFoundException
     *             id Environment is not in BBDD
     * @throws ProductInstanceNotFoundException
     *             if ProductInstance is not in BBDD
     */
    /*
     * @POST
     * @Path("/")
     * @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
     * @Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) EnvironmentInstance
     * insert(EnvironmentInstanceDto environmentInstanceDto) throws AlreadyExistsEnvironmentInstanceException,
     * InvalidEnvironmentInstanceException, EnvironmentNotFoundException, ProductInstanceNotFoundException;
     */

    /**
     * Retrieve all EnvironmentInstances available created in the system.
     * 
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @return the EnvironmentInstances.
     */

    /*
     * @GET
     * @Path("/")
     * @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) List<EnvironmentInstance>
     * findAll(@QueryParam("page") Integer page,
     * @QueryParam("pageSize") Integer pageSize,
     * @QueryParam("orderBy") String orderBy,
     * @QueryParam("orderType") String orderType);
     */

    /**
     * Retrieve the selected Environment.
     * 
     * @param name
     *            the environment name
     * @return the environment.
     * @throws EnvironmentNotFoundException
     *             if the environment does not exist
     */
    /*
     * @GET
     * @Path("/{Id}")
     * @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) EnvironmentInstance load(@PathParam("Id")
     * Long Id) throws EnvironmentInstanceNotFoundException;
     */

    /**
     * Delete the EnvironmentInstance in BBDD,
     * 
     * @param name
     *            the env name
     * @throws EnvironmentInstanceNotFoundException
     *             if the EnvironmentInstance does not exists
     * @throws ApplicationInstanceStillInstalledException
     */

    /*
     * @DELETE
     * @Path("/{Id}")
     * @Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) void delete(@PathParam("Id") Long Id)
     * throws EnvironmentInstanceNotFoundException, ApplicationInstanceStillInstalledException;
     */

    /**
     * Update the EnvironmentInstance in BBDD,
     * 
     * @param EnvironmentInstanceDto
     *            the EnvironmentInstance
     * @throwsEnvironmentInstanceNotFoundException if the EnvironmentInstance does not exists
     * @throws InvalidEnvironmentInstanceException
     *             if the EnvironmentInstance is not valid
     * @throws EnvironmentNotFoundException
     *             if the Environment is not in BBDD
     * @throws ProductInstanceNotFoundException
     *             if any ProdutInstance is not in BBDD
     */

    /*
     * @PUT
     * @Path("/")
     * @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
     * @Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) EnvironmentInstance
     * update(EnvironmentInstanceDto environmentInstanceDto) throws EnvironmentInstanceNotFoundException,
     * InvalidEnvironmentInstanceException,EnvironmentNotFoundException, ProductInstanceNotFoundException;
     */
}
