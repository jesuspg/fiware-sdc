package com.telefonica.euro_iaas.sdc.dao;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;

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

   protected void createProductRelease() throws Exception  {

        ProductRelease productRelease = new ProductRelease();
        productRelease.setVersion("0.1.1");
        productRelease.setReleaseNotes("prueba ReelaseNotes");

        OS so = new OS("Prueba I", "Prueba I Description","Prueba I Version");
        try{
            so = soDao.load(so.getName());
            System.out.println("The OS " + so.getName()
                    + " already exists");
        } catch (EntityNotFoundException e)
        {
            System.out.println("The Product " +so.getName()
                + " does not exist");
            so = soDao.create(so);
        }

        List<OS> supportedOOSS =  Arrays.asList(so);
        productRelease.setSupportedOOSS(supportedOOSS);

        Product product = new Product();
        product.setName("yum");
        product.setDescription("yum description");
        try{
            product = productDao.load(product.getName());
            System.out.println("The Product " +product.getName()
                    + " already exists");
        } catch (EntityNotFoundException e)
        {
            System.out.println("The Product " +product.getName()
                    + " does not exist");
            product = productDao.create(product);
        }
        productRelease.setProduct(product);

        Attribute privateAttribute = new Attribute("ssl_port",
        "8443", "The ssl listen port");
        Attribute privateAttributeII = new Attribute("port",
         "8080", "The listen port");

        List<Attribute> privateAttributes =
            Arrays.asList(privateAttribute, privateAttributeII);
        productRelease.setPrivateAttributes(privateAttributes);

        ProductRelease createdRelease = productReleaseDao.create(productRelease);

        Assert.assertEquals(createdRelease, productRelease);

    }

    public void testCreateAndFindByCriteria() throws Exception {
        createProduct();
        Product product = productDao.findAll().get(0);

        ProductRelease release =
            new ProductRelease("v1", "releaseNotes1", null, product,
                    soDao.findAll(), null);
        ProductRelease createdRelease = productReleaseDao.create(release);
        assertEquals(createdRelease, release);

        ProductReleaseSearchCriteria criteria =
            new ProductReleaseSearchCriteria();
        assertEquals(1, productReleaseDao.findByCriteria(criteria).size());

        ProductRelease loadedRelease = productReleaseDao.load(product, "v1");
        assertEquals(release, loadedRelease);

        try {
            loadedRelease = productReleaseDao.load(product, "v2");
            fail("EntityNotFoundException expected");
        } catch (EntityNotFoundException e) {
            //it's ok, this exception was expected
        }
    }

//    public void testDuplicateProductRelease() throws Exception{
//        createProductRelease();
//        try {
//            createProductRelease();
//            fail("AlreadyExistsEntityException expected");
//        } catch (AlreadyExistsEntityException e) {
//            //it's ok, this exception was expected
//        }
//    }


    /**
     * @param productDao the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * @param soDao the soDao to set
     */
    public void setSoDao(OSDao soDao) {
        this.soDao = soDao;
    }

    /**
     * @param productReleaseDao the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }


}
