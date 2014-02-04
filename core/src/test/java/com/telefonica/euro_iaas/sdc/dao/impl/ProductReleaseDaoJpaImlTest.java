/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
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

    protected void createProductRelease() throws Exception {

        ProductRelease productRelease = new ProductRelease();
        productRelease.setVersion("0.1.1");
        productRelease.setReleaseNotes("prueba ReelaseNotes");

        OS so = new OS("Prueba I", "1", "Prueba I Description", "Prueba I Version");
        try {
            so = osDao.load(so.getName());
            System.out.println("The OS " + so.getName() + " already exists");
        } catch (EntityNotFoundException e) {
            System.out.println("The Product " + so.getName() + " does not exist");
            so = osDao.create(so);
        }

        List<OS> supportedOOSS = Arrays.asList(so);
        productRelease.setSupportedOOSS(supportedOOSS);

        Attribute privateAttribute = new Attribute("ssl_port", "8443", "The ssl listen port");
        Attribute privateAttributeII = new Attribute("port", "8080", "The listen port");

        List<Attribute> privateAttributes = Arrays.asList(privateAttribute, privateAttributeII);

        List<Metadata> metadatas = Arrays.asList(new Metadata("key1", "value1", "desc1"), new Metadata("key2",
                "value2", "desc2"));

        Product product = new Product();
        product.setName("yum");
        product.setDescription("yum description");
        product.setAttributes(privateAttributes);
        product.setMetadatas(metadatas);

        try {
            product = productDao.load(product.getName());
            System.out.println("The Product " + product.getName() + " already exists");
        } catch (EntityNotFoundException e) {
            System.out.println("The Product " + product.getName() + " does not exist");
            product = productDao.create(product);
        }
        productRelease.setProduct(product);

        // productRelease.setPrivateAttributes(privateAttributes);

        ProductRelease createdRelease = productReleaseDao.create(productRelease);

        assertEquals(createdRelease, productRelease);

    }

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
