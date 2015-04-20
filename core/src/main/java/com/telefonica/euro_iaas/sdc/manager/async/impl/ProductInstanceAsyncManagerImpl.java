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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.manager.async.ProductInstanceAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.TaskError;
import com.telefonica.euro_iaas.sdc.model.TaskReference;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.util.TaskNotificator;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

/**
 * Default implementation for ProductInstanceAsyncManager.
 * 
 * @author Sergio Arroyo
 */
public class ProductInstanceAsyncManagerImpl implements ProductInstanceAsyncManager {
    private static Logger log = LoggerFactory.getLogger(ProductInstanceAsyncManagerImpl.class);
    private SystemPropertiesProvider systemPropertiesProvider;
    private ProductInstanceManager productInstanceManager;
    private TaskManager taskManager;

    private TaskNotificator taskNotificator;

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void install(VM vm, String vdc, ProductRelease productRelease, List<Attribute> attributes, String token,
            Task task, String callback) {
        try {

            ProductInstance productInstance = null;
            productInstance = productInstanceManager.install(vm, vdc, productRelease, attributes, token);

            updateSuccessTask(task, productInstance);

            log.info("Product " + productRelease.getProduct().getName() + '-' + productRelease.getVersion()
                    + " installed successfully");
        } catch (NodeExecutionException e) {
            String errorMsg = "The product " + productRelease.getProduct().getName() + "-"
                    + productRelease.getVersion() + " can not be installed in" + vm
                    + " due to a problem when executing in node";
            processErrorTask(vm, productRelease, task, e, errorMsg);
        } catch (InvalidInstallProductRequestException e) {
            String errorMsg = e.getMessage();
            processErrorTask(vm, productRelease, task, e, errorMsg);

        } catch (AlreadyInstalledException e) {
            String errorMsg = e.getMessage();
            log.warn(errorMsg);
            processErrorTask(vm, productRelease, task, e, errorMsg);

        } catch (Exception e) {
            String errorMsg = e.getMessage();
            log.warn(errorMsg);
            processErrorTask(vm, productRelease, task, e, errorMsg);

        } catch (Throwable e) {
            String errorMsg = "The product " + productRelease.getProduct().getName() + "-"
                    + productRelease.getVersion() + " can not be installed in" + vm + " " + e.getMessage();
            log.warn(errorMsg);
            processErrorTask(vm, productRelease, task, e, errorMsg);
        } finally {
            notifyTask(callback, task);
        }
    }

