/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.manager.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.WEBDAV_PRODUCT_BASEDIR;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
import com.telefonica.euro_iaas.sdc.validation.ProductReleaseValidator;
import com.xmlsolutions.annotation.UseCase;

/**
 * Default ProductManager implementation.
 * 
 * @author Sergio Arroyo, Jesus M. Movilla
 */
@UseCase(traceTo = "UC_101", status = "partially implemented")
public class ProductManagerImpl extends BaseInstallableManager implements ProductManager {

    private ProductReleaseValidator validator;
    private ProductDao productDao;
    private ProductReleaseDao productReleaseDao;
    private OSDao osDao;
    private static Logger LOGGER = Logger.getLogger("ProductManagerImpl");

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findByCriteria(ProductSearchCriteria criteria) {
        return productDao.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product load(String name) throws EntityNotFoundException {
        return productDao.load(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    public ProductRelease insert(ProductRelease productRelease, File cookbook, File installable)
        throws AlreadyExistsProductReleaseException, InvalidProductReleaseException {

        LOGGER.info("Inserting productRelease " + productRelease.getVersion() + " "
            + productRelease.getProduct().getName());

        ProductRelease productReleaseOut = insertProductReleaseBBDD(productRelease);

        ReleaseDto releaseDto = new ReleaseDto();
        releaseDto.setName(productRelease.getProduct().getName());
        releaseDto.setVersion(productRelease.getVersion());

        releaseDto.setType(propertiesProvider.getProperty(WEBDAV_PRODUCT_BASEDIR));

        uploadInstallable(installable, releaseDto);

        uploadRecipe(cookbook, releaseDto.getName());

        return productReleaseOut;
    }

    /**
     * Insert a ProductRelease in DB.
     * 
     * @param productRelease
     * @throws AlreadyExistsProductReleaseException
     *             , InvalidProductReleaseException
     */
    public ProductRelease insert(ProductRelease productRelease) throws AlreadyExistsProductReleaseException,
        InvalidProductReleaseException {
        LOGGER.info("Inserting productRelease " + productRelease.getVersion() + " "
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
        releaseDto.setType(propertiesProvider.getProperty(WEBDAV_PRODUCT_BASEDIR));

        deleteProductReleaseBBDD(productRelease);

        // deleteInstallable(releaseDto);

        // if (lastRelease)
        // deleteRecipe(releaseDto.getName(), releaseDto.getVersion());
    }

    /**
     * {@inheritDoc}
     * 
     * @throws InvalidProductReleaseException
     * @throws ProductReleaseNotFoundException
     */
    @Override
    @UseCase(traceTo = "UC_101.3", status = "implemented and tested")
    public ProductRelease update(ProductRelease productRelease, File cookbook, File installable)
        throws ProductReleaseNotFoundException, InvalidProductReleaseException {

        if (productRelease != null) {
            productRelease = updateProductReleaseBBDD(productRelease);
        }

        ReleaseDto releaseDto = new ReleaseDto();
        releaseDto.setName(productRelease.getProduct().getName());
        releaseDto.setVersion(productRelease.getVersion());
        releaseDto.setType(propertiesProvider.getProperty(WEBDAV_PRODUCT_BASEDIR));

        if (installable != null) {
            uploadInstallable(installable, releaseDto);
        }

        if (cookbook != null) {
            uploadRecipe(cookbook, releaseDto.getName());
        }

        return productRelease;
    }

    // *********** PRIVATE METHODS ************//
    private ProductRelease insertProductReleaseBBDD(ProductRelease productRelease)
        throws AlreadyExistsProductReleaseException, InvalidProductReleaseException {

        LOGGER.info("Inserting insertProductReleaseBBDD " + productRelease.getVersion() + " "
            + productRelease.getProduct().getName());
        Product product;
        ProductRelease productReleaseOut;

        List<OS> oss = insertProductReleaseLoadSO(productRelease);

        productRelease.setSupportedOOSS(oss);
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
                LOGGER.log(Level.INFO, "OS " + productRelease.getSupportedOOSS().get(i).getName() + " LOADED");
                oss.add(os);
            } catch (EntityNotFoundException e) {
                try {
                    os = osDao.create(productRelease.getSupportedOOSS().get(i));
                    LOGGER.log(Level.INFO, "OS " + productRelease.getSupportedOOSS().get(i).getName() + " CREATED");
                    oss.add(os);
                } catch (InvalidEntityException e1) {
                    String invalidOSMessageLog = "The supportedOS "
                        + productRelease.getSupportedOOSS().get(i).getName() + " in Product Release"
                        + productRelease.getProduct().getName() + productRelease.getVersion()
                        + " is invalid. Please Insert a valid OS";

                    LOGGER.log(Level.SEVERE, invalidOSMessageLog);
                    throw new InvalidProductReleaseException(invalidOSMessageLog, e1);

                } catch (AlreadyExistsEntityException e1) {
                    LOGGER.log(Level.SEVERE, e1.getMessage());
                    throw new SdcRuntimeException(e1);
                }
            }
        }
        return oss;
    }

    private Product insertProductReleaseLoadProduct(ProductRelease productRelease) 
        throws InvalidProductReleaseException {

        LOGGER.info("Inserting insertProductReleaseLoadProduct " + productRelease.getVersion() + " "
            + productRelease.getProduct().getName());
        Product product, loadedProduct;
        // Insert Product if needed
        try {
            loadedProduct = productDao.load(productRelease.getProduct().getName());
            LOGGER.log(Level.INFO, "Product " + loadedProduct.getName() + " LOADED");

            if ((productRelease.getProduct().getAttributes() != null) && (loadedProduct.getAttributes() == null)) {
                LOGGER.info("Attributes " + productRelease.getProduct().getAttributes().size());
                for (Attribute att : productRelease.getAttributes()) {
                    loadedProduct.addAttribute(att);
                }
            }

            if ((productRelease.getProduct().getMetadatas() != null) && (loadedProduct.getMetadatas() == null)) {
                LOGGER.info("Metadatas " + productRelease.getProduct().getMetadatas().size());
                for (Metadata metadata : productRelease.getProduct().getMetadatas()) {
                    loadedProduct.addMetadata(metadata);
                }
            }

            product = productDao.update(loadedProduct);
            LOGGER.log(Level.INFO, "Product " + product.getName() + " UPDATED");

        } catch (EntityNotFoundException e) {
            try {
                product = productDao.create(productRelease.getProduct());
                LOGGER.log(Level.INFO, "Product " + product.getName() + " CREATED");
            } catch (InvalidEntityException e1) {
                String messageLog = "The Product " + productRelease.getProduct().getName() + " in Product Release"
                    + productRelease.getProduct().getName() + productRelease.getVersion()
                    + " is invalid. Please Insert a valid Product ";

                LOGGER.log(Level.SEVERE, messageLog);
                throw new InvalidProductReleaseException(messageLog, e1);

            } catch (AlreadyExistsEntityException e1) {
                LOGGER.log(Level.SEVERE, e1.getMessage());
                throw new SdcRuntimeException(e1);
            }
        } catch (InvalidEntityException iee) {
            String messageLog = "The Product " + productRelease.getProduct().getName() + " in Product Release"
                + productRelease.getProduct().getName() + productRelease.getVersion()
                + " is invalid. Please Insert a valid Product ";
            LOGGER.log(Level.SEVERE, messageLog);
            throw new InvalidProductReleaseException(messageLog, iee);
        }
        return product;
    }

    private ProductRelease insertProductRelease(ProductRelease productRelease) throws InvalidProductReleaseException,
        AlreadyExistsProductReleaseException {
        LOGGER.info("Inserting insertProductRelease " + productRelease.getVersion() + " "
               + productRelease.getProduct().getName());
        ProductRelease productReleaseOut;
        // Insert ProductRelease if not in BBDD
        try {
            productReleaseOut = productReleaseDao.load(productRelease.getProduct(), productRelease.getVersion());
            LOGGER.log(Level.INFO,
                "Product Release" + productRelease.getProduct().getName() + "-"
                + productRelease.getVersion() + " LOADED");
        } catch (EntityNotFoundException e) {
            try {
                productReleaseOut = productReleaseDao.create(productRelease);
                LOGGER.log(Level.INFO,
                      "ProductRelease " + productRelease.getProduct().getName() + "-"
                      + productRelease.getVersion() + " CREATED");
            } catch (InvalidEntityException e1) {
                String invalidEntityMessageLog = "The Product Release " + productRelease.getProduct().getName()
                      + productRelease.getVersion() + " is invalid. Please Insert a valid Product Release "
                      + e1.getMessage();

                LOGGER.log(Level.SEVERE, invalidEntityMessageLog);
                throw new InvalidProductReleaseException(invalidEntityMessageLog, e1);

            } catch (AlreadyExistsEntityException e1) {
                String alreadyExistsMessageLog = "The Product Release " + productRelease.getProduct().getName()
                    + productRelease.getVersion() + " already exist " + e1.getMessage();

                LOGGER.log(Level.SEVERE, alreadyExistsMessageLog);
                throw new AlreadyExistsProductReleaseException(alreadyExistsMessageLog, e1);
            } catch (Exception e1) {
                String invalidEntityMessageLog = "The Product Release " + productRelease.getProduct().getName()
                    + productRelease.getVersion() + " is invalid. Please Insert a valid Product Release "
                    + e1.getMessage();

                LOGGER.log(Level.SEVERE, invalidEntityMessageLog);
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
            LOGGER.log(Level.INFO, "Product " + existedProduct.getName() + " LOADED");

            if (productRelease.getProduct().getDescription() != null) {
                existedProduct.setDescription(productRelease.getProduct().getDescription());
            }
            if (productRelease.getProduct().getAttributes() != null) {
                existedProduct.setAttributes(productRelease.getProduct().getAttributes());
            }
            if (productRelease.getProduct().getMetadatas() != null) {
                existedProduct.setMetadatas(productRelease.getProduct().getMetadatas());
            }

            product = productDao.update(existedProduct);
            LOGGER.log(Level.INFO, "Product " + existedProduct.getName() + " UPDATED");

            // Cargamos el product otra vez por problemas con la politica de
            // bloqueo
            // que impone el @version en la entidad Product y ProductRelease
            // necesitamos la ultima copia que hay en BBDD
            // productOut = productDao.load(product.getName());
            // LOGGER.log(Level.INFO, "Product " + productOut.getName() +
            // " LOADED");

        } catch (EntityNotFoundException e) {
            String entityNotFoundMessageLog = "The Product " + productRelease.getProduct().getName()
                + " has not been found in the System ";

            LOGGER.log(Level.SEVERE, entityNotFoundMessageLog);
            throw new ProductReleaseNotFoundException(entityNotFoundMessageLog, e);

        } catch (InvalidEntityException e) {
            String invalidEntityException = "The Product " + productRelease.getProduct().getName()
                + " to be updated is Invalid ";

            LOGGER.log(Level.SEVERE, invalidEntityException);
            throw new InvalidProductReleaseException(invalidEntityException, e);
        }
        return product;
    }

    private ProductRelease updateProductRelease(ProductRelease productRelease) throws InvalidProductReleaseException,
        ProductReleaseNotFoundException {

        ProductRelease productReleaseOut, existedProductRelease;
        try {
            LOGGER.log(Level.INFO, "Product Release Before Loading " + productRelease.getProduct().getName() + "-"
                + productRelease.getVersion());

            existedProductRelease = productReleaseDao.load(productRelease.getProduct(), productRelease.getVersion());

            LOGGER.log(Level.INFO, "Product Release " + productRelease.getProduct().getName() + "-"
                + productRelease.getVersion() + " LOADED");

            if (productRelease.getPrivateAttributes() != null) {
                existedProductRelease.setPrivateAttributes(productRelease.getPrivateAttributes());
            }
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
            LOGGER.log(Level.INFO, "ProductRelease " + productRelease.getProduct().getName() + "-"
                 + productRelease.getVersion() + " UPDATED");

        } catch (EntityNotFoundException e) {
            String entityNotFoundMessageLog = "The Product Release" + productRelease.getProduct().getName() + "-"
                + productRelease.getVersion() + " has not been found in the System ";
            LOGGER.log(Level.SEVERE, entityNotFoundMessageLog);
            throw new ProductReleaseNotFoundException(entityNotFoundMessageLog, e);

        } catch (InvalidEntityException e) {
            String invalidEntityException = "The Product Release" + productRelease.getProduct().getName() + " version "
                + productRelease.getVersion() + " is Invalid ";

            LOGGER.log(Level.SEVERE, invalidEntityException);
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

    @Override
    public void delete(Product product) {
        productDao.remove(product);

    }
}
