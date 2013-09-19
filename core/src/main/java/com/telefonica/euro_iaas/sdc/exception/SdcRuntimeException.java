package com.telefonica.euro_iaas.sdc.exception;

/**
 * Generic runtime exception for the application.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class SdcRuntimeException extends RuntimeException {

	public SdcRuntimeException() {
		super();
	}

	public SdcRuntimeException(String msg) {
		super(msg);
	}

	public SdcRuntimeException(Throwable e) {
		super(e);
	}

	public SdcRuntimeException(String msg, Throwable e) {
		super(msg, e);
	}
}
