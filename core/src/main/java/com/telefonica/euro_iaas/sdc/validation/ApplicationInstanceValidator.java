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
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.IncompatibleProductsException;
import com.telefonica.euro_iaas.sdc.exception.NotInstalledProductsException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;

/**
 * Defines the methods to validate the selected operation is valid for the given application instance.
 * 
 * @author Sergio Arroyo
 */
public interface ApplicationInstanceValidator {

    /**
     * Verify if the ApplicationInstance could be installed
     * 
     * @param application
     *            the application
     * @throws IncompatibleProductsException
     *             if the selected products are not compatible with the given application
     * @throws AlreadyInstalledException
     *             if the application is running on the system
     * @throws NotInstalledProductsException
     *             if the needed products to install the application are not installed
     */
    void validateInstall(ApplicationInstance application) throws IncompatibleProductsException,
            AlreadyInstalledException, NotInstalledProductsException;

    /**
     * Verify if the ApplicationInstance could be uninstalled
     * 
     * @param application
     *            the application
     * @throws FSMViolationException
     *             if the application is in status which is not configure to uninstalled.
     */
    void validateUninstall(ApplicationInstance application) throws FSMViolationException;

    /**
     * Verify if the ApplicationInstance could be configured
     * 
     * @param application
     *            the application
     * @throws FSMViolationException
     *             if the application is in status which is not compatible to configure.
     */
    void validateConfigure(ApplicationInstance application) throws FSMViolationException;

    /**
     * Verify if the ApplicationInstance could be updated
     * 
     * @param application
     *            the application
     * @param newRelease
     *            the new version of the application
     * @throws IncompatibleProductsException
     *             if the products where the application is installed, are incompatible with the new version
     * @throws NotTransitableException
     *             if the application can not be update to the new version
     * @throws FSMViolationException
     *             if the application is in status which is not compatible with the update
     */
    void validateUpdate(ApplicationInstance application, ApplicationRelease newRelease)
            throws IncompatibleProductsException, NotTransitableException, FSMViolationException;
}
