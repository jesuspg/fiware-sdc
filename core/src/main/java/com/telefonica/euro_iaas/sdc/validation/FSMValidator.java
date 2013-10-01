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

import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;

/**
 * Defines the way to guaranty there defined FSM for Installable Releases is not violated. See the image bellow to know
 * the valid and invalid transitions. <img src="http://plantuml.com/plantuml/svg/YzQALT3LjLFmp2ikISp9oSnBv
 * -L2i96bKbFWCgafO8dGl4nCNJ2vWlIYn1Gi4ixvUMcPwQL5O2cuAdIBa5IXIo7RYkeC55ce
 * TT5QiRnS65voBIhABq9t6LGGrKrGGNJtmDIYkmLT7DLeC0Lp5W00"/>
 * 
 * @author Sergio Arroyo
 */
public interface FSMValidator {
    /**
     * Validates the transition between the actuall state of the InstallableRelease and the future status.
     * 
     * @param instance
     * @param nextStatus
     * @throws FSMViolationException
     */
    void validate(InstallableInstance instance, Status nextStatus) throws FSMViolationException;
}
