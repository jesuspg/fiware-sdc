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


    private SDCClientUtils sdcClientUtils;
    /**
     * {@inheritDoc}
     */
    @Override
    public VM getVm(String ip) {

        return sdcClientUtils.getVM(ip);
    }
    /**
     * @param sdcClientUtils the sdcClientUtils to set
     */
    public void setSdcClientUtils(SDCClientUtils sdcClientUtils) {
        this.sdcClientUtils = sdcClientUtils;
    }


}
