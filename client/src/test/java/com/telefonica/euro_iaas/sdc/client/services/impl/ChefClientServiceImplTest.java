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

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ChefClientService;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

public class ChefClientServiceImplTest {

    ChefClientService chefClientService;
    String type = "application/json";
    String baseHost = "baseHost";
    String token = "token";
    Client client = mock(Client.class);

    @Before
    public void setUp() {

        chefClientService = new ChefClientServiceImpl(client, baseHost, type);
    }

    @Test
    public void shouldReturnAChiefClientWhenLoad() throws ResourceNotFoundException {
        // given

        String vdc = "vdc";
        String chefClientName = "chefClientName";

        ChefClient expectedChefClient = new ChefClient();
        String url = baseHost + "/vdc/" + vdc + "/chefClient/" + chefClientName;
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);

        // when
        when(client.resource(url)).thenReturn(webResource);
        when(webResource.accept(type)).thenReturn(builder);
        when(builder.type(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);
        when(builder.get(ChefClient.class)).thenReturn(expectedChefClient);

        ChefClient resultChefClient = chefClientService.load(vdc, chefClientName, token);

        // then
        assertNotNull(resultChefClient);
        verify(client).resource(url);
        verify(webResource).accept(type);
        verify(builder).get(ChefClient.class);
    }

    @Test
    public void shouldReturnAChiefClientWhenLoadByHostname() throws ResourceNotFoundException {
        // given

        String vdc = "my_vdc";
        String chefClientName = "chefClientName";

        ChefClient expectedChefClient = new ChefClient();
        ChefClient resultChefClient;
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);

        String url = baseHost + "/vdc/" + vdc + "/chefClient?hostname=" + chefClientName;
        // when

        when(client.resource(url)).thenReturn(webResource);
        when(webResource.accept(type)).thenReturn(builder);
        when(builder.type(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);
        when(builder.get(ChefClient.class)).thenReturn(expectedChefClient);

        resultChefClient = chefClientService.loadByHostname(vdc, chefClientName,token);

        // then
        assertNotNull(resultChefClient);
        verify(client).resource(url);
        verify(webResource).accept(type);
        verify(builder).get(ChefClient.class);
    }

    @Test
    public void shouldDeleteByClientName() {
        // given
        String chefClientName = "clientName";
        String vdc = "virtualDataCenter";
        String url = baseHost + "/vdc/" + vdc + "/chefClient/" + chefClientName;
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        Task expectedTask = new Task();

        // when
        when(client.resource(url)).thenReturn(webResource);
        when(webResource.accept(type)).thenReturn(builder);
        when(builder.type(type)).thenReturn(builder);
        when(builder.delete(Task.class)).thenReturn(expectedTask);

        Task task = null;
        try {
            task = chefClientService.delete(vdc, chefClientName,token);
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
