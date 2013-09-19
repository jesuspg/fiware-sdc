package com.telefonica.euro_iaas.sdc.util;

import java.util.List;

import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Provides some methods to add the SDC-Client behavior.
 * 
 * @author Sergio Arroyo
 * 
 */
public interface SDCClientUtils {

	/**
	 * Retrieve the VM where the node is
	 * 
	 * @param ip
	 *            the ip
	 * @param fqn
	 *            the fqn
	 * @param osType
	 *            the OSType
	 * @return the VM containing the hostname and domain.
	 */
	VM getVM(String ip, String fqn, String osType);

	/**
	 * Make the target node call Chef to execute the enqueued tasks
	 * 
	 * @param vm
	 *            the node
	 */
	void execute(VM vm) throws NodeExecutionException;

	/**
	 * Add or update some configuration parameters to node
	 * 
	 * @param vm
	 *            the node
	 * @param configuration
	 *            the configuration properties
	 * @return the actual values in client.
	 */
	List<Attribute> configure(VM vm, List<Attribute> configuration);

	/**
	 * Add or update some configuration parameters to node
	 * 
	 * @param vm
	 *            the node
	 * @param the
	 *            property to be modfied
	 * @return the modified property.
	 */
	Attribute configureProperty(VM vm, Attribute attriute);

	/**
	 * set the NodeCommands in a particular node
	 * 
	 * @param vm
	 *            the node
	 */
	void setNodeCommands(VM vm) ;//throws InvalidInstallProductRequestException;
	
	/**
	 * Check if the node is ready to install software
	 * @param ip
	 * @throws NodeExecutionException
	 */
	void checkIfSdcNodeIsReady (String ip) throws NodeExecutionException;
}
