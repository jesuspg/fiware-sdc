package com.telefonica.euro_iaas.sdc.validation;

import java.util.List;

import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.ApplicationInstanceStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.InvalidEnvironmentInstanceException;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;

public class EnvironmentInstanceValidatorImpl implements
		EnvironmentInstanceValidator {

	private ApplicationInstanceDao applicationInstanceDao;
	@Override
	public void validateInsert(EnvironmentInstance environmentInstance)
			throws InvalidEnvironmentInstanceException {
		
		List<ProductInstance> productInstances = environmentInstance.getProductInstances();
		
		for (int i=0; i < productInstances.size(); i++){
			if (!productInstances.get(i).getStatus().equals(Status.INSTALLED))
				throw new InvalidEnvironmentInstanceException ("" +
						"The Product Instance " +  productInstances.get(i).getId() +
						" is NOT INSTALLED");
		}

	}

	@Override
	public void validateUpdate(EnvironmentInstance environmentInstance)
			throws InvalidEnvironmentInstanceException {
		
		List<ProductInstance> productInstances = environmentInstance.getProductInstances();
		
		for (int i=0; i < productInstances.size(); i++){
			if (!productInstances.get(i).getStatus().equals(Status.INSTALLED))
				throw new InvalidEnvironmentInstanceException ("" +
						"The Product Instance " +  productInstances.get(i).getId() +
						"is NOT INSTALLED");
		}

	}
	
	//Verify that there is not any ApplicationInstance installed 
	//on the EnvironmentInstance
	@Override
	public void validateDelete(EnvironmentInstance environmentInstance)
			throws ApplicationInstanceStillInstalledException {
		
		EnvironmentInstance envInstance;
		List<ApplicationInstance> applicationInstances = 
				applicationInstanceDao.findAll();
		
		for (int i=0; i< applicationInstances.size(); i++){
			/*if (applicationInstances.get(i).getEnvironmentInstance () != null){
				envInstance = 
					applicationInstances.get(i).getEnvironmentInstance();
			
				if (!envInstance.getId().equals(environmentInstance.getId()))
						throw new ApplicationInstanceStillInstalledException ("" +
								"Application Instance " + applicationInstances.get(i).getId() +
								"is STILL INSTALLED on Environment " + environmentInstance.toString());
			}*/
			
			if (applicationInstances.get(i).getEnvironmentInstance()
					.equals(environmentInstance))
				throw new ApplicationInstanceStillInstalledException ("" +
						"Application Instance " + applicationInstances.get(i).getId() +
						"is STILL INSTALLED on Environment " + environmentInstance.toString());
		}
	}

    
	/**
     * @param applicationInstanceDao the applicationInstanceDao to set
     */
    public void setApplicationInstanceDao(
            ApplicationInstanceDao applicationInstanceDao) {
        this.applicationInstanceDao = applicationInstanceDao;
    }
}
