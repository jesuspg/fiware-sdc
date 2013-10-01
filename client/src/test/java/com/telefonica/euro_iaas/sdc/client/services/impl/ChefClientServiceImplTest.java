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

package com.telefonica.euro_iaas.sdc.client.services.impl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ChefClientService;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChefClientServiceImplTest {

    SDCWebResourceFactory sdcWebResourceFactory;
    SDCWebResource sdcWebResource;
    ChefClientService chefClientService;
    String type = "application/json";
    String baseHost = "baseHost";
    Client client = mock(Client.class);

    @Before
    public void setUp() {
        sdcWebResourceFactory = mock(SDCWebResourceFactory.class);
        sdcWebResource = mock(SDCWebResource.class);

        chefClientService = new ChefClientServiceImpl(client, baseHost, type);
        ((ChefClientServiceImpl) chefClientService).setSdcWebResourceFactory(sdcWebResourceFactory);
        when(sdcWebResourceFactory.getInstance(any(WebResource.class))).thenReturn(sdcWebResource);
    }

    @Test
    public void shouldReturnAChiefClientWhenLoad() throws ResourceNotFoundException {
        // given

        String vdc = "vdc";
        String chefClientName = "chefClientName";

        ChefClient expectedChefClient = new ChefClient();
        ChefClient resultChefClient;
        String url = baseHost + "/vdc/" + vdc + "/chefClient/" + chefClientName;

        WebResource webResource = mock(WebResource.class);
        // when
        when(client.resource(url)).thenReturn(webResource);

        when(sdcWebResource.get(type, ChefClient.class)).thenReturn(expectedChefClient);

        resultChefClient = chefClientService.load(vdc, chefClientName);

        // then
        assertNotNull(resultChefClient);
        verify(client).resource(url);
    }

    @Test
    public void shouldReturnAChiefClientWhenLoadByHostname() throws ResourceNotFoundException {
        // given

        String vdc = "my_vdc";
        String chefClientName = "chefClientName";

        ChefClient expectedChefClient = new ChefClient();
        ChefClient resultChefClient;
        WebResource webResource = mock(WebResource.class);

        String url = baseHost + "/vdc/" + vdc + "/chefClient?hostname=" + chefClientName;
        // when

        when(client.resource(url)).thenReturn(webResource);
        when(sdcWebResource.get(type, ChefClient.class)).thenReturn(expectedChefClient);

        resultChefClient = chefClientService.loadByHostname(vdc, chefClientName);

        // then
        assertNotNull(resultChefClient);
        verify(client).resource(url);
        verify(sdcWebResource).get(type, ChefClient.class);
    }

    @Test
    public void shouldDeleteByClientName() {
        // given
        String chefClientName = "clientName";
        String vdc = "virtualDataCenter";
        String url = baseHost + "/vdc/" + vdc + "/chefClient/" + chefClientName;
        WebResource webResource = mock(WebResource.class);

        // when
        when(client.resource(url)).thenReturn(webResource);
        when(sdcWebResource.delete(type)).thenReturn(new Task());

        Task task = null;
        try {
            task = chefClientService.delete(vdc, chefClientName);
        } catch (InvalidExecutionException e) {
            fail();
        }

        // then
        assertNotNull(task);
        verify(client).resource(url);
        verify(sdcWebResource).delete(type);
    }

}
