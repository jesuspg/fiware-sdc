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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.exception.ChefClientExecutionException;
import com.telefonica.euro_iaas.sdc.manager.ChefClientManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.util.TaskNotificator;

public class ChefClientAsyncManagerImplTest {

    ChefClientAsyncManagerImpl chefClientAsyncManager;
    ChefClientManager chefCientManager = mock(ChefClientManager.class);
    TaskNotificator taskNotificator = mock(TaskNotificator.class);
    SystemPropertiesProvider propertiesProvider = mock(SystemPropertiesProvider.class);
    TaskManager taskManager = mock(TaskManager.class);

    @Before
    public void setup() {
        chefClientAsyncManager = new ChefClientAsyncManagerImpl();
        chefClientAsyncManager.setChefClientManager(chefCientManager);
        chefClientAsyncManager.setTaskNotificator(taskNotificator);
        chefClientAsyncManager.setPropertiesProvider(propertiesProvider);
        chefClientAsyncManager.setTaskManager(taskManager);
    }

    @Test
    public void shouldDelete() throws ChefClientExecutionException {
        // given
        String vdc = "virtualDataCenter";
        String chefClientName = "chefClientNames";
        Task task = new Task();
        String callback = "http://callback";

        // when
        when(propertiesProvider.getProperty(SystemPropertiesProvider.CHEF_NODE_BASE_URL)).thenReturn("http://baseurl");
        chefClientAsyncManager.chefClientDelete(vdc, chefClientName, task, callback);

        // then
        verify(chefCientManager).chefClientDelete(vdc, chefClientName);
        verify(taskManager).updateTask(task);
    }

    @Test
    public void shouldUpdateErrorTaskWhenDeleteAndChefClientExecutionError() throws ChefClientExecutionException {
        // given
        String vdc = "virtualDataCenter";
        String chefClientName = "chefClientNames";
        Task task = new Task();
        String callback = "http://callback";

        // when
        when(propertiesProvider.getProperty(SystemPropertiesProvider.CHEF_NODE_BASE_URL)).thenReturn("http://baseurl");
        doThrow(new ChefClientExecutionException("error")).when(chefCientManager).chefClientDelete(vdc, chefClientName);
        chefClientAsyncManager.chefClientDelete(vdc, chefClientName, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(chefCientManager).chefClientDelete(vdc, chefClientName);
        verify(taskManager).updateTask(task);

    }
}
