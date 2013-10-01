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

import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;

/**
 * Exception thrown when try to delete a EnvironmentInstance which is still installed
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class ApplicationInstanceStillInstalledException extends Exception {

    private ApplicationInstance applicationInstance;

    public ApplicationInstanceStillInstalledException() {
        super();
    }

    public ApplicationInstanceStillInstalledException(ApplicationInstance applicationInstance) {
        this.applicationInstance = applicationInstance;
    }

    public ApplicationInstanceStillInstalledException(String msg) {
        super(msg);
    }

    public ApplicationInstanceStillInstalledException(Throwable e) {
        super(e);
    }

    public ApplicationInstanceStillInstalledException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the applicationInstance
     */
    public ApplicationInstance getApplicationRelease() {
        return applicationInstance;
    }

    /**
     * @param applicationInstance
     *            the applicationInstance to set
     */
    public void setApplicationInstance(ApplicationInstance applicationInstance) {
        this.applicationInstance = applicationInstance;
    }

}
