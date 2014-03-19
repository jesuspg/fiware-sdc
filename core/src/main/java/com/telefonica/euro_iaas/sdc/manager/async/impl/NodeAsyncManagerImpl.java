/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.manager.async.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_NODE_BASE_URL;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.manager.NodeManager;
import com.telefonica.euro_iaas.sdc.manager.async.NodeAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.TaskError;
import com.telefonica.euro_iaas.sdc.model.TaskReference;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.util.TaskNotificator;

/**
 * @author alberts
 */
public class NodeAsyncManagerImpl implements NodeAsyncManager {

    private static Logger LOGGER = Logger.getLogger(NodeAsyncManagerImpl.class.getName());

    private TaskManager taskManager;
    private SystemPropertiesProvider propertiesProvider;
    private TaskNotificator taskNotificator;
    private NodeManager nodeManager;

    public void nodeDelete(String vdc, String nodeName, Task task, String callback) {
        try {
            nodeManager.nodeDelete(vdc, nodeName);
            updateSuccessTask(task, vdc, nodeName);
            LOGGER.info("Node   " + nodeName + " is being deleted");
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
        String piResource = MessageFormat.format(propertiesProvider.getProperty(CHEF_NODE_BASE_URL), vdc,
                chefClientname); // the product
        task.setResult(new TaskReference(piResource));
        task.setEndTime(new Date());
        task.setStatus(TaskStates.SUCCESS);
        taskManager.updateTask(task);
    }

    /*
     * Update the task with necessary information when the task is wrong and the product instance exists in the system.
     */
    private void updateErrorTask(String vdc, String chefClientname, Task task, String message, Throwable t) {
        String piResource = MessageFormat.format(propertiesProvider.getProperty(CHEF_NODE_BASE_URL), vdc,
                chefClientname); // the product
        task.setResult(new TaskReference(piResource));
        updateErrorTask(task, message, t);
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
        LOGGER.info("An error occurs while deleting a node . See task " + task.getHref()
                + "for more information");
    }

    private void notifyTask(String url, Task task) {
        if (!StringUtils.isEmpty(url)) {
            taskNotificator.notify(url, task);
        }
    }

    // ////////// I.O.C ////////////

    /**
     * @param chefClientManager
     *            the chefClientManager to set
     */
    public void setNodeManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    /**
     * @param taskManager
     *            the taskManager to set
     */
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param taskNotificator
     *            the taskNotificator to set
     */
    public void setTaskNotificator(TaskNotificator taskNotificator) {
        this.taskNotificator = taskNotificator;
    }

}
