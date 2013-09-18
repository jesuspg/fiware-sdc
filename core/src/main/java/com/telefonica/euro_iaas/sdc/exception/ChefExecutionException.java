package com.telefonica.euro_iaas.sdc.exception;
/**
 * Exception thrown when Chef node execution fails.
 *
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class ChefExecutionException extends Exception {
    public ChefExecutionException() {
        super();
    }

    public ChefExecutionException(String msg) {
        super(msg);
    }

    public ChefExecutionException(Throwable e) {
        super(e);
    }

    public ChefExecutionException(String msg, Throwable e) {
        super(msg, e);
    }

}
