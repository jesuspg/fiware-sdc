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

package com.telefonica.euro_iaas.sdc.manager.async.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.SDC_MANAGER_URL;

import java.text.MessageFormat;
import java.util.List;

import com.telefonica.fiware.commons.dao.AlreadyExistsEntityException;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.TaskDao;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.TaskSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.Configuration;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;


/**
 * Default TaskManager implementation.
 * 
 * @author Sergio Arroyo
 */
public class TaskManagerImpl implements TaskManager {

    private TaskDao taskDao;
    private SystemPropertiesProvider systemPropertiesProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public Task createTask(Task task) {
        try {
            task = taskDao.create(task);
            task.setHref(getHrefUrl(task));
            return task;
        } catch (AlreadyExistsEntityException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task updateTask(Task task) {
        task = taskDao.update(task);
        task.setHref(getHrefUrl(task));
        return task;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task load(Long id) throws EntityNotFoundException {
        Task task = taskDao.load(id);
        task.setHref(getHrefUrl(task));
        return task;
    }

    private String getHrefUrl (Task task) {
        return MessageFormat.format(systemPropertiesProvider.getProperty(SDC_MANAGER_URL) +
            Configuration.TASK_BASE_PATH, Long.valueOf(task.getId()).toString(), task.getVdc());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> findByCriteria(TaskSearchCriteria criteria) {
        List<Task> tasks = taskDao.findByCriteria(criteria);
        for (Task task : tasks) {
            task.setHref(getHrefUrl(task));
        }
        return tasks;
    }

    // //////// I.O.C ////////

    /**
     * @param taskDao
     *            the taskDao to set
     */
    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }
    
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }


}
