/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.manager;

import com.telefonica.euro_iaas.sdc.exception.ChefClientExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;

/**
 * @author jesus.movilla
 */
public interface NodeManager {

    /**
     * Delete a ChefClient in ChefServer
     * 
     * @param chefClientname
     *            the name of the chefclient to be deleted from chef server
     * @throws ChefClientExecutionException
     */
    void nodeDelete(String vdc, String nodeName) throws NodeExecutionException;

}
