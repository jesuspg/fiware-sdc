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

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.manager;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ChefClientExecutionException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

/**
 * @author jesus.movilla
 */
public interface ChefClientManager {

    /**
     * Delete a ChefClient in ChefServer
     * 
     * @param chefClientname
     *            the name of the chefclient to be deleted from chef server
     * @throws ChefClientExecutionException
     */
    void chefClientDelete(String vdc, String chefClientname) throws ChefClientExecutionException;

    /**
     * Load a ChefClient from ChefServer
     * 
     * @param chefClientname
     *            the name of the chefclient to be deleted from chef server
     * @throws ChefClientExecutionException
     * @throws EntityNotFoundException
     */
    ChefClient chefClientload(String chefClientname) throws ChefClientExecutionException, EntityNotFoundException;

    /**
     * Loads the chefclient whose hostname is hostname
     * 
     * @param hostname
     * @return ChefClient
     */
    ChefClient chefClientfindByHostname(String hostname) throws ChefClientExecutionException, EntityNotFoundException;
}
