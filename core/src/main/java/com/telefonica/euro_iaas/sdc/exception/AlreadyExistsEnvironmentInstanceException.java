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
