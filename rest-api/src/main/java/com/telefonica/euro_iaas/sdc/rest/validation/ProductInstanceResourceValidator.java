/**
 * 
 */
package com.telefonica.euro_iaas.sdc.rest.validation;

import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.sdc.rest.exception.UnauthorizedOperationException;

/**
 * Defines the methods to validate the selected operation is valid for the given Product Instance.
 * 
 * @param ProductInstanceDto
 *            product
 * @author Jesus M. Movilla
 */
public interface ProductInstanceResourceValidator {

    /**
     * Verify if the ip where products are going to be installed belongs to the OpenStackUser
     * 
     * @throws UnauthorizedOperationException
     */
    void validateInsert(ProductInstanceDto product) throws UnauthorizedOperationException;
}
