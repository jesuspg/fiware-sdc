/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.util;

import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Implement the IP to VM interface using the rest api provided by a server installed in every client.
 * 
 * @author Sergio Arroyo
 */
public class IpToVMRestClientImpl implements IpToVM {

    private SDCClientUtils sdcClientUtils;

    /**
     * {@inheritDoc}
     */
    public VM getVm(String ip, String fqn, String osType) {
        return sdcClientUtils.getVM(ip, fqn, osType);
    }

    /**
     * @param sdcClientUtils
     *            the sdcClientUtils to set
     */
    public void setSdcClientUtils(SDCClientUtils sdcClientUtils) {
        this.sdcClientUtils = sdcClientUtils;
    }

}
