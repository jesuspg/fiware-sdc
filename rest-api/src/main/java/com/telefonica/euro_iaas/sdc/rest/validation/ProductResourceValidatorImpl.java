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

package com.telefonica.euro_iaas.sdc.rest.validation;

import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;

public class ProductResourceValidatorImpl extends MultipartValidator implements ProductResourceValidator {

    public void validateUpdate(ReleaseDto releaseDto, MultiPart multiPart) throws InvalidMultiPartRequestException,
            InvalidProductReleaseUpdateRequestException {

        validateMultipart(multiPart, ProductReleaseDto.class);

        ProductReleaseDto productReleaseDto = multiPart.getBodyParts().get(0).getEntityAs(ProductReleaseDto.class);

        if (!(releaseDto.getName().equals(productReleaseDto.getProductName()))
                && !(releaseDto.getVersion().equals(productReleaseDto.getVersion())))
            throw new InvalidProductReleaseUpdateRequestException("Inconsistent ProductRelase Update Request. "
                    + "Name and Version should be equals in the URL and in " + "the ProductRelaseDto Object");
    }

    public void validateInsert(MultiPart multiPart) throws InvalidMultiPartRequestException {

        ProductReleaseDto productReleaseDto = new ProductReleaseDto();
        validateMultipart(multiPart, productReleaseDto.getClass());

    }

}
