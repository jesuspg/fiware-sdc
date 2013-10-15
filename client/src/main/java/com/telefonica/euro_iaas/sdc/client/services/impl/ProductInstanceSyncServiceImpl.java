/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
