/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.util;

import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Provides the way to retrieve (or deduce) the host an domain given an IP address.
 * 
 * @author Sergio Arroyo
 */
public interface IpToVM {
    /**
     * Retrieve some information about the virtual machine located in a concrete IP address.
     * 
     * @param ip
     *            the ip of the associated VM.
     * @param fqn
     *            the fqn of the associated VM.
     * @param osType
     *            the osType of the associated VM.
     * @return the VM with the whole information available (ip, host and domain)
     */
    VM getVm(String ip, String fqn, String osType);
}
