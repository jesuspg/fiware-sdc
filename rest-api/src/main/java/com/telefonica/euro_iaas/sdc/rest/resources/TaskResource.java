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

import java.util.Date;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;

/**
 * Provides the way to find some tasks stored in the system.
 * 
 * @author Sergio Arroyo
 */
public interface TaskResource {

    /**
     * Find a task for a given id.
     * 
     * @param id
     *            the task's id
     * @return the task
     * @throws EntityNotFoundException
     */
    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task load(@PathParam("id") Long id) throws EntityNotFoundException;

    /**
     * Retrieve the tasks that match with the given criteria
     * 
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @param states
     *            the list containing the task states
     * @param resource
     *            the resource url the task is related to
     * @param owner
     *            the task's owner url
     * @param fromDate
     *            the initial date where the task was created (included)
     * @param toDate
     *            the final date where the task was created (included)
     * @return the tasks that match with the criteria.
     */
    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Task> findAll(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType,
            @QueryParam("states") List<TaskStates> states, @QueryParam("result") String resource,
            @QueryParam("owner") String owner, @QueryParam("fromDate") Date fromDate,
            @QueryParam("toDate") Date toDate, @PathParam("vdc") String vdc);

}
