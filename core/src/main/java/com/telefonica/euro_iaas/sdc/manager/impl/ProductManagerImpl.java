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

package com.telefonica.euro_iaas.sdc.manager.impl;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;

import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
import com.xmlsolutions.annotation.UseCase;


/**
 * Default ProductManager implementation.
 * 
 * @author Sergio Arroyo, Jesus M. Movilla
 */
@UseCase(traceTo = "UC_101", status = "partially implemented")
public class ProductManagerImpl extends BaseInstallableManager implements ProductManager {

    private ProductDao productDao;
    private static Logger LOGGER = Logger.getLogger("ProductManagerImpl");

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

    @Override
    public void delete(Product product) {
        productDao.remove(product);
    }

	  /**
	  * @param productDao
	  *            the productDao to set
	  */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    /* (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.manager.ProductManager#load(com.telefonica.euro_iaas.sdc.model.Product, java.lang.String)
     */
    public ProductRelease load(Product product, String version) throws EntityNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.manager.ProductManager#findReleasesByCriteria(com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria)
     */
    public List<ProductRelease> findReleasesByCriteria(ProductReleaseSearchCriteria criteria) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.manager.ProductManager#insert(com.telefonica.euro_iaas.sdc.model.ProductRelease, java.io.File, java.io.File)
     */
    public ProductRelease insert(ProductRelease productRelase, File recipes, File installable)
                    throws AlreadyExistsProductReleaseException, InvalidProductReleaseException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.manager.ProductManager#delete(com.telefonica.euro_iaas.sdc.model.ProductRelease)
     */
    public void delete(ProductRelease productRelease) throws ProductReleaseNotFoundException,
                    ProductReleaseStillInstalledException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.manager.ProductManager#update(com.telefonica.euro_iaas.sdc.model.ProductRelease, java.io.File, java.io.File)
     */
    public ProductRelease update(ProductRelease productRelease, File recipes, File installable)
                    throws ProductReleaseNotFoundException, InvalidProductReleaseException,
                    ProductReleaseNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.manager.ProductManager#insert(com.telefonica.euro_iaas.sdc.model.ProductRelease)
     */
    public ProductRelease insert(ProductRelease productRelease) throws AlreadyExistsProductReleaseException,
                    InvalidProductReleaseException {
        // TODO Auto-generated method stub
        return null;
    }

}
	
