package com.telefonica.euro_iaas.sdc.client.services.impl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    }
}
