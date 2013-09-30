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
     * Delete the ChefClient from the Chef Server
     * 
     * @param chefClientName
     *            to be deleted
     * @return the task
     */
    Task delete(String vdc, String chefClientName) throws InvalidExecutionException;

    /**
     * Load the ChefClient
     * 
     * @param vdc
     * @return
     */
    ChefClient load(String vdc, String chefClientName) throws ResourceNotFoundException;

    /**
     * Load by hostname the ChefClient
     * 
     * @param vdc
     * @return
     */
    ChefClient loadByHostname(String vdc, String hostname) throws ResourceNotFoundException;
}
