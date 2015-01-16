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

package com.telefonica.euro_iaas.sdc.rest.resources;

import java.text.MessageFormat;

import javax.ws.rs.Path;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.telefonica.euro_iaas.sdc.manager.NodeManager;
import com.telefonica.euro_iaas.sdc.manager.async.NodeAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.sdc.rest.exception.APIException;

/**
 * 
 */

/**
 * @author jesus.movilla
 */
@Path("/vdc/{vdc}/chefClient")
@Component
@Scope("request")
public class NodeResourceImpl implements NodeResource {

    private NodeAsyncManager nodeAsyncManager;
    private TaskManager taskManager;
    private NodeManager nodeManager;

    /**
     * It obtains all the nodes for the user
     * @return
     */
    public ChefClient findAll() {
        throw new APIException(new Exception("Operation not implemented"));

    }

    /**
     * It obtains the information about the chef client.
     * @param chefClientName
     *            the ChefClientName
     * @return
     */
    public ChefClient load(String chefClientName) {
        try {
            return nodeManager.chefClientload(chefClientName, getCredentials().getToken());
        } catch (EntityNotFoundException e) {
            throw new APIException(e);
        } catch (Exception e) {
            throw new APIException(e);
        }
    }

    public Task delete(String vdc, String nodeName, String callback) {

        try {

            Task task = createTask(MessageFormat.format("Delete Node {0} from Chef/Puppet", nodeName), vdc);

            nodeAsyncManager.nodeDelete(vdc, nodeName, getCredentials().getToken(), task, callback);
            return task;
        } catch (Exception e) {
            throw new APIException(e);
        }

    }

    private Task createTask(String description, String vdc) {
        Task task = new Task(TaskStates.RUNNING);
        task.setDescription(description);
        task.setVdc(vdc);
        return taskManager.createTask(task);
    }

    public void setNodeAsyncManager(NodeAsyncManager nodeAsyncManager) {
        this.nodeAsyncManager = nodeAsyncManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void setNodeManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    public PaasManagerUser getCredentials() {
        return (PaasManagerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
