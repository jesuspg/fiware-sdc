/**
 * 
 */
package com.telefonica.euro_iaas.sdc.exception;

/**
 * @author jesus.movilla
 *
 */
public class ChefClientExecutionException extends Exception {
	
	public ChefClientExecutionException() {
		super();
	}

	public ChefClientExecutionException(String msg) {
		super(msg);
	}

	public ChefClientExecutionException(Throwable e) {
		super(e);
	}

	public ChefClientExecutionException(String msg, Throwable e) {
		super(msg, e);
	}

}
