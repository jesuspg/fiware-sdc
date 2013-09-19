package com.telefonica.euro_iaas.sdc.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;

public class ApplicationReleaseValidatorImpl 
	implements ApplicationReleaseValidator{
	
	private ProductReleaseDao productReleaseDao;
	private ApplicationInstanceDao applicationInstanceDao;
	
	public void validateInsert(ApplicationRelease applicationRelease)
		throws ProductReleaseNotFoundException{
		//validate if the product release are in the system
		List<ProductRelease> inexistentProductReleases = 
			new ArrayList<ProductRelease>();
		for (ProductRelease productRelease : 
			applicationRelease.getSupportedProducts()) {
			System.out.println("productRelease.getProduct()" +
					productRelease.getProduct());
			System.out.println("productRelease.getVersion()" +
					productRelease.getVersion());
			try{
		    	  productRelease = productReleaseDao.load(
		    			  productRelease.getProduct(), 
		    			  productRelease.getVersion());
		    } catch (EntityNotFoundException e){
		    	inexistentProductReleases.add(productRelease);
		    }
        }
		
		//validate if there are ProductReleases uninstalled
		if (!inexistentProductReleases.isEmpty()) {
            throw new ProductReleaseNotFoundException(inexistentProductReleases);
		}
	}
	
	public void validateDelete(ApplicationRelease applicationRelease)
		throws ApplicationReleaseStillInstalledException{
		//validate if the application release are installed on some VMs
		List<ApplicationInstance> applicationInstances = 
			new ArrayList<ApplicationInstance>();
		for (ApplicationInstance application : 
			getApplicationInstancesByApplication (applicationRelease)) {
            if (application.getStatus().equals(Status.INSTALLED)
                    || application.getStatus().equals(Status.CONFIGURING)
                    || application.getStatus().equals(Status.UPGRADING)
                    || application.getStatus().equals(Status.INSTALLING)) {
            	applicationInstances.add(application);
            }
        }	
		
		//validate if the products are installed
        if (!applicationInstances.isEmpty()) {
            throw new ApplicationReleaseStillInstalledException(applicationInstances);
        }
	}
	

	//***** PRIVATE METHODS *******************//
	private List<ApplicationInstance> getApplicationInstancesByApplication(
            ApplicationRelease applicationRelease) {
		
		ApplicationInstanceSearchCriteria applicationCriteria =
			new ApplicationInstanceSearchCriteria(
					Arrays.asList(Status.INSTALLED),null, null, 
					applicationRelease.getApplication().getName());
        
		return applicationInstanceDao.findByCriteria(applicationCriteria);
    }
	
	/**
     * @param productReleaseDao the productReleaseDao to set
     */
    public void setProductReleaseDao(
            ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }
    
    /**
     * @param applicationInstanceDao the applicationInstanceDao to set
     */
    public void setApplicationInstanceDao(
            ApplicationInstanceDao applicationInstanceDao) {
        this.applicationInstanceDao = applicationInstanceDao;
    }
}
