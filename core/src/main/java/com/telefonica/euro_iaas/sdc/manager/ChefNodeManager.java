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

package com.telefonica.euro_iaas.sdc.manager;

import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;

/**
 * Defines the operacion over ChefNodes in ChefServer
 * 
 * @author Jesus M. Movilla
 */
public interface ChefNodeManager {

    /**
     * Delete a ChefNode in ChefServer
     * 
     * @param chefNodeName
     *            the name of the node to be deleted from chef server
     * @throws NodeExecutionException
     */
    void chefNodeDelete(String vdc, String chefNodename) throws NodeExecutionException;
}
