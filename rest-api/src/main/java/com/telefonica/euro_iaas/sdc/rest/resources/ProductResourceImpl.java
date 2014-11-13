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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.sdc.model.dto.ProductAndReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
import com.telefonica.euro_iaas.sdc.rest.exception.APIException;
import com.telefonica.euro_iaas.sdc.rest.validation.ProductResourceValidator;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * default ProductResource implementation.
 * 
 * @author Sergio Arroyo
 */
@Path("/catalog/product")
@Component
@Scope("request")
public class ProductResourceImpl implements ProductResource {

    // @InjectParam("productManager")

    public static String PUBLIC_METADATA = "public";
    public static String TENANT_METADATA = "tenant_id";

    private ProductManager productManager;
    private ProductResourceValidator validator;

    private SystemPropertiesProvider systemPropertiesProvider;

    private static Logger log = Logger.getLogger("ProductResourceImpl");

    /**
     * Insert a product into SDC Databse.
     * 
     * @param product
     * @return product
     */
    public Product insert(Product product) throws APIException {
    	 
    	Product productReturn = null;
    	try {
			validator.validateInsert(product);
		} catch (InvalidEntityException e) {
			log.warning("InvalidEntityException: " + e.getMessage());
			throw new APIException(new InvalidEntityException(Product.class, e));
		} catch (AlreadyExistsEntityException e) {
			log.warning("InvalidEntityException: " + e.getMessage());
			throw new APIException(new AlreadyExistsEntityException(Product.class, e));
		}

        try {
        	productReturn = productManager.insert(product, getTenantId ());
        	return productReturn;
		} catch (AlreadyExistsEntityException e) {
			log.warning("AlreadyExistsEntityException: " + e.getMessage());
			throw new APIException(new AlreadyExistsEntityException(Product.class, e));
		} catch (InvalidEntityException e) {
			log.warning("InvalidEntityException: " + e.getMessage());
			throw new APIException(new InvalidEntityException(product, e));
		}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findAll(Integer page, Integer pageSize, String orderBy, String orderType) {
        ProductSearchCriteria criteria = new ProductSearchCriteria();

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
        return filterProducts(productManager.findByCriteria(criteria));
    }
    
    private List<Product> filterProducts(List<Product> products) {
        List<Product> filterProducts = new ArrayList<Product>();

        for (Product product : products) {
            if (product.getMapMetadata().get(PUBLIC_METADATA) != null
                    && product.getMapMetadata().get(PUBLIC_METADATA).equals("no")) {
                if (checkProduct(product)) {
                    filterProducts.add(product);
                }
            } else {
                filterProducts.add(product);
            }
        }

        return filterProducts;

    }
    
    private boolean checkProduct(Product product) {
        PaasManagerUser credentials = this.getCredentials();
        if (credentials == null) {
        	return false;
        }
        if (product.getMapMetadata().get(TENANT_METADATA) != null
                && product.getMapMetadata().get(TENANT_METADATA).equals(credentials.getTenantId())) {
            return true;
        }
        return false;
    }

    public PaasManagerUser getCredentials() {
        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
            return (PaasManagerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            return null;
        }

    }
    
    private String getTenantId () {
    	if (getCredentials() == null) {
    		return "";
    	} else {
    		return getCredentials().getTenantId();
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product load(String name) throws APIException {
        try {
			return productManager.load(name);
		} catch (EntityNotFoundException e) {
			log.warning("EntityNotFoundException: " + e.getMessage());
			throw new APIException(new EntityNotFoundException(Product.class,name, e));
		}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attribute> loadAttributes(String name) throws EntityNotFoundException {
        try {
        	return productManager.load(name).getAttributes();
        } catch (EntityNotFoundException e) {
        	log.warning("EntityNotFoundException: " + e.getMessage());
        	throw new APIException(new EntityNotFoundException(Product.class,name, e));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Metadata> loadMetadatas(String name) throws EntityNotFoundException {
        try {
        	return productManager.load(name).getMetadatas();
        } catch (EntityNotFoundException e) {
        	log.warning("EntityNotFoundException: " + e.getMessage());
        	throw new APIException(new EntityNotFoundException(Product.class,name, e));
        }
       
    }

    @Override
    public void updateMetadata(String name, String metadataName, Metadata metadata) throws EntityNotFoundException {
        Product product = null;
        try {
            product = productManager.load(name);
            if (product.getMetadata(metadataName) == null || ! metadata.getKey().equals(metadataName)) {
                log.warning("Exception: metadata not found or wrong " + metadata);
                throw new APIException(new Exception("Exception: metadata not found or wrong " + metadata));
            }
        } catch (Exception e) {
            log.warning("EntityNotFoundException: " + e.getMessage());
            throw new APIException(new EntityNotFoundException(Product.class,name, e));
        }
        product.updateMetadata(metadata);
        productManager.update(product);

    }

    @Override
    public Metadata loadMetadata(String name, String metadataName) throws EntityNotFoundException {
        Product product = null;
        try {
            product =  productManager.load(name);
            if (product.getMetadata(metadataName) == null ) {
                log.warning("Metadata not found : " + metadataName);
                throw new APIException(new Exception("Metadata not found : " + metadataName));
            }
            else {
                return product.getMetadata(metadataName);
            }
        } catch (EntityNotFoundException e) {
            log.warning("EntityNotFoundException: " + e.getMessage());
            throw new APIException(new EntityNotFoundException(Product.class,name, e));
        }
    }


    /**
     * Delete the Product Resource.
     * 
     * @param name
     * @throws ProductReleaseNotFoundException
     * @throws ProductReleaseStillInstalledException
     */
    public void delete(String name)  throws APIException {
        Product product;
        try {
            product = productManager.load(name);
        } catch (EntityNotFoundException e) {
        	log.warning("EntityNotFoundException: " + e.getMessage());
        	throw new APIException(new EntityNotFoundException(Product.class,name, e));
        }
        productManager.delete(product);
    }

    /**
     * @param productManager
     *            the productManager to set
     */
    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }
    
    public void setValidator (ProductResourceValidator validator) {
    	this.validator=validator;
    }

}
