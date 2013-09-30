/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

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
