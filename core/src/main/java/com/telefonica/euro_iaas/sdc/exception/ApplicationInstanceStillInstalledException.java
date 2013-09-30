package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;

/**
 * Exception thrown when try to delete a EnvironmentInstance which is still installed
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class ApplicationInstanceStillInstalledException extends Exception {

    private ApplicationInstance applicationInstance;

    public ApplicationInstanceStillInstalledException() {
        super();
    }

    public ApplicationInstanceStillInstalledException(ApplicationInstance applicationInstance) {
        this.applicationInstance = applicationInstance;
    }

    public ApplicationInstanceStillInstalledException(String msg) {
        super(msg);
    }

    public ApplicationInstanceStillInstalledException(Throwable e) {
        super(e);
    }

    public ApplicationInstanceStillInstalledException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the applicationInstance
     */
    public ApplicationInstance getApplicationRelease() {
        return applicationInstance;
    }

    /**
     * @param applicationInstance
     *            the applicationInstance to set
     */
    public void setApplicationInstance(ApplicationInstance applicationInstance) {
        this.applicationInstance = applicationInstance;
    }

}
