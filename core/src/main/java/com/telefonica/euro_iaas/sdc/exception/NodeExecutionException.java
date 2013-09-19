package com.telefonica.euro_iaas.sdc.exception;

/**
 * Exception thrown when Chef node execution fails.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class NodeExecutionException extends Exception {
	public NodeExecutionException() {
		super();
	}

	public NodeExecutionException(String msg) {
		super(msg);
	}

	public NodeExecutionException(Throwable e) {
		super(e);
	}

	public NodeExecutionException(String msg, Throwable e) {
		super(msg, e);
	}

}
