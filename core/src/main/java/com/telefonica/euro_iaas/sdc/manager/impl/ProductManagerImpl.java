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

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.WEBDAV_PRODUCT_BASEDIR;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Metadata;
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

    public Product insert(Product product) throws AlreadyExistsEntityException, InvalidEntityException {
              
        List<Metadata> metadatas = new ArrayList<Metadata>();
        metadatas.add(new Metadata("image","df44f62d-9d66-4dc5-b084-2d6c7bc4cfe4")); //centos6.3_sdc
        metadatas.add(new Metadata("cookbook_url",""));
        metadatas.add(new Metadata("cloud","yes"));
        metadatas.add(new Metadata("installator","chef"));
        metadatas.add(new Metadata("open_ports","80 22"));
        
        List<Metadata> defaultmetadatas = new ArrayList<Metadata>();
        defaultmetadatas.add(new Metadata("image","df44f62d-9d66-4dc5-b084-2d6c7bc4cfe4"));
        defaultmetadatas.add(new Metadata("cookbook_url",""));
        defaultmetadatas.add(new Metadata("cloud","yes"));
        defaultmetadatas.add(new Metadata("installator","chef"));
        defaultmetadatas.add(new Metadata("open_ports","80 22"));
        
        if (product.getMetadatas() != null) {
            for (Metadata external_metadata : product.getMetadatas()) {
                boolean defaultmetadata = false; 
                for (Metadata default_metadata : defaultmetadatas) {
                    if (external_metadata.getKey().equals(default_metadata.getKey())) {
                        metadatas.get(metadatas.indexOf(default_metadata))
                            .setValue(external_metadata.getValue());
                        defaultmetadata = true;
                    }
                }
                if (!defaultmetadata) {
                    metadatas.add(external_metadata);
                }
            }
        } else {
            for (Metadata default_metadata : defaultmetadatas) {
                metadatas.add(default_metadata);
            }  
        }
        
        product.setMetadatas(metadatas);
        product = productDao.create(product);
        return product;
        
    }
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
}
