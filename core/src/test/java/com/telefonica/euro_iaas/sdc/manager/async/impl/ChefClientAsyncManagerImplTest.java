/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
