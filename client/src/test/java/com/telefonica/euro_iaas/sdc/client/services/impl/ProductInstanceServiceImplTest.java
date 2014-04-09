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
import static org.mockito.Matchers.any;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.model.ProductInstances;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;

public class ProductInstanceServiceImplTest {

    @Test
    public void shouldReturnTaskWithProductInstanceServiceInstall() {
        // given

        Client client = mock(Client.class);
        String baseHost = "localhost";
        String type = "application/json";
        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);

        String vdc = "vdc";
        String token = "token";
        ProductInstanceDto productDto = new ProductInstanceDto();
        String urlCallback = "http://localhost";

        String url = "localhost/vdc/vdc/productInstance/";
        Task task = new Task();
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);

        // when
        when(client.resource(url)).thenReturn(webResource);
        when(webResource.accept(type)).thenReturn(builder);
        when(builder.type(type)).thenReturn(builder);
        when(builder.entity(productDto)).thenReturn(builder);
        when(builder.header("callback", urlCallback)).thenReturn(builder);
        when(builder.post(Task.class)).thenReturn(task);

        Task resultTask = productInstanceService.install(vdc, productDto, urlCallback, token);

        // then
        assertNotNull(resultTask);
        verify(client).resource(url);
        verify(webResource).accept(type);
        verify(builder).header("callback", urlCallback);
        verify(builder).post(Task.class);
    }

    @Test
    public void shouldReturnTaskWithProductInstanceServiceInstallArtifact() {
        // given

        Client client = mock(Client.class);
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
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);

        // when

        when(client.resource(url)).thenReturn(webResource);
        when(webResource.accept(type)).thenReturn(builder);
        when(builder.type(type)).thenReturn(builder);
        when(builder.entity(artifact)).thenReturn(builder);
        when(builder.header("callback", urlCallback)).thenReturn(builder);
        when(builder.post(Task.class)).thenReturn(expectedTask);
        Task task = productInstanceService.installArtifact(vdc, productInstanceId, artifact, urlCallback, token);

        // then
        assertNotNull(task);
        verify(client).resource(url);
        verify(webResource).accept(type);
        verify(builder).header("callback", urlCallback);
        verify(builder).post(Task.class);
    }

    @Test
    public void shouldReturnTaskWithProductInstanceServiceUnInstallArtifact() {
        // given

        Client client = mock(Client.class);
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
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);

        // when
        when(client.resource(url)).thenReturn(webResource);
        when(webResource.accept(type)).thenReturn(builder);
        when(builder.type(type)).thenReturn(builder);
        when(builder.entity(artifact)).thenReturn(builder);
        when(builder.header("callback", urlCallback)).thenReturn(builder);
        when(builder.delete(Task.class)).thenReturn(expectedTask);
        Task task = productInstanceService.uninstallArtifact(vdc, productInstanceId, artifact, urlCallback, token);

        // then
        assertNotNull(task);
        verify(client).resource(url);
        verify(webResource).accept(type);
        verify(builder).header("callback", urlCallback);
        verify(builder).delete(Task.class);
    }

    @Test
    public void shouldReturnAListOfProductInstance() {
        // given
        Client client = mock(Client.class);
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

        MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();
        searchParams.add("hostname", hostname);
        searchParams.add("domain", domain);
        searchParams.add("ip", ip);
        searchParams.add("fqn", fqn);
        searchParams.add("page", page.toString());
        searchParams.add("pageSize", pageSize.toString());
        searchParams.add("orderBy", orderBy);
        searchParams.add("orderType", orderType);
        searchParams.add("status", status.toString());
        searchParams.add("product", productName);

        ProductInstances expectedList = new ProductInstances();
        String url = baseHost + "/vdc/" + vdc + "/productInstance";
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);

        // when
        when(client.resource(url)).thenReturn(webResource);
        when(webResource.queryParams(searchParams)).thenReturn(webResource);
        when(webResource.accept(type)).thenReturn(builder);
        when(builder.type(type)).thenReturn(builder);

        when(builder.get(ProductInstances.class)).thenReturn(expectedList);

        List<ProductInstance> list = productInstanceService.findAll(hostname, domain, ip, fqn, page, pageSize, orderBy,
                orderType, status, vdc, productName, token);

        // then
        assertNotNull(list);
        verify(webResource).accept(type);
        verify(builder).get(ProductInstances.class);
    }

    @Test
    public void shouldLoadProductInstanceByName() {
        // given
        Client client = mock(Client.class);
        String baseHost = "localhost";
        String type = "application/json";
        String vdc = "my_vdc";
        String name = "name";
        String token = "token";

        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);

        String url = baseHost + "/vdc/" + vdc + "/productInstance/" + name;
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        ProductInstance expectedProductInstance = new ProductInstance();

        // when
        when(client.resource(url)).thenReturn(webResource);
        when(webResource.accept(type)).thenReturn(builder);
        when(builder.type(type)).thenReturn(builder);

        when(builder.get(ProductInstance.class)).thenReturn(expectedProductInstance);

        ProductInstance productInstance = null;
        try {
            productInstance = productInstanceService.load(vdc, name, token);
        } catch (ResourceNotFoundException e) {
            fail();
        }

        // then
        verify(client).resource(url);
        assertNotNull(productInstance);
        verify(webResource).accept(type);
        verify(builder).get(ProductInstance.class);
    }

    @Test
    public void shouldLoadProductInstanceByUrl() {
        // given
        Client client = mock(Client.class);
        String baseHost = "localhost";
        String type = "application/json";
        String vdc = "my_vdc";
        String name = "name";
        String token = "token";

        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);

        String url = baseHost + "/vdc/" + vdc + "/productInstance/" + name;
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        ProductInstance expectedProductInstance = new ProductInstance();

        // when
        when(client.resource(url)).thenReturn(webResource);
        when(webResource.accept(type)).thenReturn(builder);
        when(builder.type(type)).thenReturn(builder);
        when(builder.header(any(String.class), any(Object.class))).thenReturn(builder);
        
        when(builder.get(ProductInstance.class)).thenReturn(expectedProductInstance);

        ProductInstance productInstance = null;
        try {
            productInstance = productInstanceService.loadUrl(url, vdc , token);
        } catch (ResourceNotFoundException e) {
            fail();
        }

        // then
        verify(client).resource(url);
        assertNotNull(productInstance);
        verify(webResource).accept(type);
        verify(builder).get(ProductInstance.class);
    }
}
