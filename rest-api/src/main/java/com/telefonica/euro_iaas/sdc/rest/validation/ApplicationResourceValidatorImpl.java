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
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationReleaseDto;

public class ApplicationResourceValidatorImpl extends MultipartValidator implements ApplicationResourceValidator {

    public void validateUpdate(MultiPart multiPart) throws InvalidMultiPartRequestException {

        validateMultipart(multiPart, ApplicationReleaseDto.class);
    }

    public void validateInsert(MultiPart multiPart) throws InvalidMultiPartRequestException {

        ApplicationReleaseDto applicationReleaseDto = new ApplicationReleaseDto();
        validateMultipart(multiPart, applicationReleaseDto.getClass());

    }

}
