/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.exception;

/**
 * Exception thrown when our Puppet rest API returns unexpected results.
 * 
 * @author Alberts 
 */
@SuppressWarnings("serial")
public class CanNotCallPuppetException extends Exception {
    public CanNotCallPuppetException() {
        super();
    }

    public CanNotCallPuppetException(String msg) {
        super(msg);
    }

    public CanNotCallPuppetException(Throwable e) {
        super(e);
    }

    public CanNotCallPuppetException(String msg, Throwable e) {
        super(msg, e);
    }

}
