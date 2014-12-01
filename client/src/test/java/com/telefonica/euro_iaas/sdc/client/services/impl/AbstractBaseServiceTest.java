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

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import org.junit.Test;
import org.slf4j.MDC;
import org.slf4j.impl.StaticMDCBinder;
import org.slf4j.spi.MDCAdapter;

import com.telefonica.euro_iaas.sdc.client.services.SdcClientConfig;

public class AbstractBaseServiceTest {

    @Test
    public void shouldCreatesWebResource() {
        // given
        AbstractBaseService abstractBaseService = new AbstractBaseService();
        String url = "http://localhost/sdc";
        String token = "c8607e4870181c4efd43a8449801a33f";
        String tenant = "00000000000000000000000000000193";
        String type = "json";
        SdcClientConfig clientConfig = mock(SdcClientConfig.class);
        abstractBaseService.setSdcClientConfig(clientConfig);
        abstractBaseService.setType(type);
        Client client = mock(Client.class);
        when(clientConfig.getClient()).thenReturn(client);
        WebTarget webTarget = mock(WebTarget.class);
        when(client.target(url)).thenReturn(webTarget);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(webTarget.request(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);

        // when

        Invocation.Builder resultBuilder = abstractBaseService.createWebResource(url, token, tenant);

        // then
        verify(webTarget).request(type);
        verify(builder).accept(type);
        verify(client).target(url);
        verify(resultBuilder).header("X-Auth-Token", token);
        verify(resultBuilder).header("Tenant-Id", tenant);

        assertNotNull(resultBuilder);
    }

    @Test
    public void shouldCreatesWebResourceWithTransactionId() {
        // given
        AbstractBaseService abstractBaseService = new AbstractBaseService();
        String url = "http://localhost/sdc";
        String token = "c8607e4870181c4efd43a8449801a33f";
        String tenant = "00000000000000000000000000000193";
        String type = "json";
        String txId = "o0iw0e5zbntl1sv4mmkp81ksi";
        SdcClientConfig clientConfig = mock(SdcClientConfig.class);
        abstractBaseService.setSdcClientConfig(clientConfig);
        abstractBaseService.setType(type);
        Client client = mock(Client.class);
        when(clientConfig.getClient()).thenReturn(client);
        WebTarget webTarget = mock(WebTarget.class);
        when(client.target(url)).thenReturn(webTarget);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(webTarget.request(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);

        // when

        MDCAdapter mdcAdapter = StaticMDCBinder.SINGLETON.getMDCA();
        MDC.put("txId", txId);
        Invocation.Builder resultBuilder = abstractBaseService.createWebResource(url, token, tenant);

        // then
        verify(webTarget).request(type);
        verify(builder).accept(type);
        verify(client).target(url);
        verify(resultBuilder).header("X-Auth-Token", token);
        verify(resultBuilder).header("Tenant-Id", tenant);
        verify(resultBuilder).header("txId", txId);

        assertNotNull(resultBuilder);
    }

}
