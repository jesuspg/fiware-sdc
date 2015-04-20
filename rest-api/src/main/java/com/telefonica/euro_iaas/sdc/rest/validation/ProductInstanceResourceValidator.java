/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.rest.validation;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductException;
import com.telefonica.fiware.commons.openstack.auth.exception.OpenStackException;
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
     * @throws OpenStackException 
     * @throws InvalidProductException 
     * @throws EntityNotFoundException 
     */
    void validateInsert(ProductInstanceDto product) throws UnauthorizedOperationException, OpenStackException, InvalidProductException, EntityNotFoundException;
}
