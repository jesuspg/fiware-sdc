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
package com.telefonica.euro_iaas.sdc.dao;

import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

/**
 * @author jesus.movilla
 */
public interface ChefClientDao {

    /**
     * Delete the ChefClient from ChefServer
     * 
     * @param chefClientName
     *            the chefClientName to be deleted
     * @throws CanNotCallChefException
     *             if Chef Server returns an unexpected error code
     */
    void deleteChefClient(String chefClientName, String token) throws CanNotCallChefException;

    /**
     * Get the ChefClient
     * 
     * @param chefClientName
     * @return
     * @throws CanNotCallChefException
     */
    ChefClient getChefClient(String chefClientName, String token) throws CanNotCallChefException,
            EntityNotFoundException;

    /**
     * FindAll ChefClients
     * 
     * @return
     * @throws CanNotCallChefException
     */
    ChefClient chefClientfindByHostname(String hostname, String token) throws EntityNotFoundException,
            CanNotCallChefException;
}
