/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.client.services.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;

public class ProductInstanceServiceImplTest {

    SDCWebResourceFactory sdcWebResourceFactory;
    SDCWebResource sdcWebResource;

    @Before
    public void setUp() {
        sdcWebResourceFactory = mock(SDCWebResourceFactory.class);
        sdcWebResource = mock(SDCWebResource.class);
    }

    @Test
    public void shouldReturnTaskWithProductInstanceServiceInstall() {
        // given

        Client client = mock(Client.class);
        Task expectedTask = new Task();
        String baseHost = "localhost";
        String type = "application/json";
        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);
        ((ProductInstanceServiceImpl) productInstanceService).setSdcWebResourceFactory(sdcWebResourceFactory);

        String vdc = "vdc";
        ProductInstanceDto productDto = new ProductInstanceDto();
        String urlCallback = "http://localhost";

        // when

        when(sdcWebResourceFactory.getInstance(any(WebResource.class))).thenReturn(sdcWebResource);
        when(sdcWebResource.post(type, productDto, urlCallback)).thenReturn(expectedTask);
        Task task = productInstanceService.install(vdc, productDto, urlCallback);

        // then
        assertNotNull(task);
        verify(sdcWebResourceFactory).getInstance(any(WebResource.class));
    }

    @Test
    public void shouldReturnTaskWithProductInstanceServiceInstallArtifact() {
        // given

        Client client = mock(Client.class);
        Task expectedTask = new Task();
        String baseHost = "localhost";
        String type = "application/json";
        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);
        ((ProductInstanceServiceImpl) productInstanceService).setSdcWebResourceFactory(sdcWebResourceFactory);

        String vdc = "vdc";
        String urlCallback = "http://localhost";
        Artifact artifact = new Artifact();
        String productInstanceId = "id";

        // when

        when(sdcWebResourceFactory.getInstance(any(WebResource.class))).thenReturn(sdcWebResource);
        when(sdcWebResource.post(type, artifact, urlCallback)).thenReturn(expectedTask);
        Task task = productInstanceService.installArtifact(vdc, productInstanceId, artifact, urlCallback);

        // then
        assertNotNull(task);
        verify(sdcWebResourceFactory).getInstance(any(WebResource.class));
    }

    @Test
    public void shouldReturnTaskWithProductInstanceServiceUnInstallArtifact() {
        // given

        Client client = mock(Client.class);
        Task expectedTask = new Task();
        String baseHost = "localhost";
        String type = "application/json";
        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);
        ((ProductInstanceServiceImpl) productInstanceService).setSdcWebResourceFactory(sdcWebResourceFactory);

        String vdc = "vdc";
        String urlCallback = "http://localhost";
        Artifact artifact = new Artifact();
        String productInstanceId = "id";

        // when

        when(sdcWebResourceFactory.getInstance(any(WebResource.class))).thenReturn(sdcWebResource);
        when(sdcWebResource.delete(type, urlCallback)).thenReturn(expectedTask);
        Task task = productInstanceService.uninstallArtifact(vdc, productInstanceId, artifact, urlCallback);

        // then
        assertNotNull(task);
        verify(sdcWebResourceFactory).getInstance(any(WebResource.class));
    }

    @Test
    public void shouldReturnAListOfProductInstance() {
        // given
        Client client = mock(Client.class);
        String baseHost = "localhost";
        String type = "application/json";

        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);
        ((ProductInstanceServiceImpl) productInstanceService).setSdcWebResourceFactory(sdcWebResourceFactory);

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

        List<ProductInstance> expectedList = new ArrayList<ProductInstance>();

        // when

        when(sdcWebResourceFactory.getInstance(any(WebResource.class))).thenReturn(sdcWebResource);
        when(sdcWebResource.queryParams(searchParams, type)).thenReturn(expectedList);

        List<ProductInstance> list = productInstanceService.findAll(hostname, domain, ip, fqn, page, pageSize, orderBy,
                orderType, status, vdc, productName);

        // then
        assertNotNull(list);
        verify(sdcWebResource).queryParams(searchParams, type);
    }

    @Test
    public void shouldLoadProductInstanceByName() {
        // given
        Client client = mock(Client.class);
        String baseHost = "localhost";
        String type = "application/json";
        String vdc = "my_vdc";
        String name = "name";

        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);
        ((ProductInstanceServiceImpl) productInstanceService).setSdcWebResourceFactory(sdcWebResourceFactory);

        String url = baseHost + "/vdc/" + vdc + "/productInstance/" + name;
        WebResource webResource = mock(WebResource.class);
        ProductInstance expectedProductInstance = new ProductInstance();

        // when
        when(client.resource(url)).thenReturn(webResource);
        when(sdcWebResourceFactory.getInstance(any(WebResource.class))).thenReturn(sdcWebResource);
        when(sdcWebResource.get(type, ProductInstance.class)).thenReturn(expectedProductInstance);

        ProductInstance productInstance = null;
        try {
            productInstance = productInstanceService.load(vdc, name);
        } catch (ResourceNotFoundException e) {
            fail();
        }

        // then
        verify(client).resource(url);
        assertNotNull(productInstance);
    }

    @Test
    public void shouldLoadProductInstanceByUrl() {
        // given
        Client client = mock(Client.class);
        String baseHost = "localhost";
        String type = "application/json";
        String vdc = "my_vdc";
        String name = "name";

        ProductInstanceService productInstanceService = new ProductInstanceServiceImpl(client, baseHost, type);
        ((ProductInstanceServiceImpl) productInstanceService).setSdcWebResourceFactory(sdcWebResourceFactory);

        String url = baseHost + "/vdc/" + vdc + "/productInstance/" + name;
        WebResource webResource = mock(WebResource.class);
        ProductInstance expectedProductInstance = new ProductInstance();

        // when
        when(client.resource(url)).thenReturn(webResource);
        when(sdcWebResourceFactory.getInstance(any(WebResource.class))).thenReturn(sdcWebResource);
        when(sdcWebResource.get(type, ProductInstance.class)).thenReturn(expectedProductInstance);

        ProductInstance productInstance = null;
        try {
            productInstance = productInstanceService.load(url);
        } catch (ResourceNotFoundException e) {
            fail();
        }

        // then
        verify(client).resource(url);
        assertNotNull(productInstance);
    }
}
