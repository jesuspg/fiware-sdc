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

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.client.services;

import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

/**
 * @author jesus.movilla
 */
public interface ChefClientService {

    /**
     * Delete the ChefClient from the Chef Server.
     * 
     * @param chefClientName
     *            to be deleted
     * @return the task
     */
    Task delete(String vdc, String chefClientName, String token) throws InvalidExecutionException;

    /**
     * Load the ChefClient.
     * 
     * @param vdc
     * @return
     */
    ChefClient load(String vdc, String chefClientName, String token) throws ResourceNotFoundException;

    /**
     * Load by hostname the ChefClient.
     * 
     * @param vdc
     * @return
     */
    ChefClient loadByHostname(String vdc, String hostname, String token) throws ResourceNotFoundException;
}
