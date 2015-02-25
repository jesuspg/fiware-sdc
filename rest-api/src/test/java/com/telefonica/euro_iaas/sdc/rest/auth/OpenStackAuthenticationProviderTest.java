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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.openstack.docs.identity.api.v2.AuthenticateResponse;
import org.openstack.docs.identity.api.v2.TenantForAuthenticateResponse;
import org.openstack.docs.identity.api.v2.Token;
import org.openstack.docs.identity.api.v2.UserForAuthenticateResponse;

import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * Test class to check the OpenStackAuthenticationProvider.
 */
public class OpenStackAuthenticationProviderTest {

    private SystemPropertiesProvider systemPropertiesProvider;

    private OpenStackAuthenticationToken openStackAuthenticationToken;

    /**
     * Initialize some parameters before starting the tests.
     */
    @Before
    public void setup() {

        systemPropertiesProvider = mock(SystemPropertiesProvider.class);

        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_URL)).thenReturn(
                "http://keystone.test");
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_USER)).thenReturn("admin");
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_PASS)).thenReturn("admin");
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_TENANT)).thenReturn(
                "00000000000000000000000000000001");

    }

    @Test
    public void shouldCreatesNewTokenForAdminAndUser() {

        // Given
        OpenStackAuthenticationProvider openStackAuthenticationProvider = new OpenStackAuthenticationProvider();
        openStackAuthenticationProvider.setSystemPropertiesProvider(systemPropertiesProvider);
        openStackAuthenticationToken = mock(OpenStackAuthenticationToken.class);
        openStackAuthenticationProvider.setoSAuthToken(openStackAuthenticationToken);
        when(openStackAuthenticationToken.getCredentials()).thenReturn(new String[] { "token1", "tenantId1" });
        Client client = mock(Client.class);
        openStackAuthenticationProvider.setClient(client);
        WebTarget webResource = mock(WebTarget.class);
        WebTarget webResource2 = mock(WebTarget.class);
        when(client.target("http://keystone.test")).thenReturn(webResource);
        WebTarget webTarget = mock(WebTarget.class);
        when(webResource.path("tokens")).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webResource2);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(webResource2.request()).thenReturn(builder);
        when(builder.accept(MediaType.APPLICATION_XML)).thenReturn(builder);
        when(builder.header("X-Auth-Token", "token1")).thenReturn(builder);
        Response response = mock(Response.class);
        when(builder.get()).thenReturn(response);
        when(response.getStatus()).thenReturn(200);

        // mock response
        AuthenticateResponse userForAuthenticateResponse = mock(AuthenticateResponse.class);
        when(response.readEntity(AuthenticateResponse.class)).thenReturn(userForAuthenticateResponse);
        Token token = mock(Token.class);
        when(userForAuthenticateResponse.getToken()).thenReturn(token);
        TenantForAuthenticateResponse tenant = mock(TenantForAuthenticateResponse.class);
        when(token.getTenant()).thenReturn(tenant);
        when(tenant.getId()).thenReturn("user tenantId");
        UserForAuthenticateResponse user = mock(UserForAuthenticateResponse.class);
        when(userForAuthenticateResponse.getUser()).thenReturn(user);
        Map<QName, String> map = new HashMap();
        map.put(new QName("username"), "username");
        when(user.getOtherAttributes()).thenReturn(map);

        // When
        PaasManagerUser paasManagerUser = openStackAuthenticationProvider.authenticationFiware("user token",
                "user tenantId");

        // Then
        verify(response).readEntity(AuthenticateResponse.class);
        verify(user).getOtherAttributes();
        assertNotNull(paasManagerUser);
        assertEquals("user tenantId", paasManagerUser.getTenantId());
        assertEquals("user token", paasManagerUser.getToken());

    }

}
