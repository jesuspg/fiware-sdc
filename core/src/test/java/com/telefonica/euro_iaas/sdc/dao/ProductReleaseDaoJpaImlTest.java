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

package com.telefonica.euro_iaas.sdc.dao;

import java.util.Arrays;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import junit.framework.Assert;

public class ProductReleaseDaoJpaImlTest extends AbstractJpaDaoTest {

    private ProductDao productDao;
    private OSDao soDao;
    private ProductReleaseDao productReleaseDao;

    protected void createProduct() throws Exception {
        ProductDaoJpaImplTest pdaoTest = new ProductDaoJpaImplTest();
        pdaoTest.setProductDao(productDao);
        pdaoTest.setSoDao(soDao);
        pdaoTest.testCreate();
    }

    protected void createProductRelease() throws Exception {

        ProductRelease productRelease = new ProductRelease();
        productRelease.setVersion("0.1.1");
        productRelease.setReleaseNotes("prueba ReelaseNotes");

        OS so = new OS("Prueba I", "1", "Prueba I Description", "Prueba I Version");
        try {
            so = soDao.load(so.getName());
            System.out.println("The OS " + so.getName() + " already exists");
        } catch (EntityNotFoundException e) {
            System.out.println("The Product " + so.getName() + " does not exist");
            so = soDao.create(so);
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

        productRelease.setPrivateAttributes(privateAttributes);

        ProductRelease createdRelease = productReleaseDao.create(productRelease);

        Assert.assertEquals(createdRelease, productRelease);

    }

    public void testCreateAndFindByCriteria() throws Exception {
        createProduct();
        Product product = productDao.findAll().get(0);

        ProductRelease release = new ProductRelease("v1", "releaseNotes1", null, product, soDao.findAll(), null);
        ProductRelease createdRelease = productReleaseDao.create(release);
        assertEquals(createdRelease, release);

        ProductReleaseSearchCriteria criteria = new ProductReleaseSearchCriteria();
        assertEquals(1, productReleaseDao.findByCriteria(criteria).size());

        ProductRelease loadedRelease = productReleaseDao.load(product, "v1");
        assertEquals(release, loadedRelease);

        try {
            loadedRelease = productReleaseDao.load(product, "v2");
            fail("EntityNotFoundException expected");
        } catch (EntityNotFoundException e) {
            // it's ok, this exception was expected
        }
    }

    /**
     * @param productDao
     *            the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * @param soDao
     *            the soDao to set
     */
    public void setSoDao(OSDao soDao) {
        this.soDao = soDao;
    }

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

}
