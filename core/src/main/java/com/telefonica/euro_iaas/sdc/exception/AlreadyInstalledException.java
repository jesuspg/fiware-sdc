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

import com.telefonica.euro_iaas.sdc.model.InstallableInstance;

/**
 * Exception thrown when try to install an installable unit that already exists.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class AlreadyInstalledException extends Exception {

    private InstallableInstance instace;

    public AlreadyInstalledException() {
        super();
    }

    public AlreadyInstalledException(InstallableInstance instace) {
        this.instace = instace;
    }

    public AlreadyInstalledException(String msg) {
        super(msg);
    }

    public AlreadyInstalledException(Throwable e) {
        super(e);
    }

    public AlreadyInstalledException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the instace
     */
    public InstallableInstance getInstace() {
        return instace;
    }

    /**
     * @param instace
     *            the instace to set
     */
    public void setInstace(InstallableInstance instace) {
        this.instace = instace;
    }

    @Override
    public String getMessage() {
        return "The " + instace.getClass().getSimpleName() + " " + instace.getId() + " is already installed.";
    }
}
