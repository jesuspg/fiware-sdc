package com.telefonica.euro_iaas.sdc.exception;

/**
 * Exception thrown when the type of file to upload is incorrect.
 * 
 * @author Jesus M. Movilla
 */

@SuppressWarnings("serial")
public class ApplicationReleaseInvalidTypeRequestException extends Exception {

    public ApplicationReleaseInvalidTypeRequestException() {
        super();
    }

    public ApplicationReleaseInvalidTypeRequestException(String msg) {
        super(msg);
    }

    public ApplicationReleaseInvalidTypeRequestException(Throwable e) {
        super(e);
    }

    public ApplicationReleaseInvalidTypeRequestException(String msg, Throwable e) {
        super(msg, e);
    }
}
