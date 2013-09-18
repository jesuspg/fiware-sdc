package com.telefonica.euro_iaas.sdc.exception;
/**
 * Exception thrown when Chef rest API return unexpected results.
 *
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class CanNotCallChefException extends Exception {
    public CanNotCallChefException() {
        super();
    }

    public CanNotCallChefException(String msg) {
        super(msg);
    }

    public CanNotCallChefException(Throwable e) {
        super(e);
    }

    public CanNotCallChefException(String msg, Throwable e) {
        super(msg, e);
    }

}
