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

import static com.telefonica.euro_iaas.sdc.util.Configuration.PRODUCT_INSTANCE_BASE_PATH;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.SDC_MANAGER_URL;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.manager.ArtifactManager;
import com.telefonica.euro_iaas.sdc.manager.async.ArtifactAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.TaskError;
import com.telefonica.euro_iaas.sdc.model.TaskReference;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ArtifactSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.TaskNotificator;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;


/**
 * Default implementation for ProductInstanceAsyncManager.
 * 
 * @author Sergio Arroyo
 */
public class ArtifactAsyncManagerImpl implements ArtifactAsyncManager {
    private Logger log = Logger.getLogger(ArtifactAsyncManagerImpl.class.getName());
    private ArtifactManager artifactManager;
    private TaskManager taskManager;
    private TaskNotificator taskNotificator;
    private SystemPropertiesProvider systemPropertiesProvider;


    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void deployArtifact(ProductInstance productInstance, Artifact artifact,  String token,
                               Task task, String callback) {
        try {
            artifactManager.deployArtifact(productInstance, artifact, token);
            updateSuccessTask(task, productInstance);
            log.info("Artefact  " + artifact.getName() + " installed in Product "
                    + productInstance.getProductRelease().getProduct().getName() + '-'
                    + productInstance.getProductRelease().getVersion() + " successfully");
        } catch (FSMViolationException e) {
            updateErrorTask(productInstance, task,
                    "The product " + productInstance.getProductRelease().getProduct().getName() + "-"
                            + productInstance.getProductRelease().getVersion() + " can not have the artefact "
                            + artifact.getName() + " deployed.", e);
        } catch (NodeExecutionException e) {
            updateErrorTask(productInstance, task, "The product " + productInstance.getId()
                    + " can not have the artefact " + artifact.getName() + " deployed.", e);
        } catch (Throwable e) {
            updateErrorTask(productInstance, task, "The product " + productInstance.getId()
                    + " can not have the artefact " + artifact.getName() + " deployed.", e);
        } finally {
            notifyTask(callback, task);
        }
    }

    @Override
    public void undeployArtifact(ProductInstance productInstance, String artifactName, String token,
                                 Task task, String callback) {
        try {
            artifactManager.undeployArtifact(productInstance, artifactName, token);
            updateSuccessTask(task, productInstance);
            log.info("Artefact  " + artifactName + " uninstalled in Product "
                    + productInstance.getProductRelease().getProduct().getName() + '-'
                    + productInstance.getProductRelease().getVersion() + " successfully");
        } catch (FSMViolationException e) {
            updateErrorTask(productInstance, task, "The product "
                    + productInstance.getProductRelease().getProduct().getName() + "-"
                    + productInstance.getProductRelease().getVersion() + " can not have the artefact " + artifactName
                    + " undeployed.", e);
        } catch (NodeExecutionException e) {
            updateErrorTask(productInstance, task, "The product " + productInstance.getId()
                    + " can not have the artefact " + artifactName + " undeployed.", e);
        } catch (Throwable e) {
            updateErrorTask(productInstance, task, "The product " + productInstance.getId()
                    + " can not have the artefact " + artifactName + " undeployed.", e);
        } finally {
            notifyTask(callback, task);
        }

    }

    @Override
    public List<Artifact> findByCriteria(ArtifactSearchCriteria criteria) {
        return artifactManager.findByCriteria(criteria);
    }

    @Override
    public Artifact load(String vdc, String productInstance, String name) throws EntityNotFoundException {
        return artifactManager.load(vdc, productInstance, name);
    }

    // //////// PRIVATE METHODS ///////////

    /*
     * Update the task with necessary information when the task is success.
     */
    private void updateSuccessTask(Task task, ProductInstance productInstance) {
        String piResource = MessageFormat.format(systemPropertiesProvider.getProperty(SDC_MANAGER_URL)
                + PRODUCT_INSTANCE_BASE_PATH,
                productInstance.getName(), // the name
                productInstance.getVm().getHostname(), // the hostname
                productInstance.getVm().getDomain(), // the domain
                productInstance.getProductRelease().getProduct().getName(), productInstance.getVdc()); // the product
        task.setResult(new TaskReference(piResource));
        task.setEndTime(new Date());
        task.setStatus(TaskStates.SUCCESS);
        taskManager.updateTask(task);
    }

    /*
     * Update the task with necessary information when the task is wrong and the product instance exists in the system.
     */
    private void updateErrorTask(ProductInstance productInstance, Task task, String message, Throwable t) {
        String piResource = MessageFormat.format(systemPropertiesProvider.getProperty(SDC_MANAGER_URL)
                + PRODUCT_INSTANCE_BASE_PATH,
        // productInstance.getId(), // the id
                productInstance.getName(), // the id
                productInstance.getVm().getHostname(), // the hostname
                productInstance.getVm().getDomain(), // the domain
                productInstance.getProductRelease().getProduct().getName(), productInstance.getVdc()); // the product
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
        log.info("An error occurs while executing a product action. See task " + task.getHref()
                + " for more information");
    }

    private void notifyTask(String url, Task task) {
        if (!StringUtils.isEmpty(url)) {
            taskNotificator.notify(url, task);
        }
    }

    // ////////// I.O.C ////////////

    public void setArtifactManager(ArtifactManager artifactManager) {
        this.artifactManager = artifactManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void setTaskNotificator(TaskNotificator taskNotificator) {
        this.taskNotificator = taskNotificator;
    }

    /**
     * It sets the system properties provider.
     * @param systemPropertiesProvider
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }
}
