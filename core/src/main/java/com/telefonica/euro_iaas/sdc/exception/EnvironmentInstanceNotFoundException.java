package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;

/**
 * Exception thrown when trying to delete a Environment that does not exist.
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class EnvironmentInstanceNotFoundException extends Exception {

    private EnvironmentInstance environment;

    public EnvironmentInstanceNotFoundException() {
        super();
    }

    public EnvironmentInstanceNotFoundException(EnvironmentInstance environment) {
        this.environment = environment;
    }

    public EnvironmentInstanceNotFoundException(String msg) {
        super(msg);
    }

    public EnvironmentInstanceNotFoundException(Throwable e) {
        super(e);
    }

    public EnvironmentInstanceNotFoundException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the environmentInstance
     */
    public EnvironmentInstance getEnvironment() {
        return environment;
    }

    /**
     * @param environmentInstance
     *            the environmentInstance to set
     */
    public void setEnvironment(EnvironmentInstance environment) {
        this.environment = environment;
    }
}
