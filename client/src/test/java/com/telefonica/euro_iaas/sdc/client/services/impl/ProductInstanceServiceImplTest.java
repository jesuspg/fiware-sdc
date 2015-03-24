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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.model.ProductInstances;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.client.services.SdcClientConfig;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;

public class ProductInstanceServiceImplTest {
    SdcClientConfig client = mock(SdcClientConfig.class);

    @Before
    public void setUp() {
        Client c = mock(Client.class);
        when(client.getClient()).thenReturn(c);
    }

    @Test
    public void shouldReturnTaskWithProductInstanceServiceInstall() {
        // given

        String baseHost = "localhost";
        String type = "application/json";
        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);

        String vdc = "vdc";
        String token = "token";
        ProductInstanceDto productDto = new ProductInstanceDto();
        String urlCallback = "http://localhost";

        String url = "localhost/vdc/vdc/productInstance/";
        Task task = new Task();
        WebTarget webResource = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);

        Response response = mock(Response.class);

        // when
        when(client.getClient().target(url)).thenReturn(webResource);
        when(webResource.request(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);
        when(builder.header("callback", urlCallback)).thenReturn(builder);
        when(builder.post(any(Entity.class))).thenReturn(response);
        when(response.readEntity(Task.class)).thenReturn(task);

        Task resultTask = productInstanceService.install(vdc, productDto, urlCallback, token);

        // then
        assertNotNull(resultTask);
        verify(client.getClient()).target(url);
        verify(webResource).request(type);
        verify(builder).accept(type);
        verify(builder).header("callback", urlCallback);
        verify(builder).post(any(Entity.class));
    }

    @Test
    public void shouldReturnTaskWithProductInstanceServiceInstallArtifact() {
        // given

        Task expectedTask = new Task();
        String baseHost = "localhost";
        String type = "application/json";
        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);

        String vdc = "vdc";
        String token = "token";
        String urlCallback = "http://localhost";
        Artifact artifact = new Artifact();
        String productInstanceId = "id";

        String url = "localhost/vdc/vdc/productInstance/" + productInstanceId + "/ac";
        WebTarget webResource = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Response response = mock(Response.class);

        // when

        when(client.getClient().target(url)).thenReturn(webResource);
        when(webResource.request(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);
        when(builder.header("callback", urlCallback)).thenReturn(builder);
        when(builder.post(any(Entity.class))).thenReturn(response);
        when(response.readEntity(Task.class)).thenReturn(expectedTask);

        Task task = productInstanceService.installArtifact(vdc, productInstanceId, artifact, urlCallback, token);

        // then
        assertNotNull(task);
        verify(client.getClient()).target(url);
        verify(webResource).request(type);
        verify(builder).accept(type);
        verify(builder).header("callback", urlCallback);
        verify(builder).post(any(Entity.class));
    }

    @Test
    public void shouldReturnTaskWithProductInstanceServiceUnInstallArtifact() {
        // given
        Task expectedTask = new Task();
        String baseHost = "localhost";
        String type = "application/json";
        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);

        String vdc = "vdc";
        String token = "token";
        String urlCallback = "http://localhost";
        Artifact artifact = new Artifact();
        artifact.setName("name");
        String productInstanceId = "id";
        String url = "localhost/vdc/vdc/productInstance/" + productInstanceId + "/ac/" + artifact.getName();
        WebTarget webResource = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Response response = mock(Response.class);

        // when
        when(client.getClient().target(url)).thenReturn(webResource);
        when(webResource.request(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);
        when(builder.header("callback", urlCallback)).thenReturn(builder);
        when(builder.delete()).thenReturn(response);
        when(response.readEntity(Task.class)).thenReturn(expectedTask);

        Task task = productInstanceService.uninstallArtifact(vdc, productInstanceId, artifact, urlCallback, token);

        // then
        assertNotNull(task);
        verify(client.getClient()).target(url);
        verify(webResource).request(type);
        verify(builder).accept(type);
        verify(builder).header("callback", urlCallback);
        verify(builder).delete();
        verify(response).readEntity(Task.class);
        verify(response).close();
    }

    @Test
    public void shouldReturnAListOfProductInstance() {
        // given
        String baseHost = "localhost";
        String type = "application/json";

        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);

        InstallableInstance.Status status = InstallableInstance.Status.INSTALLED;
        String hostname = "hostname";
        String domain = "domain";
        String ip = "127.0.0.1";
        String fqn = "full qualifier name";
        Integer page = 1;
        Integer pageSize = 10;
        String orderBy = "hostname";
        String orderType = "desc";
        String vdc = "1312321312";
        String productName = "productName";
        String token = "token";

        ProductInstances expectedList = new ProductInstances();
        String url = baseHost + "/vdc/" + vdc + "/productInstance";
        WebTarget webResource = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Response response = mock(Response.class);

        // when
        when(client.getClient().target(url)).thenReturn(webResource);

        when(webResource.request(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);

        when(builder.get()).thenReturn(response);
        when(response.readEntity(ProductInstances.class)).thenReturn(expectedList);

        List<ProductInstance> list = productInstanceService.findAll(hostname, domain, ip, fqn, page, pageSize, orderBy,
                orderType, status, vdc, productName, token);

        // then
        assertNotNull(list);
        verify(webResource).request(type);
        verify(builder).accept(type);
        verify(builder).get();
        verify(response).readEntity(ProductInstances.class);
        verify(response).close();
    }

    @Test
    public void shouldLoadProductInstanceByName() {
        // given
        String baseHost = "localhost";
        String type = "application/json";
        String vdc = "my_vdc";
        String name = "name";
        String token = "token";

        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);

        String url = baseHost + "/vdc/" + vdc + "/productInstance/" + name;
        WebTarget webResource = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        ProductInstance expectedProductInstance = new ProductInstance();
        Response response = mock(Response.class);

        // when
        when(client.getClient().target(url)).thenReturn(webResource);
        when(webResource.request(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);

        when(builder.get()).thenReturn(response);
        when(response.readEntity(ProductInstance.class)).thenReturn(expectedProductInstance);

        ProductInstance productInstance = null;
        try {
            productInstance = productInstanceService.load(vdc, name, token);
        } catch (ResourceNotFoundException e) {
            fail();
        }

        // then
        verify(client.getClient()).target(url);
        assertNotNull(productInstance);
        verify(webResource).request(type);
        verify(builder).accept(type);
        verify(builder).get();
        verify(response).readEntity(ProductInstance.class);
        verify(response).close();
    }

    @Test
    public void shouldLoadProductInstanceByUrl() {
        // given

        String baseHost = "localhost";
        String type = "application/json";
        String vdc = "my_vdc";
        String name = "name";
        String token = "token";

        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);

        String url = baseHost + "/vdc/" + vdc + "/productInstance/" + name;
        WebTarget webResource = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        ProductInstance expectedProductInstance = new ProductInstance();
        Response response = mock(Response.class);

        // when
        when(client.getClient().target(url)).thenReturn(webResource);
        when(webResource.request(type)).thenReturn(builder);
        when(builder.accept(type)).thenReturn(builder);
        when(builder.header(any(String.class), any(Object.class))).thenReturn(builder);

        when(builder.get()).thenReturn(response);
        when(response.readEntity(ProductInstance.class)).thenReturn(expectedProductInstance);

        ProductInstance productInstance = null;
        try {
            productInstance = productInstanceService.loadUrl(url, vdc, token);
        } catch (ResourceNotFoundException e) {
            fail();
        }

        // then
        verify(client.getClient()).target(url);
        assertNotNull(productInstance);
        verify(webResource).request(type);
        verify(builder).accept(type);
        verify(builder).get();
        verify(response).readEntity(ProductInstance.class);
        verify(response).close();

    }
}
