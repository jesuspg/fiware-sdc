/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

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
