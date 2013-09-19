package com.telefonica.euro_iaas.sdc.validation;

import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Defines the methods to validate the selected operation is valid for the given
 * product Release.
 * @author Jesus M. Movilla
 *
 */
public interface ProductReleaseValidator {
	
	/**
     * Verify if the ProductRelase could be deleted
     * @param productRelease to be deleted
     * @throws ProductReleaseStillInstalledException if the product Realease to be
     * deleted is still installed on some vms
     */
    void validateDelete(ProductRelease productRelease)
            throws ProductReleaseStillInstalledException;
}
