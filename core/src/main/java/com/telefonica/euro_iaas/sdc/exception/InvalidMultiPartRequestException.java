package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Exception thrown when the MultuiPart object is invalid 
 * have the right information
 *
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class InvalidMultiPartRequestException extends Exception {

		    
		private ProductRelease productRelease;
		private ApplicationRelease applicationRelease;
		    
		public InvalidMultiPartRequestException() {
	       super();
	    }

		public InvalidMultiPartRequestException(String msg) {
			super(msg);
		}

		public InvalidMultiPartRequestException(Throwable e) {
	      super(e);
		}
		
		public InvalidMultiPartRequestException(Throwable e, 
			ProductRelease productRelease) {
		      super(e);
		      this.productRelease= productRelease;
		}		    
		
		public InvalidMultiPartRequestException(Throwable e, 
			ApplicationRelease applicationRelease) {
		      super(e);
		      this.applicationRelease= applicationRelease;
		}
		
	    public InvalidMultiPartRequestException(String msg, Throwable e) {
	        super(msg, e);
	    }

}
