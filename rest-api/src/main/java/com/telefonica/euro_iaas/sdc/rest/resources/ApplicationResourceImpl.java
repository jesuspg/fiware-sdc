package com.telefonica.euro_iaas.sdc.rest.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationManager;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationSearchCriteria;
import com.telefonica.euro_iaas.sdc.rest.validation.ApplicationResourceValidator;

/**
 * default ApplicationResource implementation
 * @author Sergio Arroyo
 *
 */
@Path("/catalog/application")
@Component
@Scope("request")
public class ApplicationResourceImpl implements ApplicationResource {

    @InjectParam("applicationManager")
    private ApplicationManager applicationManager;
    
    private ApplicationResourceValidator validator;
    private static Logger LOGGER = Logger.getLogger("ApplicationResourceImpl");
    
    /**
     * {@inheritDoc}
     * @throws InvalidApplicationReleaseUpdateRequestException 
     * @throws InvalidMultiPartRequestException 
     */
    @Override
    public ApplicationRelease insert(MultiPart multiPart)
    	throws AlreadyExistsApplicationReleaseException, 
    	InvalidApplicationReleaseException, ProductReleaseNotFoundException, 
    	InvalidMultiPartRequestException {
    	
    	validator.validateInsert(multiPart);
    	
    	File cookbook = null;
    	File installable = null;
    	    	
    	// First part contains a ApplicationReleaseDto object
    	ApplicationReleaseDto applicationReleaseDto =
   			(ApplicationReleaseDto)multiPart.getBodyParts().get(0).getEntity();
    	       
    	Application application = new Application (
    			applicationReleaseDto.getApplicationName(),
    			applicationReleaseDto.getApplicationDescription(),
    			applicationReleaseDto.getApplicationType());
    	
    	for (int i=0; applicationReleaseDto.getPrivateAttributes().size() < i; i++)
    		application.addAttribute(applicationReleaseDto.getPrivateAttributes().get(i));
		
		ApplicationRelease applicationRelease = new ApplicationRelease(
				applicationReleaseDto.getVersion(), 
				applicationReleaseDto.getReleaseNotes(),
				applicationReleaseDto.getPrivateAttributes(),
                application,
                applicationReleaseDto.getSupportedProducts(),
                applicationReleaseDto.getTransitableReleases() );
		
		try{
			cookbook = File.createTempFile("cookbook-" + 
					applicationReleaseDto.getApplicationName() + "-" +
					applicationReleaseDto.getVersion() + ".tar", ".tmp");  
			
	    	installable = File.createTempFile("installable-" + 
	    			applicationReleaseDto.getApplicationName() + "-" +
	    			applicationReleaseDto.getVersion() + ".tar", ".tmp");
	    	
	        cookbook = 
	        	getFileFromBodyPartEntity ((BodyPartEntity) multiPart.getBodyParts().get(1).getEntity(), cookbook);
	        installable = 
	        	getFileFromBodyPartEntity ((BodyPartEntity) multiPart.getBodyParts().get(2).getEntity() ,installable);
	       
		} catch (IOException e){
			throw new RuntimeException(e);	
		}
        return applicationManager.insert(applicationRelease, cookbook, 
				installable);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Application> findAll(Integer page, Integer pageSize,
            String orderBy, String orderType) {
        ApplicationSearchCriteria criteria = new ApplicationSearchCriteria();
        if (page != null && pageSize != null) {
            criteria.setPage(page);
            criteria.setPageSize(pageSize);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            criteria.setOrderBy(orderBy);
        }
        if (!StringUtils.isEmpty(orderType)) {
            criteria.setOrderBy(orderType);
        }
        return applicationManager.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Application load(String name) throws EntityNotFoundException {
        return applicationManager.load(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attribute> loadAttributes(String name)
            throws EntityNotFoundException {
        return applicationManager.load(name).getAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationRelease> findAll(String name, Integer page,
            Integer pageSize, String orderBy, String orderType) {
        ApplicationReleaseSearchCriteria criteria =
            new ApplicationReleaseSearchCriteria();

        if (!StringUtils.isEmpty(name)) {
            try {
                Application application = applicationManager.load(name);
                criteria.setApplication(application);
            } catch (EntityNotFoundException e) {
                throw new SdcRuntimeException(
                        "Can not find the application " + name, e);
            }
        }

        if (page != null && pageSize != null) {
            criteria.setPage(page);
            criteria.setPageSize(pageSize);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            criteria.setOrderBy(orderBy);
        }
        if (!StringUtils.isEmpty(orderType)) {
            criteria.setOrderBy(orderType);
        }
        return applicationManager.findReleasesByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationRelease load(String name, String version)
            throws EntityNotFoundException {
        Application application = applicationManager.load(name);
        return applicationManager.load(application, version);
    }

    /**
     * {@inheritDoc}
     * @throws  
     */
    @Override
    public void delete(String name, String version) 
    	throws ApplicationReleaseNotFoundException, 
    	ApplicationReleaseStillInstalledException {
    	
    	   	
    	LOGGER.log(Level.INFO,"Delete ApplicationRelease. ApplicationName : " 
    			+ name
    			+ " AppliationVersion : " + version);
    	
    	Application application = new Application();
    	application.setName(name);
    	
    	ApplicationRelease applicationRelease = new ApplicationRelease();
    	applicationRelease.setApplication(application);
    	applicationRelease.setVersion(version);
		
    	try {
			applicationManager.load(application, version);
		} catch (EntityNotFoundException e) {
			throw new ApplicationReleaseNotFoundException(e);
		}
		
		applicationManager.delete(applicationRelease);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	public ApplicationRelease update(String name, String version, 
		MultiPart multiPart) throws ApplicationReleaseNotFoundException, 
        InvalidApplicationReleaseException, ProductReleaseNotFoundException,
        InvalidApplicationReleaseUpdateRequestException,
        InvalidMultiPartRequestException{
        
    	ReleaseDto releaseDto = new ReleaseDto();
    	releaseDto.setName(name);
    	releaseDto.setVersion(version);
    	
      	validator.validateUpdate(releaseDto, multiPart);
      	
      	File cookbook = null;
       	File installable = null;
      	ApplicationReleaseDto applicationReleaseDto = new ApplicationReleaseDto();
       	ApplicationRelease applicationRelease = new ApplicationRelease();
       	Application application = new Application ();
              	
       	applicationReleaseDto =
       			(ApplicationReleaseDto)multiPart.getBodyParts().get(0).getEntity();
       		
       	application.setName(applicationReleaseDto.getApplicationName());
       	application.
       		setDescription(applicationReleaseDto.getApplicationDescription());
       	application.setType(applicationReleaseDto.getApplicationType());
        	
       	for (int i=0; applicationReleaseDto.getPrivateAttributes().size() < i; i++)
       		application.addAttribute(applicationReleaseDto.getPrivateAttributes().get(i));
    		
       	applicationRelease.setVersion(applicationReleaseDto.getVersion());
       	applicationRelease.setReleaseNotes(applicationReleaseDto.getReleaseNotes());
       	applicationRelease.
       		setPrivateAttributes(applicationReleaseDto.getPrivateAttributes());
       	applicationRelease.
       		setSupportedProducts(applicationReleaseDto.getSupportedProducts());
       	applicationRelease.
       		setTransitableReleases(applicationReleaseDto.getTransitableReleases());
        
       	try {
			cookbook = File.createTempFile("cookbook-" + 
				applicationReleaseDto.getApplicationName() + "-" +
				applicationReleaseDto.getVersion() + ".tar", ".tmp");
		} catch (IOException e) {
			throw new SdcRuntimeException(e);
		}  
       	
		cookbook = 
           	getFileFromBodyPartEntity (
        		(BodyPartEntity) multiPart.getBodyParts().get(1).getEntity(), 
        		cookbook); 
       	
       	try {
			installable = File.createTempFile("installable-" + 
			   	applicationReleaseDto.getApplicationName() + "-" +
			   	applicationReleaseDto.getVersion() + ".tar", ".tmp");
		} catch (IOException e) {
			throw new SdcRuntimeException(e);
		}  
       	installable = 
           	getFileFromBodyPartEntity (
           		(BodyPartEntity) multiPart.getBodyParts().get(2).getEntity() 
        	    ,installable);
       	
       	try {
			applicationManager.load(application, version);
		} catch (EntityNotFoundException e) {
			throw new ApplicationReleaseNotFoundException(e);
		}
		
       	return applicationManager.update(applicationRelease,cookbook,installable); 		
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attribute> loadAttributes(String name, String version)
            throws EntityNotFoundException {
        return load(name, version).getAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationRelease> findTransitable(String name, String version)
            throws EntityNotFoundException {
        return load(name, version).getTransitableReleases();
    }
    
    private File getFileFromBodyPartEntity(BodyPartEntity bpe, File file )
    {
        try {
        	InputStream input = bpe.getInputStream();
        	
        	OutputStream out=new FileOutputStream(file);
          
        	byte[] buf =new byte[1024];
        	int len;
        	while((len=input.read(buf))>0){
        		out.write(buf,0,len);
        	}
        	out.close();
        	input.close();
                  
        }catch(IOException e){
        	LOGGER.log(Level.SEVERE, "An error was produced : "+ e.toString());
        	throw new SdcRuntimeException ();
        }
        return file;      
    }
    
    /**
     * @param validator the validator to set
     */
    public void setValidator(ApplicationResourceValidator validator) {
        this.validator = validator;
    }
}
