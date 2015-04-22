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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.rest.exception.APIException;
import com.telefonica.euro_iaas.sdc.rest.validation.GeneralResourceValidator;
import com.telefonica.euro_iaas.sdc.rest.validation.ProductResourceValidator;
import com.telefonica.fiware.commons.dao.AlreadyExistsEntityException;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.dao.InvalidEntityException;

/**
 * @author jesus.movilla
 */
@Path("/catalog/product/{pName}/release")
@Component
@Scope("request")
public class ProductReleaseResourceImpl implements ProductReleaseResource {

    private ProductReleaseManager productReleaseManager;
    private ProductManager productManager;

    private ProductResourceValidator validator;
    private GeneralResourceValidator generalValidator;
    private static Logger log = Logger.getLogger("ProductReleaseResourceImpl");

    /**
     * Insert the ProductRelease in SDC.
     * 
     * @param productReleaseDto
     * @throws AlreadyExistsProductReleaseException
     * @throws InvalidProductReleaseException
     * @return proudctRelease
     */
    public ProductRelease insert(String pName, ProductReleaseDto productReleaseDto) throws APIException {
        ProductRelease productRelease = null;
        try {
            validator.validateInsert(pName, productReleaseDto);
        } catch (InvalidEntityException e) {
            log.warning("InvalidEntityException: " + e.getMessage());
            throw new APIException(new InvalidEntityException(productReleaseDto, e));
        } catch (EntityNotFoundException e) {
            log.warning("EntityNotFoundException: " + e.getMessage());
            throw new APIException(new EntityNotFoundException(Product.class, e.getMessage(), e));
        }
        log.info("Inserting a new product release in the software catalogue " + productReleaseDto.getProductName()
                + " " + productReleaseDto.getVersion() + " " + productReleaseDto.getProductDescription());

        try {
            Product product = productManager.load(pName);
            productRelease = new ProductRelease(productReleaseDto.getVersion(), productReleaseDto.getReleaseNotes(),
                    product, productReleaseDto.getSupportedOS(), productReleaseDto.getTransitableReleases());
        } catch (EntityNotFoundException e1) {
            log.warning("EntityNotFoundException: " + e1.getMessage());
            throw new APIException(new EntityNotFoundException(Product.class, e1.getMessage(), e1));
        }

        log.info(productRelease.toString());

        try {
            return productReleaseManager.insert(productRelease);
        } catch (AlreadyExistsProductReleaseException e) {
            log.warning("InvalidEntityException: " + e.getMessage());
            throw new APIException(new AlreadyExistsEntityException(Product.class, e));
        } catch (InvalidProductReleaseException e) {
            log.warning("InvalidEntityException: " + e.getMessage());
            throw new APIException(new InvalidEntityException(productRelease, e));
        }
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
    public ProductRelease load(String pName, String version) throws APIException {
        Product product;
        try {
            product = productManager.load(pName);
        } catch (EntityNotFoundException e) {
            log.warning("EntityNotFoundException: " + e.getMessage());
            throw new APIException(new EntityNotFoundException(Product.class, pName, e));
        }
        try {
            return productReleaseManager.load(product, version);
        } catch (EntityNotFoundException e) {
            log.warning("EntityNotFoundException: " + e.getMessage());
            throw new APIException(new EntityNotFoundException(ProductRelease.class, pName + "-" + version, e));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String pName, String version) throws APIException {

        log.log(Level.INFO, "Delete ProductRelease. ProductName : " + pName + " ProductVersion : " + version);

        Product product;
        try {
            product = productManager.load(pName);
        } catch (EntityNotFoundException e) {
            log.warning("EntityNotFoundException: " + e.getMessage());
            throw new APIException(new EntityNotFoundException(Product.class, pName, e));
        }

        ProductRelease productRelease;
        try {
            productRelease = productReleaseManager.load(product, version);
        } catch (EntityNotFoundException e) {
            log.warning("EntityNotFoundException: " + e.getMessage());
            throw new APIException(new EntityNotFoundException(ProductRelease.class, pName + "-" + version, e));
        }

        try {
            productReleaseManager.delete(productRelease);
        } catch (ProductReleaseNotFoundException e) {
            log.warning("EntityNotFoundException: " + e.getMessage());
            throw new APIException(new EntityNotFoundException(ProductRelease.class, pName + "-" + version, e));
        } catch (ProductReleaseStillInstalledException e) {
            log.warning("EntityNotFoundException: " + e.getMessage());
            throw new APIException(new EntityNotFoundException(ProductRelease.class, pName + "-" + version, e));
        }
    }

    /**
     * It obtains the transsitable version.
     * 
     * @param pName
     * @param version
     *            the product version
     * @return
     * @throws APIException
     */
    @Override
    public List<ProductRelease> findTransitable(String pName, String version) throws APIException {
        return load(pName, version).getTransitableReleases();
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
