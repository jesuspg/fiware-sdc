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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;

/**
 * Default implementation for FSMValidatorImpl
 * 
 * @author Sergio Arroyo
 */
public class FSMValidatorImpl implements FSMValidator {

    private final static List<Status> INSTALLING_STATES = Arrays.asList(Status.INSTALLED, Status.ERROR);
    private final static List<Status> INSTALLED_STATES = Arrays.asList(Status.UNINSTALLING, Status.UPGRADING,
            Status.CONFIGURING, Status.DEPLOYING_ARTEFACT, Status.ERROR);
    private final static List<Status> UNINSTALLING_STATES = Arrays.asList(Status.UNINSTALLED, Status.ERROR);
    private final static List<Status> UPGRADING_STATES = Arrays.asList(Status.INSTALLED, Status.ERROR);
    private final static List<Status> ARTIFACT_STATES = Arrays.asList(Status.ARTIFACT_DEPLOYED, Status.ERROR);

    private final static List<Status> CONFIGURING_STATES = Arrays.asList(Status.INSTALLED, Status.ERROR);
    private final static List<Status> UNINSTALLED_STATES = Arrays.asList(Status.ERROR, Status.INSTALLING);
    private final static List<Status> ERROR_STATES = Arrays.asList(Status.INSTALLING);

    @SuppressWarnings("serial")
    private final static Map<Status, List<Status>> TRANSITIONS = new HashMap<Status, List<Status>>() {
        {
            put(Status.INSTALLING, INSTALLING_STATES);
            put(Status.INSTALLED, INSTALLED_STATES);
            put(Status.UNINSTALLING, UNINSTALLING_STATES);
            put(Status.UPGRADING, UPGRADING_STATES);
            put(Status.DEPLOYING_ARTEFACT, ARTIFACT_STATES);
            put(Status.CONFIGURING, CONFIGURING_STATES);
            put(Status.UNINSTALLED, UNINSTALLED_STATES);
            put(Status.ERROR, ERROR_STATES);
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(InstallableInstance instance, Status nextStatus) throws FSMViolationException {
        List<Status> transitableStates = TRANSITIONS.get(instance.getStatus());
        if (transitableStates == null || !transitableStates.contains(nextStatus)) {
            throw new FSMViolationException(instance.getStatus(), nextStatus);
        }
    }
}
