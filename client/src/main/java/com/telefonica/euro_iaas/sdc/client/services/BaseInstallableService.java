package com.telefonica.euro_iaas.sdc.client.services;

import java.util.List;

import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.Task;

public interface BaseInstallableService <T extends InstallableInstance> {

    /**
     * Upgrade the selected instance version.
     *
     * @param id the installable instance id
     * @param new version the new version to upgrade to
     * @param callback if not empty, contains the url where the result of the
     * async operation will be sent
     *
     * @return the task
     */
    Task upgrade(String vdc, Long id, String version, String callback);

    /**
     * Configure the selected instance.
     *
     * @param id the installable instance id
     * @param arguments
     *            the configuration properties
     * @param callback if not empty, contains the url where the result of the
     * async operation will be sent
     *
     * @return the task.
     */
    Task configure(String vdc, Long id, String callback,
            List<Attribute> arguments);

    /**
     * Uninstall a previously installed instance.
     *
     * @param id the installable instance id
     * @param callback if not empty, contains the url where the result of the
     * async operation will be sent
     * @return the task.
     */
    Task uninstall(String vdc, Long id, String callback);

    /**
     * Retrieve the selected application instance.
     *
     * @param vdc the vdc
     * @param id
     *            the application id
     * @return the installable instance.
     * @throws ResourceNotFoundException if the resource does not found
     */
    T load(String vdc, Long id) throws ResourceNotFoundException;

    /**
     * Retrieve the selected application instance.
     *
     * @param url the url where the installable isntance is
     * @return the installable instance.
     * @throws ResourceNotFoundException if the resource does not found
     */
    T load(String url) throws ResourceNotFoundException;
}
