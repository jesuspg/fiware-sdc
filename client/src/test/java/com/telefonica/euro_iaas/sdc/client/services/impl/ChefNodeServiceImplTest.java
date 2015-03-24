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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.telefonica.euro_iaas.sdc.client.services.SdcClientConfig;
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
        WebTarget webResource = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        SdcClientConfig client = mock(SdcClientConfig.class);
        ChefNodeServiceImpl chefNodeService = new ChefNodeServiceImpl(client, baseHost, type);
        Response response = mock(Response.class);

        // when
        Client c = mock(Client.class);
        when(client.getClient()).thenReturn(c);
        when(client.getClient().target(url)).thenReturn(webResource);
        when(webResource.request(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);
        when(builder.delete()).thenReturn(response);
        when(response.readEntity(Task.class)).thenReturn(expectedTask);

        Task task = null;

        task = chefNodeService.delete(vdc, chefNodeName, token);

        // then
        assertNotNull(task);
        verify(client.getClient()).target(url);
        verify(webResource).request(type);
        verify(builder).delete();
        verify(response).readEntity(Task.class);
        verify(response).close();
    }
}
