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

package com.telefonica.euro_iaas.sdc.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.rest.resources.ProductResourceImpl;
import com.telefonica.euro_iaas.sdc.rest.validation.ProductResourceValidator;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductResourceImplTest {
    public static String PRODUCT_NAME = "Product::server";
    public static String PRODUCT_VERSION = "Product::version";
    ProductResourceImpl productResource = null;

    @Before
    public void setUp() throws Exception {
        productResource = new ProductResourceImpl();
        ProductManager productManager = mock(ProductManager.class);
        ProductResourceValidator productResourceValidator = mock(ProductResourceValidator.class);
        productResource.setValidator(productResourceValidator);
        productResource.setProductManager(productManager);
        Product product = new Product(PRODUCT_NAME, "description");
        OS os = new OS("os1", "1", "os1 description", "v1");

        ProductRelease productRelease = new ProductRelease(PRODUCT_VERSION, "releaseNotes", null, product,
                Arrays.asList(os), null);
        List<ProductRelease> lProductRelease = new ArrayList<ProductRelease>();
        lProductRelease.add(productRelease);

        when(productManager.insert(any(ProductRelease.class))).thenReturn(productRelease);
        when(productManager.load(any(String.class))).thenReturn(product);
        when(productManager.findReleasesByCriteria(any(ProductReleaseSearchCriteria.class)))
                .thenReturn(lProductRelease);
        doNothing().when(productManager).delete(any(ProductRelease.class));

    }

    @Test
    public void testInsert() throws Exception {
        ProductReleaseDto productReleaseDto = new ProductReleaseDto();
        productReleaseDto.setProductName(PRODUCT_NAME);
        productReleaseDto.setProductDescription("yum 0.1.1 description");
        productReleaseDto.setVersion(PRODUCT_VERSION);
        productReleaseDto.setReleaseNotes("prueba ReelaseNotes");

        ProductRelease productRelease = productResource.insert(productReleaseDto);
        assertEquals(productRelease.getProduct().getName(), PRODUCT_NAME);
        assertEquals(productRelease.getVersion(), PRODUCT_VERSION);

    }

    @Test
    public void testDelete() throws Exception {
        productResource.delete(PRODUCT_NAME + "-" + PRODUCT_VERSION);

    }

    @Test
    public void testList() throws Exception {
        List<ProductRelease> lProductRelease = productResource.findAll(PRODUCT_NAME, null, null, null, null, null);
        assertEquals(lProductRelease.size(), 1);
        assertEquals(lProductRelease.get(0).getProduct().getName(), PRODUCT_NAME);
    }

}
