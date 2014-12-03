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
import com.telefonica.euro_iaas.sdc.exception.InvalidProductException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
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
		} catch (InvalidProductException e) {
		    throw new APIException(new InvalidProductException(e));
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
     * It finds all product.
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @return
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
     * It loads the product.
     * @param name
     *            the product name
     * @return
     * @throws APIException
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
     * It obtains all attributes from the product.
     * @param name
     *            the product name
     * @return
     * @throws EntityNotFoundException
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
     * It updates an attriute from the product
     * @param name
     *            the product name
     * @param attributeName
     *            the product attribute name
     * @param attribute
     * @throws EntityNotFoundException
     */
    @Override
    public void updateAttribute(String name, String attributeName, Attribute attribute) throws EntityNotFoundException {
        Product product = null;
        Attribute updatedAttribute = null;
        try {
            product = productManager.load(name);
            updatedAttribute = product.getAttribute(attributeName);
            if (updatedAttribute == null || !updatedAttribute.getKey().equals(attributeName)) {
                log.warning("Exception: metadata not found or wrong " + attribute.getKey());
                throw new APIException(new EntityNotFoundException(Metadata.class, "Metadata not found : " + attributeName, attribute));
            }
        } catch (Exception e) {
            log.warning("EntityNotFoundException: " + e.getMessage());
            throw new APIException(new EntityNotFoundException(Product.class, name, e));
        }
        product.updateAttribute(updatedAttribute);
        try {
            productManager.update(product);
        } catch (Exception e) {
            log.warning("Exception but it works "+ e.getMessage());
        }
    }

    /**
     * It inserts new attribute in the product.
     * @param name
     *            the product name
     * @param attribute
     *            the the attribute to be added
     * @throws EntityNotFoundException
     */
    @Override
    public void insertAttribute(String name, Attribute attribute) throws EntityNotFoundException {
        Product product = null;
        try {
            product = productManager.load(name);
        } catch (Exception e) {
            log.warning("EntityNotFoundException: " + e.getMessage());
            throw new APIException(new EntityNotFoundException(Product.class, name, e));
        }
        product.addAttribute(attribute);
        productManager.update(product);
    }

    /**
     * It loads an attribute.
     * @param name
     *            the product name
     * @param attName
     *            the product metadata name
     * @return attribute
     * @throws EntityNotFoundException
     */
    @Override
    public Attribute loadAttribute(String name, String attName) throws EntityNotFoundException {
        Product product = null;
        try {
            product =  productManager.load(name);
            if (product.getAttribute(attName) == null ) {
                log.warning("Attribute not found : " + attName);
                throw new APIException(new EntityNotFoundException(Metadata.class, "Attribute not found : " + attName, attName));
            }
            else {
                return product.getAttribute(attName);
            }
        } catch (EntityNotFoundException e) {
            log.warning("The product: " + name + " is not in the database");
            throw new APIException(new EntityNotFoundException(Product.class,name, e));
        }
    }

    /**
     * It deletes the product attribute.
     * @param name
     *            the product name
     * @param attName
     * @throws EntityNotFoundException
     */
    @Override
    public void deleteAttribute(String name, String attName) throws EntityNotFoundException {
        Product product = null;
        try {
            product =  productManager.load(name);
            if (product.getAttribute(attName) == null ) {
                log.warning("Attribute not found : " + attName);
                throw new APIException(new EntityNotFoundException(Metadata.class, "Attribute not found : " + attName, attName));
            }
            else {
                product.deleteMetadata(attName);
                productManager.update(product);
            }
        } catch (EntityNotFoundException e) {
            log.warning("The product: " + name + " is not in the database");
            throw new APIException(new EntityNotFoundException(Product.class,name, e));
        }
    }

    /**
     * It loads all metadatas from a product.
     * @param name
     *            the product name
     * @return
     * @throws EntityNotFoundException
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
        Metadata updatedMetadata = null;
        try {
            product = productManager.load(name);
            updatedMetadata = product.getMetadata(metadataName);
            if (updatedMetadata == null || !updatedMetadata.getKey().equals(metadataName)) {
                log.warning("Exception: metadata not found or wrong " + metadata.getKey());
                throw new APIException(new EntityNotFoundException(Metadata.class, "Metadata not found : " + metadataName, metadata));
            }
        } catch (Exception e) {
            log.warning("EntityNotFoundException: " + e.getMessage());
            throw new APIException(new EntityNotFoundException(Product.class, name, e));
        }
        if (metadata.getDescription()!= null) {
            updatedMetadata.setDescription(metadata.getDescription());
        }
        updatedMetadata.setValue(metadata.getValue());
        try {
            productManager.update(product);
        } catch (Exception e) {
            log.warning("Exception but it works "+ e.getMessage());
        }
    }

    /**
     * It inser
     * @param name
     *            the product name
     * @param metadata
     *            the metadata to be added
     * @throws EntityNotFoundException
     */
    public void insertMetadata(String name, Metadata metadata) throws EntityNotFoundException {
        Product product = null;
        try {
            product = productManager.load(name);
        } catch (Exception e) {
            log.warning("EntityNotFoundException: " + e.getMessage());
            throw new APIException(new EntityNotFoundException(Product.class, name, e));
        }
        product.addMetadata(metadata);
        productManager.update(product);
    }


    /**
     * It loads the metadata.
     * @param name
     *            the product name
     * @param metadataName
     *            the product metadata name
     * @return
     * @throws EntityNotFoundException
     */
    public Metadata loadMetadata(String name, String metadataName) throws EntityNotFoundException {
        Product product = null;
        try {
            product =  productManager.load(name);
            if (product.getMetadata(metadataName) == null ) {
                log.warning("Metadata not found : " + metadataName);
                throw new APIException(new EntityNotFoundException(Metadata.class, "Metadata not found : " + metadataName, metadataName));
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
     * It deletes the metadata from the product.
     * @param name
     *            the product name
     * @param metadataName
     *            the product metadata name
     * @throws EntityNotFoundException
     */
    @Override
    public void deleteMetadata(String name, String metadataName) throws EntityNotFoundException {
        Product product = null;
        try {
            product =  productManager.load(name);
            if (product.getMetadata(metadataName) == null ) {
                log.warning("Metadata not found : " + metadataName);
                throw new APIException(new EntityNotFoundException(Metadata.class, "Metadata not found : " + metadataName, metadataName));
            }
            else {
                product.deleteMetadata(metadataName);
                productManager.update(product);
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
