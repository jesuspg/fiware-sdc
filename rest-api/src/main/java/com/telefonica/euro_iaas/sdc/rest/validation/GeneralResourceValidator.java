/**
 * 
 */
package com.telefonica.euro_iaas.sdc.rest.validation;

import com.telefonica.euro_iaas.sdc.exception.InvalidNameException;

/**
 * @author jesus.movilla
 *
 */
public interface GeneralResourceValidator {

    /**
     * Verify if the name is not empty and if is smaller than 256 characters
     * @param name
     */
    void validateName(String name) throws InvalidNameException;
}
