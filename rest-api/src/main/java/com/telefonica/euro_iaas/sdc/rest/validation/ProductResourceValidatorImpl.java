package com.telefonica.euro_iaas.sdc.rest.validation;

import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;

public class ProductResourceValidatorImpl extends MultipartValidator implements
		ProductResourceValidator {

	public void validateUpdate(ReleaseDto releaseDto, MultiPart multiPart)
			throws InvalidMultiPartRequestException,
			InvalidProductReleaseUpdateRequestException {

		validateMultipart(multiPart, ProductReleaseDto.class);

		ProductReleaseDto productReleaseDto = multiPart.getBodyParts().get(0)
				.getEntityAs(ProductReleaseDto.class);

		if (!(releaseDto.getName().equals(productReleaseDto.getProductName()))
				&& !(releaseDto.getVersion().equals(productReleaseDto
						.getVersion())))
			throw new InvalidProductReleaseUpdateRequestException(
					"Inconsistent ProductRelase Update Request. "
							+ "Name and Version should be equals in the URL and in "
							+ "the ProductRelaseDto Object");
	}

	public void validateInsert(MultiPart multiPart)
			throws InvalidMultiPartRequestException {

		ProductReleaseDto productReleaseDto = new ProductReleaseDto();
		validateMultipart(multiPart, productReleaseDto.getClass());

	}

}
