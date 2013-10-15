/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.exception;

/**
 * Generic runtime exception for the application.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class SdcRuntimeException extends RuntimeException {

    public SdcRuntimeException() {
        super();
    }

    public SdcRuntimeException(String msg) {
        super(msg);
    }

    public SdcRuntimeException(Throwable e) {
        super(e);
    }

    public SdcRuntimeException(String msg, Throwable e) {
        super(msg, e);
    }
}
