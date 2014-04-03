/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
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
