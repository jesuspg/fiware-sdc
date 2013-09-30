package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;

/**
 * Exception thrown when trying to Insert a Environment that already exists.
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class AlreadyExistsEnvironmentInstanceException extends Exception {

    private EnvironmentInstance environment;

    public AlreadyExistsEnvironmentInstanceException() {
        super();
    }

    public AlreadyExistsEnvironmentInstanceException(EnvironmentInstance environment) {
        this.environment = environment;
    }

    public AlreadyExistsEnvironmentInstanceException(String msg) {
        super(msg);
    }

    public AlreadyExistsEnvironmentInstanceException(Throwable e) {
        super(e);
    }

    public AlreadyExistsEnvironmentInstanceException(String msg, Throwable e) {
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
