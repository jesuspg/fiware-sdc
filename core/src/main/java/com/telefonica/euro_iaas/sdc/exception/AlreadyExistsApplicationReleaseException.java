package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;

/**
 * Exception thrown when trying to Insert a ApplicationRelease that already exists.
 * 
 * @author Jesus M. Movilla
 */

@SuppressWarnings("serial")
public class AlreadyExistsApplicationReleaseException extends Exception {

    private ApplicationRelease appRelease;

    public AlreadyExistsApplicationReleaseException() {
        super();
    }

    public AlreadyExistsApplicationReleaseException(ApplicationRelease appRelease) {
        this.appRelease = appRelease;
    }

    public AlreadyExistsApplicationReleaseException(String msg) {
        super(msg);
    }

    public AlreadyExistsApplicationReleaseException(Throwable e) {
        super(e);
    }

    public AlreadyExistsApplicationReleaseException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the appRelease
     */
    public ApplicationRelease getApplicationRelease() {
        return appRelease;
    }

    /**
     * @param appRelease
     *            the appRelease to set
     */
    public void setApplicationRelease(ApplicationRelease appRelease) {
        this.appRelease = appRelease;
    }
}
