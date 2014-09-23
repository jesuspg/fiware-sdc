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

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.openstack.docs.identity.api.v2.AuthenticateResponse;
import org.openstack.docs.identity.api.v2.TenantForAuthenticateResponse;
import org.openstack.docs.identity.api.v2.Token;
import org.openstack.docs.identity.api.v2.UserForAuthenticateResponse;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class OpenStackAuthenticationProviderTest {

    SystemPropertiesProvider systemPropertiesProvider;

    OpenStackAuthenticationToken openStackAuthenticationToken;

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
    public void shouldCreatesNewTokenAndValidateWhenAdminTokenIsNotAuthorized() {
        // given

        String adminToken = "fa63ea912a2a579c2542dbb90951d500";

        OpenStackAuthenticationProvider openStackAuthenticationProvider = new OpenStackAuthenticationProvider();
        openStackAuthenticationProvider.setSystemPropertiesProvider(systemPropertiesProvider);
        openStackAuthenticationToken = mock(OpenStackAuthenticationToken.class);
        openStackAuthenticationProvider.oSAuthToken = openStackAuthenticationToken;
        Client client = mock(Client.class);
        openStackAuthenticationProvider.setClient(client);
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        UniformInterfaceException uniformInterfaceException = mock(UniformInterfaceException.class);
        WebResource webResource2 = mock(WebResource.class);
        WebResource.Builder builder2 = mock(WebResource.Builder.class);
        ClientResponse clientResponse = mock(ClientResponse.class);
        AuthenticateResponse authenticateResponse = mock(AuthenticateResponse.class);

        // when
        when(openStackAuthenticationToken.getCredentials()).thenReturn(new String[] { adminToken, "string2" });
        when(client.resource("http://keystone.test")).thenReturn(webResource).thenReturn(webResource2);
        when(webResource.path(any(String.class))).thenReturn(webResource);
        when(webResource.header(anyString(), anyString())).thenReturn(builder);
        when(builder.header(eq("X-Auth-Token"), anyString())).thenReturn(builder);
        when(builder.get(AuthenticateResponse.class)).thenThrow(uniformInterfaceException);
        when(uniformInterfaceException.getResponse()).thenReturn(clientResponse);
        when(clientResponse.getStatus()).thenReturn(401);

        when(webResource2.path(any(String.class))).thenReturn(webResource2);
        when(webResource2.header(anyString(), anyString())).thenReturn(builder2);
        when(builder2.header(eq("X-Auth-Token"), anyString())).thenReturn(builder2);
        when(builder2.get(AuthenticateResponse.class)).thenReturn(authenticateResponse);

        Token validToken = mock(Token.class);
        TenantForAuthenticateResponse tenantForAuthenticateResponse = mock(TenantForAuthenticateResponse.class);
        UserForAuthenticateResponse userForAuthenticateResponse = mock(UserForAuthenticateResponse.class);
        when(authenticateResponse.getToken()).thenReturn(validToken);
        when(validToken.getTenant()).thenReturn(tenantForAuthenticateResponse);
        when(tenantForAuthenticateResponse.getId()).thenReturn("user tenantId");
        when(authenticateResponse.getUser()).thenReturn(userForAuthenticateResponse);
        when(userForAuthenticateResponse.getRoles()).thenReturn(null);
        Map<QName, String> collection = new HashMap<QName, String>();
        collection.put(QName.valueOf("username"), "value");
        when(userForAuthenticateResponse.getOtherAttributes()).thenReturn(collection);

        PaasManagerUser paasManagerUser = openStackAuthenticationProvider.authenticationFiware("user token",
                "user tenantId");

        // then
        assertNotNull(paasManagerUser);
        verify(openStackAuthenticationToken, times(2)).getCredentials();
    }
}
