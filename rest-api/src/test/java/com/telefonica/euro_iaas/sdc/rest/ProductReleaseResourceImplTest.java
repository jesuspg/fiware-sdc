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
import com.telefonica.euro_iaas.sdc.rest.validation.GeneralResourceValidator;
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
        GeneralResourceValidator generalResourceValidator = mock(GeneralResourceValidator.class);
        productReleaseResource.setValidator(productResourceValidator);
        productReleaseResource.setGeneralValidator(generalResourceValidator);
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
