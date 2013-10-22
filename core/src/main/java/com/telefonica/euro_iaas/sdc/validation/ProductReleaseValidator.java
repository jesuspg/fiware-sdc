/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.validation;

import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Defines the methods to validate the selected operation is valid for the given product Release.
 * 
 * @author Jesus M. Movilla
 */
public interface ProductReleaseValidator {

    /**
     * Verify if the ProductRelase could be deleted
     * 
     * @param productRelease
     *            to be deleted
     * @throws ProductReleaseStillInstalledException
     *             if the product Realease to be deleted is still installed on some vms
     * @throws ProductReleaseInApplicationReleaseException
     *             thrown when try to delete a ProductRelease which is included in any ApplicationRelease object
     */
    void validateDelete(ProductRelease productRelease) throws ProductReleaseStillInstalledException;
}
