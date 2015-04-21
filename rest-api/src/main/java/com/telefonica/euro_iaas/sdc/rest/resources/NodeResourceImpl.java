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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.telefonica.euro_iaas.sdc.manager.NodeManager;
import com.telefonica.euro_iaas.sdc.manager.async.NodeAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.model.dto.NodeDto;
import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.sdc.rest.auth.OpenStackAuthenticationProvider;
import com.telefonica.euro_iaas.sdc.rest.exception.APIException;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

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
    private static Logger log = LoggerFactory.getLogger(NodeResourceImpl.class);

    /**
     * It obtains all the nodes for the user.
     * 
     * @return
     */
    public NodeDto findAll() {
        throw new APIException(new Exception("Operation not implemented"));

    }

    /**
     * It obtains the information about the chef client.
     * 
     * @param nodeName
     *            the ChefClientName
     * @return
     */
    public NodeDto load(String nodeName) {
        boolean errorChef = false;
        boolean errorPuppet = false;
        NodeDto node = null;
        try {
            ChefClient chefClient = nodeManager.chefClientload(nodeName, getToken());
            node = chefClient.toNodeDto();
        } catch (Exception e) {
            errorChef = true;
        }
        try {
            node = nodeManager.puppetClientload(nodeName, getToken(), getVdc());
        } catch (Exception e) {
            errorPuppet = true;
        }
        if (errorChef && errorPuppet) {
            throw new APIException(new EntityNotFoundException(NodeDto.class, "Node " + nodeName + " not found",
                    nodeName));
        }
        return node;
    }

    /**
     * It delete the node in the chef-server and puppet master.
     * 
     * @param vdc
     *            the tenant id
     * @param nodeName
     *            the name of the node (without domain) to be deleted from Chef/Puppet
     * @param callback
     * @return
     */
    public Task delete(String vdc, String nodeName, String callback) {
        try {
            nodeManager.deleteProductsInNode(nodeName);
        } catch (EntityNotFoundException e) {
            String errorMsg = "The hostname " + nodeName + " does not have products installed " + e.getMessage();
            log.warn(errorMsg);
        }

        load(nodeName);

        try {
            Task task = createTask(MessageFormat.format("Delete Node {0} from Chef/Puppet", nodeName), vdc);
            nodeAsyncManager.nodeDelete(vdc, nodeName, getToken(), task, callback);
            return task;
        } catch (Exception e) {
            throw new APIException(e);
        }
    }

    /**
     * It creates the task.
     * 
     * @param description
     * @param vdc
     * @return
     */
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

    /**
     * It obtains the token from the credentials.
     * 
     * @return
     */
    public String getToken() {
        PaasManagerUser user = OpenStackAuthenticationProvider.getCredentials();
        if (user == null) {
            return "";
        } else {
            return user.getToken();
        }

    }

    /**
     * It gets the vdc from crendentials.
     * 
     * @return
     */
    public String getVdc() {
        PaasManagerUser user = OpenStackAuthenticationProvider.getCredentials();
        if (user == null) {
            return "";
        } else {
            return user.getTenantId();
        }

    }

}
