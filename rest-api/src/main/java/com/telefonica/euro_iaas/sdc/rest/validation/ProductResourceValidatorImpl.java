/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.rest.validation;

import java.util.List;

import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidNameException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;

public class ProductResourceValidatorImpl extends MultipartValidator implements ProductResourceValidator {

    private GeneralResourceValidator generalValidator;
    
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
    
    public void validateInsert(Product product) throws InvalidEntityException {

        try {
            generalValidator.validateName(product.getName());
            if (!(product.getMapAttributes().isEmpty()))
                validateMetadata(product.getMetadatas());
        } catch (InvalidNameException e) {
            throw new InvalidEntityException(e.getMessage());
        } catch (InvalidProductException ipe) {
            throw new InvalidEntityException(ipe.getMessage());
        }

    }
    
    private void validateMetadata(List<Metadata> metadatas) throws InvalidProductException {
        int open_port_value;
        for (int i=0; i < metadatas.size(); i++) {
            Metadata metadata = metadatas.get(i);
            
            if (metadata.getKey().equals("open_ports")){
                try { 
                    open_port_value  = Integer.parseInt(metadata.getValue());                   
                } catch(NumberFormatException e) { 
                    String msg = "The open_ports metadata is not a number";
                    throw new InvalidProductException(msg);
                }
                
               if ((0 < open_port_value) && ( open_port_value < 65535)) {
                   String msg = "The open_ports value is not in the interval [0-65535]";
                   throw new InvalidProductException(msg);
               }

            } else if (metadata.getKey().equals("installator")) {
                if (!(metadata.getValue().equals("chef")) &&  !(metadata.getValue().equals("puppet"))){
                    String msg = "Metadata " + metadata.getValue() + " MUST BE \"chef\" or \"puppet\"";
                    throw new InvalidProductException(msg);
                }
            } else if (metadata.getKey().equals("dependecies")) {           
            }
        }
    
    }
    
    /**
     * @param generalValidator
     *            the generalValidator to set
     */
    public void setGeneralValidator(GeneralResourceValidator generalValidator) {
        this.generalValidator = generalValidator;
    }
    

}
