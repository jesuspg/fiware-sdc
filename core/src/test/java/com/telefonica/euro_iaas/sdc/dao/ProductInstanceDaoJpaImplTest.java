package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;

/**
 * Unit test for ProductInstanceDaoJpaImpl
 * @author Sergio Arroyo
 *
 */
public class ProductInstanceDaoJpaImplTest extends AbstractJpaDaoTest {

    private ProductInstanceDao productInstanceDao;
    private ProductDao productDao;
    private OSDao soDao;

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
                Status.INSTALLED, new VM("ip", "hostname", "domain"));

        assertEquals(0, productInstanceDao.findAll().size());
        instance = productInstanceDao.create(instance);
        assertEquals(instance, productInstanceDao.load(instance.getId()));
        assertEquals(1, productInstanceDao.findAll().size());
    }

    public void testFindByCriteria() throws Exception {
        createProduct();
        VM host = new VM(null, "hostname", "domain");
        VM host2 = new VM("ip");

        ProductInstance pi1 = new ProductInstance(
                productDao.load(ProductDaoJpaImplTest.PRODUCT_NAME),
                Status.INSTALLED, host);

        ProductInstance pi2 = new ProductInstance(
                productDao.load(ProductDaoJpaImplTest.PRODUCT_NAME),
                Status.UNINSTALLED, host2);

        pi1 = productInstanceDao.create(pi1);
        pi2 = productInstanceDao.create(pi2);


        ProductInstanceSearchCriteria criteria =
            new ProductInstanceSearchCriteria();
        //find all
        List<ProductInstance> instances =
            productInstanceDao.findByCriteria(criteria);
        assertEquals(2, instances.size());
        //find by Host1
        criteria.setVM(host);
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi1, instances.get(0));

        //find by Host2
        criteria.setVM(host2);
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi2, instances.get(0));

        //find by Status
        criteria.setVM(null);
        criteria.setStatus(Status.INSTALLED);
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi1, instances.get(0));

        criteria.setVm(host);
        criteria.setProduct(
                productDao.load(ProductDaoJpaImplTest.PRODUCT_NAME));
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi1, instances.get(0));

        criteria.setVm(new VM(null, "hostname", "domain"));
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi1, instances.get(0));
    }

    public void testFindByHost() {

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
    public void setSoDao(OSDao soDao) {
        this.soDao = soDao;
    }

}
