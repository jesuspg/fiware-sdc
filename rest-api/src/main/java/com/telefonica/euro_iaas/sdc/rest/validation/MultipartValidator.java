package com.telefonica.euro_iaas.sdc.rest.validation;

import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

public class MultipartValidator {

	@SuppressWarnings("unchecked")
	public void validateMultipart(MultiPart multiPart, java.lang.Class clase) 
		throws InvalidMultiPartRequestException {
		
		ProductReleaseDto productReleaseDto = new ProductReleaseDto();
		ApplicationReleaseDto applicationReleaseDto = 
			new ApplicationReleaseDto();
		
		//Checking that multipart object is not null
		if (multiPart == null)
			throw new InvalidMultiPartRequestException("The " +
			"MultiPart object is null");

		//Checking thet multipart is composed of three objects
		if (multiPart.getBodyParts().size()!= 3)
			throw new InvalidMultiPartRequestException("The " +
			"MultiPart object should be composed of three parts");
		
		
		if (!multiPart.getBodyParts().get(0).getEntity().getClass().equals(clase))
			throw new InvalidMultiPartRequestException("First " +
				" entity of MultiPart is a class type " +
				multiPart.getBodyParts().get(0).getEntity().getClass() +
				" and it should be " +
				clase);
		
		if (!multiPart.getBodyParts().get(1).getEntity().getClass().equals(byte[].class))
			throw new InvalidMultiPartRequestException("Second " +
				" entity of MultiPart is a class type " +
				multiPart.getBodyParts().get(0).getEntity().getClass() +
				" and it should be " +
				byte[].class);
		
		if (!multiPart.getBodyParts().get(2).getEntity().getClass().equals(byte[].class))
			throw new InvalidMultiPartRequestException(" Third " +
				" entity of MultiPart is a class type " +
				multiPart.getBodyParts().get(0).getEntity().getClass() +
				" and it should be " +
				byte[].class);
	}
}
