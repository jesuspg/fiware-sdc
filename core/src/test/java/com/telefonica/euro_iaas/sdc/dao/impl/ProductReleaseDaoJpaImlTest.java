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
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;

/**
 * Unit test for ProductReleaseDaoImpl.
 * 
 * @author jesus.movilla
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class ProductReleaseDaoJpaImlTest {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private OSDao osDao;
    @Autowired
    private ProductReleaseDao productReleaseDao;

    /**
     * Testing method create and load ProductRelease.
     * 
     * @throws Exception
     */
    @Test(expected = EntityNotFoundException.class)
    public void testCreateAndFindByCriteria() throws Exception {

        Product product = new Product("name", "desc");

        product.addAttribute(new Attribute("clave", "valor"));
        product.addMetadata(new Metadata("metKey", "metValue"));

        productDao.create(product);

        ProductRelease release = new ProductRelease("v1", "releaseNotes1", product, osDao.findAll(), null);
        ProductRelease createdRelease = productReleaseDao.create(release);
        assertEquals(createdRelease, release);

        ProductReleaseSearchCriteria criteria = new ProductReleaseSearchCriteria();

        ProductRelease loadedRelease = productReleaseDao.load(product, "v1");
        assertEquals(release, loadedRelease);

        loadedRelease = productReleaseDao.load(product, "v2");
        fail("EntityNotFoundException expected");
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

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

}
