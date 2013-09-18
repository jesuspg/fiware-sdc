package com.telefonica.euro_iaas.sdc.dao;

import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance.Status;

/**
 * Unit test for ProductInstanceDaoJpaImpl
 * @author Sergio Arroyo
 *
 */
public class ProductInstanceDaoJpaImplTest extends AbstractJpaDaoTest {

    private ProductInstanceDao productInstanceDao;
    private ProductDao productDao;
    private SODao soDao;

    public final static String PRODUCT_NAME = "productName";
    public final static String PRODUCT_VERSION = "productVersion";

    protected void createProduct() throws Exception {
        ProductDaoJpaImplTest productDaoTest = new ProductDaoJpaImplTest();
        productDaoTest.setSoDao(soDao);
        productDaoTest.setProductDao(productDao);
        productDaoTest.testCreate();
    }

    /**
     * Test the create and load method
     */
    public void testCreate() throws Exception {
        createProduct();

        ProductInstance instance = new ProductInstance(
                productDao.load(ProductDaoJpaImplTest.PRODUCT_NAME),
                Status.INSTALLED);

        assertEquals(0, productInstanceDao.findAll().size());
        instance = productInstanceDao.create(instance);
        assertEquals(instance, productInstanceDao.load(instance.getId()));
        assertEquals(1, productInstanceDao.findAll().size());
    }

    /**
     * @param productInstanceDao the productInstanceDao to set
     */
    public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }

    /**
     * @param productDao the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * @param soDao the soDao to set
     */
    public void setSoDao(SODao soDao) {
        this.soDao = soDao;
    }

}
