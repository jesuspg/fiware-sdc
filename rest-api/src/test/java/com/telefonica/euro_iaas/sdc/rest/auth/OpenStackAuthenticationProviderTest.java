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
import static org.mockito.Mockito.times;
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
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.sdc.rest.util.TokenCache;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * Test class to check the OpenStackAuthenticationProvider.
 */
public class OpenStackAuthenticationProviderTest {

    private SystemPropertiesProvider systemPropertiesProvider;

    /**
     * Initialize some parameters before starting the tests.
     */
    @Before
    public void setup() {

        TokenCache.removeCache();

        systemPropertiesProvider = mock(SystemPropertiesProvider.class);

        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM)).thenReturn("FIWARE");

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
        String responseJSON = "{\"access\": {\"token\": {\n"
                + "            \"expires\": \"2015-04-14T10:58:54.578527Z\",\"id\": \"user token\",\"tenant\": {\n"
                + "                \"id\": \"user tenantId\",\n" + "                \"name\": \"tenant name\"\n"
                + "            }\n" + "        },\n" + "        \"user\": {\n"
                + "            \"name\": \"username1\",\n" + "            \"tenantName\": \"tenant name\",\n"
                + "            \"id\": \"aalonsog@dit.upm.es\",\n" + "            \"roles\": [\n"
                + "                {\n" + "                    \"id\": \"13abab31bc194317a009b25909f390a6\",\n"
                + "                    \"name\": \"owner\"\n" + "                }\n" + "            ],\n"
                + "            \"tenantId\": \"tenantId1\"\n" + "        }\n" + "    }\n" + "}";

        OpenStackAuthenticationProvider openStackAuthenticationProvider = new OpenStackAuthenticationProvider();
        openStackAuthenticationProvider.setSystemPropertiesProvider(systemPropertiesProvider);
        OpenStackAuthenticationToken openStackAuthenticationToken = mock(OpenStackAuthenticationToken.class);
        openStackAuthenticationProvider.setoSAuthToken(openStackAuthenticationToken);
        when(openStackAuthenticationToken.getCredentials()).thenReturn(new String[] { "tokenAdmin2", "tenantIdAdmin" });
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
        when(builder.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.header("X-Auth-Token", "tokenAdmin2")).thenReturn(builder);
        Response response = mock(Response.class, "tokenuser");
        when(builder.get()).thenReturn(response);
        when(response.getStatus()).thenReturn(200);

        // mock response
        when(response.readEntity(String.class)).thenReturn(responseJSON);

        Map<QName, String> map = new HashMap();
        map.put(new QName("username"), "username");
        openStackAuthenticationProvider.getTokenCache().removeAll();

        // When
        PaasManagerUser paasManagerUser = openStackAuthenticationProvider.authenticationFiware("user token2",
                "user tenantId");

        // Then
        verify(response).getStatus();
        verify(response).readEntity(String.class);
        assertNotNull(paasManagerUser);
        assertEquals("user tenantId", paasManagerUser.getTenantId());
        assertEquals("user token2", paasManagerUser.getToken());

    }

    @Test
    public void shouldReturnErrorWhenOpenStackReturnHTTP400() {

        // Given
        OpenStackAuthenticationProvider openStackAuthenticationProvider = new OpenStackAuthenticationProvider();
        openStackAuthenticationProvider.setSystemPropertiesProvider(systemPropertiesProvider);
        OpenStackAuthenticationToken openStackAuthenticationToken = mock(OpenStackAuthenticationToken.class);
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
        when(builder.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.header("X-Auth-Token", "token1")).thenReturn(builder);
        Response response = mock(Response.class);
        when(builder.get()).thenReturn(response);
        when(response.getStatus()).thenReturn(400);

        openStackAuthenticationProvider.getTokenCache().removeAll();

        // When
        try {
            openStackAuthenticationProvider.authenticationFiware("user token", "user tenantId");
        } catch (AuthenticationServiceException ase) {
            System.out.println(ase.getMessage());
            verify(response).getStatus();
        }

    }

    @Test
    public void shouldReturnBadCredentialsWhenOpenStackReturnHTTP401() {

        // Given
        OpenStackAuthenticationProvider openStackAuthenticationProvider = new OpenStackAuthenticationProvider();
        openStackAuthenticationProvider.setSystemPropertiesProvider(systemPropertiesProvider);
        OpenStackAuthenticationToken openStackAuthenticationToken = mock(OpenStackAuthenticationToken.class);
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
        when(builder.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.header("X-Auth-Token", "token1")).thenReturn(builder);
        Response response = mock(Response.class, "usertoken");
        when(builder.get()).thenReturn(response);
        when(response.getStatus()).thenReturn(401);

        openStackAuthenticationProvider.getTokenCache().removeAll();

        // When
        try {
            openStackAuthenticationProvider.authenticationFiware("user token", "user tenantId");

        } catch (AuthenticationServiceException ase) {
            System.out.println(ase.getMessage());
            verify(response).getStatus();
        }
        // Then

    }

    @Test
    public void shouldRetrieveUser() {
        // Given
        String responseJSON = "{\"access\": {\"token\": {\n"
                + "            \"expires\": \"2015-04-14T10:58:54.578527Z\",\"id\": \"user token\",\"tenant\": {\n"
                + "                \"id\": \"user tenantId\",\n" + "                \"name\": \"tenant name\"\n"
                + "            }\n" + "        },\n" + "        \"user\": {\n"
                + "            \"name\": \"username\",\n" + "            \"tenantName\": \"tenant name\",\n"
                + "            \"id\": \"aalonsog@dit.upm.es\",\n" + "            \"roles\": [\n"
                + "                {\n" + "                    \"id\": \"13abab31bc194317a009b25909f390a6\",\n"
                + "                    \"name\": \"owner\"\n" + "                }\n" + "            ],\n"
                + "            \"tenantId\": \"tenantId1\"\n" + "        }\n" + "    }\n" + "}";

        OpenStackAuthenticationProvider openStackAuthenticationProvider = new OpenStackAuthenticationProvider();
        openStackAuthenticationProvider.setSystemPropertiesProvider(systemPropertiesProvider);
        OpenStackAuthenticationToken openStackAuthenticationToken = mock(OpenStackAuthenticationToken.class);
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
        when(builder.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.header("X-Auth-Token", "token1")).thenReturn(builder);
        Response response = mock(Response.class);
        when(builder.get()).thenReturn(response);
        when(response.getStatus()).thenReturn(200);

        // mock response
        when(response.readEntity(String.class)).thenReturn(responseJSON);

        openStackAuthenticationProvider.getTokenCache().removeAll();

        // When
        UsernamePasswordAuthenticationToken authentication = mock(UsernamePasswordAuthenticationToken.class);
        Object objectCredentials = mock(Object.class);
        when(authentication.getCredentials()).thenReturn(objectCredentials);
        when(objectCredentials.toString()).thenReturn("user tenantId");

        UserDetails userDetails = openStackAuthenticationProvider.retrieveUser("user token", authentication);

        // Then

        assertNotNull(userDetails);
        assertEquals("username", userDetails.getUsername());
        assertEquals("user token", userDetails.getPassword());
        verify(response).getStatus();
    }

    @Test
    public void shouldCreateNewTokenAfterResetCache() throws InterruptedException {

        // Given

        String responseJSON = "{\"access\": {\"token\": {\n"
                + "            \"expires\": \"2015-04-14T10:58:54.578527Z\",\"id\": \"user token2\",\"tenant\": {\n"
                + "                \"id\": \"user tenantId\",\n" + "                \"name\": \"tenant name\"\n"
                + "            }\n" + "        },\n" + "        \"user\": {\n"
                + "            \"name\": \"username1\",\n" + "            \"tenantName\": \"tenant name\",\n"
                + "            \"id\": \"aalonsog@dit.upm.es\",\n" + "            \"roles\": [\n"
                + "                {\n" + "                    \"id\": \"13abab31bc194317a009b25909f390a6\",\n"
                + "                    \"name\": \"owner\"\n" + "                }\n" + "            ],\n"
                + "            \"tenantId\": \"tenantId1\"\n" + "        }\n" + "    }\n" + "}";

        OpenStackAuthenticationProvider openStackAuthenticationProvider = new OpenStackAuthenticationProvider();
        openStackAuthenticationProvider.setSystemPropertiesProvider(systemPropertiesProvider);
        OpenStackAuthenticationToken openStackAuthenticationToken = mock(OpenStackAuthenticationToken.class);
        openStackAuthenticationProvider.setoSAuthToken(openStackAuthenticationToken);
        when(openStackAuthenticationToken.getCredentials()).thenReturn(new String[] { "tokenAdmin2", "tenantIdAdmin" });
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
        when(builder.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.header("X-Auth-Token", "tokenAdmin2")).thenReturn(builder);
        Response response = mock(Response.class, "tokenuser");
        when(builder.get()).thenReturn(response);
        when(response.getStatus()).thenReturn(200);

        openStackAuthenticationProvider.getTokenCache().removeAll();

        // mock response
        when(response.readEntity(String.class)).thenReturn(responseJSON);

        // When
        PaasManagerUser firstTimePaasManagerUser = openStackAuthenticationProvider.authenticationFiware("user token2",
                "user tenantId");

        // force expire elements now
        openStackAuthenticationProvider.getTokenCache().get("admin").setTimeToIdle(1);
        openStackAuthenticationProvider.getTokenCache().get("admin").setTimeToLive(1);
        openStackAuthenticationProvider.getTokenCache().get("user token2-user tenantId").setTimeToIdle(1);
        openStackAuthenticationProvider.getTokenCache().get("user token2-user tenantId").setTimeToLive(1);
        Thread.sleep(1000);

        PaasManagerUser secondTimePaasManagerUser = openStackAuthenticationProvider.authenticationFiware("user token2",
                "user tenantId");

        // Then
        verify(response, times(2)).getStatus();
        assertNotNull(firstTimePaasManagerUser);
        assertNotNull(secondTimePaasManagerUser);
        assertEquals("user tenantId", firstTimePaasManagerUser.getTenantId());
        assertEquals("user token2", firstTimePaasManagerUser.getToken());
        assertEquals("user tenantId", secondTimePaasManagerUser.getTenantId());
        assertEquals("user token2", secondTimePaasManagerUser.getToken());

    }
}
