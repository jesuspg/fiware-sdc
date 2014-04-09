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
    T upgrade(String vdc, String name, String version, String token) throws MaxTimeWaitingExceedException, InvalidExecutionException;

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

    T configure(String vdc, String name, List<Attribute> arguments,String token) throws MaxTimeWaitingExceedException,
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

    T uninstall(String vdc, String name, String token) throws MaxTimeWaitingExceedException, InvalidExecutionException;

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
    T load(String vdc, String name, String token) throws ResourceNotFoundException;

    /**
     * Retrieve the selected application instance.
     * 
     * @param url
     *            the url where the installable isntance is
     * @return the installable instance.
     * @throws ResourceNotFoundException
     *             if the resource does not found
     */
    T loadUrl(String url, String token, String tenant) throws ResourceNotFoundException;

}
