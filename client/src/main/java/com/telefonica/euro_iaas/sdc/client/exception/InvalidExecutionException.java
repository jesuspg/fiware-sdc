package com.telefonica.euro_iaas.sdc.client.exception;

/**
 * Exception thrown when a task does not ends in a success status.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class InvalidExecutionException extends Exception {

    /**
     *
     */
    public InvalidExecutionException() {
        super();
    }

    /**
     * @param arg0
     * @param arg1
     */
    public InvalidExecutionException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public InvalidExecutionException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public InvalidExecutionException(Throwable arg0) {
        super(arg0);
    }

}
