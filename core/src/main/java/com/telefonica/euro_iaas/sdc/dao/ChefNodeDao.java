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

package com.telefonica.euro_iaas.sdc.dao;

import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

/**
 * Provides the methods to work with Chef Nodes.
 * 
 * @author Sergio Arroyo
 */
public interface ChefNodeDao {

    /**
     * Retrieve all the nodes (name and url) registered in ChefServer.
     * 
     * @return
     * @throws CanNotCallChefException
     */
    ChefNode loadNodeFromHostname(String hostname, String token) throws EntityNotFoundException,
            CanNotCallChefException;

    /**
     * Retrieve the node information form Chef server given a VM (containing hostname and domain).
     * 
     * @param vm
     *            the VM
     * @return the ChefNode.
     * @throws CanNotCallChefException
     *             if Chef Server returns an unexpected error code
     */
    ChefNode loadNode(String chefNodeName, String token) throws CanNotCallChefException;

    /**
     * Update the ChefNode with the actual values.
     * 
     * @param node
     *            the node
     * @return the updated node.
     * @throws CanNotCallChefException
     *             if Chef Server returns an unexpected error code
     */
    ChefNode updateNode(ChefNode node, String token) throws CanNotCallChefException;

    /**
     * Delete the ChefNode
     * 
     * @param node
     *            the node
     * @throws CanNotCallChefException
     *             if Chef Server returns an unexpected error code
     */
    void deleteNode(ChefNode node, String token) throws CanNotCallChefException;

    /**
     * Checks if ChefNode is already registered in ChefServer.
     * 
     * @param hostname
     * @return void
     * @throws CanNotCallChefException
     */
    void isNodeRegistered(String hostname, String token) throws CanNotCallChefException;
}
