/**
 * 
 */
package com.telefonica.euro_iaas.sdc.exception;

/**
 * @author jesus.movilla
 *
 */
@SuppressWarnings("serial")
public class ChefRecipeExecutionException extends Exception {
    public ChefRecipeExecutionException() {
        super();
    }

    public ChefRecipeExecutionException(String msg) {
        super(msg);
    }

    public ChefRecipeExecutionException(Throwable e) {
        super(e);
    }

    public ChefRecipeExecutionException(String msg, Throwable e) {
        super(msg, e);
    }

}
