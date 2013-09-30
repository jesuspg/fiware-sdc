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

package com.telefonica.euro_iaas.sdc.client.exception;

import java.text.MessageFormat;

/**
 * Exception thrown when a resource is not found.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class ResourceNotFoundException extends Exception {

    @SuppressWarnings("rawtypes")
    private Class clazz;
    private String url;

    /**
     * @param clazz
     * @param url
     */
    public ResourceNotFoundException(Class clazz, String url) {
        this.clazz = clazz;
        this.url = url;
    }

    @Override
    public String getMessage() {
        return MessageFormat.format("Resource of type {0} where not found in url {1}", clazz.getSimpleName(), url);
    }
}
