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

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import com.telefonica.euro_iaas.sdc.model.dto.NodeDto;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.manager.NodeManager;
import com.telefonica.euro_iaas.sdc.manager.async.NodeAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.*;
import com.telefonica.euro_iaas.sdc.rest.exception.APIException;
import com.telefonica.euro_iaas.sdc.rest.resources.NodeResourceImpl;

public class NodeResourceImplTest {
    private NodeResourceImpl nodeResource = null;
    private NodeManager nodeManager = null;
    private TaskManager taskManager = null;
    private NodeAsyncManager nodeAsyncManager = null;

    @Before
    public void setUp() throws Exception {
        nodeResource = new NodeResourceImpl();
        nodeManager = mock(NodeManager.class);
        nodeResource.setNodeManager(nodeManager);
        nodeAsyncManager = mock (NodeAsyncManager.class);
        nodeResource.setNodeAsyncManager(nodeAsyncManager);
        taskManager = mock (TaskManager.class);
        nodeResource.setTaskManager(taskManager);

    }

    /**
     * It tests the load of chefclient.
     *
     * @throws Exception
     */
    @Test
    public void testLoadChefPuppet() throws Exception {
        NodeDto client = new NodeDto();
        ChefClient clientChef = new ChefClient ();
        when(nodeManager.chefClientload(anyString(), anyString())).thenReturn(clientChef);
        when(nodeManager.puppetClientload(anyString(), anyString(), anyString())).thenReturn(client);
        NodeDto node = nodeResource.load("node");
        assertNotNull(node);
    }

    /**
     * It tests loading a node when it is not in puppet master
     * @throws Exception
     */
    @Test
    public void testLoadChef() throws Exception {
        NodeDto client = new NodeDto();
        ChefClient clientChef = new ChefClient ();
        when(nodeManager.chefClientload(anyString(), anyString())).thenReturn(clientChef);
        when(nodeManager.puppetClientload(anyString(), anyString(), anyString())).
            thenThrow(new EntityNotFoundException(NodeDto.class, "error", "node"));
        NodeDto clientReturned = nodeResource.load("node");
        assertNotNull(clientReturned);
    }

    /**
     * It tests loading a node when it is not in chefserver
     * @throws Exception
     */
    @Test
    public void testLoadPuppet() throws Exception {
        NodeDto client = new NodeDto();
        when(nodeManager.chefClientload(anyString(), anyString())).
            thenThrow(new EntityNotFoundException(NodeDto.class, "error", "node"));
        when(nodeManager.puppetClientload(anyString(), anyString(), anyString())).
            thenReturn(client);
        NodeDto clientReturned = nodeResource.load("node");
        assertNotNull(clientReturned);
    }

    /**
     * It test loading a node with error.
     * @throws Exception
     */
    @Test(expected=APIException.class)
    public void testLoadError() throws Exception {
        when(nodeManager.chefClientload(anyString(), anyString())).
            thenThrow(new EntityNotFoundException (NodeDto.class,"men", "node"));
        when(nodeManager.puppetClientload(anyString(), anyString(), anyString())).
            thenThrow(new EntityNotFoundException(NodeDto.class, "error", "node"));
        nodeResource.load("node");
    }

    /**
     * It tests delete the node.
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        when(nodeManager.chefClientload(anyString(), anyString())).thenReturn(new ChefClient());
        Mockito.doNothing().when(nodeAsyncManager).nodeDelete(anyString(), anyString(), anyString(),
            any(Task.class), anyString());
        Task task = new Task();
        when(taskManager.createTask(any(Task.class))).thenReturn(task);
        task = nodeResource.delete("vdc", "node", "");
        assertNotNull(task);
    }

    /**
     * It tests deleting the node with errors.
     * @throws Exception
     */
    @Test(expected=APIException.class)
    public void testDeleteError() throws Exception {
        when(nodeManager.chefClientload(anyString(), anyString())).
            thenThrow(new EntityNotFoundException (NodeDto.class,"men", "node"));
        when(nodeManager.puppetClientload(anyString(), anyString(), anyString())).
            thenThrow(new EntityNotFoundException (NodeDto.class,"men", "node"));
        nodeResource.delete("node", "vdc", "");
    }

    /**
     * It tests find all nodes.
     * @throws Exception
     */
    @Test(expected=APIException.class)
    public void testFindAll() throws Exception {
        nodeResource.findAll();
    }


}
