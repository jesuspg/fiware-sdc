package com.telefonica.euro_iaas.sdc.util;

import com.telefonica.euro_iaas.sdc.model.dto.VM;
/**
 * Implement the IP to VM interface using the rest api provided by a server
 * installed in every client.
 *
 * @author Sergio Arroyo
 *
 */
public class IpToVMRestClientImpl implements IpToVM {

    /**
     * {@inheritDoc}
     */
    @Override
    public VM getVm(String ip) {
        // TODO Sergio Arroyo implements this when the agent deployed in client
        //is implemented.
        return new VM(ip, "debian", ".flexiscale.com");
    }

}
