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

package com.telefonica.euro_iaas.sdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.exception.InsertResourceException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ProductReleaseService;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

/**
 * @author jesus.movilla
 */
public class ProductReleaseIT {

    SDCClient client;
    String baseUrl;
    String mediaType;
    
    public static String TOKEN ="token";
    public static String TENANT ="vdc";

    @Before
    public void setUp() {
        client = new SDCClient();
        baseUrl = "http://localhost:8888/sdc/rest";
        mediaType = "application/xml";
    }

    @Test
    public void shouldFailWhenLoadAnUnknownProduct() {
        // given
        ProductReleaseService productReleaseService = client.getProductReleaseService(baseUrl, mediaType);

        // when

        ProductRelease productRelease = null;
        try {
            productRelease = productReleaseService.load("kk", "",TOKEN,TENANT );
            fail("The product kk should not exist");
        } catch (ResourceNotFoundException e) {
            // then
            assertNotNull(productReleaseService);
            assertNull(productRelease);

        }

    }

    @Test
    public void shouldLoadAProduct() throws ResourceNotFoundException {
        // given

        ProductReleaseService productReleaseService = client.getProductReleaseService(baseUrl, mediaType);
        // when

        ProductRelease productRelease = productReleaseService.load("tomcat", "6",TOKEN,TENANT );
        // then
        assertNotNull(productReleaseService);
        assertNotNull(productRelease);
        assertEquals(productRelease.getProduct().getName(), "tomcat");
        assertEquals(productRelease.getVersion(), "6");

    }

    @Test
    public void shouldReturnError404WithFindAllWithoutProductName() {
        // given
        ProductReleaseService productReleaseService = client.getProductReleaseService(baseUrl, mediaType);

        // when
        try {

            productReleaseService.findAll(null, null, null, null, null, null, TOKEN, TENANT);
            fail();
        } catch (com.sun.jersey.api.client.UniformInterfaceException ex) {
            // then
            assertEquals(ex.getResponse().getStatus(), 404);
        }

    }

    @Test
    public void shouldReturnProductReleaseFindByTomcat() {
        // given

        // when
        ProductReleaseService productReleaseService = client.getProductReleaseService(baseUrl, mediaType);

        List<ProductRelease> list = productReleaseService.findAll(null, null, null, null, "tomcat", null,TOKEN,TENANT );
        // then
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(list.get(0).getProduct().getName(), "tomcat");

    }

    @Test
    public void shouldReturnError404WithFindByUnknownProduct() {
        // given

        ProductReleaseService productReleaseService = client.getProductReleaseService(baseUrl, mediaType);
        // when
        try {
            productReleaseService.findAll(null, null, null, null, "kk", null,TOKEN,TENANT );
            fail();
        } catch (com.sun.jersey.api.client.UniformInterfaceException ex) {
            // then
            assertEquals(ex.getResponse().getStatus(), 404);
        }

    }

    @Test
    public void shouldAddProductToCatalogAndProductReleaseAfterButWithouProductReleaseName() {
        // given

        ProductReleaseDto productReleaseDto = new ProductReleaseDto();
        productReleaseDto.setVersion("6");
        productReleaseDto.setReleaseNotes("release notes");
        productReleaseDto.setProductDescription("desc");

        String productName = "mytomcat";

        Product product = new Product();
        product.setName(productName);
        product.setDescription("desc");
        Client jerseyClient = new Client();

        // when
        try {
            client.getProductService(baseUrl, mediaType).add(product,TOKEN,TENANT );
        } catch (InsertResourceException e) {
            fail();
        }

        String url = baseUrl + MessageFormat.format(ClientConstants.BASE_PRODUCT_RELEASE_PATH, productName);
        WebResource wr = jerseyClient.resource(url);
        ProductRelease productRelease = wr.accept(mediaType).type(mediaType)
                .post(ProductRelease.class, productReleaseDto);

        // then
        assertNotNull(productRelease);
        assertEquals(productRelease.getProduct().getName(), "mytomcat");
        assertEquals(productRelease.getVersion(), "6");
    }

    @Test
    public void shouldAddProductToCatalogAndProductReleaseAfter() {
        // given
        String productName = "mytomcat";
        String version = "6";
        String description = "mytomcatt 6";

        List<OS> supportedOS = new ArrayList<OS>();
        OS os1 = new OS("95", "Debian", "Debian 5", "5");
        supportedOS.add(os1);

        List<ProductRelease> transitableReleases = new ArrayList<ProductRelease>();
        ProductRelease productRelease1 = new ProductRelease();
        transitableReleases.add(productRelease1);
        List<Attribute> attributes = new ArrayList<Attribute>();

        String releaseNotes = "release notes";
        List<Metadata> metadatas = new ArrayList<Metadata>();
        ProductReleaseDto productReleaseDto = new ProductReleaseDto(productName, description, version, releaseNotes,
                attributes, metadatas, supportedOS, transitableReleases);
        Product product = new Product();
        product.setName(productName);
        product.setDescription("desc");

        // when
        try {
            client.getProductService(baseUrl, mediaType).add(product,TOKEN,TENANT );
        } catch (InsertResourceException e) {
            fail();
        }
        ProductRelease productRelease = client.getProductReleaseService(baseUrl, mediaType).add(productReleaseDto,TOKEN,TENANT );

        // then
        assertNotNull(productRelease);
        assertEquals(productRelease.getProduct().getName(), productName);
        assertEquals(productRelease.getVersion(), "6");
    }

    @Test
    @Ignore
    public void shouldAddProductReleaseWithCookbookAndFilesToCatalog() {
        // given
        String productName = "tomcat";
        String version = "6";
        String description = "tomcat 6";
        InputStream cookbook = this.getClass().getResourceAsStream("/files/" + productName + version + "-cookbook.tar");
        InputStream files = this.getClass().getResourceAsStream("/files/" + productName + version + "-bin.tar");

        List<OS> supportedOS = new ArrayList<OS>();
        OS os1 = new OS();
        os1.setName("Debian");
        os1.setOsType("95");
        os1.setVersion("9");
        supportedOS.add(os1);
        supportedOS.add(os1);
        List<ProductRelease> transitableReleases = new ArrayList<ProductRelease>();
        ProductRelease productRelease1 = new ProductRelease();
        Product product = new Product();
        product.setName("tomcat");
        productRelease1.setProduct(product);
        transitableReleases.add(productRelease1);
        List<Attribute> attributes = new ArrayList<Attribute>();

        String releaseNotes = "release notes";
        List<Metadata> metadatas = new ArrayList<Metadata>();
        ProductReleaseDto productReleaseDto = new ProductReleaseDto(productName, description, version, releaseNotes,
                attributes, metadatas, supportedOS, transitableReleases);
        // when
        ProductRelease productRelease = client.getProductReleaseService(baseUrl, mediaType).add(productReleaseDto,
                cookbook, files,TOKEN,TENANT );

        // then
        assertNotNull(productRelease);
    }
}
