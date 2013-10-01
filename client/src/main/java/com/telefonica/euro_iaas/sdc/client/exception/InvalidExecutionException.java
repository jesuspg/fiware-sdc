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

package com.telefonica.euro_iaas.sdc.client.exception;

/**
 * Exception thrown when a task does not ends in a success status.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class InvalidExecutionException extends Exception {

    /**
     *
     */
    public InvalidExecutionException() {
        super();
    }

    /**
     * @param arg0
     * @param arg1
     */
    public InvalidExecutionException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public InvalidExecutionException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public InvalidExecutionException(Throwable arg0) {
        super(arg0);
    }

}
