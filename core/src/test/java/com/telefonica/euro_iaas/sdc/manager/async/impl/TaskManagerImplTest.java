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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.fiware.commons.dao.AlreadyExistsEntityException;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.TaskDao;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.TaskSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class TaskManagerImplTest {

    TaskManagerImpl taskManager;
    private SystemPropertiesProvider propertiesProvider;
    
    TaskDao taskDao;


    @Before
    public void setUp() {
        taskManager = new TaskManagerImpl();
        taskDao = mock(TaskDao.class);
        propertiesProvider = mock (SystemPropertiesProvider.class);
        taskManager.setTaskDao(taskDao);
        taskManager.setSystemPropertiesProvider(propertiesProvider);
        
        when(propertiesProvider.getProperty(anyString())).thenReturn("dd");


    }

    @Test
    public void shouldCreatesTask() throws AlreadyExistsEntityException, InvalidEntityException {
        // given
        Task task = new Task();
        Task createdTask = new Task();
        createdTask.setId(1L);

        // when

        when(taskDao.create(task)).thenReturn(createdTask);
        Task resultTask = taskManager.createTask(task);

        // then
        assertNotNull(resultTask);
        verify(taskDao).create(task);

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

    @Test
    public void shouldUpdateTask() throws AlreadyExistsEntityException {
        // given
        Task task = new Task();
        Task updatedTask = new Task();
        updatedTask.setId(1L);

        // when

        when(taskDao.update(task)).thenReturn(updatedTask);
        Task resultTask = taskManager.updateTask(task);

        // then
        assertNotNull(resultTask);
        verify(taskDao).update(task);

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

        Task resultTask = taskManager.load(1L);

        // then
        assertNotNull(resultTask);
        verify(taskDao).load(1L);

    }

}
