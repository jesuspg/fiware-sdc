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

import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Exception thrown when trying to instal a Product and the incoming data is not adequate
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
     * @param vm
     *            the vm to set
     */
    public void setVM(VM vm) {
        this.vm = vm;
    }
}
