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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.model.Artifact;
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
