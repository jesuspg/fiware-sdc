package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.util.List;

import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ApplicationInstanceService;
import com.telefonica.euro_iaas.sdc.client.services.ApplicationInstanceSyncService;
import com.telefonica.euro_iaas.sdc.client.services.TaskService;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationInstanceDto;

/**
 * Default ApplicationInstanceSyncService implementation using active waiting
 * 
 * @author Sergio Arroyo
 */
public class ApplicationInstanceSyncServiceImpl implements ApplicationInstanceSyncService {

    private ApplicationInstanceService applicationInstanceService;
    private TaskService taskService;

    /**
     * @param service
     * @param taskService
     */
    public ApplicationInstanceSyncServiceImpl(ApplicationInstanceService applicationInstanceService,
            TaskService taskService) {
        this.applicationInstanceService = applicationInstanceService;
        this.taskService = taskService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance upgrade(String vdc, String name, String version) throws MaxTimeWaitingExceedException,
            InvalidExecutionException {
        Task task = applicationInstanceService.upgrade(vdc, name, version, null);
        return waitForTask(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance configure(String vdc, String name, List<Attribute> arguments)
            throws MaxTimeWaitingExceedException, InvalidExecutionException {
        Task task = applicationInstanceService.configure(vdc, name, null, arguments);
        return waitForTask(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance uninstall(String vdc, String name) throws MaxTimeWaitingExceedException,
            InvalidExecutionException {
        Task task = applicationInstanceService.uninstall(vdc, name, null);
        return waitForTask(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance load(String url) throws ResourceNotFoundException {
        return applicationInstanceService.load(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance install(String vdc, ApplicationInstanceDto application)
            throws MaxTimeWaitingExceedException, InvalidExecutionException {
        Task task = applicationInstanceService.install(vdc, application, null);
        return waitForTask(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findAll(String hostname, String domain, String ip, String fqn, Integer page,
            Integer pageSize, String orderBy, String orderType, List<Status> status, String vdc, String applicationName) {
        return applicationInstanceService.findAll(hostname, domain, ip, fqn, page, pageSize, orderBy, orderType,
                status, vdc, applicationName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance load(String vdc, String name) throws ResourceNotFoundException {
        return applicationInstanceService.load(vdc, name);
    }

    private ApplicationInstance waitForTask(Task task) throws MaxTimeWaitingExceedException, InvalidExecutionException {
        task = taskService.waitForTask(task.getHref());
        if (!task.getStatus().equals(TaskStates.SUCCESS)) {
            throw new InvalidExecutionException(task.getError().getMessage());
        }
        try {
            return applicationInstanceService.load(task.getResult().getHref());
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
