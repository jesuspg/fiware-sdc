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

package com.telefonica.euro_iaas.sdc.manager.async.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.manager.ChefNodeManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.util.TaskNotificator;

public class ChefNodeAsyncManagerImplTest {

    ChefNodeAsyncManagerImpl chefNodeAsyncManager;
    ChefNodeManager chefNodeManager = mock(ChefNodeManager.class);
    TaskNotificator taskNotificator = mock(TaskNotificator.class);
    SystemPropertiesProvider propertiesProvider = mock(SystemPropertiesProvider.class);
    TaskManager taskManager = mock(TaskManager.class);

    @Before
    public void setup() {
        chefNodeAsyncManager = new ChefNodeAsyncManagerImpl();
        chefNodeAsyncManager.setChefNodeManager(chefNodeManager);
        chefNodeAsyncManager.setTaskNotificator(taskNotificator);
        chefNodeAsyncManager.setPropertiesProvider(propertiesProvider);
        chefNodeAsyncManager.setTaskManager(taskManager);
    }

    @Test
    public void shouldDelete() throws NodeExecutionException {
        // given
        String vdc = "virtualDataCenter";
        String chefNodeName = "chefNodeName";
        Task task = new Task();
        String callback = "http://callback";

        // when
        when(propertiesProvider.getProperty(SystemPropertiesProvider.CHEF_NODE_BASE_URL)).thenReturn("http://baseurl");
        chefNodeAsyncManager.chefNodeDelete(vdc, chefNodeName, task, callback);

        // then
        verify(chefNodeManager).chefNodeDelete(vdc, chefNodeName);
        verify(taskManager).updateTask(task);
        assertEquals(task.getStatus(), Task.TaskStates.SUCCESS);
    }

    @Test
    public void shouldUpdateErrorTaskBeforeDeleteWhenNodeExecutionException() throws NodeExecutionException {
        // given
        String vdc = "virtualDataCenter";
        String chefNodeName = "chefNodeName";
        Task task = new Task();
        String callback = "http://callback";

        // when
        when(propertiesProvider.getProperty(SystemPropertiesProvider.CHEF_NODE_BASE_URL)).thenReturn("http://baseurl");
        doThrow(new NodeExecutionException("node exception")).when(chefNodeManager).chefNodeDelete(vdc, chefNodeName);

        chefNodeAsyncManager.chefNodeDelete(vdc, chefNodeName, task, callback);

        // then
        verify(chefNodeManager).chefNodeDelete(vdc, chefNodeName);
        verify(taskManager).updateTask(task);
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
    }
}
