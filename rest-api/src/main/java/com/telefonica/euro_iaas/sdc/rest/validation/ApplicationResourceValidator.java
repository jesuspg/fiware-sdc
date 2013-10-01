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
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;

/**
 * Defines the methods to validate the selected operation is valid for the given Application Resource.
 * 
 * @author Jesus M. Movilla
 */
public interface ApplicationResourceValidator {

    /**
     * Verify if the ApplicationRelase could be updated
     * 
     * @param MultiPart
     *            composed of three objects: ApplicationReleaseDto, File cookbook and File installable
     * @throws InvalidApplicationReleaseUpdateRequestException
     *             if all the objects are null
     */
    void validateUpdate(MultiPart multipart) throws InvalidMultiPartRequestException;

    /**
     * Verify if the ApplicationRelase could be inserted
     * 
     * @param MultiPart
     *            composed of three objects: ApplicationReleaseDto, File cookbook and File installable
     * @throws InvalidMultiPartRequestException
     *             if the multipart object is null or its size is different to three of if the parts are not filled with
     *             the right type of Object
     */
    void validateInsert(MultiPart multipart) throws InvalidMultiPartRequestException;
}
