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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.EnvironmentDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsEnvironmentException;
import com.telefonica.euro_iaas.sdc.exception.EnvironmentNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidEnvironmentException;
import com.telefonica.euro_iaas.sdc.exception.ProductNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseInApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.manager.EnvironmentManager;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.EnvironmentSearchCriteria;
import com.telefonica.euro_iaas.sdc.validation.EnvironmentValidator;
import com.xmlsolutions.annotation.UseCase;

public class EnvironmentManagerImpl implements EnvironmentManager {

    private EnvironmentValidator validator;
    private ProductDao productDao;
    private ProductReleaseDao productReleaseDao;
    private EnvironmentDao environmentDao;
    private static Logger LOGGER = Logger.getLogger("EnvironmentManagerImpl");

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Environment> findAll() {
        return environmentDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Environment> findByCriteria(EnvironmentSearchCriteria criteria) {
        return environmentDao.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws EnvironmentNotFoundException
     */
    @Override
    public Environment load(String name) throws EnvironmentNotFoundException {

        try {
            return environmentDao.load(name);
        } catch (EntityNotFoundException e) {
            String environmentNotFoundException = " The Environment " + name + " DOES NOT EXIST ";
            LOGGER.log(Level.SEVERE, environmentNotFoundException);
            throw new EnvironmentNotFoundException(environmentNotFoundException);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ProductNotFoundException
     * @throws AlreadyExistsEnvironmentException
     * @throws InvalidEnvironmentException
     */
    @Override
    @UseCase(traceTo = "UC_105.1", status = "developed")
    public Environment insert(Environment environment) throws ProductNotFoundException,
            ProductReleaseNotFoundException, AlreadyExistsEnvironmentException, InvalidEnvironmentException {

        return insertEnvironmentBBDD(environment);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws EnvironmentNotFoundException
     * @throws ProductReleaseInApplicationReleaseException
     * @throws ProductReleaseStillInstalledException
     */
    @Override
    @UseCase(traceTo = "UC_105.2", status = "developed")
    public void delete(String name) throws EnvironmentNotFoundException, ProductReleaseStillInstalledException,
            ProductReleaseInApplicationReleaseException {

        deleteEnvironmentBBDD(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws AlreadyExistsApplicationReleaseException
     * @throws InvalidApplicationReleaseException
     * @throws ProductReleaseNotFoundException
     */
    @Override
    @UseCase(traceTo = "UC_105.2", status = "developed")
    public Environment update(Environment environment) throws EnvironmentNotFoundException,
            InvalidEnvironmentException, EnvironmentNotFoundException, ProductReleaseNotFoundException,
            ProductNotFoundException {

        return updateEnvironmentBBDD(environment);
    }

    private Environment insertEnvironmentBBDD(Environment environment) throws ProductNotFoundException,
            AlreadyExistsEnvironmentException, InvalidEnvironmentException, ProductReleaseNotFoundException {

        List<ProductRelease> productReleases = environment.getProductReleases();
        List<ProductRelease> productReleasesEnvironment = new ArrayList<ProductRelease>();
        Product product;
        ProductRelease productRelease;
        Environment env;

        for (int i = 0; i < productReleases.size(); i++) {
            // Product
            try {
                product = productDao.load(productReleases.get(i).getProduct().getName());
            } catch (EntityNotFoundException e) {
                String productNotFoundException = " The Product " + productReleases.get(i).getProduct().getName()
                        + " NOT FOUND ";
                LOGGER.log(Level.SEVERE, productNotFoundException);
                throw new ProductNotFoundException(productNotFoundException);
            }
            // Product Release
            try {
                productRelease = productReleaseDao.load(product, productReleases.get(i).getVersion());
            } catch (EntityNotFoundException e) {
                String productReleaseNotFoundException = " The Product Release "
                        + productReleases.get(i).getProduct().getName() + "-" + productReleases.get(i).getVersion()
                        + " NOT FOUND ";
                LOGGER.log(Level.SEVERE, productReleaseNotFoundException);
                throw new ProductReleaseNotFoundException(productReleaseNotFoundException);
            }
            productReleasesEnvironment.add(productRelease);
        }

        Environment envIn = new Environment(productReleasesEnvironment);
        // Environment
        try {
            env = environmentDao.load(envIn.getName());
        } catch (EntityNotFoundException e) {
            try {
                env = environmentDao.create(envIn);
                LOGGER.log(Level.INFO, "eNVIORNMENT " + env.getName() + " CREATED");
            } catch (InvalidEntityException e1) {
                String invalidEnvironmentException = " The Environment " + envIn.getName() + " is INVALID ";
                LOGGER.log(Level.SEVERE, invalidEnvironmentException);
                throw new InvalidEnvironmentException(invalidEnvironmentException);
            } catch (AlreadyExistsEntityException e1) {
                String alreadyExistsEnvironmentException = " The Environment " + envIn.getName() + " ALREADY EXISTS ";
                LOGGER.log(Level.SEVERE, alreadyExistsEnvironmentException);
                throw new AlreadyExistsEnvironmentException(alreadyExistsEnvironmentException);
            }

        }
        return env;
    }

    private Environment updateEnvironmentBBDD(Environment environment) throws EnvironmentNotFoundException,
            InvalidEnvironmentException, ProductNotFoundException, ProductReleaseNotFoundException {

        ProductRelease existedProductRelease;
        Product existedProduct;
        List<ProductRelease> existedProductReleases = new ArrayList<ProductRelease>();

        LOGGER.log(Level.INFO, "Environment  Before Loading " + environment.getName());

        for (int i = 0; i < environment.getProductReleases().size(); i++) {
            ProductRelease productRelease = environment.getProductReleases().get(i);
            Product product = productRelease.getProduct();

            try {
                existedProduct = productDao.load(product.getName());
            } catch (EntityNotFoundException e) {
                String productNotFoundException = " The Product " + productRelease.getProduct().getName()
                        + " NOT FOUND ";
                LOGGER.log(Level.SEVERE, productNotFoundException);
                throw new ProductNotFoundException(productNotFoundException);
            }

            try {
                existedProductRelease = productReleaseDao.load(existedProduct, productRelease.getVersion());
            } catch (EntityNotFoundException e) {
                String productReleaseNotFoundException = " The Product Release "
                        + productRelease.getProduct().getName() + "-" + productRelease.getVersion() + " NOT FOUND ";
                LOGGER.log(Level.SEVERE, productReleaseNotFoundException);
                throw new ProductReleaseNotFoundException(productReleaseNotFoundException);
            }

            existedProductReleases.add(existedProductRelease);
        }
        environment.setProductReleases(existedProductReleases);

        // Update Environment
        try {
            environment = environmentDao.update(environment);
        } catch (InvalidEntityException e) {
            String InvalidEnvironmentNotFoundException = " The Environment " + environment.getName() + " IS NOT VALID ";
            LOGGER.log(Level.SEVERE, InvalidEnvironmentNotFoundException);
            throw new ProductReleaseNotFoundException(InvalidEnvironmentNotFoundException);

        }
        return environment;
    }

    private void deleteEnvironmentBBDD(String name) throws EnvironmentNotFoundException,
            ProductReleaseStillInstalledException, ProductReleaseInApplicationReleaseException {

        Environment env;
        try {
            env = environmentDao.load(name);
            // validator.validateDelete(env);
        } catch (EntityNotFoundException e) {
            String environmentNotFoundException = " The Environment " + name + " DOES NOT EXIST ";
            LOGGER.log(Level.SEVERE, environmentNotFoundException);
            throw new EnvironmentNotFoundException(environmentNotFoundException);
        }
        environmentDao.remove(env);
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
     * @param environmentDao
     *            the environmentDao to set
     */
    public void setEnvironmentDao(EnvironmentDao environmentDao) {
        this.environmentDao = environmentDao;
    }

    /**
     * @param validator
     *            the validator to set
     */
    public void setValidator(EnvironmentValidator validator) {
        this.validator = validator;
    }

}
