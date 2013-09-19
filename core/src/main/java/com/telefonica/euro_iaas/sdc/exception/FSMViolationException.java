package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;

/**
 * Exception thrown when the Finite State Machine defined for Product and Application
 * instance is violated.
 *
 * @author Sergio Arroyo
 */
@SuppressWarnings("serial")
public class FSMViolationException extends Exception {

    private Status previousState;
    private Status futureState;

    public FSMViolationException(Status previousState, Status futureState) {
        super("Invalid transition from " + previousState  + " to " + futureState);
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
