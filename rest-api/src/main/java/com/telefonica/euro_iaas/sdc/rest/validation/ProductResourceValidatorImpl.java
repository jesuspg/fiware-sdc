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
import java.util.logging.Logger;

import org.glassfish.jersey.media.multipart.MultiPart;

import com.telefonica.fiware.commons.dao.AlreadyExistsEntityException;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidNameException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.rest.exception.APIException;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class ProductResourceValidatorImpl extends MultipartValidator implements ProductResourceValidator {

    private GeneralResourceValidator generalValidator;
    private ProductManager productManager;
    private ProductReleaseManager productReleaseManager;
    private SystemPropertiesProvider systemPropertiesProvider;

    private static Logger log = Logger.getLogger("ProductResourceValidatorImpl");

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

    public void validateInsert(String pName, ProductReleaseDto productRelease) throws InvalidEntityException,
            EntityNotFoundException {

        try {
            generalValidator.validateName(pName);
            generalValidator.validateVesion(productRelease.getVersion());
        } catch (InvalidNameException e) {
            log.warning("InvalidEntityException: " + e.getMessage());
            throw new InvalidEntityException(new InvalidEntityException(productRelease, e));
        }

        Product product = null;
        try {
            product = productManager.load(pName);

        } catch (Exception e1) {
            log.warning("EntityNotFoundException: " + e1.getMessage());
            throw new InvalidEntityException(new EntityNotFoundException(Product.class,
                    productRelease.getProductName(), e1));
        }

        try {
            productReleaseManager.load(product, productRelease.getVersion());
            String mes = "The product release " + productRelease.getProductName() + " version "
                    + productRelease.getVersion() + " already exists";
            throw new InvalidEntityException(new AlreadyExistsEntityException(ProductRelease.class, new Exception(mes)));
        } catch (EntityNotFoundException e1) {
            log.warning("EntityNotFoundException: " + e1.getMessage());
        }
        
    }

    public void validateLoad(ReleaseDto releaseDto) throws EntityNotFoundException {

        Product product = null;

        try {
            product = productManager.load(releaseDto.getName());

        } catch (Exception e1) {
            log.warning("EntityNotFoundException: " + e1.getMessage());
            throw new APIException(new EntityNotFoundException(Product.class, releaseDto.getName(), e1));
        }

        try {
            productReleaseManager.load(product, releaseDto.getVersion());
        } catch (EntityNotFoundException e1) {
            log.warning("EntityNotFoundException: " + e1.getMessage());
            throw new APIException(new EntityNotFoundException(ProductRelease.class, releaseDto.getName(), e1));
        }

    }

    public void validateInsert(Product product) throws InvalidEntityException, AlreadyExistsEntityException,
            InvalidProductException {

        if (productManager.exist(product.getName())) {
            String mens = "Entity already exist : " + product.getName();
            log.warning(mens);
            throw new AlreadyExistsEntityException(Product.class, new Exception(mens));
        }
        if (product.getAttributes() != null) {
            validateAttributesType(product.getAttributes());
        }

        commonValidation(product);

    }

    private void validateAttributesType(List<Attribute> attributes) throws InvalidProductException {
        String msg = "Attribute type is incorrect.";
        for (Attribute att : attributes) {
            if (att.getType() == null) {
                log.warning(msg + " Adding Plain as default value.");
                att.setType("Plain");
            }

            String availableTypes = systemPropertiesProvider
                    .getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES);

            StringTokenizer st2 = new StringTokenizer(availableTypes, "|");
            boolean error = true;
            while (st2.hasMoreElements()) {
                if (att.getType().equals(st2.nextElement())) {
                    error = false;
                    break;
                }
            }
            if (error) {
                throw new InvalidProductException(msg);
            }
        }

    }

    private void commonValidation(Product product) throws InvalidEntityException {
        try {
            generalValidator.validateName(product.getName());

            if (!(product.getMapMetadata() == null && product.getMapMetadata().isEmpty())) {
                validateMetadata(product.getMetadatas());
            }
        } catch (InvalidNameException e) {
            throw new InvalidEntityException(product, e);
        } catch (InvalidProductException ipe) {
            throw new InvalidEntityException(product, ipe);
        }
    }

    private void validateMetadata(List<Metadata> metadatas) throws InvalidProductException {
        for (int i = 0; i < metadatas.size(); i++) {
            Metadata metadata = metadatas.get(i);

            if (metadata.getKey().equals("open_ports") ||
                metadata.getKey().equals("open_ports_udp")) {
                List<String> ports = getFields((String) metadata.getValue());
                for (String port : ports) {
                    checkPortMetadata(port);
                }
            } else if (metadata.getKey().equals("installator")) {
                if (!(metadata.getValue().equals("chef")) && !(metadata.getValue().equals("puppet"))) {
                    String msg = "Metadata " + metadata.getValue() + " MUST BE \"chef\" or \"puppet\"";
                    throw new InvalidProductException(msg);
                }
            } else if (metadata.getKey().equals("dependencies")) {
                checkDependence(metadata.getValue());
            } else if (metadata.getKey().equals("public") ||
                metadata.getKey().equals("cloud")) {
                if (!(metadata.getValue().equals("no")) && !(metadata.getValue().equals("yes"))) {
                    String msg = "Metadata " + metadata.getValue() + " MUST BE \"yes\" or \"not\"";
                    throw new InvalidProductException(msg);
                }
            } 
        }

    }

    private void checkPortMetadata(String port) throws InvalidProductException {

        if (port.contains("-")) {
            checkPort(port.substring(0, port.indexOf("-")));
            checkPort(port.substring( port.indexOf("-")+1, port.length()));
        }
        else {
            checkPort(port);
        }
    }

    private void checkPort(String port) throws InvalidProductException{
        int openPortValue;
        try {
            openPortValue = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            String msg = "The open_ports metadata is not a number";
            throw new InvalidProductException(msg);
        }

        if ((openPortValue < 0) || (openPortValue > 65535)) {
            String msg = "The open_ports value is not in the interval [0-65535]";
            throw new InvalidProductException(msg);
        }
    }

    private void checkDependence(String dependence) throws InvalidProductException {
        if (dependence.isEmpty()) {
            return;
        }
        List<String> dependeces = getFields(dependence);

        for (String depen : dependeces) {
            if (!productManager.exist(depen)) {
                String msg = "The product " + dependence + " does not exist and it is a dependence";
                throw new InvalidProductException(msg);
            }
        }
    }

    private List<String> getFields(String portString) {
        StringTokenizer st = new StringTokenizer(portString);
        List<String> ports = new ArrayList();

        while (st.hasMoreElements()) {
            ports.add((String) st.nextElement());
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

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setProductReleaseManager(ProductReleaseManager productReleaseManager) {
        this.productReleaseManager = productReleaseManager;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

}
