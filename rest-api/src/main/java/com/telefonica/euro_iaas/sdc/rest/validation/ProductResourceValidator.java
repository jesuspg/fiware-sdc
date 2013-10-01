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

package com.telefonica.euro_iaas.sdc.rest.validation;

import com.sun.jersey.multipart.MultiPart;

import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;

/**
 * Defines the methods to validate the selected operation is valid for the given Application Release.
 * 
 * @author Jesus M. Movilla
 */
public interface ProductResourceValidator {

    /**
     * Verify if the ProductRelase could be updated
     * 
     * @aparam ReleaseDto (name, version, type)
     * @param MultiPart
     *            composed of three objects: ApplicationReleaseDto, File cookbook and File installable
     * @throws InvalidApplicationReleaseUpdateRequestException
     *             if all the objects are null
     */
    void validateUpdate(ReleaseDto rleleaseDto, MultiPart multipart) throws InvalidMultiPartRequestException,
            InvalidProductReleaseUpdateRequestException;

    /**
     * Verify if the ProductRelase could be inserted
     * 
     * @aparam ReleaseDto (name, version, type)
     * @param MultiPart
     *            composed of three objects: ApplicationReleaseDto, File cookbook and File installable
     * @throws InvalidApplicationReleaseUpdateRequestException
     *             if all the objects are null
     */
    void validateInsert(MultiPart multipart) throws InvalidMultiPartRequestException;
}
