/**
 * 
 */
package com.telefonica.euro_iaas.sdc.exception;

/**
 * @author jesus.movilla
 *
 */
public class InvalidNameException extends Exception {

    private String name;
    
    public InvalidNameException() {
        super();
    }

    public InvalidNameException(String msg) {
        super(msg);
    }

    public InvalidNameException(Throwable e) {
        super(e);
    }

    public InvalidNameException(Throwable e, String name) {
        super(e);
        this.name = name;
    }
    
    public InvalidNameException(String msg, String name) {
        super(msg);
        this.name = name;
    }

    public InvalidNameException(String msg, Throwable e) {
        super(msg, e);
    }
}
