package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;

/**
 * Exception thrown when trying to delete a ApplicationRelease that does not exist.
 *
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class ApplicationReleaseNotFoundException extends Exception {

    private ApplicationRelease applicationRelease;

    public ApplicationReleaseNotFoundException() {
        super();
    }

    public ApplicationReleaseNotFoundException(ApplicationRelease applicationRelease) {
        this.applicationRelease = applicationRelease;
    }
    
    public ApplicationReleaseNotFoundException(String msg) {
        super(msg);
    }

    public ApplicationReleaseNotFoundException(Throwable e) {
        super(e);
    }

    public ApplicationReleaseNotFoundException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the applicationReleaseRelease
     */
    public ApplicationRelease getInstace() {
        return applicationRelease;
    }

    /**
     * @param applicationRelease the applicationRelease to set
     */
    public void setApplicationRelease (ApplicationRelease applicationRelease) {
        this.applicationRelease = applicationRelease;
    }
}

