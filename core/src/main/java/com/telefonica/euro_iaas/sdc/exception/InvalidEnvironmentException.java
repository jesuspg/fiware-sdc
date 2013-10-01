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
 * Exception thrown when trying to insert a ProductRelease that does not have the right information
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class InvalidEnvironmentException extends Exception {

    private Environment environment;

    public InvalidEnvironmentException() {
        super();
    }

    public InvalidEnvironmentException(Environment environment) {
        this.environment = environment;
    }

    public InvalidEnvironmentException(String msg) {
        super(msg);
    }

    public InvalidEnvironmentException(Throwable e) {
        super(e);
    }

    public InvalidEnvironmentException(String msg, Throwable e) {
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
