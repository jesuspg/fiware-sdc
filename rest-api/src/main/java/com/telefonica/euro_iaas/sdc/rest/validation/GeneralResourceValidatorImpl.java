/**
 * 
 */
package com.telefonica.euro_iaas.sdc.rest.validation;

import com.telefonica.euro_iaas.sdc.exception.InvalidNameException;

/**
 * @author jesus.movilla
 *
 */
public class GeneralResourceValidatorImpl implements GeneralResourceValidator {

    public void validateName(String name) throws InvalidNameException {
    
        if ((name == null) || (name.isEmpty())) {
            String message =" The name " + name + " is NULL or EMPTY";
            throw new InvalidNameException(message, name);
        }
        
        if (name.length() > 256){
            String message =" The length of name  " + name + " is bigger than 256 characters";
            throw new InvalidNameException(message, name);
        }
        
    }
}
