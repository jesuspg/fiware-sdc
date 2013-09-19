package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.Environment;

/**
 * Exception thrown when trying to delete a Environment that does not exist.
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class EnvironmentNotFoundException extends Exception {

	private Environment environment;

	public EnvironmentNotFoundException() {
		super();
	}

	public EnvironmentNotFoundException(Environment environment) {
		this.environment = environment;
	}

	public EnvironmentNotFoundException(String msg) {
		super(msg);
	}

	public EnvironmentNotFoundException(Throwable e) {
		super(e);
	}

	public EnvironmentNotFoundException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @param environment
	 *            the environment to set
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
