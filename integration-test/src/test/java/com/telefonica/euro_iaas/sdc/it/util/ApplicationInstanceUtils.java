package com.telefonica.euro_iaas.sdc.it.util;

import java.util.List;

import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ApplicationInstanceSyncService;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.VM;


import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.BASE_URL;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.MIME_TYPE;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;

/**
 * Provides some utility methods to work with A
 * 
 * @author Sergio Arroyo Jesus M. Movilla
 */
public class ApplicationInstanceUtils {

    private SDCClient client = new SDCClient();
    private ApplicationInstanceSyncService service;

    /**
     * Install the application
     * 
     * @param applicationName
     *            the application name
     * @param version
     *            the version
     * @param ip
     *            the ip where the application will be installed
     * @param vdc
     *            the vdc where the node is
     * @return the installed application
     * @throws MaxTimeWaitingExceedException
     *             if the installation takes more time than expected
     * @throws InvalidExecutionException
     *             if the application can not be installed
     */
    public ApplicationInstance install(String applicationName, String version, String ip, String vdc,
            EnvironmentInstanceDto environmentInstance) throws MaxTimeWaitingExceedException, InvalidExecutionException {
        service = client.getApplicationInstanceSyncService(getProperty(BASE_URL), getProperty(MIME_TYPE));

        ApplicationInstanceDto application = new ApplicationInstanceDto(applicationName, version, new VM(ip),
                environmentInstance);
        return service.install(vdc, application);
    }

    /**
     * Configure the selected application
     * 
     * @param vdc
     * @param name
     * @param attributes
     * @return the configured application
     * @throws MaxTimeWaitingExceedException
     *             if the installation takes more time than expected
     * @throws InvalidExecutionException
     *             if the application can not be configured
     */
    public ApplicationInstance configure(String vdc, String name, List<Attribute> attributes)
            throws MaxTimeWaitingExceedException, InvalidExecutionException {
        service = client.getApplicationInstanceSyncService(getProperty(BASE_URL), getProperty(MIME_TYPE));
        return service.configure(vdc, name, attributes);
    }

    /**
     * Upgrade the selected application from actual version to "newVersion".
     * 
     * @param vdc
     * @param name
     * @param newVersion
     * @return the upgraded application
     * @throws MaxTimeWaitingExceedException
     *             if the installation takes more time than expected
     * @throws InvalidExecutionException
     *             if the application can not be configured
     */
    public ApplicationInstance upgrade(String vdc, String name, String newVersion)
            throws MaxTimeWaitingExceedException, InvalidExecutionException {
        service = client.getApplicationInstanceSyncService(getProperty(BASE_URL), getProperty(MIME_TYPE));
        return service.upgrade(vdc, name, newVersion);
    }

    /**
     * Uninstall the selected application.
     * 
     * @param vdc
     * @param name
     * @return the uninstalled application
     * @throws MaxTimeWaitingExceedException
     *             if the installation takes more time than expected
     * @throws InvalidExecutionException
     *             if the application can not be configured
     */
    public ApplicationInstance uninstall(String vdc, String name) throws MaxTimeWaitingExceedException,
            InvalidExecutionException {
        service = client.getApplicationInstanceSyncService(getProperty(BASE_URL), getProperty(MIME_TYPE));
        return service.uninstall(vdc, name);
    }

    /**
     * Load the selected application.
     * 
     * @param vdc
     * @param id
     * @return the loaded application
     * @throws ResourceNotFoundException
     */
    public ApplicationInstance load(String vdc, String name) throws ResourceNotFoundException {
        service = client.getApplicationInstanceSyncService(getProperty(BASE_URL), getProperty(MIME_TYPE));
        return service.load(vdc, name);
    }

    /**
     * Install the application if not previously installed
     * 
     * @param applicationName
     * @param version
     * @param ip
     * @param vdc
     * @return
     * @throws MaxTimeWaitingExceedException
     *             if the installation takes more time than expected
     * @throws InvalidExecutionException
     *             if the application can not be configured
     */
    public ApplicationInstance installIfNotInstalled(String applicationName, String version, String ip, String vdc,
            EnvironmentInstanceDto envronmentInstance) throws MaxTimeWaitingExceedException, InvalidExecutionException {
        ApplicationInstance instance = null;
        service = client.getApplicationInstanceSyncService(getProperty(BASE_URL), getProperty(MIME_TYPE));
        List<ApplicationInstance> instances = service.findAll(null, null, ip, null, null, null, null, null, null, vdc,
                applicationName);
        if (!instances.isEmpty()) {
            instance = instances.iterator().next();
            instance = uninstallIfNeed(instance, version);
        } else {
            instance = install(applicationName, version, ip, vdc, envronmentInstance);
        }

        if (!isInstalled(instance)) {
            instance = install(applicationName, version, ip, vdc, envronmentInstance);
        }
        return instance;
    }

    /**
     * Uninstall the application if the version is not the correct one.
     * 
     * @param instance
     * @param version
     * @return
     * @throws MaxTimeWaitingExceedException
     * @throws InvalidExecutionException
     */
    private ApplicationInstance uninstallIfNeed(ApplicationInstance instance, String version)
            throws MaxTimeWaitingExceedException, InvalidExecutionException {
        if (!instance.getApplication().getVersion().equals(version) && (isInstalled(instance))) {
            instance = uninstall(instance.getVdc(), instance.getName());
        }
        return instance;
    }

    /**
     * Decides if a application instance is installed
     * 
     * @param instance
     * @return
     */
    private boolean isInstalled(ApplicationInstance instance) {
        return (!instance.getStatus().equals(Status.ERROR) && !instance.getStatus().equals(Status.UNINSTALLED));
    }

}
