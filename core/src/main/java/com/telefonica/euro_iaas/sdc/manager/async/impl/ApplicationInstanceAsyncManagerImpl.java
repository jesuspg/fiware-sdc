package com.telefonica.euro_iaas.sdc.manager.async.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.APPLICATION_INSTANCE_BASE_URL;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.IncompatibleProductsException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotInstalledProductsException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.sdc.manager.async.ApplicationInstanceAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.TaskError;
import com.telefonica.euro_iaas.sdc.model.TaskReference;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.util.TaskNotificator;

/**
 * Default ApplicationInstanceAsyncManager implementation.
 * @author Sergio Arroyo, Jesus M. Movilla
 *
 */
public class ApplicationInstanceAsyncManagerImpl
    implements ApplicationInstanceAsyncManager {

    private static Logger LOGGER =
            Logger.getLogger(ApplicationInstanceAsyncManagerImpl.class.getName());
    private ApplicationInstanceManager applicationInstanceManager;
    private TaskManager taskManager;
    private TaskNotificator taskNotificator;
    private SystemPropertiesProvider propertiesProvider;

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void install(VM vm,  String vdc, EnvironmentInstance environmentInstance,
            ApplicationRelease application, List<Attribute> configuration,
            Task task, String callback) {
        try {
            ApplicationInstance applicationInstance =
                    applicationInstanceManager.install(
                            vm, vdc, environmentInstance, application, configuration);
            updateSuccessTask(task, applicationInstance);
            LOGGER.info("Application " + application.getApplication().getName()
                    + '-' + application.getVersion() + " installed successfully");

        } catch (NodeExecutionException e) {
            String errorMsg = "The application "
                    + application.getApplication().getName()
                    + "-" + application.getVersion()
                    + " can not be installed in" + vm + "due to a problem when"
                    + " executing in node";
            updateErrorTaskOnInstall(application, vm, task, errorMsg, e);
        } catch (IncompatibleProductsException e) {
            String errorMsg = "The application "
                    + application.getApplication().getName()
                    + "-" + application.getVersion()
                    + " can not be installed in" + vm + "due "
                    + e.getProducts().size() + " selected products"
                    + "to install on are incompatible";
            updateErrorTaskOnInstall(application, vm, task, errorMsg, e);
        } catch (AlreadyInstalledException e) {
            String errorMsg = "The application "
                    + application.getApplication().getName()
                    + "-" + application.getVersion()
                    + " is already installed in" + vm;
            updateErrorTaskOnInstall(application, vm, task, errorMsg, e);
        } catch (NotInstalledProductsException e) {
            String errorMsg = "The application "
                    + application.getApplication().getName()
                    + "-" + application.getVersion()
                    + " can not be installed in" + vm + "due "
                    + e.getProducts().size() + " selected products"
                    + "to install on are not installed";
            updateErrorTaskOnInstall(application, vm, task, errorMsg, e);
        } catch (Throwable e) {
            String errorMsg = "The application "
                    + application.getApplication().getName()
                    + "-" + application.getVersion()
                    + " can not be installed in" + vm + "due to unexpected error.";
            updateErrorTaskOnInstall(application, vm, task, errorMsg, e);
        } finally {
            notifyTask(callback, task);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void configure(ApplicationInstance applicationInstance,
            List<Attribute> configuration, Task task, String callback) {
        try {
            applicationInstanceManager.configure(applicationInstance, configuration);
            LOGGER.info("Application "
                    + applicationInstance.getApplication().getApplication().getName()
                    + "-" + applicationInstance.getApplication().getVersion()
                    + "  configured successfully");
            updateSuccessTask(task, applicationInstance);
        } catch (NodeExecutionException e) {
            updateErrorTask(applicationInstance, task,
                    "The application " + applicationInstance.getId()
                    + " can not be configured due to an error executing in node.", e);
        } catch (FSMViolationException e) {
            updateErrorTask(applicationInstance, task,
                    "The application " + applicationInstance.getId()
                    + " can not be configured due to previous status", e);
        } catch (Throwable e) {
            updateErrorTask(applicationInstance, task,
                    "The application " + applicationInstance.getId()
                    + " can not be configured due to unexpected error", e);
        } finally {
            notifyTask(callback, task);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void upgrade(ApplicationInstance applicationInstance,
            ApplicationRelease newRelease, Task task, String callback) {
        try {
            applicationInstanceManager.upgrade(applicationInstance, newRelease);
            updateSuccessTask(task, applicationInstance);
            LOGGER.info("Application " 
            		+ applicationInstance.getApplication().getApplication().getName()
                    + "-" + applicationInstance.getApplication().getVersion()
                    + " upgraded successfully");
        } catch (NodeExecutionException e) {
            updateErrorTask(applicationInstance, task,
                    "The application " + applicationInstance.getId()
                    + " can not be upgraded due to an error executing in node.", e);
        } catch (NotTransitableException e) {
            updateErrorTask(applicationInstance, task,
                    "The application " + applicationInstance.getId()
                    + " can not be upgraded due to the new version is "
                    +"incompatible with the installed version", e);
        } catch (IncompatibleProductsException e) {
            updateErrorTask(applicationInstance, task,
                    "The application " + applicationInstance.getId()
                    + " can not be upgraded due to the new version is "
                    +" incompatible with " + e.getProducts().size()
                    + " products where the application is installed on.", e);
        } catch (FSMViolationException e) {
            updateErrorTask(applicationInstance, task,
                    "The application " + applicationInstance.getId()
                    + " can not be upgraded due to previous status", e);
        } catch (Throwable e) {
            updateErrorTask(applicationInstance, task,
                    "The application " + applicationInstance.getId()
                    + " can not be configured due to unexpected error", e);
        } finally {
            notifyTask(callback, task);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void uninstall(ApplicationInstance applicationInstance, Task task,
            String callback) {
        try {
            applicationInstanceManager.uninstall(applicationInstance);
            updateSuccessTask(task, applicationInstance);
            LOGGER.info("Application " 
            		+ applicationInstance.getApplication().getApplication().getName()
                    + "-" + applicationInstance.getApplication().getVersion()
                    + " uninstalled successfully");
        } catch (FSMViolationException e) {
            updateErrorTask(applicationInstance, task,
                    "The application " + applicationInstance.getId()
                    + " can not be uninstalled due to previous status", e);
        } catch (NodeExecutionException e) {
            updateErrorTask(applicationInstance, task,
                    "The application " + applicationInstance.getId()
                    + " can not be uninstalled due to an error executing in node.", e);
        } catch (Throwable e) {
            updateErrorTask(applicationInstance, task,
                    "The application " + applicationInstance.getId()
                    + " can not be uninstalled due to unexpected error.", e);
        } finally {
            notifyTask(callback, task);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance load(Long id) throws EntityNotFoundException {
        return applicationInstanceManager.load(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance loadByCriteria(
            ApplicationInstanceSearchCriteria criteria)
            throws EntityNotFoundException, NotUniqueResultException {
        return applicationInstanceManager.loadByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findByCriteria(
            ApplicationInstanceSearchCriteria criteria) {
        return applicationInstanceManager.findByCriteria(criteria);
    }


    ////////// PRIVATE METHODS ///////////

    /*
     * Update the task with necessary information when the task is success.
     */
    private void updateSuccessTask(Task task, ApplicationInstance applicationInstance) {
        VM vm = applicationInstance.getEnvironmentInstance().getProductInstances()
        		.iterator().next().getVm();
        String aiResource = MessageFormat.format(
                propertiesProvider.getProperty(APPLICATION_INSTANCE_BASE_URL),
                applicationInstance.getId(), // the id
                vm.getHostname(), // the hostname
                vm.getDomain(), // the domain
                applicationInstance.getApplication().getApplication().getName(),
                applicationInstance.getVdc()); // the product
        task.setResult(new TaskReference(aiResource));
        task.setEndTime(new Date());
        task.setStatus(TaskStates.SUCCESS);
        taskManager.updateTask(task);
    }


    /*
     * Update the task with necessary information when the task is wrong and the
     * product instance exists in the system.
     */
    private void updateErrorTask(ApplicationInstance applicationInstance, Task task,
            String message, Throwable t) {
        VM vm = applicationInstance.getEnvironmentInstance().getProductInstances()
        		.iterator().next().getVm();
        String aiResource = MessageFormat.format(
                propertiesProvider.getProperty(APPLICATION_INSTANCE_BASE_URL),
                applicationInstance.getId(), // the id
                vm.getHostname(), // the hostname
                vm.getDomain(), // the domain
                applicationInstance.getApplication().getApplication().getName(),
                applicationInstance.getVdc()); // the product
        task.setResult(new TaskReference(aiResource));
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
        LOGGER.info("An error occurs while executing a product action. See task "
                + task.getHref() + "for more information");
    }

    private ApplicationInstance getInstalledApplication(
            ApplicationRelease application, VM vm) {
        ApplicationInstanceSearchCriteria criteria = new ApplicationInstanceSearchCriteria();
        criteria.setVm(vm);
        criteria.setApplicationName(application.getApplication().getName());
        try {
            return loadByCriteria(criteria);
        } catch (EntityNotFoundException e) {
            return null;
        } catch (NotUniqueResultException e) {
            return null;
        }
    }

    private void notifyTask(String url, Task task) {
        if (!StringUtils.isEmpty(url)) {
            taskNotificator.notify(url, task);
        }
    }
    /*
     * Update the error task in install operation. The installation is a bit
     * different to the other operations because the instance could exist or not
     * so this method shall do this check.
     */
    private void updateErrorTaskOnInstall(ApplicationRelease application, VM vm,
            Task task, String errorMsg, Throwable e) {
        ApplicationInstance instance = getInstalledApplication(application, vm);
        if (instance != null) {
            updateErrorTask(instance, task, errorMsg, e);
        } else {
            updateErrorTask(task, errorMsg, e);
        }
    }

    ///////////// I.O.C ////////////
    /**
     * @param applicationInstanceManager the applicationInstanceManager to set
     */
    public void setApplicationInstanceManager(
            ApplicationInstanceManager applicationInstanceManager) {
        this.applicationInstanceManager = applicationInstanceManager;
    }

    /**
     * @param taskManager the taskManager to set
     */
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * @param taskNotificator the taskNotificator to set
     */
    public void setTaskNotificator(TaskNotificator taskNotificator) {
        this.taskNotificator = taskNotificator;
    }

    /**
     * @param propertiesProvider the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

}
