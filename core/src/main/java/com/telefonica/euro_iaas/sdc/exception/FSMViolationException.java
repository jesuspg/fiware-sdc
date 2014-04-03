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

package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;

/**
 * Exception thrown when the Finite State Machine defined for Product and Application instance is violated.
 * 
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class FSMViolationException extends Exception {

    private Status previousState;
    private Status futureState;

    public FSMViolationException(Status previousState, Status futureState) {
        super("Invalid transition from " + previousState + " to " + futureState);
        this.previousState = previousState;
        this.futureState = futureState;
    }

    public FSMViolationException(String msg) {
        super(msg);
    }

    public FSMViolationException(Throwable e) {
        super(e);
    }

    public FSMViolationException(String msg, Throwable e) {
        super(msg, e);
    }

    public Status getPreviousState() {
        return previousState;
    }

    public void setPreviousState(Status previousState) {
        this.previousState = previousState;
    }

    public Status getFutureState() {
        return futureState;
    }

    public void setFutureState(Status futureState) {
        this.futureState = futureState;
    }

}
