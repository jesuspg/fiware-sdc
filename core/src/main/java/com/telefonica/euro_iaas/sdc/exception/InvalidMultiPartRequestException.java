/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Exception thrown when the MultuiPart object is invalid have the right information
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class InvalidMultiPartRequestException extends Exception {

    private ProductRelease productRelease;

    public InvalidMultiPartRequestException() {
        super();
    }

    public InvalidMultiPartRequestException(String msg) {
        super(msg);
    }

    public InvalidMultiPartRequestException(Throwable e) {
        super(e);
    }

    public InvalidMultiPartRequestException(Throwable e, ProductRelease productRelease) {
        super(e);
        this.productRelease = productRelease;
    }

    public InvalidMultiPartRequestException(String msg, Throwable e) {
        super(msg, e);
    }

}
