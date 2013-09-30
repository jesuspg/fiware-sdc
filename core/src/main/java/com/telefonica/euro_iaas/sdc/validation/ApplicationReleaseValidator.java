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

import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;

/**
 * Defines the methods to validate the selected operation is valid for the given Application Release.
 * 
 * @author Jesus M. Movilla
 */
public interface ApplicationReleaseValidator {

    /**
     * Verify if the ApplicationRelase could be inserted
     * 
     * @param applicationRelease
     *            to be inserted
     * @throws ProductReleaseNotFoundException
     *             if the product Release on which the Application Release is installed on exists in the system
     */
    void validateInsert(ApplicationRelease applicationRelease) throws ProductReleaseNotFoundException;

    /**
     * Verify if the ApplicationRelase could be deleted
     * 
     * @param applicationRelease
     *            to be deleted
     * @throws ApplicationReleaseStillInstalledException
     *             if the application Realease to be deleted is still installed on some vms
     */
    void validateDelete(ApplicationRelease applicationRelease) throws ApplicationReleaseStillInstalledException;

}
