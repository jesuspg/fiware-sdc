package com.telefonica.euro_iaas.sdc.exception;

@SuppressWarnings("serial")
public class ShellCommandException extends Exception {

	private String description;

	/**
	 * Constructor of the class.
	 * 
	 * @param entity
	 *            The requested entity
	 */

	public ShellCommandException(String description) {
		this.description = description;
	}

	/**
	 * Constructor of the class.
	 * 
	 * @param entity
	 *            The requested entity
	 * @param cause
	 *            Parent exception
	 */
	public ShellCommandException(String description, Exception cause) {
		super(cause);
		this.description = description;
	}

	/** {@inheritDoc} */
	@Override
	public String getMessage() {
		return "Trying to persist a duplicated " + description
				+ " entity. Caused by: " + getCause();
	}
}
