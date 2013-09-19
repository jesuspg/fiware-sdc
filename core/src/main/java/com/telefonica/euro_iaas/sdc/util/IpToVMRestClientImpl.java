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
