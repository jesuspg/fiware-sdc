package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
/**
 * Default ProductManager implementation.
 * @author Sergio Arroyo
 *
 */
public class ProductManagerImpl implements ProductManager {

    private ProductDao productDao;
    private ProductReleaseDao productReleaseDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findByCriteria(ProductSearchCriteria criteria) {
        return productDao.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product load(String name) throws EntityNotFoundException {
        return productDao.load(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductRelease load(Product product, String version)
            throws EntityNotFoundException {
        return productReleaseDao.load(product, version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductRelease> findReleasesByCriteria(
            ProductReleaseSearchCriteria criteria) {
        return productReleaseDao.findByCriteria(criteria);
    }


    /**
     * @param productDao the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * @param productReleaseDao the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

}
