/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.rest.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author jesusmmovilla
 */
public class UnauthorizedOperationException extends WebApplicationException {

    /**
     * Create the exception with the message to throw.
     * 
     * @param message
     */
    public UnauthorizedOperationException(final String message) {
        super(Response.status(Status.UNAUTHORIZED).type(MediaType.APPLICATION_XHTML_XML).entity(message).build());
    }
}
