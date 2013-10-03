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

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
import com.telefonica.euro_iaas.sdc.rest.validation.ProductResourceValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * default ProductResource implementation
 * 
 * @author Sergio Arroyo
 */
@Path("/catalog/product")
@Component
@Scope("request")
public class ProductResourceImpl implements ProductResource {

    @InjectParam("productManager")
	  private ProductManager productManager;

	  private ProductResourceValidator validator;
	  private static Logger LOGGER = Logger.getLogger("ProductResourceImpl");

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
		    return productManager.findByCriteria(criteria);
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
	  public List<ProductRelease> findAll(String name, String osType, Integer page, Integer pageSize, String orderBy, 
	      String orderType) {
		
		    ProductReleaseSearchCriteria criteria = new ProductReleaseSearchCriteria();

		    if (!StringUtils.isEmpty(name)) {
			      try {
				        Product product = productManager.load(name);
				        criteria.setProduct(product);
			      } catch (EntityNotFoundException e) {
				        throw new SdcRuntimeException("Can not find the application " + name, e);
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
		    return productManager.findReleasesByCriteria(criteria);
	  }

    /**
     * Delete a product in SDC Database
     * 
     * @param name
     *           the name of the product
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
	   * @param validator
	   *            the validator to set
	   */
    public void setProductManager(ProductManager productManager) {
		    this.productManager = productManager;
	  }
}
