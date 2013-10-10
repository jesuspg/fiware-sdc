/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.exception;

@SuppressWarnings("serial")
public class ShellCommandException extends Exception {

    private String description;

    /**
     * Constructor of the class.
     * 
     * @param entity
     *            The requested entity
     */

    public ShellCommandException(String description) {
        this.description = description;
    }

    /**
     * Constructor of the class.
     * 
     * @param entity
     *            The requested entity
     * @param cause
     *            Parent exception
     */
    public ShellCommandException(String description, Exception cause) {
        super(cause);
        this.description = description;
    }

    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return "Trying to persist a duplicated " + description + " entity. Caused by: " + getCause();
    }
}
