package com.telefonica.euro_iaas.sdc.util;

import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Provides the way to retrieve (or deduce) the host an domain given an IP
 * address.
 * 
 * @author Sergio Arroyo
 * 
 */
public interface IpToVM {
	/**
	 * Retrieve some information about the virtual machine located in a concrete
	 * IP address.
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
