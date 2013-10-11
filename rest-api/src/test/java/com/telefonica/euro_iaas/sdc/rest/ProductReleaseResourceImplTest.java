/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.rest.resources.ProductReleaseResourceImpl;
import com.telefonica.euro_iaas.sdc.rest.validation.ProductResourceValidator;

/**
 * Unit Test for ProductReleaseResourceImpl.
 * 
 * @author jesus.movilla
 */
public class ProductReleaseResourceImplTest {
    public static String PRODUCT_NAME = "Product::server";
    public static String PRODUCT_VERSION = "Product::version";
    private ProductReleaseResourceImpl productReleaseResource = null;

    /**
     * Prepare method.
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        productReleaseResource = new ProductReleaseResourceImpl();
        ProductReleaseManager productReleaseManager = mock(ProductReleaseManager.class);
        ProductManager productManager = mock(ProductManager.class);
        ProductResourceValidator productResourceValidator = mock(ProductResourceValidator.class);
        productReleaseResource.setValidator(productResourceValidator);
        productReleaseResource.setProductReleaseManager(productReleaseManager);
        productReleaseResource.setProductManager(productManager);
        Product product = new Product(PRODUCT_NAME, "description");
        OS os = new OS("os1", "1", "os1 description", "v1");

        ProductRelease productRelease = new ProductRelease(PRODUCT_VERSION, "releaseNotes", product, Arrays.asList(os),
                null);
        List<ProductRelease> lProductRelease = new ArrayList<ProductRelease>();
        lProductRelease.add(productRelease);

        when(productReleaseManager.insert(any(ProductRelease.class))).thenReturn(productRelease);
        when(productManager.load(any(String.class))).thenReturn(product);
        when(productReleaseManager.findReleasesByCriteria(any(ProductReleaseSearchCriteria.class))).thenReturn(
                lProductRelease);
        doNothing().when(productReleaseManager).delete(any(ProductRelease.class));

    }

    /**
     * Unit Test for Product Release Insert.
     * 
     * @throws Exception
     */
    @Test
    public void testInsert() throws Exception {
        ProductReleaseDto productReleaseDto = new ProductReleaseDto();
        productReleaseDto.setProductName(PRODUCT_NAME);
        productReleaseDto.setProductDescription("yum 0.1.1 description");
        productReleaseDto.setVersion(PRODUCT_VERSION);
        productReleaseDto.setReleaseNotes("prueba ReelaseNotes");

        ProductRelease productRelease = productReleaseResource.insert(PRODUCT_NAME, productReleaseDto);
        assertEquals(productRelease.getProduct().getName(), PRODUCT_NAME);
        assertEquals(productRelease.getVersion(), PRODUCT_VERSION);

    }

    /**
     * Unit Test for ProductRelease delete functionality
     * 
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        productReleaseResource.delete(PRODUCT_NAME, PRODUCT_VERSION);

    }
}
