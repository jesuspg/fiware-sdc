package com.telefonica.euro_iaas.sdc.manager.impl;

import java.io.File;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;

/**
 * Default ProductManager implementation.
 * @author Sergio Arroyo
 *
 */
public class ProductManagerImpl extends BaseInstallableManager
	implements ProductManager {

    private ProductDao productDao;
    private ProductReleaseDao productReleaseDao;
    private OSDao osDao;

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
     * {@inheritDoc}
     */
    @Override
    public ProductRelease insert(
    		ProductRelease productRelease, File cookbook, File installable) 
    throws AlreadyExistsEntityException, InvalidEntityException {
    	
    	Product product = null;
    	ProductRelease productReleaseOut;
    	ProductRelease productReleaseIn = new ProductRelease();
    	OS os;
    	
    	uploadInstallable(installable, productRelease);
    	
    	//Insert cookbook in chef-server
    	try {
			uploadRecipe(cookbook, productRelease.getProduct().getName());
			System.out.println ("Recipe uploaded ");
		} catch (ShellCommandException e) {
			throw new SdcRuntimeException(e);
		}
    	
		
		//Insert OS in BBDD
		for (int i=0; i<productRelease.getSupportedOOSS().size(); i++) {
			try
			{ 
				os = osDao.load(productRelease.getSupportedOOSS().get(i).getName());
				System.out.println ("OS " + 
						productRelease.getSupportedOOSS().get(i).getName()  
					+ " LOADED");
			} catch (EntityNotFoundException e) {
				os = osDao.create(productRelease.getSupportedOOSS().get(i));
				System.out.println ("OS " + 
						productRelease.getSupportedOOSS().get(i).getName()  
					+ " CREATED");
			}
		}
		
		
		try
		{ 
			product = productDao.load(productRelease.getProduct().getName());
			System.out.println ("Product " + product.getName() 
					+ " LOADED");
		} catch (EntityNotFoundException e) {
			product = productDao.create(productRelease.getProduct());
			System.out.println ("Product " + product.getName() + " CREATED");
		}
		
		
		try
		{ 
			productReleaseOut =
				productReleaseDao.load(product, 
						productRelease.getVersion());
			System.out.println("ProductRelease " + product.getName() 
					+ "-" + productRelease.getVersion() + " LOADED");
		} catch (EntityNotFoundException e) {
			productReleaseIn.setProduct(product);
			productReleaseIn.setVersion(productRelease.getVersion());
			productReleaseOut = productReleaseDao.create(productReleaseIn);
			System.out.println("ProductRelease " + productReleaseIn.getProduct().getName() 
					+ "-" + productReleaseIn.getVersion() + " CREATED");
		}
		
		return productReleaseOut;
    }
    
    @Override
    public void delete(
    		ProductRelease productRelease) {
    	
    	Product product =null;
    	
    	deleteInstallable(productRelease);
    	
    	//Delete cookbook in chef-server
    	try {
			deleteRecipe(productRelease.getProduct().getName(),
					productRelease.getVersion());
			System.out.println ("Recipe DELETED ");
		} catch (ShellCommandException e) {
			throw new SdcRuntimeException(e);
		}
		
		try
		{ 
			product = productDao.load(productRelease.getProduct().getName());
			try{	
				productRelease = 
				productReleaseDao.load(product, 
							productRelease.getVersion());
				System.out.println("ProductRelease " + product.getName() 
						+ "-" + productRelease.getVersion() + " LOADED");
				productReleaseDao.remove(productRelease);
				System.out.println("ProductRelease " + product.getName() 
						+ "-" + productRelease.getVersion() + " REMOVED");
			} catch (EntityNotFoundException e) {
				System.out.println ("Product Release " + 
						productRelease.getProduct().getName() + 
						"-" + productRelease.getVersion() + " does NOT exist");
			}
		} catch (EntityNotFoundException e) {
			System.out.println ("Product " + 
					productRelease.getProduct().getName() + " does NOT exist");
		}
		
    }
    
    /**
     * @param osDao the osDao to set
     */
    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
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
