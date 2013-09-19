package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Exception thrown when trying to insert an ProductRelease that does not 
 * have the right information
 *
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class InvalidProductReleaseUpdateRequestException extends Exception {
	    
	private ProductRelease productRelease;
	    
	public InvalidProductReleaseUpdateRequestException() {
       super();
    }

	public InvalidProductReleaseUpdateRequestException(ProductRelease productRelease) {
       this.productRelease = productRelease;
    }
	    
	public InvalidProductReleaseUpdateRequestException(String msg) {
		super(msg);
	}

	public InvalidProductReleaseUpdateRequestException(Throwable e) {
      super(e);
	}
			    
    public InvalidProductReleaseUpdateRequestException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
    * return the productRelease
    */
    public ProductRelease getProductRelease() {
        return productRelease;
    }

    /**
     * @param productRelease the productRelease to set
     */
    public void setProductRelease(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }
}

