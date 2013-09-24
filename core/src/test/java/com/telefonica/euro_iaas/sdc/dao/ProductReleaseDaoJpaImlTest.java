package com.telefonica.euro_iaas.sdc.dao;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
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
