package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;

/**
 * Exception thrown when try to uninstall an application that has any application running on it.
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class IncompatibleEnvironmentException extends Exception {

    private ApplicationInstance application;
    private EnvironmentInstance environmentInstance;

    /**
     * @param applications
     * @param environmentInstance
     */
    public IncompatibleEnvironmentException(ApplicationInstance application, EnvironmentInstance environmentInstance) {
        this.application = application;
        this.environmentInstance = environmentInstance;

    }

    @Override
    public String getMessage() {
        return "The application " + application.getApplication().getApplication().getName() + "-"
                + application.getApplication().getVersion() + "can not be installed in this environment: "
                + environmentInstance.getEnvironment().getName();
    }

    /**
     * @return the application
     */
    public ApplicationInstance getApplication() {
        return application;
    }

    /**
     * @param application
     *            the application to set
     */
    public void setApplication(ApplicationInstance application) {
        this.application = application;
    }

    /**
     * @return the environmentInstance
     */
    public EnvironmentInstance getEnvironmentInstance() {
        return environmentInstance;
    }

    /**
     * @param environmentInstance
     *            the environmentInstance to set
     */
    public void setProducts(EnvironmentInstance environmentInstance) {
        this.environmentInstance = environmentInstance;
    }

}
