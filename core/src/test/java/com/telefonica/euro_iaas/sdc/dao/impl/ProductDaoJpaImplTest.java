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

package com.telefonica.euro_iaas.sdc.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;

/**
 * Unit test for SODaoJpaImpl
 * 
 * @author Sergio Arroyo
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class ProductDaoJpaImplTest {

    @Autowired
    ProductDao productDao;

    @Autowired
    OSDao osDao;

    /**
     * Test the create and load method
     */
    @Test
    public void testCreate() throws Exception {

        Product product = new Product();
        product.setName("productName1");
        product.addAttribute(new Attribute("key", "value"));
        product.addMetadata(new Metadata("netkey", "metvalue"));

        product = productDao.create(product);
        assertEquals(product, productDao.load("productName1"));
        assertNotNull(product.getId());

    }

    /**
     * Test the update and load method
     */
    @Test
    public void testUpdate() throws Exception {

        // given
        Product product = new Product();
        product.setName("myproduct");
        product.addAttribute(new Attribute("key1", "value"));
        product.addMetadata(new Metadata("netkey1", "metvalue"));
        productDao.create(product);

        Product loadedProduct = productDao.load("myproduct");

        loadedProduct.addAttribute(new Attribute("key2", "value"));
        loadedProduct.addMetadata(new Metadata("met_key2", "met_value2"));

        // when
        Product updatedProduct = productDao.update(loadedProduct);

        // then
        assertEquals(updatedProduct.getAttributes().size(), 2);
        assertEquals(updatedProduct.getMetadatas().size(), 2);

    }

    /**
     * @param productDao
     *            the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * @param osDao
     *            the osDao to set
     */
    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
    }

}
