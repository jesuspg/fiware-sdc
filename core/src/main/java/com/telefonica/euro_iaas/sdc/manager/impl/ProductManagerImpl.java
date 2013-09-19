package com.telefonica.euro_iaas.sdc.manager.impl;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
import com.telefonica.euro_iaas.sdc.validation.ProductReleaseValidator;
import com.xmlsolutions.annotation.UseCase;

/**
 * Default ProductManager implementation.
 * @author Sergio Arroyo, Jesus M. Movilla
 *
 */
@UseCase(traceTo="UC_101", status="partially implemented")
public class ProductManagerImpl extends BaseInstallableManager
	implements ProductManager {
	
	private ProductReleaseValidator validator;
    private ProductDao productDao;
    private ProductReleaseDao productReleaseDao;
    private OSDao osDao;
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
    @UseCase(traceTo="UC_101.1", status="implemented")
    public ProductRelease insert(ProductRelease productRelease, File cookbook, 
    	File installable) throws AlreadyExistsProductReleaseException, 
    	InvalidProductReleaseException {
    	
    	ProductRelease productReleaseOut = 
    		insertProductReleaseBBDD (productRelease);
    	
    	ReleaseDto releaseDto = new ReleaseDto();
    	releaseDto.setName(productRelease.getProduct().getName());
    	releaseDto.setVersion(productRelease.getVersion());
    	
    	releaseDto.setType("product");
    	
    	uploadInstallable(installable, releaseDto);
    	
    	uploadRecipe(cookbook, releaseDto.getName());
			
		return productReleaseOut;
    }
    
    /**
     * {@inheritDoc}
     * @throws ProductReleaseStillInstalledException 
     */
    @Override
    @UseCase(traceTo="UC_101.3", status="implemented")
    public void delete(ProductRelease productRelease) 
    	throws ProductReleaseNotFoundException, 
    	ProductReleaseStillInstalledException{
    	
    	validator.validateDelete(productRelease);
    	
    	ReleaseDto releaseDto = new ReleaseDto();
    	releaseDto.setName(productRelease.getProduct().getName());
    	releaseDto.setVersion(productRelease.getVersion());
    	releaseDto.setType("product");
    	
    	deleteProductReleaseBBDD (productRelease);
    	
    	deleteInstallable(releaseDto);
    	
    	deleteRecipe(releaseDto.getName(),
    			releaseDto.getVersion());
    }
    
    /**
     * {@inheritDoc}
	 * @throws AlreadyExistsApplicationReleaseException
     * @throws InvalidApplicationReleaseException
     * @throws ProductReleaseNotFoundException 
     */
    @Override
    @UseCase(traceTo="UC_101.3", status="implemented not tested")
    public ProductRelease update(ProductRelease productRelease, File cookbook, 
    	File installable) throws ProductReleaseNotFoundException, 
		InvalidProductReleaseException, ProductReleaseNotFoundException {
    	
    	if (productRelease != null)
    		productRelease = updateProductReleaseBBDD (productRelease);
    	
    	ReleaseDto releaseDto = new ReleaseDto();
    	releaseDto.setName(productRelease.getProduct().getName());
    	releaseDto.setVersion(productRelease.getVersion());
    	
    	if (installable != null)    		
    		uploadInstallable(installable, releaseDto);
    	
    	if (cookbook != null)
        	uploadRecipe(cookbook, releaseDto.getName());
    	
    	return productRelease;
    }
    // *********** PRIVATE METHODS ************//
    private ProductRelease insertProductReleaseBBDD (ProductRelease productRelease)
    	throws AlreadyExistsProductReleaseException, 
    	InvalidProductReleaseException {
    	
    	Product product;
    	ProductRelease productReleaseOut;
    	   	
    	insertProductReleaseLoadSO (productRelease);
    	
    	product = insertProductReleaseLoadProduct (productRelease);
    	
    	productReleaseOut = insertProductRelease (productRelease, product);
		
		return productReleaseOut;
    }
    
    private void deleteProductReleaseBBDD (ProductRelease productRelease)
    	throws ProductReleaseNotFoundException{
   	
    	productReleaseDao.remove(productRelease);	
    }
    
    private ProductRelease updateProductReleaseBBDD (
    	ProductRelease productRelease) throws ProductReleaseNotFoundException, 
    	InvalidProductReleaseException {
        	
    	ProductRelease productReleaseOut;
    	
    	updateProductReleaseLoadSO (productRelease);
    	
    	updateProductReleaseLoadProduct(productRelease);
    	
    	productReleaseOut = updateProductRelease (productRelease);
		
		return productReleaseOut;
    }
    
    private void insertProductReleaseLoadSO (ProductRelease productRelease) 
    	throws InvalidProductReleaseException {
    	
    	OS os;
    	
		for (int i=0; i<productRelease.getSupportedOOSS().size(); i++) {
			try { 
				os = osDao.load(productRelease.getSupportedOOSS().get(i).getName());
				LOGGER.log(Level.INFO,"OS " + 
						productRelease.getSupportedOOSS().get(i).getName()  
					+ " LOADED");
			} catch (EntityNotFoundException e) {
				try {
					os = osDao.create(productRelease.getSupportedOOSS().get(i));
					LOGGER.log(Level.INFO,"OS " + 
							productRelease.getSupportedOOSS().get(i).getName()  
						+ " CREATED");
				} catch (InvalidEntityException e1) {
					String invalidOSMessageLog = "The supportedOS " + 
						productRelease.getSupportedOOSS().get(i).getName() +
						" in Product Release" +
						productRelease.getProduct().getName() +
						productRelease.getVersion() +
						" is invalid. Please Insert a valid OS";
					
					LOGGER.log(Level.SEVERE, invalidOSMessageLog);
					throw new InvalidProductReleaseException (
						invalidOSMessageLog,e1);
					
				} catch (AlreadyExistsEntityException e1) {
					LOGGER.log(Level.SEVERE, e1.getMessage());
					throw new SdcRuntimeException (e1);
				}
			}
		}
		
    }
    
    private Product insertProductReleaseLoadProduct (ProductRelease productRelease) 
		throws InvalidProductReleaseException {
    	
    	Product product;
    	//Insert Product if needed
		try { 
			product = productDao.load(productRelease.getProduct().getName());
			LOGGER.log(Level.INFO, "Product " + product.getName() + " LOADED");
		} catch (EntityNotFoundException e) {
			try {
				product = productDao.create(productRelease.getProduct());
				LOGGER.log(Level.INFO, "Product " + product.getName() + " CREATED");
			} catch (InvalidEntityException e1) {
				String messageLog = "The Product " + 
					productRelease.getProduct().getName() +
					" in Product Release" +
					productRelease.getProduct().getName() +
					productRelease.getVersion() +
					" is invalid. Please Insert a valid Product ";
				
				LOGGER.log(Level.SEVERE, messageLog);
				throw new InvalidProductReleaseException (messageLog,e1);
				
			} catch (AlreadyExistsEntityException e1) {
				LOGGER.log(Level.SEVERE, e1.getMessage());
				throw new SdcRuntimeException (e1);
			}
		}
		return product;
    }
    
    private ProductRelease insertProductRelease (ProductRelease productRelease,
    	Product product) throws InvalidProductReleaseException,
    	AlreadyExistsProductReleaseException{
    	
    	ProductRelease productReleaseOut;
    	//This only works if the @uniqueconstraint  annotation 
    	try {
    		productReleaseOut = productReleaseDao.create(productRelease);
			LOGGER.log(Level.INFO, "ProductRelease " + 
					productRelease.getProduct().getName() 
					+ "-" + productRelease.getVersion() + " CREATED");
		} catch (InvalidEntityException e1){
			String invalidEntityMessageLog = "The Product Release " + 
				productRelease.getProduct().getName() +
				productRelease.getVersion() +
				" is invalid. Please Insert a valid Product Release";
			
			LOGGER.log(Level.SEVERE, invalidEntityMessageLog);
			throw new InvalidProductReleaseException (
					invalidEntityMessageLog ,e1);
			
		} catch (AlreadyExistsEntityException e1) {
			String alreadyExistsMessageLog = "The Product Release " + 
				productRelease.getProduct().getName() +
				productRelease.getVersion() + " already exist";
			
			LOGGER.log(Level.SEVERE, alreadyExistsMessageLog);
			throw new AlreadyExistsProductReleaseException (
					alreadyExistsMessageLog,e1);
		}
    	return productReleaseOut;
    }
    
    private void updateProductReleaseLoadSO (ProductRelease productRelease)
    	throws ProductReleaseNotFoundException{
    	
    	OS os;
    	for (int i=0; i<productRelease.getSupportedOOSS().size(); i++) {
			try { 
				os = osDao.load(productRelease.getSupportedOOSS().get(i).getName());
				LOGGER.log(Level.INFO,"OS " + 
						productRelease.getSupportedOOSS().get(i).getName()  
					+ " LOADED");
			} catch (EntityNotFoundException e) {
				String entityNotFoundMessageLog = "The OS "
		        	+ productRelease.getSupportedOOSS().get(i).getName() + 
		        	" has not been found in the System ";
				
				LOGGER.log (Level.SEVERE, entityNotFoundMessageLog);
		        throw new ProductReleaseNotFoundException (
		        		entityNotFoundMessageLog, e);
			}
		}
    }
    
    private void updateProductReleaseLoadProduct(ProductRelease productRelease)
    	throws ProductReleaseNotFoundException{
    	
    	Product product = null;
    	
		try { 
			product = productDao.load(productRelease.getProduct().getName());
			LOGGER.log(Level.INFO, "Product " + product.getName() + " LOADED");
		} catch (EntityNotFoundException e) {
			String entityNotFoundMessageLog = "The Product "
	        	+ productRelease.getProduct().getName() + 
	        	" has not been found in the System ";
			
			LOGGER.log (Level.SEVERE, entityNotFoundMessageLog);
			throw new ProductReleaseNotFoundException (entityNotFoundMessageLog, e);
		}
    }
    
    private ProductRelease updateProductRelease (ProductRelease productRelease)
     throws InvalidProductReleaseException {
    	
    	ProductRelease productReleaseOut;
    	
		try
		{ 
			productReleaseOut =
				productReleaseDao.update(productRelease);
			LOGGER.log(Level.INFO, "ProductRelease " + 
					productRelease.getProduct().getName() 
	    			+ "-" + productRelease.getVersion() + " UPLOADED");
	    } catch (InvalidEntityException e) {
	    	String invalidEntityException = "The Product Release"
	    		+ productRelease.getProduct().getName() + 
	        	" version " + productRelease.getVersion () + 
	        	" is Invalid ";
	    	
	    	LOGGER.log (Level.SEVERE, invalidEntityException);
	        throw new InvalidProductReleaseException (
	        		invalidEntityException, e);   
	    }
	    return productReleaseOut;
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

    /**
     * @param validator the validator to set
     */
    public void setValidator(ProductReleaseValidator validator) {
        this.validator = validator;
    }
}
