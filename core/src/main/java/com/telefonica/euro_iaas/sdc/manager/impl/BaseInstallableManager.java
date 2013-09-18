package com.telefonica.euro_iaas.sdc.manager.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_DIRECTORY_COOKBOOK;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.DELETE_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UPLOAD_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.WEBDAV_BASE_URL;

import java.io.File;
import java.text.MessageFormat;

import com.googlecode.sardine.util.SardineException;
import com.telefonica.euro_iaas.sdc.dao.WebDavDao;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.util.CommandExecutor;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class BaseInstallableManager {

	private CommandExecutor commandExecutor;
	protected SystemPropertiesProvider propertiesProvider;
	private WebDavDao webdavDao;
	
	private String INSTALLABLE_NOT_FOUND = "404";
	
	protected void uploadRecipe(File cookbook, String productName) 
		throws ShellCommandException {
	    	
	   
	    String untarCommand = "tar xvf " + cookbook.getAbsolutePath() 
	       	+ " -C "
	       	+ propertiesProvider.getProperty(CHEF_DIRECTORY_COOKBOOK);
	       	
	    
	    System.out.println("untarCommand : " + untarCommand);
	    commandExecutor.executeCommand(untarCommand);
	       
	    String uploadRecipeCommand = MessageFormat.format(propertiesProvider
	    		.getProperty(UPLOAD_RECIPES_SCRIPT), productName);
	        
	    System.out.println("uploadRecipeCommand : " + uploadRecipeCommand);
	    commandExecutor.executeCommand(uploadRecipeCommand);
	    cookbook.deleteOnExit();
	        
	}
	    
	protected void deleteRecipe(String productName, 
			String version) throws ShellCommandException {
    	
	    	String deleteRecipeCommand = MessageFormat.format(propertiesProvider
                    .getProperty(DELETE_RECIPES_SCRIPT), productName, version);
        
	    	System.out.println("deleteRecipeCommand : " + deleteRecipeCommand);
	    	commandExecutor.executeCommand(deleteRecipeCommand);
	    }
	    
	    protected void uploadInstallable(File installable, 
	    			ProductRelease productRelease){
	    	
	    	String webdavFileUrl = 
	    		propertiesProvider.getProperty(WEBDAV_BASE_URL) +
    			"/product/"
				+ productRelease.getProduct().getName() + "/"
				+ productRelease.getVersion() + "/"
				+ "installable-"
				+ productRelease.getProduct().getName() + "-"
				+ productRelease.getVersion() +  ".tar";	
				
	    	createWebDavDirectoryStructure(productRelease);
	    	try {
				webdavDao.insertFile(webdavFileUrl, installable);
			} catch (SardineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally
			{
				installable.deleteOnExit();
			}
	    	
    	}
	    
	    
	    protected void deleteInstallable(ProductRelease productRelease){
    	
    	String webdavFileUrl = 
    		propertiesProvider.getProperty(WEBDAV_BASE_URL) +
			"/product/"
			+ productRelease.getProduct().getName() + "/"
			+ productRelease.getVersion() + "/"
			+ "installable-"
			+ productRelease.getProduct().getName() + "-"
			+ productRelease.getVersion() +  ".tar";	
    	
    		try {
	    		if (webdavDao.directoryExists(
	    				propertiesProvider.getProperty(WEBDAV_BASE_URL) +
	    				"/product/"
	    				+ productRelease.getProduct().getName() + "/"
	    				+ productRelease.getVersion() + "/", webdavFileUrl))	    		
	    			webdavDao.delete(webdavFileUrl);
	    		else
					System.out.println ("File " + 
							propertiesProvider.getProperty(WEBDAV_BASE_URL) +
							"/product/"
							+ productRelease.getProduct().getName()	+ "/" + 
							" does NOT exist");	
			} catch (SardineException e) {
				if (String.valueOf(e.getStatusCode()).equals(INSTALLABLE_NOT_FOUND)){
					System.out.println (webdavFileUrl + " does not EXIST");
				}
				else
					e.printStackTrace();
			}
			
			deleteWebDavDirectoryStructure(productRelease);
	    }
    
	    private void deleteWebDavDirectoryStructure (ProductRelease productRelease)
	    {
	    		
	    	try {	
	    		if (webdavDao.directoryExists(
	    				propertiesProvider.getProperty(WEBDAV_BASE_URL) +
						"/product/"
						+ productRelease.getProduct().getName() + "/",
						propertiesProvider.getProperty(WEBDAV_BASE_URL) +
		    			"/product/"
	    				+ productRelease.getProduct().getName() + "/"
	    				+ productRelease.getVersion() + "/"))
					
						webdavDao.delete(propertiesProvider.getProperty(WEBDAV_BASE_URL) 
							+ "/product/"
							+ productRelease.getProduct().getName() + "/"
							+ productRelease.getVersion() + "/");
				else
					System.out.println ("Directory " + 
							propertiesProvider.getProperty(WEBDAV_BASE_URL) +
			    			"/product/"
		    				+ productRelease.getProduct().getName() + "/"
		    				+ productRelease.getVersion() + "/" + 
							" does NOT exist");
	    	} catch (SardineException e) {
	    		if (String.valueOf(e.getStatusCode()).equals(INSTALLABLE_NOT_FOUND)){
					System.out.println (
		    				propertiesProvider.getProperty(WEBDAV_BASE_URL) +
							"/product/"
							+ productRelease.getProduct().getName() + "/"
							+ " does not EXIST");
				}
				else
					e.printStackTrace();
	    	}
	    	
	    	try {
	    		if (webdavDao.directoryExists(
	    				propertiesProvider.getProperty(WEBDAV_BASE_URL) +
	    	    		"/product/",
	    	    		propertiesProvider.getProperty(WEBDAV_BASE_URL) +
	    				"/product/"
	    				+ productRelease.getProduct().getName() + "/"))
					
						webdavDao.delete(propertiesProvider.getProperty(WEBDAV_BASE_URL) 
							+ "/product/"
							+ productRelease.getProduct().getName()+ "/");
					
				else
	    	    	System.out.println ("Directory " + 
	    				propertiesProvider.getProperty(WEBDAV_BASE_URL)
	    				+ "/product/"
	    				+ productRelease.getProduct().getName()	+ "/"
	    				+ "  does NOT exist");
	    	} catch (SardineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	    }
	    
	    private void createWebDavDirectoryStructure (ProductRelease productRelease)
	    {
	    	
	    	System.out.println(propertiesProvider.getProperty(WEBDAV_BASE_URL) +
			"/product/"+ productRelease.getProduct().getName() + "/");
	    	try {
	    		if (!webdavDao.directoryExists(
	    				propertiesProvider.getProperty(WEBDAV_BASE_URL) +
	    				"/product/",
	    				propertiesProvider.getProperty(WEBDAV_BASE_URL) +
						"/product/"
						+ productRelease.getProduct().getName() + "/"))	    		
				
	    			webdavDao.createDirectory(
						propertiesProvider.getProperty(WEBDAV_BASE_URL) +
						"/product/"
						+ productRelease.getProduct().getName());
	    		else
					System.out.println ("Directory " + 
							propertiesProvider.getProperty(WEBDAV_BASE_URL) +
							"/product/"
							+ productRelease.getProduct().getName()	+ "/" + 
							" already CREATED");	
			} catch (SardineException e) {
				e.printStackTrace();
			}
	    	
	    	try {
	    		
	    		if (!webdavDao.directoryExists(
	    				propertiesProvider.getProperty(WEBDAV_BASE_URL) +
						"/product/"
						+ productRelease.getProduct().getName() + "/",
						propertiesProvider.getProperty(WEBDAV_BASE_URL) +
		    			"/product/"
	    				+ productRelease.getProduct().getName() + "/"
	    				+ productRelease.getVersion() + "/"))
	    		webdavDao.createDirectory(
		    			propertiesProvider.getProperty(WEBDAV_BASE_URL) +
		    			"/product/"
	    				+ productRelease.getProduct().getName() + "/"
	    				+ productRelease.getVersion());	
	    		else
					System.out.println ("Directory " + 
							propertiesProvider.getProperty(WEBDAV_BASE_URL) +
			    			"/product/"
		    				+ productRelease.getProduct().getName() + "/"
		    				+ productRelease.getVersion()	+ 
							" already CREATED");
	    	
	    	} catch (SardineException e) {
	    		e.printStackTrace();
			}
	    	
	    }
	    
	    /**
	     * @param propertiesProvider
	     *            the propertiesProvider to set
	     */
	    public void setPropertiesProvider(
	            SystemPropertiesProvider propertiesProvider) {
	        this.propertiesProvider = propertiesProvider;
	    }
	    /**
	     * @param commandExecutor the commandExecutor to set
	     */
	    public void setCommandExecutor(CommandExecutor commandExecutor) {
	        this.commandExecutor = commandExecutor;
	    }
	    
	    /**
	     * @param WebDavDao the webdavDao to set
	     */
	    public void setWebDavDao(WebDavDao webdavDao) {
	        this.webdavDao = webdavDao;
	    }
}
