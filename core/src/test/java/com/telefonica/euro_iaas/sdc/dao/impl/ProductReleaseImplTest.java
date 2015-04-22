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

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.fiware.commons.dao.AlreadyExistsEntityException;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class ProductReleaseImplTest {

    @Autowired
    private ProductReleaseDao productReleaseDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OSDao osDao;

    private Product product;

    @Before
    public void prepare() throws Exception {
        product = new Product();
        product.setName("yum");
        product.setDescription("yum description");
        productDao.create(product);

    }

    @Test
    public void createProductRelease() throws AlreadyExistsEntityException, InvalidEntityException,
            EntityNotFoundException {
        // given
        ProductRelease productRelease = new ProductRelease();
        productRelease.setProduct(product);
        productRelease.setVersion("v1");
        productRelease.setReleaseNotes("prueba ReelaseNotes");

        OS os = new OS("Deb", "95", "Debian def 5.2", "5.2");
        osDao.create(os);
        List<OS> supportedOOSS = Arrays.asList(os);
        productRelease.setSupportedOOSS(supportedOOSS);

        // When
        ProductRelease createdRelease = productReleaseDao.create(productRelease);

        // then
        assertEquals(productRelease, createdRelease);

        ProductRelease loadedRelease;
        loadedRelease = productReleaseDao.load(product, "v1");
        assertEquals(productRelease, loadedRelease);
    }

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
    }
}
