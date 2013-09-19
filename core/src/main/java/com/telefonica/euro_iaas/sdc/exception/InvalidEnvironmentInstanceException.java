package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;

/**
 * Exception thrown when trying to insert a ProductRelease that does not have 
 * the right information
 *
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class InvalidEnvironmentInstanceException extends Exception {

    private EnvironmentInstance environment;

    public InvalidEnvironmentInstanceException() {
        super();
    }

    public InvalidEnvironmentInstanceException(EnvironmentInstance environment) {
        this.environment = environment;
    }

    public InvalidEnvironmentInstanceException(String msg) {
        super(msg);
    }

    public InvalidEnvironmentInstanceException(Throwable e) {
        super(e);
    }
    
    public InvalidEnvironmentInstanceException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the environmentInstance
     */
    public EnvironmentInstance getEnvironment() {
        return environment;
    }

    /**
     * @param environmentInstance the environmentInstance to set
     */
    public void setEnvironment(EnvironmentInstance environment) {
        this.environment = environment;
    }
}
