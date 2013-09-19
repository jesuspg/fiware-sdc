package com.telefonica.euro_iaas.sdc.manager.impl;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationDao;
import com.telefonica.euro_iaas.sdc.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationManager;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationSearchCriteria;
import com.telefonica.euro_iaas.sdc.validation.ApplicationReleaseValidator;
import com.xmlsolutions.annotation.UseCase;
/**
 * Default ApplicationManager implementation.
 * @author Sergio Arroyo & Jesus M. Movilla
 *
 */
public class ApplicationManagerImpl extends BaseInstallableManager
implements ApplicationManager {

	private ApplicationReleaseValidator validator;
    private ApplicationDao applicationDao;
    private ApplicationReleaseDao applicationReleaseDao;
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
     */
    @Override
    @UseCase(traceTo="UC_102.1", status="implemented but not tested")
    public ApplicationRelease insert(ApplicationRelease applicationRelease, 
    	File cookbook, File installable)
    	throws AlreadyExistsApplicationReleaseException, 
    	InvalidApplicationReleaseException, ProductReleaseNotFoundException {
    	
    	validator.validateInsert(applicationRelease);
    	
    	ApplicationRelease appReleaseOut = 
    		insertApplicationReleaseBBDD (applicationRelease);
    	
    	ReleaseDto releaseDto = new ReleaseDto();
       	releaseDto.setName(applicationRelease.getApplication().getName());
       	releaseDto.setVersion(applicationRelease.getVersion());
       	releaseDto.setType("application");
       	
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
    @UseCase(traceTo="UC_102.4", status="implemented but not tested")
    public void delete(ApplicationRelease applicationRelease) 
    	throws ApplicationReleaseNotFoundException, 
    	ApplicationReleaseStillInstalledException{
    	
    	validator.validateDelete(applicationRelease);
    	
    	deleteApplicationReleaseBBDD (applicationRelease);
    	
    	ReleaseDto releaseDto = new ReleaseDto();
    	releaseDto.setName(applicationRelease.getApplication().getName());
    	releaseDto.setVersion(applicationRelease.getVersion());
    	releaseDto.setType("application");
    	
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
    @UseCase(traceTo="UC_102.3", status="not implemented")
    public ApplicationRelease update(ApplicationRelease applicationRelease, 
    	File cookbook, File installable) 
     	throws ApplicationReleaseNotFoundException, 
     	InvalidApplicationReleaseException,	ProductReleaseNotFoundException {
    	
    	if (applicationRelease != null)
    		applicationRelease = updateApplicationReleaseBBDD (applicationRelease);
    	
       	ReleaseDto releaseDto = new ReleaseDto();
       	releaseDto.setName(applicationRelease.getApplication().getName());
       	releaseDto.setVersion(applicationRelease.getVersion());
       	releaseDto.setType("application");
       	
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
		InvalidApplicationReleaseException {
	
    	Application application;
    	ApplicationRelease applicationReleaseOut;
	
    	application = insertApplicationReleaseLoadApplication (applicationRelease);
    	applicationReleaseOut = insertApplicationRelease(applicationRelease, 
    			application);
    	
    	return applicationReleaseOut;
    }

    private void deleteApplicationReleaseBBDD (
    		ApplicationRelease applicationRelease){
   		applicationReleaseDao.remove(applicationRelease);
    }
    
    private ApplicationRelease updateApplicationReleaseBBDD (
        ApplicationRelease applicationRelease) 
    	throws ApplicationReleaseNotFoundException, InvalidApplicationReleaseException {
    	
        Application application;
        ApplicationRelease applicationReleaseOut;
        
        application = uploadApplicationReleaseLoadApplication (applicationRelease);
        
        applicationReleaseOut = uploadApplicationRelease (applicationRelease);
        
        return applicationReleaseOut;
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
    	ApplicationRelease applicationRelease, Application application)
		throws InvalidApplicationReleaseException,
		AlreadyExistsApplicationReleaseException{
    	
    	ApplicationRelease applicationReleaseOut;
    	
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
        	applicationRelease.setApplication(application);
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
    	throws InvalidApplicationReleaseException{
    	
    	ApplicationRelease applicationReleaseOut;
    	
        try { 
        	applicationReleaseOut =
        		applicationReleaseDao.update(applicationRelease);	
        	LOGGER.log(Level.INFO, "ApplicationRelease " 
        			+ applicationRelease.getApplication().getName() 
    			+ "-" + applicationRelease.getVersion() + " UPLOADED");
        } catch (InvalidEntityException e) {
        	String invalidEntityMessageLog = "The Application Release"
        		+ applicationRelease.getApplication().getName() + 
        		" version " + applicationRelease.getVersion () + 
        		" is Invalid ";
        	
        	LOGGER.log (Level.SEVERE, invalidEntityMessageLog);
        	throw new InvalidApplicationReleaseException (
        			invalidEntityMessageLog, e);
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
     * @param validator the validator to set
     */
    public void setValidator(ApplicationReleaseValidator validator) {
        this.validator = validator;
    }
}
