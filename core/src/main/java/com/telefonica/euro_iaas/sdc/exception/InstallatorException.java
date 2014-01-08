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
public class InstallatorException extends Exception {
    public InstallatorException() {
        super();
    }

    public InstallatorException(String msg) {
        super(msg);
    }

    public InstallatorException(Throwable e) {
        super(e);
    }

    public InstallatorException(String msg, Throwable e) {
        super(msg, e);
    }

}
