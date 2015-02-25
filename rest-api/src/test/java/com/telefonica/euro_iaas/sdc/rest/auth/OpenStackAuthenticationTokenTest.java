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

package com.telefonica.euro_iaas.sdc.rest.auth;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.telefonica.euro_iaas.sdc.rest.exception.AuthenticationConnectionException;

public class OpenStackAuthenticationTokenTest {

    @Test
    public void getCredentialsTest() throws AuthenticationConnectionException, IOException {

        OpenStackAuthenticationToken openStackAuthenticationToken;

        // Given
        ArrayList<Object> params = new ArrayList<Object>();
        String url = "http://keystone";

        String payload = "{ \"access\":{\"token\":{\"expires\":\"2015-07-09T15:16:07Z\","
                + "\"id\":\"0bd52c4b2fa09951aa057b4590c4aa6d\",\"tenant\":{\"description\":\"Tenant from IDM\","
                + "\"enabled\":\"true\",\"id\":\"00000000000000000000000000001348\",\"name\":\"tenantName\"}"
                + "      }}}";

        Client client = mock(Client.class);
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);

        params.add(url);
        params.add("tenant");
        params.add("user");
        params.add("passw");
        params.add(client);

        openStackAuthenticationToken = new OpenStackAuthenticationToken(params);
        when(client.target(url)).thenReturn(webTarget);
        when(webTarget.request(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        Response response = mock(Response.class);
        when(builder.post(any(Entity.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(200);
        when(response.readEntity(String.class)).thenReturn(payload);

        // when
        String credentials[] = openStackAuthenticationToken.getCredentials();

        // then
        verify(client).target(url);
        verify(webTarget).request(MediaType.APPLICATION_JSON);
        verify(builder).accept(MediaType.APPLICATION_JSON);
        verify(response).getStatus();
        verify(response).readEntity(String.class);
        assertNotNull(credentials);
        assertEquals("0bd52c4b2fa09951aa057b4590c4aa6d", credentials[0]);
        assertEquals("00000000000000000000000000001348", credentials[1]);

    }

    @Test(expected = AuthenticationConnectionException.class)
    public void shouldGetCredentialsWithErrorAfterRequestToken() throws IOException {
        OpenStackAuthenticationToken openStackAuthenticationToken;

        // Given
        ArrayList<Object> params = new ArrayList<Object>();
        String url = "http://keystone";

        String payload = "{ \"access\":{\"token\":{\"expires\":\"2015-07-09T15:16:07Z\","
                + "\"id\":\"0bd52c4b2fa09951aa057b4590c4aa6d\",\"tenant\":{\"description\":\"Tenant from IDM\","
                + "\"enabled\":\"true\",\"id\":\"00000000000000000000000000001348\",\"name\":\"tenantName\"}"
                + "      }}}";

        Client client = mock(Client.class);
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);

        params.add(url);
        params.add("tenant");
        params.add("user");
        params.add("passw");
        params.add(client);

        openStackAuthenticationToken = new OpenStackAuthenticationToken(params);
        when(client.target(url)).thenReturn(webTarget);
        when(webTarget.request(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        Response response = mock(Response.class);
        when(builder.post(any(Entity.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(400);
        when(response.readEntity(String.class)).thenReturn(payload);

        // when
        openStackAuthenticationToken.getCredentials();

        // then

    }
}
