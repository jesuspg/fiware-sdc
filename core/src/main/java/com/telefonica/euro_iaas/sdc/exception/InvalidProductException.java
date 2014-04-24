/**
 * 
 */
package com.telefonica.euro_iaas.sdc.exception;

import com.telefonica.euro_iaas.sdc.model.Product;

/**
 * @author jesus.movilla
 *
 */
public class InvalidProductException extends Exception {

    private Product product;
    
    public InvalidProductException() {
        super();
    }

    public InvalidProductException(String msg) {
        super(msg);
    }

    public InvalidProductException(Throwable e) {
        super(e);
    }

    public InvalidProductException(Throwable e, Product product) {
        super(e);
        this.product = product;
    }
    
    public InvalidProductException(String msg, Product product) {
        super(msg);
        this.product = product;
    }

    public InvalidProductException(String msg, Throwable e) {
        super(msg, e);
    }
}
