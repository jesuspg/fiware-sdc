/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.manager.async;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.TaskSearchCriteria;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

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
