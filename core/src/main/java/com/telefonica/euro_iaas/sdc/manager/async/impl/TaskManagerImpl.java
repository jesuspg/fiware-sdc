package com.telefonica.euro_iaas.sdc.manager.async.impl;

import java.text.MessageFormat;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.TaskDao;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.TaskSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;


import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.TASK_BASE_URL;

/**
 * Default TaskManager implementation.
 * 
 * @author Sergio Arroyo
 */
public class TaskManagerImpl implements TaskManager {

    private TaskDao taskDao;
    private SystemPropertiesProvider propertiesProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public Task createTask(Task task) {
        try {
            task = taskDao.create(task);
            String taskId = Long.valueOf(task.getId()).toString().replace(".", "");
            task.setHref(MessageFormat.format(propertiesProvider.getProperty(TASK_BASE_URL), Long.valueOf(task.getId())
                    .toString(), task.getVdc()));

            return task;
        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(e);
        } catch (AlreadyExistsEntityException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task updateTask(Task task) {
        try {
            task = taskDao.update(task);
            task.setHref(MessageFormat.format(propertiesProvider.getProperty(TASK_BASE_URL), Long.valueOf(task.getId())
                    .toString(), task.getVdc()));
            return task;
        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task load(Long id) throws EntityNotFoundException {
        Task task = taskDao.load(id);
        task.setHref(MessageFormat.format(propertiesProvider.getProperty(TASK_BASE_URL), Long.valueOf(task.getId())
                .toString(), task.getVdc()));
        return task;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> findByCriteria(TaskSearchCriteria criteria) {
        List<Task> tasks = taskDao.findByCriteria(criteria);
        String taskUrl = propertiesProvider.getProperty(TASK_BASE_URL);
        for (Task task : tasks) {
            task.setHref(MessageFormat.format(taskUrl, Long.valueOf(task.getId()).toString(), task.getVdc()));
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

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

}
