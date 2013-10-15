/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.client.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.client.services.ProductReleaseService;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

public class ProductReleaseServiceImplTest {

    @Test
    public void shouldAddProductRelease() {
        // given
        Client client = mock(Client.class);
        String baseHost = "http://localhost";
        String type = "application/json";
        ProductReleaseService productReleaseService = new ProductReleaseServiceImpl(client, baseHost, type);
        ProductReleaseDto productReleaseDto = new ProductReleaseDto();
        productReleaseDto.setProductName("tomcat");
        productReleaseDto.setVersion("6");

        ProductRelease productReleaseCreated = new ProductRelease();
        productReleaseCreated.setVersion("6");
        Product product = new Product();
        product.setName("tomcat");
        productReleaseCreated.setProduct(product);

        String url = "http://localhost/catalog/product/tomcat/release/";
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        // when

        when(client.resource(url)).thenReturn(webResource);
        when(webResource.accept(type)).thenReturn(builder);
        when(builder.type(type)).thenReturn(builder);
        when(builder.post(ProductRelease.class, productReleaseDto)).thenReturn(productReleaseCreated);

        ProductRelease productRelease = productReleaseService.add(productReleaseDto);

        // then
        assertNotNull(productRelease);
        assertEquals(productRelease.getProduct().getName(), "tomcat");
        assertEquals(productRelease.getVersion(), "6");

    }
}
