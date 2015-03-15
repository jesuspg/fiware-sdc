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

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.manager.async.impl;

import static com.telefonica.euro_iaas.sdc.util.Configuration.CHEF_NODE_BASE_PATH;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.SDC_MANAGER_URL;


import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.manager.NodeManager;
import com.telefonica.euro_iaas.sdc.manager.async.NodeAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.TaskError;
import com.telefonica.euro_iaas.sdc.model.TaskReference;

import com.telefonica.euro_iaas.sdc.util.TaskNotificator;
import org.springframework.scheduling.annotation.Async;

/**
 * @author alberts
 */
public class NodeAsyncManagerImpl implements NodeAsyncManager {

    private static Logger log = LoggerFactory.getLogger(NodeAsyncManagerImpl.class);

    private TaskManager taskManager;
    private TaskNotificator taskNotificator;
    private NodeManager nodeManager;
    private SystemPropertiesProvider systemPropertiesProvider;

    /**
     * It deletes the node.
     * @param vdc
     * @param nodeName
     *            the name of the node to be deleted from chef server / puppet master
     * @param token
     * @param task
     *            the task which contains the information about the async execution
     * @param callback
     */
    @Async
    @Override
    public void nodeDelete(String vdc, String nodeName, String token, Task task, String callback) {
        try {
            nodeManager.nodeDelete(vdc, nodeName, token);
            updateSuccessTask(task, vdc, nodeName);
            log.info("Node   " + nodeName + " is being deleted");
        } catch (NodeExecutionException e) {
            updateErrorTask(vdc, nodeName, task, "The node " + nodeName
                    + " can not be deleted due to an error executing in node.", e);
        } catch (Throwable e) {
            updateErrorTask(vdc, nodeName, task, "The node " + nodeName
                    + " can not be deleted due to unexpected error.", e);
        } finally {
            notifyTask(callback, task);
        }
    }

    // //////// PRIVATE METHODS ///////////

    /*
     * Update the task with necessary information when the task is success.
     */
    private void updateSuccessTask(Task task, String vdc, String chefClientname) {
        task.setResult(new TaskReference(getTaskUrl(vdc, chefClientname)));
        task.setEndTime(new Date());
        task.setStatus(TaskStates.SUCCESS);
        taskManager.updateTask(task);
    }

    /*
     * Update the task with necessary information when the task is wrong and
     * the product instance exists in the system.
     */
    private void updateErrorTask(String vdc, String chefClientname, Task task, String message, Throwable t) {

        task.setResult(new TaskReference(getTaskUrl(vdc, chefClientname)));
        updateErrorTask(task, message, t);
    }

    private String getTaskUrl(String vdc, String chefClientName) {
        return MessageFormat.format(systemPropertiesProvider.getProperty(SDC_MANAGER_URL)
            + CHEF_NODE_BASE_PATH, chefClientName, vdc);
    }
    /*
     * Update the task with necessary information when the task is wrong.
     */
    private void updateErrorTask(Task task, String message, Throwable t) {
        TaskError error = new TaskError(message);
        error.setMajorErrorCode(t.getMessage());
        error.setMinorErrorCode(t.getClass().getSimpleName());
        task.setEndTime(new Date());
        task.setStatus(TaskStates.ERROR);
        task.setError(error);
        taskManager.updateTask(task);
        log.info("An error occurs while deleting a node . See task " + task.getHref()
                + "for more information");
    }

    private void notifyTask(String url, Task task) {
        if (!StringUtils.isEmpty(url)) {
            taskNotificator.notify(url, task);
        }
    }

    // ////////// I.O.C ////////////

    /**
     * @param nodeManager
     *            the nodeManager to set
     */
    public void setNodeManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    /**
     * It sets the system properties provider.
     * @param systemPropertiesProvider
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    /**
     * @param taskManager
     *            the taskManager to set
     */
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * @param taskNotificator
     *            the taskNotificator to set
     */
    public void setTaskNotificator(TaskNotificator taskNotificator) {
        this.taskNotificator = taskNotificator;
    }

}