    private void processErrorTask(VM vm, ProductRelease productRelease, Task task, Throwable e, String errorMsg) {
        ProductInstance instance = getInstalledProduct(productRelease, vm, task.getVdc());
        if (instance != null) {
            updateErrorTask(instance, task, errorMsg, e);
        } else {
            updateErrorTask(task, errorMsg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void configure(ProductInstance productInstance, List<Attribute> configuration, String token, Task task,
            String callback) {
        try {
            productInstanceManager.configure(productInstance, configuration, token);
            updateSuccessTask(task, productInstance);
            log.info("Product " + productInstance.getProductRelease().getProduct().getName() + '-'
                    + productInstance.getProductRelease().getVersion() + " configured successfully");
        } catch (FSMViolationException e) {
            updateErrorTask(productInstance, task, "The product "
                    + productInstance.getProductRelease().getProduct().getName() + "-"
                    + productInstance.getProductRelease().getVersion()
                    + " can not be configured due to previous status", e);
        } catch (NodeExecutionException e) {
            updateErrorTask(productInstance, task, "The product " + productInstance.getId()
                    + " can not be configured due to an error executing in node.", e);
        } catch (Throwable e) {
            updateErrorTask(productInstance, task, "The product " + productInstance.getId()
                    + " can not be configured due to an unexpected error.", e);
        } finally {
            notifyTask(callback, task);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void upgrade(ProductInstance productInstance, ProductRelease productRelease, String token, Task task,
            String callback) {
        try {
            productInstanceManager.upgrade(productInstance, productRelease, token);
            updateSuccessTask(task, productInstance);
            log.info("Product " + productInstance.getProductRelease().getProduct().getName() + "-"
                    + productInstance.getProductRelease().getVersion() + " upgraded successfully");
        } catch (NotTransitableException e) {
            updateErrorTask(productInstance, task, "The product "
                    + productInstance.getProductRelease().getProduct().getName() + "-"
                    + productInstance.getProductRelease().getVersion() + " can not be updated to version "
                    + productRelease.getVersion(), e);

        } catch (FSMViolationException e) {
            updateErrorTask(productInstance, task, "The product " + productInstance.getId()
                    + " can not be uninstalled due to previous status", e);
        } catch (NodeExecutionException e) {
            updateErrorTask(productInstance, task, "The product " + productInstance.getId()
                    + " can not be upgrade to version " + productRelease.getVersion()
                    + "due to an error executing in node.", e);
        } catch (Throwable e) {
            updateErrorTask(productInstance, task, "The product " + productInstance.getId()
                    + " can not be upgrade to version " + productRelease.getVersion() + "due to unexpected error.", e);
        } finally {
            notifyTask(callback, task);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void uninstall(ProductInstance productInstance, String token, Task task, String callback) {

        try {
            productInstanceManager.uninstall(productInstance, token);
            updateSuccessTask(task, productInstance);
            log.info("Product " + productInstance.getProductRelease().getProduct().getName() + "-"
                    + productInstance.getProductRelease().getVersion() + " uninstalled successfully");
        } catch (FSMViolationException e) {
            updateErrorTask(productInstance, task, "The product " + productInstance.getId()
                    + " can not be uninstalled due to previous status", e);
        } catch (NodeExecutionException e) {
            updateErrorTask(productInstance, task, "The product " + productInstance.getId()
                    + " can not be uninstalled due to an error executing in node.", e);
        } catch (Throwable e) {
            updateErrorTask(productInstance, task, "The product " + productInstance.getId()
                    + " can not be uninstalled due to unexpected error.", e);
        } finally {
            notifyTask(callback, task);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance load(String vdc, Long id) throws EntityNotFoundException {
        return productInstanceManager.load(vdc, id);
    }

    @Override
    public ProductInstance load(String vdc, String name) throws EntityNotFoundException {

        return productInstanceManager.load(vdc, name);
    }

    @Override
    public ProductInstance loadByCriteria(ProductInstanceSearchCriteria criteria) throws EntityNotFoundException,
            NotUniqueResultException {
        return productInstanceManager.loadByCriteria(criteria);
    }

    public List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria) {
        return productInstanceManager.findByCriteria(criteria);
    }

    // //////// PRIVATE METHODS ///////////

    /*
     * Update the task with necessary information when the task is success.
     */
    private void updateSuccessTask(Task task, ProductInstance productInstance) {
        // the
        // product
        task.setResult(new TaskReference(getUrlResourceTaks(productInstance)));
        task.setEndTime(new Date());
        task.setStatus(TaskStates.SUCCESS);
        taskManager.updateTask(task);

    }

    private String getUrlResourceTaks(ProductInstance productInstance) {
        String piResource = MessageFormat.format(systemPropertiesProvider.getProperty(SDC_MANAGER_URL)
                + PRODUCT_INSTANCE_BASE_PATH, productInstance.getName(), // the name
                productInstance.getVm().getHostname(), // the hostname
                productInstance.getVm().getDomain(), // the domain
                productInstance.getProductRelease().getProduct().getName(), productInstance.getVdc());
        return piResource;
    }

    /*
     * Update the task with necessary information when the task is wrong and the product instance exists in the system.
     */
    private void updateErrorTask(ProductInstance productInstance, Task task, String message, Throwable t) {
        task.setResult(new TaskReference(getUrlResourceTaks(productInstance)));
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
        log.error("An error occured while executing a product action. ERROR:\"" + error.getMessage() + "\". See task "
                + task.getHref() + " for more information");
    }

    private ProductInstance getInstalledProduct(ProductRelease productRelease, VM vm, String vdc) {
        try {
            return productInstanceManager.load(vm, productRelease, vdc);
        } catch (EntityNotFoundException e1) {
            log.warn("The instance is not found");
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }
    }

    private void notifyTask(String url, Task task) {
        if (!StringUtils.isEmpty(url)) {
            taskNotificator.notify(url, task);
        }
    }

    // ////////// I.O.C ////////////

    /**
     * @param productInstanceManager
     *            the productInstanceManager to set
     */
    public void setProductInstanceManager(ProductInstanceManager productInstanceManager) {
        this.productInstanceManager = productInstanceManager;
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

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

}
