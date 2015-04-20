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
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
import com.telefonica.euro_iaas.sdc.rest.exception.APIException;
import com.telefonica.euro_iaas.sdc.rest.resources.ProductResourceImpl;
import com.telefonica.euro_iaas.sdc.rest.validation.ProductResourceValidator;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class ProductResourceImplTest {
    public static String PRODUCT_NAME = "Product::server";
    public static String PRODUCT_VERSION = "Product::version";
    private ProductResourceImpl productResource = null;
    private ProductResourceValidator productResourceValidator = null;
    private ProductManager productManager = null;
    private Product product = null;

    @Before
    public void setUp() throws Exception {
        productResource = new ProductResourceImpl();
        productManager = mock(ProductManager.class);
        productResourceValidator = mock(ProductResourceValidator.class);
        productResource.setProductManager(productManager);
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("nofiware");
        productResource.setSystemPropertiesProvider(systemPropertiesProvider);
        doNothing().when(productResourceValidator).validateInsert(any(Product.class));
        productResource.setValidator(productResourceValidator);
        product = new Product(PRODUCT_NAME, "description");
        OS os = new OS("os1", "1", "os1 description", "v1");

        ProductRelease productRelease = new ProductRelease(PRODUCT_VERSION, "releaseNotes", product, Arrays.asList(os),
                null);
        List<ProductRelease> lProductRelease = new ArrayList<ProductRelease>();
        lProductRelease.add(productRelease);

        when(productManager.insert(any(Product.class), any(String.class))).thenReturn(product);
        when(productManager.load(any(String.class))).thenReturn(product);
        doNothing().when(productManager).delete(any(Product.class));

    }

    /**
     * It tests the insertion of the product.
     * 
     * @throws Exception
     */
    @Test
    public void testInsert() throws Exception {
        Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.setDescription("description");

        Product createdProduct = productResource.insert(product);
        assertEquals(createdProduct.getName(), PRODUCT_NAME);
    }

    /**
     * It validates the insertion of the product.
     * 
     * @throws Exception
     */
    @Test(expected = APIException.class)
    public void testInsertErroProductVAlidation() throws Exception {
        Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.setDescription("description");
        Mockito.doThrow(new InvalidEntityException(product, null)).when(productResourceValidator)
                .validateInsert(any(Product.class));
        productResource.insert(product);
    }

    /**
     * It finds all product.
     * 
     * @throws Exception
     */
    @Test
    public void testFindAll() throws Exception {
        List<Product> product = productResource.findAll(new Integer(1), new Integer(1), null, null);
        assertNotNull(product);
    }

    /**
     * It tests the load of product.
     * 
     * @throws Exception
     */
    @Test
    public void testLoad() throws Exception {
        Product product = productResource.load("name");
        assertNotNull(product);
    }

    /**
     * It tests loading all attributes in the product.
     * 
     * @throws Exception
     */
    @Test
    public void testLoadAttributes() throws Exception {
        List<Attribute> att = productResource.loadAttributes("name");
        assertNotNull(att);
    }

    /**
     * It loads the all attributes metadata.
     * 
     * @throws Exception
     */
    @Test
    public void testLoadMetadatas() throws Exception {
        List<Metadata> meta = productResource.loadMetadatas("name");
        assertNotNull(meta);
    }

    /**
     * It tests the insertion of a metadata.
     * 
     * @throws Exception
     */
    @Test
    public void testInsertMetadata() throws Exception {
        Metadata meta = new Metadata("metaInsert", "value");
        when(productManager.load(any(String.class))).thenReturn(product);
        productResource.insertMetadata(product.getName(), meta);
        verify(productManager).update(any(Product.class));
    }

    /**
     * It tests the insertion of an attribute.
     * 
     * @throws Exception
     */
    @Test
    public void testInsertAttribute() throws Exception {
        Attribute att = new Attribute("metaInsert", "value");
        when(productManager.load(any(String.class))).thenReturn(product);
        productResource.insertAttribute(product.getName(), att);
        verify(productManager).update(any(Product.class));
    }

    /**
     * It tests loading a metadata.
     * 
     * @throws Exception
     */
    @Test
    public void testLoadMetadata() throws Exception {
        product.addMetadata(new Metadata("metadata", "value"));
        when(productManager.load(any(String.class))).thenReturn(product);
        Metadata meta = productResource.loadMetadata("productname", "metadata");
        assertNotNull(meta);
    }

    /**
     * It tests loading a metadata with errors..
     * 
     * @throws Exception
     */
    @Test
    public void testLoadMetadataError() throws Exception {
        product.addMetadata(new Metadata("metadata", "value"));
        when(productManager.load(any(String.class))).thenThrow(
                new EntityNotFoundException(Product.class, "name", product.getName()));
        try {
            productResource.loadMetadata("productname", "metadata");
            fail("An exception should have been lanched");
        } catch (Exception e) {
            verify(productManager).load(anyString());
        }
    }

    /**
     * It tests loading an attribute.
     * 
     * @throws Exception
     */
    @Test
    public void testLoadAttribute() throws Exception {
        product.addAttribute(new Attribute("att", "value"));
        when(productManager.load(any(String.class))).thenReturn(product);
        Attribute meta = productResource.loadAttribute(product.getName(), "att");
        assertNotNull(meta);
    }

    /**
     * It updates a metadata of a product.
     * 
     * @throws Exception
     */
    @Test
    public void testUpdateMetadata() throws Exception {
        Metadata metadata = new Metadata();
        metadata.setKey("key");

        when(productManager.loadMetadata(anyString(), anyString())).thenReturn(metadata);
        doNothing().when(productManager).updateMetadata(metadata);

        productResource.updateMetadata("productname", "key", new Metadata("key", "value"));
        verify(productManager).updateMetadata(metadata);

    }

    /**
     * Update metadata, with key and description
     */
    @Test
    public void shouldUpdateDescriptionForMetadata() throws EntityNotFoundException {
        // given
        Metadata metadata = new Metadata();
        metadata.setKey("key1");
        metadata.setValue(null);
        metadata.setDescription("new description");
        Metadata metadataSaved = new Metadata();
        metadataSaved.setKey("key1");
        metadataSaved.setValue("value");
        metadataSaved.setDescription("description");

        when(productManager.loadMetadata(anyString(), anyString())).thenReturn(metadataSaved);
        doNothing().when(productManager).updateMetadata(metadata);
        Metadata updatedMetadata = new Metadata();
        updatedMetadata.setKey("key1");
        updatedMetadata.setValue("value");
        updatedMetadata.setDescription("new description");

        // when
        productResource.updateMetadata("productName", "key1", updatedMetadata);

        // then
        verify(productManager).updateMetadata(updatedMetadata);
        assertNotNull(updatedMetadata.getValue());

    }

    /**
     * Return error when error returned by database
     * 
     * @throws EntityNotFoundException
     */
    @Test(expected = APIException.class)
    public void shouldReturnErrorWhenProblemInDatabaseWithUpdateMetadata() throws EntityNotFoundException {
        // given
        Metadata updatedMetadata = new Metadata();
        updatedMetadata.setKey("key1");
        updatedMetadata.setValue("value");
        updatedMetadata.setDescription("new description");
        when(productManager.loadMetadata(anyString(), anyString())).thenReturn(updatedMetadata);
        doThrow(new org.hibernate.exception.ConstraintViolationException("", new SQLException(), "")).when(
                productManager).updateMetadata(any(Metadata.class));

        // when
        productResource.updateMetadata("productName", "key1", updatedMetadata);

        // then
    }

    @Test(expected = APIException.class)
    public void shouldReturnErrorTryingToChangeTheMetadataKeyInUpdateMetadata() throws EntityNotFoundException {
        // given
        Metadata updatedMetadata = new Metadata();
        updatedMetadata.setKey("key_new");
        updatedMetadata.setValue("value");
        updatedMetadata.setDescription("new description");
        Metadata metadataInDataBase = new Metadata();
        metadataInDataBase.setKey("key1");
        metadataInDataBase.setValue("original_value");
        metadataInDataBase.setDescription("description");
        when(productManager.loadMetadata(anyString(), anyString())).thenReturn(metadataInDataBase);

        // when
        productResource.updateMetadata("productName", "key1", updatedMetadata);

        // then
    }

    /**
     * It updates a attribute of a product.
     * 
     * @throws Exception
     */
    @Test
    public void testUpdateAttribute() throws Exception {
        product.addAttribute(new Attribute("att", "value"));
        when(productManager.load(any(String.class))).thenReturn(product);
        doNothing().when(productManager).update(any(Product.class));
        productResource.updateAttribute("productname", "att", new Attribute("att", "value2"));

    }

    /**
     * It deletes an attribute in a product.
     * 
     * @throws Exception
     */
    @Test
    public void testDeleteAttribute() throws Exception {
        product.addAttribute(new Attribute("att", "value"));
        when(productManager.load(any(String.class))).thenReturn(product);
        doNothing().when(productManager).update(any(Product.class));
        productResource.deleteAttribute(product.getName(), "att");

    }

    /**
     * It tests the deletion of a product with error.
     * 
     * @throws Exception
     */
    @Test
    public void testDeleteAttributeNoExistProduct() throws Exception {
        product.addAttribute(new Attribute("att", "value"));
        when(productManager.load(any(String.class))).thenThrow(
                new EntityNotFoundException(Product.class, "name", product.getName()));

        try {
            productResource.deleteAttribute("productname", "att");
            fail("An exception should have been lanched");
        } catch (Exception e) {
            verify(productManager).load(anyString());
        }

    }

    /**
     * It tests the deletion of a product with error.
     * 
     * @throws Exception
     */
    @Test
    public void testDeleteAttributeNoExistMetadata() throws Exception {
        product.addAttribute(new Attribute("metadata", "value"));
        when(productManager.load(any(String.class))).thenReturn(product);
        try {
            productResource.deleteAttribute(product.getName(), "NOEXIST");
            fail("An exception should have been lanched");
        } catch (Exception e) {
            verify(productManager).load(anyString());
        }

    }

    /**
     * It test the deletion of a metadata.
     * 
     * @throws Exception
     */
    @Test
    public void testDeleteMetadata() throws Exception {
        product.addMetadata(new Metadata("metadata", "value"));
        when(productManager.load(any(String.class))).thenReturn(product);
        doNothing().when(productManager).delete(any(Product.class));
        productResource.deleteMetadata("productname", "metadata");

    }

    /**
     * It test the deletion of a metadata with errors.
     * 
     * @throws Exception
     */
    @Test
    public void testDeleteMetadataNoExistProduct() throws Exception {
        product.addMetadata(new Metadata("metadata", "value"));
        when(productManager.load(any(String.class))).thenThrow(
                new EntityNotFoundException(Product.class, "name", product.getName()));
        try {
            productResource.deleteMetadata("productname", "metadata");
            fail("An exception should have been lanched");
        } catch (Exception e) {
            verify(productManager).load(anyString());
        }

    }

    /**
     * It test the deletion of a metadata with errors.
     * 
     * @throws Exception
     */
    @Test
    public void testDeleteMetadataNoExistMetadata() throws Exception {
        product.addMetadata(new Metadata("metadata", "value"));
        when(productManager.load(any(String.class))).thenReturn(product);
        try {
            productResource.deleteMetadata(product.getName(), "NOEXIST");
            fail("An exception should have been lanched");
        } catch (Exception e) {
            verify(productManager).load(anyString());
        }
    }

    /**
     * It find all product with some restrictions.
     */
    @Test
    public void testFindAllFilterProduct() {
        Product product = new Product("name", "description");
        product.addMetadata(new Metadata("public", "yes"));
        Product product2 = new Product("name2", "description");
        product2.addMetadata(new Metadata("public", "no"));
        List<Product> products = new ArrayList();
        products.add(product);
        products.add(product2);
        when(productManager.findByCriteria(any(ProductSearchCriteria.class))).thenReturn(products);
        List<Product> productReturn = productResource.findAll(null, null, null, null);
        assertEquals(productReturn.size(), 1);
    }

    /**
     * It tests the deletion of a product.
     * 
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        productResource.delete(PRODUCT_NAME);

    }
}
