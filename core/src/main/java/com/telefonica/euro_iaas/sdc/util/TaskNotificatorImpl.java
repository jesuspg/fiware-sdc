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

package com.telefonica.euro_iaas.sdc.util;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.model.Task;

/**
 * TaskNotificator rest implementation.
 * 
 * @author Sergio Arroyo
 */
public class TaskNotificatorImpl implements TaskNotificator {

    private Client client;

    /**
     * {@inheritDoc}
     */
    @Override
    public void notify(String url, Task task) {
        WebResource webResource = client.resource(url);
        webResource.type(MediaType.APPLICATION_XML).entity(task).post();
    }

    /**
     * @param client
     *            the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
}
