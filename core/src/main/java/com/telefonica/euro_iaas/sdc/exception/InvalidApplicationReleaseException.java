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

import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;

/**
 * Exception thrown when trying to insert an ApplicationRelease that does not have the right information
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class InvalidApplicationReleaseException extends Exception {

    private ApplicationRelease appRelease;

    public InvalidApplicationReleaseException() {
        super();
    }

    public InvalidApplicationReleaseException(ApplicationRelease appRelease) {
        this.appRelease = appRelease;
    }

    public InvalidApplicationReleaseException(String msg) {
        super(msg);
    }

    public InvalidApplicationReleaseException(Throwable e) {
        super(e);
    }

    public InvalidApplicationReleaseException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the productRelease
     */
    public ApplicationRelease getApplicationRelease() {
        return appRelease;
    }

    /**
     * @param productRelease
     *            the productRelease to set
     */
    public void setApplicationRelease(ApplicationRelease appRelease) {
        this.appRelease = appRelease;
    }
}
