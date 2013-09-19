package com.telefonica.euro_iaas.sdc.rest.validation;

import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;

public class ApplicationResourceValidatorImpl extends MultipartValidator
	implements ApplicationResourceValidator{

	public void validateUpdate(ReleaseDto releaseDto, MultiPart multiPart) 
		throws InvalidMultiPartRequestException,
		InvalidApplicationReleaseUpdateRequestException{
		
		validateMultipart(multiPart, ApplicationReleaseDto.class);
		
		ApplicationReleaseDto applicationReleaseDto =
					(ApplicationReleaseDto)multiPart.getBodyParts().get(0).getEntity();
		
		if (!(releaseDto.getName().equals(applicationReleaseDto.getApplicationName())) &&
				!(releaseDto.getVersion().equals(applicationReleaseDto.getVersion())))
			throw new InvalidApplicationReleaseUpdateRequestException(
					"Inconsisten ApplicationRelase Update Request. " +
					"Name and Version should be equals in the URL and in " +
					"the ApplicationRelaseDt Object");
	}
	
	public void validateInsert(MultiPart multiPart) 
		throws InvalidMultiPartRequestException{
		
		ApplicationReleaseDto applicationReleaseDto = new ApplicationReleaseDto();
		validateMultipart(multiPart, applicationReleaseDto.getClass());

		
	}

}
