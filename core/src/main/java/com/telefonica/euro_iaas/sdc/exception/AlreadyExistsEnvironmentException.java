package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.Environment;


/**
 * Exception thrown when trying to Insert a Environment that already exists.
 *
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class AlreadyExistsEnvironmentException extends Exception {

    private Environment environment;

    public AlreadyExistsEnvironmentException() {
        super();
    }

    public AlreadyExistsEnvironmentException(Environment environment) {
        this.environment = environment;
    }

    public AlreadyExistsEnvironmentException(String msg) {
        super(msg);
    }

    public AlreadyExistsEnvironmentException(Throwable e) {
        super(e);
    }
    
    public AlreadyExistsEnvironmentException(String msg, Throwable e) {
        super(msg, e);
    }

   
    /**
     * @return the environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * @param environment the environment to set
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
