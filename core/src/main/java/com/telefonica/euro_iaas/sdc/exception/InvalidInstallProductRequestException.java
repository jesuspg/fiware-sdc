package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Exception thrown when trying to instal a Product and the incoming data is 
 * not adequate
 *
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class InvalidInstallProductRequestException extends Exception {
    
	private VM vm;
    
	public InvalidInstallProductRequestException() {
       super();
    }

    public InvalidInstallProductRequestException(VM vm) {
        this.vm = vm;
    }

    public InvalidInstallProductRequestException(String msg) {
        super(msg);
    }

    public InvalidInstallProductRequestException(Throwable e) {
       super(e);
    }
	    
    public InvalidInstallProductRequestException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * @return the vm
     */
    public VM getVM() {
        return vm;
    }
	    /**
     * @param vm the vm to set
     */
    public void setVM(VM vm) {
        this.vm = vm;
    }
}


