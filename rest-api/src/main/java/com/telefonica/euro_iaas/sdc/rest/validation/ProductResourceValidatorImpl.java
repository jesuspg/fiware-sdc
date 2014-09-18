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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidNameException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;

public class ProductResourceValidatorImpl extends MultipartValidator implements ProductResourceValidator {

    private GeneralResourceValidator generalValidator;
    private ProductManager productManager;
    
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
    public void validateInsert (ProductRelease productRelease ) throws InvalidEntityException {
    	
    	validateInsert (productRelease.getProduct());
    	try {
			generalValidator.validateVesion(productRelease.getVersion());
		} catch (InvalidNameException e) {
			throw new InvalidEntityException(e.getMessage());
		}

    }
    
    public void validateInsert(Product product) throws InvalidEntityException, AlreadyExistsEntityException {
    	
        try {
        	
        	if (productManager.exist(product.getName())) {
        		throw new AlreadyExistsEntityException("The product " + product.getName() + " already exists");
        	}
            generalValidator.validateName(product.getName());
         
            if (!(product.getMapMetadata().isEmpty())) {
                validateMetadata(product.getMetadatas());
            }
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
            	List<String> ports = getPorts((String)metadata.getValue());
            	for (String port: ports) {
                try { 
                    open_port_value  = Integer.parseInt(port);                   
                    } catch(NumberFormatException e) { 
                        String msg = "The open_ports metadata is not a number";
                        throw new InvalidProductException(msg);
                    }
                
                   if ((open_port_value < 0) && ( open_port_value > 65535)) {
                       String msg = "The open_ports value is not in the interval [0-65535]";
                       throw new InvalidProductException(msg);
                   }
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
    
    private List<String> getPorts (String portString) {
    	StringTokenizer st = new StringTokenizer(portString);
    	List<String> ports = new ArrayList ();

		while (st.hasMoreElements()) {
			ports.add((String)st.nextElement());
		}
		return ports;

    }
    
    /**
     * @param generalValidator
     *            the generalValidator to set
     */
    public void setGeneralValidator(GeneralResourceValidator generalValidator) {
        this.generalValidator = generalValidator;
    }
    public void setProductManager (ProductManager productManager) {
    	this.productManager = productManager;
    }
    

}
