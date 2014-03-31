/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Path;

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
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
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
    
    private SystemPropertiesProvider systemPropertiesProvider;

    private static Logger LOGGER = Logger.getLogger("ProductResourceImpl");

    /**
     * Insert a product into SDC Databse.
     * 
     * @param product
     * @return product
     */
    public Product insert(Product product) throws AlreadyExistsEntityException, InvalidEntityException {
        return productManager.insert(product);
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
    
    private List<Product> filterProducts (List<Product> products) {
    	List<Product> filterProducts = new ArrayList<Product> ();

    	for (Product product: products) {
    		
    		LOGGER.info("Product get public metadata " + product.getMapMetadata().get(PUBLIC_METADATA));
   		
    		if (product.getMapMetadata().get(PUBLIC_METADATA) != null && 
    				product.getMapMetadata().get(PUBLIC_METADATA).equals("no")) {
    			if (checkProduct (product)) {
    				LOGGER.info("ADding product " + product.getName());
    				filterProducts.add(product);
    			}    			
    		}
    		else {
    			LOGGER.info("ADding product " + product.getName());
    			filterProducts.add(product);
    		}
    	}
    	
    	return filterProducts;
    	
    }
    
    private boolean checkProduct (Product product) {
    	PaasManagerUser credentials = this.getCredentials();
    	LOGGER.info (product.getMapMetadata().get(TENANT_METADATA) + " " + credentials.getTenantId());
    	if (product.getMapMetadata().get(TENANT_METADATA)!= null && 
    			product.getMapMetadata().get(TENANT_METADATA).equals(credentials.getTenantId())) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Product load(String name) throws EntityNotFoundException {
        return productManager.load(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attribute> loadAttributes(String name) throws EntityNotFoundException {
        return productManager.load(name).getAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Metadata> loadMetadatas(String name) throws EntityNotFoundException {
        return productManager.load(name).getMetadatas();
    }

    /**
     * Delete the Product Resource.
     * 
     * @param name
     * @throws ProductReleaseNotFoundException
     * @throws ProductReleaseStillInstalledException
     */
    public void delete(String name) throws ProductReleaseNotFoundException, ProductReleaseStillInstalledException {
        Product product;
        try {
            product = productManager.load(name);
        } catch (EntityNotFoundException e) {
            throw new ProductReleaseNotFoundException(e);
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

}
