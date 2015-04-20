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

package com.telefonica.euro_iaas.sdc.manager.impl;

import static com.telefonica.euro_iaas.sdc.util.Configuration.WEBDAV_PRODUCT_BASEDIR;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.validation.ProductReleaseValidator;
import com.telefonica.fiware.commons.dao.AlreadyExistsEntityException;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.xmlsolutions.annotation.UseCase;

/**
 * @author jesus.movilla
 */
public class ProductReleaseManagerImpl extends BaseInstallableManager implements ProductReleaseManager {

    private ProductReleaseValidator validator;
    private ProductDao productDao;
    private ProductReleaseDao productReleaseDao;
    private OSDao osDao;
    private static Logger log = Logger.getLogger("ProductReleaseManagerImpl");

    // @Override
    public ProductRelease load(Product product, String version) throws EntityNotFoundException {
        return productReleaseDao.load(product, version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductRelease> findReleasesByCriteria(ProductReleaseSearchCriteria criteria) {
        return productReleaseDao.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UseCase(traceTo = "UC_101.1", status = "implemented and tested")
    public ProductRelease insert(ProductRelease productRelease, File cookbook, File installable, String token)
            throws AlreadyExistsProductReleaseException, InvalidProductReleaseException {

        log.info("Inserting productRelease " + productRelease.getVersion() + " "
                + productRelease.getProduct().getName());

        ProductRelease productReleaseOut = insertProductReleaseBBDD(productRelease);

        ReleaseDto releaseDto = new ReleaseDto();
        releaseDto.setName(productRelease.getProduct().getName());
        releaseDto.setVersion(productRelease.getVersion());
        releaseDto.setType(WEBDAV_PRODUCT_BASEDIR);

        uploadInstallable(installable, releaseDto, token);
        uploadRecipe(cookbook, releaseDto.getName());

        return productReleaseOut;
    }

    /**
     * Insert ProductRelease in SDC.
     * 
     * @param productRelease
     * @throws AlreadyExistsProductReleaseException
     * @throws InvalidProductReleaseException
     */
    public ProductRelease insert(ProductRelease productRelease) throws AlreadyExistsProductReleaseException,
            InvalidProductReleaseException {
        log.info("Inserting productRelease " + productRelease.getVersion() + " "
                + productRelease.getProduct().getName());
        ProductRelease productReleaseOut = insertProductReleaseBBDD(productRelease);

        ReleaseDto releaseDto = new ReleaseDto();
        releaseDto.setName(productRelease.getProduct().getName());
        releaseDto.setVersion(productRelease.getVersion());

        return productReleaseOut;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ProductReleaseStillInstalledException
     */
    @Override
    @UseCase(traceTo = "UC_101.3", status = "implemented and tested")
    public void delete(ProductRelease productRelease) throws ProductReleaseNotFoundException,
            ProductReleaseStillInstalledException {

        boolean lastRelease = false;
        validator.validateDelete(productRelease);

        // Check if the current application Release is the last one associated
        // to
        // the application so you can delete the recipe completely
        lastRelease = isLastProductRelease(productRelease);

        ReleaseDto releaseDto = new ReleaseDto();
        releaseDto.setName(productRelease.getProduct().getName());
        releaseDto.setVersion(productRelease.getVersion());
        releaseDto.setType(WEBDAV_PRODUCT_BASEDIR);

        deleteProductReleaseBBDD(productRelease);

        // deleteInstallable(releaseDto);
        // if (lastRelease)
        // deleteRecipe(releaseDto.getName(), releaseDto.getVersion());
    }

    @Override
    @UseCase(traceTo = "UC_101.3", status = "implemented and tested")
    public ProductRelease update(ProductRelease productRelease, File cookbook, File installable, String token)
            throws ProductReleaseNotFoundException, InvalidProductReleaseException {

        if (productRelease != null) {
            productRelease = updateProductReleaseBBDD(productRelease);
        }
        ReleaseDto releaseDto = new ReleaseDto();
        releaseDto.setName(productRelease.getProduct().getName());
        releaseDto.setVersion(productRelease.getVersion());
        releaseDto.setType(WEBDAV_PRODUCT_BASEDIR);

        if (installable != null) {
            uploadInstallable(installable, releaseDto, token);
        }
        if (cookbook != null) {
            uploadRecipe(cookbook, releaseDto.getName());
        }
        return productRelease;
    }

    // *********** PRIVATE METHODS ************//
    private ProductRelease insertProductReleaseBBDD(ProductRelease productRelease)
            throws AlreadyExistsProductReleaseException, InvalidProductReleaseException {

        log.info("Inserting insertProductReleaseBBDD " + productRelease.getVersion() + " "
                + productRelease.getProduct().getName());
        Product product;
        ProductRelease productReleaseOut;
        // List<OS> OSs = insertProductReleaseLoadSO(productRelease);
        // productRelease.setSupportedOOSS(OSs);
        product = insertProductReleaseLoadProduct(productRelease);

        productRelease.setProduct(product);
        productReleaseOut = insertProductRelease(productRelease);

        return productReleaseOut;
    }

    private void deleteProductReleaseBBDD(ProductRelease productRelease) throws ProductReleaseNotFoundException {
        productReleaseDao.remove(productRelease);
    }

    private boolean isLastProductRelease(ProductRelease productRelease) throws ProductReleaseNotFoundException {
        boolean lastRelease = false;
        ProductReleaseSearchCriteria criteria = new ProductReleaseSearchCriteria();

        Product product;
        try {
            product = productDao.load(productRelease.getProduct().getName());
        } catch (EntityNotFoundException e) {
            throw new ProductReleaseNotFoundException();
        }

        criteria.setProduct(product);
        List<ProductRelease> productReleases = productReleaseDao.findByCriteria(criteria);

        if (productReleases.size() == 1) {
            lastRelease = true;
        }
        return lastRelease;
    }

    private ProductRelease updateProductReleaseBBDD(ProductRelease productRelease)
            throws ProductReleaseNotFoundException, InvalidProductReleaseException {
        ProductRelease productReleaseOut;
        Product product;

        if (productRelease.getSupportedOOSS() != null) {
            List<OS> oss = updateProductReleaseLoadSO(productRelease);
            productRelease.setSupportedOOSS(oss);
        }

        product = updateProductReleaseLoadProduct(productRelease);

        productRelease.setProduct(product);
        productReleaseOut = updateProductRelease(productRelease);

        return productReleaseOut;
    }

    private List<OS> insertProductReleaseLoadSO(ProductRelease productRelease) throws InvalidProductReleaseException {

        OS os;
        List<OS> oss = new ArrayList<OS>();

        if (productRelease.getSupportedOOSS() == null || productRelease.getSupportedOOSS().size() == 0) {
            return oss;
        }

        for (int i = 0; i < productRelease.getSupportedOOSS().size(); i++) {
            try {
                os = osDao.load(productRelease.getSupportedOOSS().get(i).getName());
                log.log(Level.INFO, "OS " + productRelease.getSupportedOOSS().get(i).getName() + " LOADED");
                oss.add(os);
            } catch (EntityNotFoundException e) {
                try {
                    os = osDao.create(productRelease.getSupportedOOSS().get(i));
                    log.log(Level.INFO, "OS " + productRelease.getSupportedOOSS().get(i).getName() + " CREATED");
                    oss.add(os);
                } catch (Exception e1) {
                    String invalidOSMessageLog = "The supportedOS "
                            + productRelease.getSupportedOOSS().get(i).getName() + " in Product Release"
                            + productRelease.getProduct().getName() + productRelease.getVersion()
                            + " is invalid. Please Insert a valid OS";
                    log.log(Level.SEVERE, invalidOSMessageLog);
                    throw new InvalidProductReleaseException(invalidOSMessageLog, e1);
                }
            }
        }
        return oss;
    }

    private Product insertProductReleaseLoadProduct(ProductRelease productRelease)
            throws InvalidProductReleaseException {

        log.info("Inserting insertProductReleaseLoadProduct " + productRelease.getVersion() + " "
                + productRelease.getProduct().getName());
        Product product;
        // Insert Product if needed
        try {
            product = productDao.load(productRelease.getProduct().getName());
            log.log(Level.INFO, "Product " + product.getName() + " LOADED");
        } catch (EntityNotFoundException e) {
            try {
                product = productDao.create(productRelease.getProduct());
                log.log(Level.INFO, "Product " + product.getName() + " CREATED");
            } catch (Exception e1) {
                String messageLog = "The Product " + productRelease.getProduct().getName() + " in Product Release"
                        + productRelease.getProduct().getName() + productRelease.getVersion()
                        + " is invalid. Please Insert a valid Product ";
                log.log(Level.SEVERE, messageLog);
                throw new InvalidProductReleaseException(messageLog, e1);
            }
        }
        return product;
    }

    private ProductRelease insertProductRelease(ProductRelease productRelease) throws InvalidProductReleaseException,
            AlreadyExistsProductReleaseException {
        log.info("Inserting insertProductRelease " + productRelease.getVersion() + " "
                + productRelease.getProduct().getName());
        ProductRelease productReleaseOut;
        // Insert ProductRelease if not in BBDD
        try {
            productReleaseOut = productReleaseDao.load(productRelease.getProduct(), productRelease.getVersion());
            log.log(Level.INFO,
                    "Product Release" + productRelease.getProduct().getName() + "-" + productRelease.getVersion()
                            + " LOADED");
        } catch (EntityNotFoundException e) {
            try {
                log.log(Level.INFO, "Product Release not found, creating..." + productRelease.getProduct().getName()
                        + "-" + productRelease.getVersion());
                productReleaseOut = productReleaseDao.create(productRelease);
                log.log(Level.INFO,
                        "ProductRelease " + productRelease.getProduct().getName() + "-" + productRelease.getVersion()
                                + " CREATED");
            } catch (AlreadyExistsEntityException e1) {
                String alreadyExistsMessageLog = "The Product Release " + productRelease.getProduct().getName()
                        + productRelease.getVersion() + " already exist " + e1.getMessage();
                log.log(Level.SEVERE, alreadyExistsMessageLog);
                throw new AlreadyExistsProductReleaseException(alreadyExistsMessageLog, e1);
            } catch (Exception e1) {
                String invalidEntityMessageLog = "The Product Release " + productRelease.getProduct().getName()
                        + productRelease.getVersion() + " is invalid. Please Insert a valid Product Release "
                        + e1.getMessage();
                log.log(Level.SEVERE, invalidEntityMessageLog);
                throw new InvalidProductReleaseException(invalidEntityMessageLog, e1);
            }
        }
        return productReleaseOut;
    }

    private List<OS> updateProductReleaseLoadSO(ProductRelease productRelease) throws ProductReleaseNotFoundException,
            InvalidProductReleaseException {

        return insertProductReleaseLoadSO(productRelease);

        /*
         * OS os; List<OS> OSs = new ArrayList<OS>(); for (int i=0; i<productRelease.getSupportedOOSS().size(); i++) {
         * try { os = osDao.load(productRelease.getSupportedOOSS().get(i).getName()); LOGGER.log(Level.INFO,"OS " +
         * productRelease.getSupportedOOSS().get(i).getName() + " LOADED"); OSs.add(os); } catch
         * (EntityNotFoundException e) { String entityNotFoundMessageLog = "The OS " +
         * productRelease.getSupportedOOSS().get(i).getName() + " has not been found in the System "; LOGGER.log
         * (Level.SEVERE, entityNotFoundMessageLog); throw new ProductReleaseNotFoundException (
         * entityNotFoundMessageLog, e); } } return OSs;
         */
    }

    private Product updateProductReleaseLoadProduct(ProductRelease productRelease)
            throws ProductReleaseNotFoundException, InvalidProductReleaseException {

        Product product = null;
        // Product productOut = null;
        Product existedProduct = null;

        try {
            existedProduct = productDao.load(productRelease.getProduct().getName());
            log.log(Level.INFO, "Product " + existedProduct.getName() + " LOADED");

            if (productRelease.getProduct().getDescription() != null) {
                existedProduct.setDescription(productRelease.getProduct().getDescription());
            }

            if (productRelease.getProduct().getAttributes() != null) {
                existedProduct.setAttributes(productRelease.getProduct().getAttributes());
            }

            product = productDao.update(existedProduct);
            log.log(Level.INFO, "Product " + existedProduct.getName() + " UPDATED");
        } catch (EntityNotFoundException e) {
            String entityNotFoundMessageLog = "The Product " + productRelease.getProduct().getName()
                    + " has not been found in the System ";
            log.log(Level.SEVERE, entityNotFoundMessageLog);
            throw new ProductReleaseNotFoundException(entityNotFoundMessageLog, e);
        } catch (Exception e) {
            String invalidEntityException = "The Product " + productRelease.getProduct().getName()
                    + " to be updated is Invalid ";
            log.log(Level.SEVERE, invalidEntityException);
            throw new InvalidProductReleaseException(invalidEntityException, e);
        }
        return product;
    }

    private ProductRelease updateProductRelease(ProductRelease productRelease) throws InvalidProductReleaseException,
            ProductReleaseNotFoundException {

        ProductRelease productReleaseOut, existedProductRelease;
        try {
            log.log(Level.INFO, "Product Release Before Loading " + productRelease.getProduct().getName() + "-"
                    + productRelease.getVersion());

            existedProductRelease = productReleaseDao.load(productRelease.getProduct(), productRelease.getVersion());

            log.log(Level.INFO,
                    "Product Release " + productRelease.getProduct().getName() + "-" + productRelease.getVersion()
                            + " LOADED");

            /*
             * if (productRelease.getPrivateAttributes() != null) {
             * existedProductRelease.setPrivateAttributes(productRelease.getPrivateAttributes()); }
             */
            if (productRelease.getReleaseNotes() != null) {
                existedProductRelease.setReleaseNotes(productRelease.getReleaseNotes());
            }
            if (productRelease.getSupportedOOSS() != null) {
                existedProductRelease.setSupportedOOSS(productRelease.getSupportedOOSS());
            }
            if (productRelease.getTransitableReleases() != null) {
                existedProductRelease.setTransitableReleases(productRelease.getTransitableReleases());
            }

            productReleaseOut = productReleaseDao.update(existedProductRelease);
            log.log(Level.INFO,
                    "ProductRelease " + productRelease.getProduct().getName() + "-" + productRelease.getVersion()
                            + " UPDATED");

        } catch (EntityNotFoundException e) {
            String entityNotFoundMessageLog = "The Product Release" + productRelease.getProduct().getName() + "-"
                    + productRelease.getVersion() + " has not been found in the System ";
            log.log(Level.SEVERE, entityNotFoundMessageLog);
            throw new ProductReleaseNotFoundException(entityNotFoundMessageLog, e);
        } catch (Exception e) {
            String invalidEntityException = "The Product Release" + productRelease.getProduct().getName() + " version "
                    + productRelease.getVersion() + " is Invalid ";
            log.log(Level.SEVERE, invalidEntityException);
            throw new InvalidProductReleaseException(invalidEntityException, e);
        }
        return productReleaseOut;
    }

    /**
     * @param osDao
     *            the osDao to set
     */
    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
    }

    /**
     * @param productDao
     *            the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

    /**
     * @param validator
     *            the validator to set
     */
    public void setValidator(ProductReleaseValidator validator) {
        this.validator = validator;
    }
}
