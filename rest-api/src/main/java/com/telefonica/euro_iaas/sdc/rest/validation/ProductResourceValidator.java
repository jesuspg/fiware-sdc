package com.telefonica.euro_iaas.sdc.rest.validation;

import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;

/**
 * Defines the methods to validate the selected operation is valid for the given
 * Application Release.
 * @author Jesus M. Movilla
 *
 */
public interface ProductResourceValidator {

	  /**
     * Verify if the ProductRelase could be updated
     * @aparam ReleaseDto (name, version, type)
     * @param MultiPart composed of three objects: ApplicationReleaseDto,
     * File cookbook and File installable
     * @throws InvalidApplicationReleaseUpdateRequestException 
     * if all the objects are null
     */
    void validateUpdate (ReleaseDto rleleaseDto, MultiPart multipart) 
    	throws InvalidMultiPartRequestException,
    	InvalidProductReleaseUpdateRequestException;
    
    /**
     * Verify if the ProductRelase could be inserted
     * @aparam ReleaseDto (name, version, type)
     * @param MultiPart composed of three objects: ApplicationReleaseDto,
     * File cookbook and File installable
     * @throws InvalidApplicationReleaseUpdateRequestException 
     * if all the objects are null
     */
    void validateInsert(MultiPart multipart) 
    	throws InvalidMultiPartRequestException;
}


