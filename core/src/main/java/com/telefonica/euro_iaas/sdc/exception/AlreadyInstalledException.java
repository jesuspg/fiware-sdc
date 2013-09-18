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
     * @param instace the instace to set
     */
    public void setInstace(InstallableInstance instace) {
        this.instace = instace;
    }
}
