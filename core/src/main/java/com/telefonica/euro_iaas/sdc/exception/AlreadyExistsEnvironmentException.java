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
     * @param environment
     *            the environment to set
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
