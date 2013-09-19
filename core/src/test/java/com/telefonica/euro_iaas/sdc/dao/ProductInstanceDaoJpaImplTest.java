package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
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
    private ProductReleaseDao productReleaseDao;

    public final static String PRODUCT_NAME = "productName";
    public final static String PRODUCT_VERSION = "productVersion";

    protected void createProductRelease() throws Exception {
        ProductReleaseDaoJpaImlTest productDaoTest = new ProductReleaseDaoJpaImlTest();
        productDaoTest.setSoDao(soDao);
        productDaoTest.setProductDao(productDao);
        productDaoTest.setProductReleaseDao(productReleaseDao);
        productDaoTest.testCreateAndFindByCriteria();
    }

    /**
     * Test the create and load method
     */
    public void testCreate() throws Exception {
        createProductRelease();

        ProductRelease release = productReleaseDao.findAll().get(0);
        ProductInstance instance = new ProductInstance(release,
                Status.INSTALLED, new VM("ip", "hostname", "domain"), "vdc");

        assertEquals(0, productInstanceDao.findAll().size());
        instance = productInstanceDao.create(instance);
        assertEquals(instance, productInstanceDao.load(instance.getId()));
        assertEquals(1, productInstanceDao.findAll().size());
    }

    public void testFindByCriteria() throws Exception {
        createProductRelease();
        VM host = new VM(null, "hostname", "domain");
        VM host2 = new VM("ip");

        ProductRelease release = productReleaseDao.findAll().get(0);

        ProductInstance pi1 = new ProductInstance(release,
                Status.INSTALLED, host, "vdc");

        ProductInstance pi2 = new ProductInstance(release,
                Status.UNINSTALLED, host2, "vdc");

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
        criteria.setProduct(release);
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi1, instances.get(0));

        criteria.setVm(new VM(null, "hostname", "domain"));
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi1, instances.get(0));

        criteria.setProductName("asd");
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(0, instances.size());
        try {
           productInstanceDao.findUniqueByCriteria(criteria);
           fail("NotUniqueResultException expected");
        } catch (NotUniqueResultException e) {
            //it's ok, exception expected
        }

        criteria.setProductName(PRODUCT_NAME);
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi1, instances.get(0));
        assertEquals(pi1, productInstanceDao.findUniqueByCriteria(criteria));
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

    /**
     * @param productReleaseDao the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }


}
