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

package com.telefonica.euro_iaas.sdc.rest.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidNameException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.rest.validation.GeneralResourceValidator;
import com.telefonica.euro_iaas.sdc.rest.validation.ProductResourceValidator;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
@Path("/catalog/product/{pName}/release")
@Component
@Scope("request")
public class ProductReleaseResourceImpl implements ProductReleaseResource {

    @InjectParam("productReleaseManager")
    private ProductReleaseManager productReleaseManager;
    @InjectParam("productManager")
    private ProductManager productManager;
    


    private ProductResourceValidator validator;
    private GeneralResourceValidator generalValidator;
    private static Logger LOGGER = Logger.getLogger("ProductReleaseResourceImpl");

    /**
     * Insert the ProductRelease in SDC.
     * 
     * @param productReleaseDto
     * @throws AlreadyExistsProductReleaseException
     * @throws InvalidProductReleaseException
     * @return proudctRelease
     */
    public ProductRelease insert(String pName, ProductReleaseDto productReleaseDto)
            throws AlreadyExistsProductReleaseException, InvalidProductReleaseException {

        try {
            generalValidator.validateName(pName);
        } catch (InvalidNameException e) {
            throw new InvalidProductReleaseException(e.getMessage());
        }
        productReleaseDto.setProductName(pName);

        LOGGER.info("Inserting a new product release in the software catalogue " + productReleaseDto.getProductName()
                + " " + productReleaseDto.getVersion() + " " + productReleaseDto.getProductDescription());
        Product product = new Product(productReleaseDto.getProductName(), productReleaseDto.getProductDescription());

        ProductRelease productRelease = new ProductRelease(productReleaseDto.getVersion(),
                productReleaseDto.getReleaseNotes(), product, productReleaseDto.getSupportedOS(),
                productReleaseDto.getTransitableReleases());

        LOGGER.info(productRelease.toString());
        return productReleaseManager.insert(productRelease);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws InvalidProductReleaseUpdateRequestException
     * @throws InvalidMultiPartRequestException
     */
    public ProductRelease insert(MultiPart multiPart) throws AlreadyExistsProductReleaseException,
            InvalidProductReleaseException, InvalidMultiPartRequestException {

        validator.validateInsert(multiPart);

        File cookbook = null;
        File installable = null;

        // First part contains a Project object
        ProductReleaseDto productReleaseDto = multiPart.getBodyParts().get(0).getEntityAs(ProductReleaseDto.class);
        LOGGER.log(Level.INFO, " Insert ProductRelease " + productReleaseDto.getProductName() + " version "
                + productReleaseDto.getVersion());

        Product product = new Product(productReleaseDto.getProductName(), productReleaseDto.getProductDescription());

        /*
         * for (int i = 0; productReleaseDto.getPrivateAttributes().size() < 1; i++) {
         * product.addAttribute(productReleaseDto.getPrivateAttributes().get(i)); }
         */

        ProductRelease productRelease = new ProductRelease(productReleaseDto.getVersion(),
                productReleaseDto.getReleaseNotes(),
                // productReleaseDto.getPrivateAttributes(),
                product, productReleaseDto.getSupportedOS(), productReleaseDto.getTransitableReleases());

        try {
            cookbook = File.createTempFile(
                    "cookbook-" + productReleaseDto.getProductName() + "-" + productReleaseDto.getVersion() + ".tar",
                    ".tmp");

            installable = File.createTempFile("installable-" + productReleaseDto.getProductName() + "-"
                    + productReleaseDto.getVersion() + ".tar", ".tmp");

            cookbook = getFileFromBodyPartEntity((BodyPartEntity) multiPart.getBodyParts().get(1).getEntity(), cookbook);
            installable = getFileFromBodyPartEntity((BodyPartEntity) multiPart.getBodyParts().get(2).getEntity(),
                    installable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return productReleaseManager.insert(productRelease, cookbook, installable);
    }

    /**
     * {@inheritDoc}
     */

    public List<ProductRelease> findAll(String pName, String osType, Integer page, Integer pageSize, String orderBy,
            String orderType) {
    	
    	
    	
        ProductReleaseSearchCriteria criteria = new ProductReleaseSearchCriteria();

        if (!StringUtils.isEmpty(pName)) {
            try {
                Product product = productManager.load(pName);
                criteria.setProduct(product);
            } catch (EntityNotFoundException ex) {
                throw new WebApplicationException(ex, Response.Status.NOT_FOUND);

            }
        }

        if (!StringUtils.isEmpty(osType)) {
            criteria.setOSType(osType);
        }
        if (page != null && pageSize != null) {
            criteria.setPage(page);
            criteria.setPageSize(pageSize);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            criteria.setOrderBy(orderBy);
        }
        if (!StringUtils.isEmpty(orderType)) {
            criteria.setOrderBy(orderType);
        }
        return productReleaseManager.findReleasesByCriteria(criteria);
    }
    
   

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductRelease load(String pName, String version) throws EntityNotFoundException {
        Product product = productManager.load(pName);
        return productReleaseManager.load(product, version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String pName, String version) throws ProductReleaseNotFoundException,
            ProductReleaseStillInstalledException {

        LOGGER.log(Level.INFO, "Delete ProductRelease. ProductName : " + pName + " ProductVersion : " + version);

        Product product;
        try {
            product = productManager.load(pName);
        } catch (EntityNotFoundException e) {
            throw new ProductReleaseNotFoundException(e);
        }

        ProductRelease productRelease;
        try {
            productRelease = productReleaseManager.load(product, version);
        } catch (EntityNotFoundException e) {
            throw new ProductReleaseNotFoundException(e);
        }

        productReleaseManager.delete(productRelease);
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.rest.resources.ProductReleaseResource#findTransitable(java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<ProductRelease> findTransitable(String pName, String version) throws EntityNotFoundException {
        return load(pName, version).getTransitableReleases();
    }

    /**
     * Update the ProductRelease (productReleaseDto, cookbooks, installable).
     * 
     * @param multipart
     * @throws ProductReleaseNotFoundException
     * @throws InvalidProductReleaseException
     * @throws InvalidProductReleaseException
     * @throws InvalidMultiPartRequestException
     * @return productRelease
     */
    public ProductRelease update(MultiPart multiPart) throws ProductReleaseNotFoundException,
            InvalidProductReleaseException, InvalidProductReleaseUpdateRequestException,
            InvalidMultiPartRequestException {

        ProductReleaseDto productReleaseDto = multiPart.getBodyParts().get(0).getEntityAs(ProductReleaseDto.class);
        LOGGER.log(Level.INFO,
                "ProductRelease " + productReleaseDto.getProductName() + " version " + productReleaseDto.getVersion());

        // TODO Validar el Objeto ProductReleaseDto en las validaciones
        Product product = new Product();
        ProductRelease productRelease = new ProductRelease();

        product.setName(productReleaseDto.getProductName());

        if (productReleaseDto.getProductDescription() != null) {
            product.setDescription(productReleaseDto.getProductDescription());
        }

        /*
         * if (productReleaseDto.getPrivateAttributes() != null) { for (int i = 0;
         * productReleaseDto.getPrivateAttributes().size() < 1; i++) {
         * product.addAttribute(productReleaseDto.getPrivateAttributes().get(i)); } }
         */

        productRelease.setProduct(product);

        if (productReleaseDto.getVersion() != null) {
            productRelease.setVersion(productReleaseDto.getVersion());
        }

        // ReleaseNotes
        if (productReleaseDto.getReleaseNotes() != null) {
            productRelease.setReleaseNotes(productReleaseDto.getReleaseNotes());
        }

        // PrivateAttributes
        /*
         * if (productReleaseDto.getPrivateAttributes() != null) {
         * productRelease.setPrivateAttributes(productReleaseDto.getPrivateAttributes()); }
         */

        // SupportedOS
        if (productReleaseDto.getSupportedOS() != null) {
            productRelease.setSupportedOOSS(productReleaseDto.getSupportedOS());
        }

        // TransitableRelease
        if (productReleaseDto.getTransitableReleases() != null) {
            productRelease.setTransitableReleases(productReleaseDto.getTransitableReleases());
        }

        ReleaseDto releaseDto = new ReleaseDto(productReleaseDto.getProductName(), productReleaseDto.getVersion(),
                "product");

        validator.validateUpdate(releaseDto, multiPart);

        File cookbook = null;
        File installable = null;

        try {
            cookbook = File.createTempFile("cookbook-" + releaseDto.getName() + "-" + releaseDto.getVersion() + ".tar",
                    ".tmp");
            cookbook = getFileFromBodyPartEntity((BodyPartEntity) multiPart.getBodyParts().get(1).getEntity(), cookbook);
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
        }

        try {
            installable = File.createTempFile("installable-" + releaseDto.getName() + "-" + releaseDto.getVersion()
                    + ".tar", ".tmp");

            installable = getFileFromBodyPartEntity((BodyPartEntity) multiPart.getBodyParts().get(2).getEntity(),
                    installable);
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
        }
        return productReleaseManager.update(productRelease, cookbook, installable);
    }

    private File getFileFromBodyPartEntity(BodyPartEntity bpe, File file) {
        try {
            InputStream input = bpe.getInputStream();
            OutputStream out = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            int len;
            while ((len = input.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            input.close();
        } catch (IOException e) {
            System.out.println("An error was produced : " + e.toString());
        }
        return file;
    }
    

    


    /**
     * @param validator
     *            the validator to set
     */
    public void setValidator(ProductResourceValidator validator) {
        this.validator = validator;
    }
    
    /**
     * @param generalValidator
     *            the generalValidator to set
     */
    public void setGeneralValidator(GeneralResourceValidator generalValidator) {
        this.generalValidator = generalValidator;
    }

    /**
     * @param productReleaseManager
     *            the productReleaseManager to set
     */
    public void setProductReleaseManager(ProductReleaseManager productReleaseManager) {
        this.productReleaseManager = productReleaseManager;
    }

    /**
     * @param productManager
     *            the productManager to set
     */
    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }
}
