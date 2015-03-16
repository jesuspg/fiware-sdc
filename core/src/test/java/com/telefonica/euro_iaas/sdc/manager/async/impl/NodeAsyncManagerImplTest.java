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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.exception.ChefClientExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.manager.NodeManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.util.TaskNotificator;

public class NodeAsyncManagerImplTest {

    NodeAsyncManagerImpl nodeAsyncManager;
    NodeManager nodeManager = mock(NodeManager.class);
    TaskNotificator taskNotificator = mock(TaskNotificator.class);
    SystemPropertiesProvider systemPropertiesProvider = null;
   
    TaskManager taskManager = mock(TaskManager.class);

    @Before
    public void setup() {
        nodeAsyncManager = new NodeAsyncManagerImpl();
        nodeAsyncManager.setNodeManager(nodeManager);
        nodeAsyncManager.setTaskNotificator(taskNotificator);
        nodeAsyncManager.setTaskManager(taskManager);
        systemPropertiesProvider = mock (SystemPropertiesProvider.class);
        nodeAsyncManager.setSystemPropertiesProvider(systemPropertiesProvider);
    }

    @Test
    public void shouldDelete() throws NodeExecutionException {
        // given
        String vdc = "virtualDataCenter";
        String nodeName = "chefClientNames";
        Task task = new Task();
        String callback = "http://callback";

        // when
       
        nodeAsyncManager.nodeDelete(vdc, nodeName, "token" , task, callback);

        // then
        verify(nodeManager).nodeDelete(vdc, nodeName, "token");
        verify(taskManager).updateTask(task);
    }

    @Test
    public void shouldDeleteCheckTask() throws NodeExecutionException {
        Task task = new Task();
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("http://sdc");
        nodeAsyncManager.nodeDelete("vdc", "node", "token" , task, "");

        // then
        assertEquals(task.getResult().getHref(), "http://sdc/rest/vdc/vdc/node/node");
    }

    @Test
    public void shouldUpdateErrorTaskWhenDeleteAndChefClientExecutionError() throws NodeExecutionException {
        // given
        String vdc = "virtualDataCenter";
        String nodeName = "nodeNames";
        Task task = new Task();
        String callback = "http://callback";

        // when
       
        doThrow(new NodeExecutionException("error")).when(nodeManager).nodeDelete(vdc, nodeName, "token");
        nodeAsyncManager.nodeDelete(vdc, nodeName, "token", task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(nodeManager).nodeDelete(vdc, nodeName, "token");
        verify(taskManager).updateTask(task);

    }
}
