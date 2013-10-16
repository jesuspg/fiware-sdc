/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
