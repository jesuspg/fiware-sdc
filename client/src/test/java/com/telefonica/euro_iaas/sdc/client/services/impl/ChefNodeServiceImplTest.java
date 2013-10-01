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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.services.ChefNodeService;
import com.telefonica.euro_iaas.sdc.model.Task;

public class ChefNodeServiceImplTest {

    SDCWebResourceFactory sdcWebResourceFactory;
    SDCWebResource sdcWebResource;
    ChefNodeService chefNodeService;
    String type = "application/json";
    String baseHost = "baseHost";
    Client client = mock(Client.class);

    @Before
    public void setUp() {
        sdcWebResourceFactory = mock(SDCWebResourceFactory.class);
        sdcWebResource = mock(SDCWebResource.class);

        chefNodeService = new ChefNodeServiceImpl(client, baseHost, type);
        ((ChefNodeServiceImpl) chefNodeService).setSdcWebResourceFactory(sdcWebResourceFactory);
        when(sdcWebResourceFactory.getInstance(any(WebResource.class))).thenReturn(sdcWebResource);
    }

    @Test
    public void shouldDeleteNode() {
        // given
        String chefNodeName = "nodeName";
        String vdc = "virtualDataCenter";
        String url = baseHost + "/vdc/" + vdc + "/node/" + chefNodeName;
        WebResource webResource = mock(WebResource.class);

        // when
        when(client.resource(url)).thenReturn(webResource);
        when(sdcWebResource.delete(type)).thenReturn(new Task());

        Task task = null;
        try {
            task = chefNodeService.delete(vdc, chefNodeName);
        } catch (InvalidExecutionException e) {
            fail();
        }

        // then
        assertNotNull(task);
        verify(client).resource(url);
    }
}
