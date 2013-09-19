package com.telefonica.euro_iaas.sdc.exception;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;

/**
 * Exception thrown when try to install an application and the needed products
 * are not installed.
 * 
 * @author Jesus M. Movilla
 * 
 **/

@SuppressWarnings("serial")
public class NotInstalledEnvironmentException extends Exception {

	private EnvironmentInstance environmentInstance;

	/**
	 * @param applications
	 * @param environmentInstance
	 */
	public NotInstalledEnvironmentException(
			EnvironmentInstance environmentInstance) {
		this.environmentInstance = environmentInstance;

	}

	private String listEnvironemnts(List<Environment> environments) {
		String environmentList = "";
		for (Environment environment : environments) {
			environmentList = environmentList.concat(", "
					+ environment.getName());
		}
		return environmentList;
	}

	@Override
	public String getMessage() {
		return "This Environment is not installed "
				+ environmentInstance.getEnvironment().getName();
	}

	/**
	 * @return the environmentInstance
	 */
	public EnvironmentInstance getEnvironmentInstance() {
		return environmentInstance;
	}

}
