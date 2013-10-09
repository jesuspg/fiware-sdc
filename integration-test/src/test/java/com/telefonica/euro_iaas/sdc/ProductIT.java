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

package com.telefonica.euro_iaas.sdc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
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

    @Before
    public void setUp() {
        client = new SDCClient();
        baseUrl = "http://localhost:8888/sdc/rest";
        mediaType = "application/xml";
    }

    @Test
    public void shouldFailWhenLoadAnUnknownProduct() {
        System.out.println("Starting shouldFailWhenLoadAnUnknownProduct");
        // given
        ProductService productService = client.getProductService(baseUrl, mediaType);

        // when

        Product product = null;
        try {
            product = productService.load("kk");
            fail("The product kk should not exist");
        } catch (ResourceNotFoundException e) {
            // then
            assertNotNull(productService);
            assertNull(product);

        }

    }

    @Test
    public void shouldLoadAProduct() throws ResourceNotFoundException {
        System.out.println("Starting shouldLoadAProduct");
        // given
        ProductService productService = client.getProductService(baseUrl, mediaType);
        // when

        Product product = productService.load("tomcat");
        // then
        assertNotNull(productService);
        assertNotNull(product);

    }

    @Test
    public void shouldListProductCatalog() {
        System.out.println("Starting shouldListProductCatalog");
        // given
        ProductService productService = client.getProductService(baseUrl, mediaType);

        // when

        List<Product> list = productService.findAll(null, null, null, null);

        // then
        assertNotNull(list);
        assertFalse(list.isEmpty());

    }

    @Test
    public void shouldAddProductToCatalog() {
        System.out.println("Starting shouldAddProductToCatalog");
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
        Product createdProduct = null;
        try {
            createdProduct = productService.add(product);
            // then
            assertNotNull(createdProduct);
        } catch (InsertResourceException e) {
            // then
            assertNotNull(productService);
            assertNull(createdProduct);
        }
    }

    @Test
    public void shouldAddProductToCatalogWithoutMetadatas() {
        System.out.println("Starting shouldAddProductToCatalogWithoutMetadatas");
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
            createdProduct = productService.add(product);
            // then
            assertNotNull(createdProduct);
        } catch (InsertResourceException e) {
            // then
            assertNotNull(productService);
            assertNull(createdProduct);
        }
    }
}
