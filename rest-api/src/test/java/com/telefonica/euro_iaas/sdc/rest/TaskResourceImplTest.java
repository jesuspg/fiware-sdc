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

package com.telefonica.euro_iaas.sdc.rest;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.telefonica.euro_iaas.sdc.model.searchcriteria.TaskSearchCriteria;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;


import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.rest.exception.APIException;
import com.telefonica.euro_iaas.sdc.rest.resources.TaskResourceImpl;

public class TaskResourceImplTest {
    private TaskResourceImpl taskResource = null;
    private TaskManager taskManager = null;

    @Before
    public void setUp() throws Exception {
        taskResource = new TaskResourceImpl();
        taskManager = mock(TaskManager.class);
        taskResource.setTaskManager(taskManager);
    }

    /**
     * It tests loading of a task.
     *
     * @throws Exception
     */
    @Test
    public void testLoad() throws Exception {
        Task task = new Task();
        when(taskManager.load(anyLong())).thenReturn(task);
        task = taskResource.load(new Long(1));
        assertNotNull(task);
    }

    /**
     * It test loading a task with error.
     * @throws Exception
     */
    @Test(expected=EntityNotFoundException.class)
    public void testLoadError() throws Exception {
        when(taskManager.load(anyLong())).
            thenThrow(new EntityNotFoundException(Task.class, "men", "task"));
        taskResource.load(new Long(1));
    }

    /**
     * It tests find all
     * @throws Exception
     */
    @Test
    public void testFindAll() throws Exception {
        List<Task> tasks = new ArrayList();
        when(taskManager.findByCriteria(any(TaskSearchCriteria.class))).thenReturn(tasks);
        tasks = taskResource.findAll(new Integer(1), new Integer(1), "", "", null, "", "", null, null, "vdc");
        assertNotNull(tasks);
    }



}
