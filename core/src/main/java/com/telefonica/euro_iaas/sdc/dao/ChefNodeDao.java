/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;

/**
 * Provides the methods to work with Chef Nodes.
 * 
 * @author Sergio Arroyo
 */
public interface ChefNodeDao {

    /**
     * Retrieve all the nodes (name and url) registered in ChefServer.
     * @return
     * @throws CanNotCallChefException
     */
    ChefNode loadNodeFromHostname(String hostname) throws EntityNotFoundException, 
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
    ChefNode loadNode(String chefNodeName) throws CanNotCallChefException;

    /**
     * Update the ChefNode with the actual values.
     * 
     * @param node
     *            the node
     * @return the updated node.
     * @throws CanNotCallChefException
     *             if Chef Server returns an unexpected error code
     */
    ChefNode updateNode(ChefNode node) throws CanNotCallChefException;

    /**
     * Delete the ChefNode
     * 
     * @param node
     *            the node
     * @throws CanNotCallChefException
     *             if Chef Server returns an unexpected error code
     */
    void deleteNode(ChefNode node) throws CanNotCallChefException;
    
    
    /**
     * Checks if ChefNode is already registered in ChefServer.
     * 
     * @param hostname
     * @return void
     * @throws CanNotCallChefException
     */
    void isNodeRegistered (String hostname) throws CanNotCallChefException;
}
