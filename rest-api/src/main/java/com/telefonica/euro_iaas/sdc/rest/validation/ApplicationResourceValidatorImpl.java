package com.telefonica.euro_iaas.sdc.rest.validation;

import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;

public class ApplicationResourceValidatorImpl extends MultipartValidator
	implements ApplicationResourceValidator{

	public void validateUpdate(MultiPart multiPart) 
		throws InvalidMultiPartRequestException {
		
		validateMultipart(multiPart, ApplicationReleaseDto.class);
	}
	
	public void validateInsert(MultiPart multiPart) 
		throws InvalidMultiPartRequestException{
		
		ApplicationReleaseDto applicationReleaseDto = new ApplicationReleaseDto();
		validateMultipart(multiPart, applicationReleaseDto.getClass());

		
	}

}
