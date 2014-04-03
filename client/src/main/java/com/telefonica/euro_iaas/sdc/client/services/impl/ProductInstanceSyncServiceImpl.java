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

package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.util.List;

import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceSyncService;
import com.telefonica.euro_iaas.sdc.client.services.TaskService;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;

/**
 * Default @link ProductInsatnceSyncService implementation using active waiting
 * 
 * @author Sergio Arroyo
 */
public class ProductInstanceSyncServiceImpl implements ProductInstanceSyncService {

    private ProductInstanceService productInstanceService;
    private TaskService taskService;

    /**
     * @param productInstanceService
     * @param taskService
     */
    public ProductInstanceSyncServiceImpl(ProductInstanceService productInstanceService, TaskService taskService) {
        this.productInstanceService = productInstanceService;
        this.taskService = taskService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance upgrade(String vdc, String name, String version) throws MaxTimeWaitingExceedException,
            InvalidExecutionException {
        Task task = productInstanceService.upgrade(vdc, name, version, null);
        return this.waitForTask(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance configure(String vdc, String name, List<Attribute> arguments)
            throws MaxTimeWaitingExceedException, InvalidExecutionException {
        Task task = productInstanceService.configure(vdc, name, null, arguments);
        return this.waitForTask(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance uninstall(String vdc, String name) throws MaxTimeWaitingExceedException,
            InvalidExecutionException {
        Task task = productInstanceService.uninstall(vdc, name, null);
        return this.waitForTask(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance load(String url) throws ResourceNotFoundException {
        return productInstanceService.load(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance install(String vdc, ProductInstanceDto product) throws MaxTimeWaitingExceedException,
            InvalidExecutionException {
        Task task = productInstanceService.install(vdc, product, null);
        return this.waitForTask(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductInstance> findAll(String hostname, String domain, String ip, String fqn, Integer page,
            Integer pageSize, String orderBy, String orderType, Status status, String vdc, String productName) {
        return productInstanceService.findAll(hostname, domain, ip, fqn, page, pageSize, orderBy, orderType, status,
                vdc, productName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance load(String vdc, String name) throws ResourceNotFoundException {
        return productInstanceService.load(vdc, name);
    }

    private ProductInstance waitForTask(Task task) throws MaxTimeWaitingExceedException, InvalidExecutionException {
        task = taskService.waitForTask(task.getHref());
        if (!task.getStatus().equals(TaskStates.SUCCESS)) {
            throw new InvalidExecutionException(task.getError().getMessage());
        }
        try {
            return productInstanceService.load(task.getResult().getHref());
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
