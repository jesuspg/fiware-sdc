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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
import com.telefonica.euro_iaas.sdc.rest.exception.APIException;
import com.telefonica.euro_iaas.sdc.rest.resources.ProductResourceImpl;
import com.telefonica.euro_iaas.sdc.rest.validation.ProductResourceValidator;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class ProductResourceImplTest {
    public static String PRODUCT_NAME = "Product::server";
    public static String PRODUCT_VERSION = "Product::version";
    private ProductResourceImpl productResource = null;
    private ProductResourceValidator productResourceValidator=null;
    private ProductManager productManager = null;

    @Before
    public void setUp() throws Exception {
        productResource = new ProductResourceImpl();
        productManager = mock(ProductManager.class);
        productResourceValidator = mock(ProductResourceValidator.class);
        productResource.setProductManager(productManager);
        SystemPropertiesProvider systemPropertiesProvider = mock (SystemPropertiesProvider.class);
        when (systemPropertiesProvider.getProperty(any(String.class))).thenReturn("nofiware");
        productResource.setSystemPropertiesProvider(systemPropertiesProvider);
        doNothing().when(productResourceValidator).validateInsert(any(Product.class));
        productResource.setValidator(productResourceValidator);
        Product product = new Product(PRODUCT_NAME, "description");
        OS os = new OS("os1", "1", "os1 description", "v1");

        ProductRelease productRelease = new ProductRelease(PRODUCT_VERSION, "releaseNotes",
            product, Arrays.asList(os), null);
        List<ProductRelease> lProductRelease = new ArrayList<ProductRelease>();
        lProductRelease.add(productRelease);

        when(productManager.insert(any(Product.class))).thenReturn(product);
        when(productManager.load(any(String.class))).thenReturn(product);
        doNothing().when(productManager).delete(any(Product.class));

    }

    @Test
    public void testInsert() throws Exception {
        Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.setDescription("description");
        
        Product createdProduct = productResource.insert(product);
        assertEquals(createdProduct.getName(), PRODUCT_NAME);
    }
    
    @Test(expected=APIException.class)
    public void testInsertErroProductVAlidation() throws Exception {
        Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.setDescription("description");
        Mockito.doThrow(new InvalidEntityException(product, null)).when(productResourceValidator).validateInsert(any(Product.class));
        productResource.insert(product);
    }
    
    @Test
    public void testFindAll() throws Exception {
        List<Product> product = productResource.findAll(new Integer(1), new Integer(1), null, null);
        assertNotNull (product);
    }
    
    @Test
    public void testLoad() throws Exception {
        Product product = productResource.load("name");
        assertNotNull (product);
    }
    
    @Test
    public void testLoadAttributes () throws Exception {
        List<Attribute> att = productResource.loadAttributes("name");
        assertNotNull (att);
    }
    
    @Test
    public void testLoadMetada () throws Exception {
    	 List<Metadata> meta = productResource.loadMetadatas("name");
         assertNotNull (meta);
    }
    
    @Test
    public void testFindAllFilterProduct () {
    	Product product = new Product ("name", "description");
    	product.addMetadata(new Metadata("public", "yes"));
    	Product product2 = new Product ("name2", "description");
    	product2.addMetadata(new Metadata("public", "no"));
    	List<Product> products = new ArrayList ();
    	products.add(product);
    	products.add(product2);
    	when (productManager.findByCriteria(any(ProductSearchCriteria.class))).thenReturn(products);
    	List<Product> productReturn = productResource.findAll(null, null, null, null);
    	assertEquals (productReturn.size() , 1);
    }

    @Test
    public void testDelete() throws Exception {
        productResource.delete(PRODUCT_NAME);

    }
}
