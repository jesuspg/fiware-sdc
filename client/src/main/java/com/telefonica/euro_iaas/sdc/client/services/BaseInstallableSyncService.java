/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.client.services;

import java.util.List;

import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;

public interface BaseInstallableSyncService<T extends InstallableInstance> {

    /**
     * Upgrade the selected instance version.
     * 
     * @param id
     *            the installable instance id
     * @param new version the new version to upgrade to async operation will be sent
     * @return the task
     * @throws MaxTimeWaitingExceedException
     *             if the operation spend more time that is allowed
     * @throws InvalidExecutionException
     *             if the return of the task is not success
     */
    T upgrade(String vdc, String name, String version) throws MaxTimeWaitingExceedException, InvalidExecutionException;

    /**
     * Configure the selected instance.
     * 
     * @param id
     *            the installable instance id
     * @param arguments
     *            the configuration properties async operation will be sent
     * @return the task.
     * @throws MaxTimeWaitingExceedException
     *             if the operation spend more time that is allowed
     * @throws InvalidExecutionException
     *             if the return of the task is not success
     */
    // T configure(String vdc, Long id, List<Attribute> arguments)
    // throws MaxTimeWaitingExceedException, InvalidExecutionException;

    T configure(String vdc, String name, List<Attribute> arguments) throws MaxTimeWaitingExceedException,
            InvalidExecutionException;

    /**
     * Uninstall a previously installed instance.
     * 
     * @param id
     *            the installable instance id
     * @param callback
     *            if not empty, contains the url where the result of the async operation will be sent
     * @return the task.
     * @throws MaxTimeWaitingExceedException
     *             if the operation spend more time that is allowed
     * @throws InvalidExecutionException
     *             if the return of the task is not success
     */
    // T uninstall(String vdc, Long id)
    // throws MaxTimeWaitingExceedException, InvalidExecutionException;

    T uninstall(String vdc, String name) throws MaxTimeWaitingExceedException, InvalidExecutionException;

    /**
     * Retrieve the selected application instance.
     * 
     * @param vdc
     *            the vdc
     * @param id
     *            the application id
     * @return the installable instance.
     * @throws ResourceNotFoundException
     *             if the resource does not found
     */
    // T load(String vdc, Long id) throws ResourceNotFoundException;
    T load(String vdc, String name) throws ResourceNotFoundException;

    /**
     * Retrieve the selected application instance.
     * 
     * @param url
     *            the url where the installable isntance is
     * @return the installable instance.
     * @throws ResourceNotFoundException
     *             if the resource does not found
     */
    T load(String url) throws ResourceNotFoundException;

}
