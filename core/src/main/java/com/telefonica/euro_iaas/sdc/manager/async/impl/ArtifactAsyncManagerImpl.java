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

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
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
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.util.TaskNotificator;

/**
 * Default implementation for ProductInstanceAsyncManager.
 * 
 * @author Sergio Arroyo
 */
public class ArtifactAsyncManagerImpl implements ArtifactAsyncManager {
    private Logger logger = Logger.getLogger(ArtifactAsyncManagerImpl.class.getName());
    private ArtifactManager artifactManager;
    private TaskManager taskManager;
    private SystemPropertiesProvider propertiesProvider;
    private TaskNotificator taskNotificator;

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void deployArtifact(ProductInstance productInstance, Artifact artifact, Task task, String callback) {
        try {
            artifactManager.deployArtifact(productInstance, artifact);
            updateSuccessTask(task, productInstance);
            logger.info("Artefact  " + artifact.getName() + " installed in Product "
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
    public void undeployArtifact(ProductInstance productInstance, String artifactName, Task task, String callback) {
        try {
            artifactManager.undeployArtifact(productInstance, artifactName);
            updateSuccessTask(task, productInstance);
            logger.info("Artefact  " + artifactName + " uninstalled in Product "
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
        String piResource = MessageFormat.format(propertiesProvider.getProperty(PRODUCT_INSTANCE_BASE_URL),
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
        String piResource = MessageFormat.format(propertiesProvider.getProperty(PRODUCT_INSTANCE_BASE_URL),
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
        logger.info("An error occurs while executing a product action. See task " + task.getHref()
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

    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    public void setTaskNotificator(TaskNotificator taskNotificator) {
        this.taskNotificator = taskNotificator;
    }
}
