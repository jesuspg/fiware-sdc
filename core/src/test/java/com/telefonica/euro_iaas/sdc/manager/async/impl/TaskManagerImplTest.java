/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.manager.async.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.TaskDao;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.TaskSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class TaskManagerImplTest {

    TaskManagerImpl taskManager;
    TaskDao taskDao;
    SystemPropertiesProvider propertiesProvider;

    @Before
    public void setUp() {
        taskManager = new TaskManagerImpl();
        taskDao = mock(TaskDao.class);
        propertiesProvider = mock(SystemPropertiesProvider.class);
        taskManager.setTaskDao(taskDao);
        taskManager.setPropertiesProvider(propertiesProvider);

    }

    @Test
    public void shouldCreatesTask() throws AlreadyExistsEntityException, InvalidEntityException {
        // given
        Task task = new Task();
        Task createdTask = new Task();
        createdTask.setId(1L);

        // when

        when(taskDao.create(task)).thenReturn(createdTask);
        when(propertiesProvider.getProperty(SystemPropertiesProvider.TASK_BASE_URL)).thenReturn("http://url");
        Task resultTask = taskManager.createTask(task);

        // then
        assertNotNull(resultTask);
        verify(taskDao).create(task);
        verify(propertiesProvider).getProperty(SystemPropertiesProvider.TASK_BASE_URL);
    }

    @Test(expected = SdcRuntimeException.class)
    public void shouldThrowExceptionInCreatesTaskWhenAlreadyExistsEntity() throws AlreadyExistsEntityException,
            InvalidEntityException {
        // given
        Task task = new Task();

        // when

        when(taskDao.create(task)).thenThrow(new AlreadyExistsEntityException("already exist"));
        taskManager.createTask(task);

        // then

    }

    @Test(expected = SdcRuntimeException.class)
    public void shouldThrowExceptionInCreatesTaskWhenInvalidEntity() throws AlreadyExistsEntityException,
            InvalidEntityException {
        // given
        Task task = new Task();

        // when

        when(taskDao.create(task)).thenThrow(new InvalidEntityException("invalid entity"));
        taskManager.createTask(task);

        // then

    }

    @Test
    public void shouldUpdateTask() throws AlreadyExistsEntityException, InvalidEntityException {
        // given
        Task task = new Task();
        Task updatedTask = new Task();
        updatedTask.setId(1L);

        // when

        when(taskDao.update(task)).thenReturn(updatedTask);
        when(propertiesProvider.getProperty(SystemPropertiesProvider.TASK_BASE_URL)).thenReturn("http://url");
        Task resultTask = taskManager.updateTask(task);

        // then
        assertNotNull(resultTask);
        verify(taskDao).update(task);
        verify(propertiesProvider).getProperty(SystemPropertiesProvider.TASK_BASE_URL);
    }

    @Test(expected = SdcRuntimeException.class)
    public void shouldThrowExceptionInUpdatesTaskWhenInvalidEntity() throws InvalidEntityException {
        // given
        Task task = new Task();

        // when

        when(taskDao.update(task)).thenThrow(new InvalidEntityException("invalid entity"));
        taskManager.updateTask(task);

        // then

    }

    @Test
    public void shouldReturnEmptyListInFindByCriteria() {
        // given

        TaskSearchCriteria criteria = new TaskSearchCriteria();
        List<Task> tasks = new ArrayList<Task>(2);
        // when
        when(taskDao.findByCriteria(criteria)).thenReturn(tasks);
        List<Task> result = taskManager.findByCriteria(criteria);

        // then
        assertEquals(result.size(), 0);
    }

    @Test
    public void shouldReturnTwoElementsInFindByCriteriaWithTwoElementsInDatabase() {
        // given

        TaskSearchCriteria criteria = new TaskSearchCriteria();
        List<Task> tasks = new ArrayList<Task>(2);
        Task task1 = new Task();
        task1.setId(1L);
        task1.setVdc("vdc1");
        Task task2 = new Task();
        task2.setId(2L);
        task2.setVdc("vdc2");

        tasks.add(task1);
        tasks.add(task2);

        // when
        when(taskDao.findByCriteria(criteria)).thenReturn(tasks);
        when(propertiesProvider.getProperty(SystemPropertiesProvider.TASK_BASE_URL)).thenReturn("http://url");
        List<Task> result = taskManager.findByCriteria(criteria);

        // then
        assertEquals(result.size(), 2);
    }

    @Test
    public void shouldLoadTask() throws EntityNotFoundException {
        // given

        Task task1 = new Task();
        task1.setId(1L);
        // when
        when(taskDao.load(1L)).thenReturn(task1);
        when(propertiesProvider.getProperty(SystemPropertiesProvider.TASK_BASE_URL)).thenReturn("http://url");
        Task resultTask = taskManager.load(1L);

        // then
        assertNotNull(resultTask);
        verify(taskDao).load(1L);
        verify(propertiesProvider).getProperty(SystemPropertiesProvider.TASK_BASE_URL);
    }

}
