package com.telefonica.euro_iaas.sdc.validation;

import com.telefonica.euro_iaas.sdc.exception.ApplicationInstanceStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.InvalidEnvironmentInstanceException;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;

public interface EnvironmentInstanceValidator {

	/**
     * Verify if the PoductInstances that forms the Environment Instances are
     * all INSTALLED before inserting the environment Instances in BBDD
     * @param environment to be deleted
     * @throws InvalidEvironmentInstanceException if a product Instance in the
     * environment Instance is not INSTALLED
     */
    void validateInsert(EnvironmentInstance environmentInstance)
            throws InvalidEnvironmentInstanceException;
    
    /**
     * Verify if the PoductInstances that forms the Environment Instances are
     * all INSTALLED before updating the environment Instances in BBDD
     * @param environment to be deleted
     * @throws InvalidEvironmentInstanceException if a product Instance in the
     * environment Instance is not INSTALLED
     */
    void validateUpdate(EnvironmentInstance environmentInstance)
            throws InvalidEnvironmentInstanceException;
    
    /**
     * Verify that there is not any application Instance installed 
     * 		on the Enviroment Instance
     * @param environment to be deleted
     * @throws InvalidEvironmentInstanceException if a product Instance in the
     * 		environment Instance is not INSTALLED
     * @throws ApplicationInstanceStillInstalledException if there are any 
     * 			ApplicationInstances installed in teh system
     */
    void validateDelete(EnvironmentInstance environmentInstance)
            throws ApplicationInstanceStillInstalledException;
}
