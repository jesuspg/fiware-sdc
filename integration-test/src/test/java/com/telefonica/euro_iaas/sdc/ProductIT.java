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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.exception.InsertResourceException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ProductService;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;

public class ProductIT {

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
        ProductService productService = client.getProductService(baseUrl, mediaType);

        // when

        Product product = null;
        try {
            product = productService.load("kk",TOKEN,TENANT );
            fail("The product kk should not exist");
        } catch (ResourceNotFoundException e) {
            // then
            assertNotNull(productService);
            assertNull(product);

        }

    }

    @Test
    public void shouldLoadAProductTomcat() throws ResourceNotFoundException {
        // given
        ProductService productService = client.getProductService(baseUrl, mediaType);
        // when

        Product product = productService.load("tomcat", TOKEN, TENANT);
        // then
        assertNotNull(productService);
        assertNotNull(product);

    }

    @Test
    public void shouldLoadAProductWithJSONMediaType() throws ResourceNotFoundException {
        mediaType = "application/json";
        // given
        ProductService productService = client.getProductService(baseUrl, mediaType);
        // when

        Product product = productService.load("tomcat", TOKEN, TENANT);
        // then
        assertNotNull(productService);
        assertNotNull(product);

    }

    @Test
    public void shouldListProductCatalog() {
        // given
        ProductService productService = client.getProductService(baseUrl, mediaType);

        // when

        List<Product> list = productService.findAll(null, null, null, null, TOKEN, TENANT);

        // then
        assertNotNull(list);
        assertFalse(list.isEmpty());

    }

    @Test
    public void shouldAddProductToCatalog() {
        // given
        String productName = "tomcattest";
        String description = "tomcattest 6";

        Product product = new Product();
        product.setName(productName);
        product.setDescription(description);

        List<Attribute> attributes = new ArrayList<Attribute>();
        product.setAttributes(attributes);

        List<Metadata> metadatas = new ArrayList<Metadata>();
        product.setMetadatas(metadatas);

        // when
        ProductService productService = client.getProductService(baseUrl, mediaType);
        try {
            Product createdProduct = productService.add(product, TOKEN, TENANT);
            // then
            assertNotNull(createdProduct);
        } catch (InsertResourceException e) {
            fail();
        }
    }

    @Test
    public void shouldAddProductToCatalogWithJson() {
        mediaType = "application/json";
        // given
        String productName = "tomcattest";
        String description = "tomcattest 6";

        Product product = new Product();
        product.setName(productName);
        product.setDescription(description);

        List<Attribute> attributes = new ArrayList<Attribute>();
        product.setAttributes(attributes);

        List<Metadata> metadatas = new ArrayList<Metadata>();
        product.setMetadatas(metadatas);

        // when
        ProductService productService = client.getProductService(baseUrl, mediaType);
        try {
            Product createdProduct = productService.add(product, TOKEN, TENANT);
            // then
            assertNotNull(createdProduct);
        } catch (InsertResourceException e) {
            fail();
        }

    }

    @Test
    public void shouldAddProductToCatalogWithoutMetadatas() {
        // given
        String productName = "tomcattestnometadatas";
        String description = "tomcattestnometadatas 6";

        Product product = new Product();
        product.setName(productName);
        product.setDescription(description);

        List<Attribute> attributes = new ArrayList<Attribute>();
        product.setAttributes(attributes);

        ProductService productService = client.getProductService(baseUrl, mediaType);
        // when
        Product createdProduct = null;
        try {
            createdProduct = productService.add(product, TOKEN, TENANT);
            // then
            assertNotNull(createdProduct);
        } catch (InsertResourceException e) {
            // then
            fail();
        }
    }

    @Test
    public void shouldDeleteAProduct() throws InsertResourceException {
        // given

        ProductService productService = client.getProductService(baseUrl, mediaType);
        Product produtToDelete = new Product();
        produtToDelete.setName("productToDelete");
        produtToDelete.setDescription("test product");

        productService.add(produtToDelete, TOKEN, TENANT);

        // when

        productService.delete("productToDelete", TOKEN, TENANT);

        // then
        try {
            productService.load("productToDelete", TOKEN, TENANT);
            fail();
        } catch (ResourceNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    @Ignore
    public void shouldLoadAttributes() {
        // given
        ProductService productService = client.getProductService(baseUrl, mediaType);
        createTestProduct("kk1");
        // when

        try {
            productService.loadAttributes("kk1", TOKEN, TENANT);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();  // To change body of catch statement use File | Settings | File Templates.
            fail("ResourceNotFoundException " + e.getMessage());
        }
        // then
    }

    @Test
    @Ignore
    public void shouldLoadMetadatas() {
        // given
        ProductService productService = client.getProductService(baseUrl, mediaType);
        createTestProduct("kk2");
        // when

        try {
            productService.loadMetadatas("kk2", TOKEN, TENANT);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();  // To change body of catch statement use File | Settings | File Templates.
            fail("ResourceNotFoundException " + e.getMessage());
        }
        // then
    }

    private Product createTestProduct(String productName) {
        String description = "tomcattest 6";
        mediaType = "application/json";

        Product product = new Product();
        product.setName(productName);
        product.setDescription(description);

        List<Attribute> attributes = new ArrayList<Attribute>();
        product.setAttributes(attributes);

        Attribute userRootAttribute = new Attribute();
        userRootAttribute.setValue("8080");
        userRootAttribute.setKey("port");
        userRootAttribute.setDescription("desc");
        attributes.add(userRootAttribute);

        List<Metadata> metadatas = new ArrayList<Metadata>();
        product.setMetadatas(metadatas);

        // when
        ProductService productService = client.getProductService(baseUrl, mediaType);
        try {
            return productService.add(product, TOKEN, TENANT);
        } catch (InsertResourceException e) {
            fail("InsertResourceException:" + e);
        }
        return null;
    }

}
