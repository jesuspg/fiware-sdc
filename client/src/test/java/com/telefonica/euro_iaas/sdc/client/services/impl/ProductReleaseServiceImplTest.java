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
        String token = "token";
        String tenant = "tenant";
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

        ProductRelease productRelease = productReleaseService.add(productReleaseDto, token, tenant);

        // then
        assertNotNull(productRelease);
        assertEquals(productRelease.getProduct().getName(), "tomcat");
        assertEquals(productRelease.getVersion(), "6");

    }
}
