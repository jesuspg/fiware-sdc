/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.dao.impl;

import static org.junit.Assert.assertEquals;

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

    public final static String PRODUCT_NAME = "productName";
    public final static String PRODUCT_VERSION = "productVersion";

    /**
     * Test the create and load method
     */
    @Test
    public void testCreate() throws Exception {

        Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.addAttribute(new Attribute("key", "value"));
        product.addMetadata(new Metadata("netkey", "metvalue"));

        assertEquals(0, productDao.findAll().size());
        product = productDao.create(product);
        assertEquals(product, productDao.load(PRODUCT_NAME));
        assertEquals(1, productDao.findAll().size());

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
