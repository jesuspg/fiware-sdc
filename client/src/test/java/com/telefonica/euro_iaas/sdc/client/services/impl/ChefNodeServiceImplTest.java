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
