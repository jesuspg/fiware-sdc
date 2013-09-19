package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;

/**
 * Exception thrown when trying to insert an ApplicationRelease that does not 
 * have the right information
 *
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class InvalidApplicationReleaseUpdateRequestException extends Exception {
    
	private ApplicationRelease appRelease;
    
	public InvalidApplicationReleaseUpdateRequestException() {
       super();
    }

	public InvalidApplicationReleaseUpdateRequestException(ApplicationRelease appRelease) {
       this.appRelease = appRelease;
    }
    
	public InvalidApplicationReleaseUpdateRequestException(String msg) {
		super(msg);
	}

	public InvalidApplicationReleaseUpdateRequestException(Throwable e) {
      super(e);
	}
		    
    public InvalidApplicationReleaseUpdateRequestException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
    * return the productRelease
    */
    public ApplicationRelease getApplicationRelease() {
        return appRelease;
    }

    /**
     * @param productRelease the productRelease to set
     */

    public void setApplicationRelease(ApplicationRelease appRelease) {
        this.appRelease = appRelease;
    }
}
