/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.client.services.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ChefClientService;
import com.telefonica.euro_iaas.sdc.client.services.SdcClientConfig;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

public class ChefClientServiceImplTest {

    ChefClientService chefClientService;
    String type = "application/json";
    String baseHost = "baseHost";
    String token = "token";

    SdcClientConfig client = mock(SdcClientConfig.class);

    @Before
    public void setUp() {

        chefClientService = new ChefClientServiceImpl(client, baseHost, type);
        Client c = mock(Client.class);
        when(client.getClient()).thenReturn(c);
    }

    @Test
    public void shouldReturnAChiefClientWhenLoad() throws ResourceNotFoundException {
        // given

        String vdc = "vdc";
        String chefClientName = "chefClientName";

        ChefClient expectedChefClient = new ChefClient();
        String url = baseHost + "/vdc/" + vdc + "/chefClient/" + chefClientName;
        WebTarget webResource = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);

        // when
        when(client.getClient().target(url)).thenReturn(webResource);
        when(webResource.request().accept(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);
        when(builder.get(ChefClient.class)).thenReturn(expectedChefClient);

        ChefClient resultChefClient = chefClientService.load(vdc, chefClientName, token);

        // then
        assertNotNull(resultChefClient);
        verify(client.getClient()).target(url);
        verify(webResource).request().accept(type);
        verify(builder).get(ChefClient.class);
    }

    @Test
    public void shouldReturnAChiefClientWhenLoadByHostname() throws ResourceNotFoundException {
        // given

        String vdc = "my_vdc";
        String chefClientName = "chefClientName";

        ChefClient expectedChefClient = new ChefClient();
        ChefClient resultChefClient;
        WebTarget webResource = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);

        String url = baseHost + "/vdc/" + vdc + "/chefClient?hostname=" + chefClientName;
        // when

        when(client.getClient().target(url)).thenReturn(webResource);
        when(webResource.request(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);
        when(builder.get(ChefClient.class)).thenReturn(expectedChefClient);

        resultChefClient = chefClientService.loadByHostname(vdc, chefClientName, token);

        // then
        assertNotNull(resultChefClient);
        verify(client.getClient()).target(url);
        verify(webResource).request(type);
        verify(builder).get(ChefClient.class);
    }

    @Test
    public void shouldDeleteByClientName() {
        // given
        String chefClientName = "clientName";
        String vdc = "virtualDataCenter";
        String url = baseHost + "/vdc/" + vdc + "/chefClient/" + chefClientName;
        WebTarget webResource = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Task expectedTask = new Task();

        // when
        when(client.getClient().target(url)).thenReturn(webResource);
        when(webResource.request(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);
        when(builder.delete(Task.class)).thenReturn(expectedTask);

        Task task = null;
        try {
            task = chefClientService.delete(vdc, chefClientName, token);
        } catch (InvalidExecutionException e) {
            fail();
        }

        // then
        assertNotNull(task);
        verify(client.getClient()).target(url);
        verify(webResource).request(type);
        verify(builder).delete(Task.class);
    }

}
