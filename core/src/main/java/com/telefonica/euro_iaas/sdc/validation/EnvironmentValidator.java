package com.telefonica.euro_iaas.sdc.validation;

import com.telefonica.euro_iaas.sdc.exception.ProductReleaseInApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.model.Environment;

/**
 * Defines the methods to validate the selected operation is valid for the given
 * product Release.
 * 
 * @author Jesus M. Movilla
 * 
 */
public interface EnvironmentValidator {

	/**
	 * Verify if the Environment could be deleted
	 * 
	 * @param environment
	 *            to be deleted
	 * @throws ProductReleaseStillInstalledException
	 *             if the product Release to be deleted is still installed on
	 *             some vms
	 * @throws ProductReleaseInApplicationReleaseException
	 *             thrown when try to delete a ProductRelease which is included
	 *             in any ApplicationRelease object
	 */
	void validateDelete(Environment environment)
			throws ProductReleaseStillInstalledException,
			ProductReleaseInApplicationReleaseException;
}
