/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.dao;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

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
    void deleteChefClient(String chefClientName) throws CanNotCallChefException;

    /**
     * Get the ChefClient
     * 
     * @param chefClientName
     * @return
     * @throws CanNotCallChefException
     */
    ChefClient getChefClient(String chefClientName) throws CanNotCallChefException, EntityNotFoundException;

    /**
     * FindAll ChefClients
     * 
     * @return
     * @throws CanNotCallChefException
     */
    ChefClient chefClientfindByHostname(String hostname) throws EntityNotFoundException, CanNotCallChefException;
}
