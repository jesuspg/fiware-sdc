package com.telefonica.euro_iaas.sdc.manager.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationDao;
import com.telefonica.euro_iaas.sdc.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationManager;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.validation.ApplicationReleaseValidator;
import com.xmlsolutions.annotation.UseCase;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.WEBDAV_APPLICATION_BASEDIR;


/**
 * Default ApplicationManager implementation.
 * @author Sergio Arroyo & Jesus M. Movilla
 *
 */
public class ApplicationManagerImpl extends BaseInstallableManager
implements ApplicationManager {

	//private SystemPropertiesProvider propertiesProvider;
	private ApplicationReleaseValidator validator;
    private ApplicationDao applicationDao;
    private ApplicationReleaseDao applicationReleaseDao;
    private ProductReleaseDao productReleaseDao;
    private ProductDao productDao;
    private static Logger LOGGER = Logger.getLogger("ApplicationManagerImpl");

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Application> findAll() {
        return applicationDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Application> findByCriteria(ApplicationSearchCriteria criteria) {
        return applicationDao.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationRelease load(Application application, String version)
            throws EntityNotFoundException {
        return applicationReleaseDao.load(application, version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationRelease> findReleasesByCriteria(
            ApplicationReleaseSearchCriteria criteria) {
        return applicationReleaseDao.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Application load(String name) throws EntityNotFoundException {
        return applicationDao.load(name);
    }
    
    /**
     * {@inheritDoc}
     * @throws InvalidProductReleaseException 
     */
    @Override
    @UseCase(traceTo="UC_102.1", status="implemented and tested")
    public ApplicationRelease insert(ApplicationRelease applicationRelease, 
    	File cookbook, File installable)
    	throws AlreadyExistsApplicationReleaseException, 
    	InvalidApplicationReleaseException, ProductReleaseNotFoundException, 
    	InvalidProductReleaseException {
    	
    	validator.validateInsert(applicationRelease);
    	
    	ApplicationRelease appReleaseOut = 
    		insertApplicationReleaseBBDD (applicationRelease);
    	
    	ReleaseDto releaseDto = new ReleaseDto();
       	releaseDto.setName(applicationRelease.getApplication().getName());
       	releaseDto.setVersion(applicationRelease.getVersion());
       	releaseDto.setType(propertiesProvider.getProperty(WEBDAV_APPLICATION_BASEDIR));
       	
    	uploadInstallable(installable, releaseDto);
    	
    	uploadRecipe(cookbook, releaseDto.getName());
			
    	return appReleaseOut;	
    }
    
    /**
     * {@inheritDoc}
     * @throws ProductReleaseStillInstalledException 
     * @throws ApplicationReleaseStillInstalledException
     */
    @Override
    @UseCase(traceTo="UC_102.4", status="implemented and tested")
    public void delete(ApplicationRelease applicationRelease) 
    	throws ApplicationReleaseNotFoundException, 
    	ApplicationReleaseStillInstalledException{
    	
    	validator.validateDelete(applicationRelease);
    	
    	deleteApplicationReleaseBBDD (applicationRelease);
    	
    	ReleaseDto releaseDto = new ReleaseDto();
    	releaseDto.setName(applicationRelease.getApplication().getName());
    	releaseDto.setVersion(applicationRelease.getVersion());
    	releaseDto.setType(propertiesProvider.getProperty(WEBDAV_APPLICATION_BASEDIR));
    	
    	deleteInstallable(releaseDto);
    	
    	deleteRecipe(releaseDto.getName(), releaseDto.getVersion());
    }
    
    
    /**
     * {@inheritDoc}
	 * @throws AlreadyExistsApplicationReleaseException
     * @throws InvalidApplicationReleaseException
     * @throws ProductReleaseNotFoundException 
     */
    @Override
    @UseCase(traceTo="UC_102.3", status="implemented")
    public ApplicationRelease update(ApplicationRelease applicationRelease, 
    	File cookbook, File installable) 
     	throws ApplicationReleaseNotFoundException, 
     	InvalidApplicationReleaseException,	ProductReleaseNotFoundException {
    	
    	if (applicationRelease != null)
    		applicationRelease = updateApplicationReleaseBBDD (applicationRelease);
    	
       	ReleaseDto releaseDto = new ReleaseDto();
       	releaseDto.setName(applicationRelease.getApplication().getName());
       	releaseDto.setVersion(applicationRelease.getVersion());
       	releaseDto.setType(propertiesProvider.getProperty(WEBDAV_APPLICATION_BASEDIR));
       	
    	if (installable != null)
    		uploadInstallable(installable, releaseDto);
    	
    	if (cookbook != null)
        	uploadRecipe(cookbook, releaseDto.getName());
    	
    	
    	return applicationRelease;
    }
    
    // ****************		PRIVATE METHODS 		**************************//
    //***** BBDD *****//
    private ApplicationRelease insertApplicationReleaseBBDD (
    	ApplicationRelease applicationRelease)
		throws AlreadyExistsApplicationReleaseException, 
		InvalidApplicationReleaseException,
		ProductReleaseNotFoundException, InvalidProductReleaseException {
	
    	Application application;
    	ApplicationRelease applicationReleaseOut;
    	
    	List<ProductRelease> productReleases =  loadSupportedProductRelease (
    			applicationRelease);
    	applicationRelease.setSupportedProducts(productReleases);
    	
    	application = insertApplicationReleaseLoadApplication (applicationRelease);
    	applicationRelease.setApplication(application);
    	
    	applicationReleaseOut = insertApplicationRelease(applicationRelease);
    	
    	return applicationReleaseOut;
    }

    private void deleteApplicationReleaseBBDD (
    		ApplicationRelease applicationRelease){
   		applicationReleaseDao.remove(applicationRelease);
    }
    
    private ApplicationRelease updateApplicationReleaseBBDD (
        ApplicationRelease applicationRelease) 
    	throws ApplicationReleaseNotFoundException, InvalidApplicationReleaseException,
    	ProductReleaseNotFoundException {
    	
    	ApplicationRelease applicationReleaseOut;
    	Application application;
    	
    	if (applicationRelease.getSupportedProducts()!=null)
    	{
    		List<ProductRelease> productReleases 
    			= updateApplicationReleaseLoadProductRelease (applicationRelease);
    		
    		applicationRelease.setSupportedProducts(productReleases);
    	}
    	
    	application = uploadApplicationReleaseLoadApplication (applicationRelease);
    	
    	applicationRelease.setApplication(application);
    	applicationReleaseOut = uploadApplicationRelease (applicationRelease);
    	
    	return applicationReleaseOut;
    }
    
    private List<ProductRelease> loadSupportedProductRelease (
    	ApplicationRelease applicationRelease) 
    	throws ProductReleaseNotFoundException, InvalidProductReleaseException {
    	
    	String productNotFoundMessage = null;
    	ProductRelease productRelease;
    	Product product;
    	List<ProductRelease> productReleases  = new ArrayList<ProductRelease>();
    	
		for (int i=0; i<applicationRelease.getSupportedProducts().size(); i++) {
			try { 
				product = productDao.
						load(applicationRelease.getSupportedProducts().get(i).getProduct().getName());
				productNotFoundMessage = "Product " + 
						applicationRelease.getSupportedProducts().get(i)
						.getProduct().getName()	+ " LOADED";
				
				LOGGER.log(Level.INFO,productNotFoundMessage);
			} catch (EntityNotFoundException e) {
				throw new ProductReleaseNotFoundException(productNotFoundMessage,e);
			}
			
			try {
				productRelease = productReleaseDao.load(product, 
						applicationRelease.getSupportedProducts().get(i).getVersion());
				
				LOGGER.log(Level.INFO,"ProductRelease " + 
					applicationRelease.getSupportedProducts().get(i)
					.getProduct().getName()  + "-" + 
					applicationRelease.getSupportedProducts().get(i).getVersion()
					+ " LOADED");
				
				productReleases.add(productRelease);
			
			} catch (EntityNotFoundException e) {
					String productReleaseNotFoundMessage = "Product Release " + 
						applicationRelease.getSupportedProducts().get(i).
						getProduct().getName()  + "-" +
						applicationRelease.getSupportedProducts().get(i).getVersion()
						+ " has not been Found in the system";
					
					LOGGER.log(Level.INFO,productReleaseNotFoundMessage);
					throw new ProductReleaseNotFoundException (
							productReleaseNotFoundMessage,e);
			}
		}
		return productReleases;
    	
    }
    
    private List<ProductRelease> 
    	updateApplicationReleaseLoadProductRelease (ApplicationRelease applicationRelease) 
    	throws ProductReleaseNotFoundException {
        
    	List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
    	ProductRelease productRelease;
    	Product product;
    	
    	for (int i=0; i < applicationRelease.getSupportedProducts().size(); i++){
    		try {
				product = productDao.
					load(applicationRelease.getSupportedProducts().get(i).getProduct().getName());
				LOGGER.log(Level.INFO,"Product " + 
					applicationRelease.getSupportedProducts().get(i).getProduct().getName() 
					+ " LOADED");
			} catch (EntityNotFoundException e) {
				String productReleaseNotFoundMessageLog = "Product " +
					applicationRelease.getSupportedProducts().get(i).getProduct().getName()
					+ " not found in the system";
				throw new ProductReleaseNotFoundException(productReleaseNotFoundMessageLog,e);
			}
    		try {
				productRelease = productReleaseDao.
					load(product, applicationRelease.getSupportedProducts().get(i).getVersion());
				
				LOGGER.log(Level.INFO,"Product Release" + 
					product.getName() + "-" +  
					applicationRelease.getSupportedProducts().get(i).getVersion()
					+ " LOADED");
				productReleases.add(productRelease);
				
			} catch (EntityNotFoundException e) {
				String productReleaseNotFoundMessageLog = "Product " +
					applicationRelease.getSupportedProducts().get(i).getProduct().getName()
					+ " not found in the system";
				throw new ProductReleaseNotFoundException(productReleaseNotFoundMessageLog,e);
			}	

    	}
    	
    	return productReleases;
    }
    
    private Application insertApplicationReleaseLoadApplication (
    	ApplicationRelease applicationRelease)
		throws InvalidApplicationReleaseException {
    	
    	Application application;
    	
    	try { 
    		application = applicationDao.load(
				applicationRelease.getApplication().getName());
    		LOGGER.log(Level.INFO, "Application " + application.getName() + " LOADED");
    	} catch (EntityNotFoundException e) {
    		try {
    			application = applicationDao.create(applicationRelease.getApplication());
    			LOGGER.log(Level.INFO, "Application " + application.getName() + " CREATED");
    		} catch (InvalidEntityException e1) {
    			String invalidApplicationMessageLog = "The Application " + 
					applicationRelease.getApplication().getName() +
					" in Application Release" +
					applicationRelease.getApplication().getName() +
					applicationRelease.getVersion() +
					" is invalid. Please Insert a valid Product ";			
    			LOGGER.log(Level.SEVERE, invalidApplicationMessageLog);
    			throw new InvalidApplicationReleaseException (
    					invalidApplicationMessageLog ,e1);
    		} catch (AlreadyExistsEntityException e1) {
    			LOGGER.log(Level.SEVERE, e1.getMessage());
    			throw new SdcRuntimeException (e1);
    		}
    	}
    	return application;
    }
    
    private ApplicationRelease insertApplicationRelease(
    	ApplicationRelease applicationRelease)
		throws InvalidApplicationReleaseException,
		AlreadyExistsApplicationReleaseException{
    	
    	ApplicationRelease applicationReleaseOut;
    	
    	try {
			applicationReleaseOut = applicationReleaseDao
					.load(applicationRelease.getApplication(),
							applicationRelease.getVersion());
			LOGGER.log(Level.INFO, "ApplicationRelease " + 
					applicationRelease.getApplication().getName() 
				+ "-" + applicationRelease.getVersion() + " LOADED");
		} catch (EntityNotFoundException e) {
			try {
				applicationReleaseOut = applicationReleaseDao.create(applicationRelease);
				LOGGER.log(Level.INFO, "ApplicationRelease " + 
						applicationRelease.getApplication().getName() 
					+ "-" + applicationRelease.getVersion() + " CREATED");
			} catch (InvalidEntityException e1) {
				String invalidApplicationReleaseMessageLog = 
					"The Application Release " + 
					applicationRelease.getApplication().getName() +
					applicationRelease.getVersion() +
					" is invalid. Please Insert a valid Application Release";
				
				LOGGER.log(Level.SEVERE, invalidApplicationReleaseMessageLog);
				throw new InvalidApplicationReleaseException (
						invalidApplicationReleaseMessageLog,e1);
			} catch (AlreadyExistsEntityException e1) {
				LOGGER.log(Level.SEVERE, e1.getMessage());
				throw new AlreadyExistsApplicationReleaseException (
					"The Application Release " + 
					applicationRelease.getApplication().getName() +
					applicationRelease.getVersion() + " already exist" ,e1);
			}
		}
    	
    	return applicationReleaseOut;
    }
    
    private Application uploadApplicationReleaseLoadApplication (
       	ApplicationRelease applicationRelease)
    	throws  ApplicationReleaseNotFoundException{
    	
    	Application application;
        try { 
        	application = applicationDao.load(
    			applicationRelease.getApplication().getName());
        	LOGGER.log(Level.INFO, "Application " + application.getName() + " LOADED");
        } catch (EntityNotFoundException e) {
        	String entityNotFoundMessageLog = "The Application"
        		+ applicationRelease.getApplication().getName() + 
        		"has not been found in the System ";
        	
        	LOGGER.log (Level.SEVERE, entityNotFoundMessageLog);
        	throw new ApplicationReleaseNotFoundException (
        			entityNotFoundMessageLog, e);
        }
        return application;
    }
    
    private ApplicationRelease uploadApplicationRelease (
    	ApplicationRelease applicationRelease)
    	throws ApplicationReleaseNotFoundException,
    	InvalidApplicationReleaseException{
    	
    	ApplicationRelease applicationReleaseOut, existedApplicationRelease;
		try
		{ 
			LOGGER.log(Level.INFO, "Application Release Before Loading " + 
					applicationRelease.getApplication().getName() 
	    			+ "-" + applicationRelease.getVersion());
			
			existedApplicationRelease = applicationReleaseDao
					.load(applicationRelease.getApplication(), applicationRelease.getVersion());
			
			LOGGER.log(Level.INFO, "Application Release " + 
					applicationRelease.getApplication().getName() 
	    			+ "-" + applicationRelease.getVersion() + " LOADED");
			
			if ( applicationRelease.getPrivateAttributes() != null )
				existedApplicationRelease.setPrivateAttributes(applicationRelease.getPrivateAttributes());
			if ( applicationRelease.getReleaseNotes() != null )
				existedApplicationRelease.setReleaseNotes(applicationRelease.getReleaseNotes());
			if ( applicationRelease.getSupportedProducts() != null )
				existedApplicationRelease.setSupportedProducts(applicationRelease.getSupportedProducts());
			if ( applicationRelease.getTransitableReleases() != null )
				existedApplicationRelease.setTransitableReleases(applicationRelease.getTransitableReleases());
			
			applicationReleaseOut =
					applicationReleaseDao.update(existedApplicationRelease);
			LOGGER.log(Level.INFO, "existedApplicationRelease " + 
					applicationRelease.getApplication().getName() 
	    			+ "-" + applicationRelease.getVersion() + " UPDATED");
			
		} catch (EntityNotFoundException e ) {
			String entityNotFoundMessageLog = "The Application Release"
		        	+ applicationRelease.getApplication().getName() + "-"
		        	+ applicationRelease.getVersion() + 					
		        	" has not been found in the System ";
			LOGGER.log (Level.SEVERE, entityNotFoundMessageLog);
			throw new ApplicationReleaseNotFoundException (
					entityNotFoundMessageLog, e); 
			
    	}catch (InvalidEntityException e) {
	    	String invalidEntityException = "The Application Release"
	    		+ applicationRelease.getApplication().getName() + 
	        	" version " + applicationRelease.getVersion () + 
	        	" is Invalid ";
	    	
	    	LOGGER.log (Level.SEVERE, invalidEntityException);
	        throw new InvalidApplicationReleaseException (
	        		invalidEntityException, e);   
	    }
	    return applicationReleaseOut;
         
    }
    /**
     * @param productDao the productDao to set
     */
    public void setApplicationDao(ApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    /**
     * @param applicationReleaseDao the applicationReleaseDao to set
     */
    public void setApplicationReleaseDao(ApplicationReleaseDao applicationReleaseDao) {
        this.applicationReleaseDao = applicationReleaseDao;
    }

    /**
     * @param applicationReleaseDao the applicationReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }
    
    /**
     * @param applicationReleaseDao the productReleaseDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }
    
    /**
     * @param validator the validator to set
     */
    public void setValidator(ApplicationReleaseValidator validator) {
        this.validator = validator;
    }
}
