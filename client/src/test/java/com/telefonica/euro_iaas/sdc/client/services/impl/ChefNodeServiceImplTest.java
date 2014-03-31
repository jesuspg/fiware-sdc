/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.client.services.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.model.Task;

public class ChefNodeServiceImplTest {

    String type = "application/json";
    String baseHost = "baseHost";
    String token = "token";

    @Test
    public void shouldDeleteNode() {
        // given
        Task expectedTask = new Task();
        String chefNodeName = "nodeName";
        String vdc = "virtualDataCenter";
        String url = baseHost + "/vdc/" + vdc + "/node/" + chefNodeName;
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        Client client = mock(Client.class);
        ChefNodeServiceImpl chefNodeService = new ChefNodeServiceImpl(client, baseHost, type);

        // when
        when(client.resource(url)).thenReturn(webResource);
        when(webResource.accept(type)).thenReturn(builder);
        when(builder.type(type)).thenReturn(builder);
        when(builder.delete(Task.class)).thenReturn(expectedTask);

        Task task = null;
        try {
            task = chefNodeService.delete(vdc, chefNodeName, token);
        } catch (InvalidExecutionException e) {
            fail();
        }

        // then
        assertNotNull(task);
        verify(client).resource(url);
        verify(webResource).accept(type);
        verify(builder).delete(Task.class);
    }
}
