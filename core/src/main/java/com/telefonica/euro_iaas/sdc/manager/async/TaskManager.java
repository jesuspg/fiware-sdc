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

package com.telefonica.euro_iaas.sdc.manager.async;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.TaskSearchCriteria;

/**
 * Provides the methods to work with tasks.
 * 
 * @author Sergio Arroyo
 */
public interface TaskManager {

    /**
     * Creates the task and persist it.
     * 
     * @param task
     * @return
     */
    Task createTask(Task task);

    /**
     * Update the task.
     * 
     * @param task
     * @return the updated task.
     */
    Task updateTask(Task task);

    /**
     * Retrieve the task by id.
     * 
     * @param id
     *            the id
     * @return the task.
     * @throws EntityNotFoundException
     */
    Task load(Long id) throws EntityNotFoundException;

    /**
     * Find all tasks that match with the given criteria
     * 
     * @param criteria
     *            the search criteria
     * @return the tasks.
     */
    List<Task> findByCriteria(TaskSearchCriteria criteria);
}
