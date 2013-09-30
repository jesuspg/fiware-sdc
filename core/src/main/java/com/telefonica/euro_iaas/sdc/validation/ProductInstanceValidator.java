/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.validation;

import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationIncompatibleException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationInstalledException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Define all possible validations for product instances
 * 
 * @author Sergio Arroyo
 */
public interface ProductInstanceValidator {

    /**
     * Verifies the product could be installed
     * 
     * @param product
     *            the product
     * @throws AlreadyInstalledException
     */
    void validateInstall(ProductInstance product) throws AlreadyInstalledException;

    /**
     * Verify if the given product could be uninstalled
     * 
     * @param product
     *            the product
     * @throws ApplicationInstalledException
     *             if there is some applications installed on the product
     * @throws FSMViolationException
     *             if it can not be uninstalled due to previous status
     */
    void validateUninstall(ProductInstance product) throws ApplicationInstalledException, FSMViolationException;

    /**
     * Verify if the given product could be configured
     * 
     * @param product
     *            the product
     * @throws FSMViolationException
     *             if it can not be configured due to previous status
     */
    void validateConfigure(ProductInstance product) throws FSMViolationException;

    /**
     * Verify if the given product could have acs deployed
     * 
     * @param product
     *            the product
     * @throws FSMViolationException
     *             if it can not be configured due to previous status
     */
    void validateDeployArtifact(ProductInstance product) throws FSMViolationException;

    /**
     * Verify if the given product could be upgraded to the selected version
     * 
     * @param product
     *            the product
     * @param newRelease
     *            the new version
     * @throws FSMViolationException
     * @throws NotTransitableException
     * @throws ApplicationIncompatibleException
     */
    void validateUpdate(ProductInstance product, ProductRelease newRelease) throws FSMViolationException,
            NotTransitableException, ApplicationIncompatibleException;
}
