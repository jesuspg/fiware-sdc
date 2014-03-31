/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.text.MessageFormat;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.InsertResourceException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.model.Products;
import com.telefonica.euro_iaas.sdc.client.services.ProductService;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;

/**
 * Default ProductService implementation.
 * 
 * @author Sergio Arroyo
 */
public class ProductServiceImpl extends AbstractBaseService implements ProductService {

    public ProductServiceImpl(Client client, String baseUrl, String mediaType) {
        setBaseHost(baseUrl);
        setType(mediaType);
        setClient(client);
    }

    /**
     * {@inheritDoc}
     */
    public Product add(Product product,  String token, String tenant) throws InsertResourceException {
        String url = getBaseHost() + ClientConstants.BASE_PRODUCT_PATH;
        try {
        	Builder wr = createWebResource (url, token, tenant);
            return wr.accept(getType()).type(getType()).entity(product).post(Product.class);
        } catch (Exception e) {
            throw new InsertResourceException(Product.class, url);
        }
    }

    /**
     * {@inheritDoc}
     */

    public void delete(String pname, String token, String tenant) {

        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_PATH, pname);

        Builder wr = createWebResource (url, token, tenant);

        wr.accept(getType()).type(getType()).delete(ClientResponse.class);
    }

    /**
     * {@inheritDoc}
     */

    public Product load(String pName, String token, String tenant) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_PATH, pName);
        Builder wr = createWebResource (url, token, tenant);
        try {
            return wr.accept(getType()).get(Product.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException(Product.class, url);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Product> findAll(Integer page, Integer pageSize, String orderBy, String orderType, String token, String tenant) {
        String url = getBaseHost() + ClientConstants.BASE_PRODUCT_PATH;

        WebResource wr = getClient().resource(url);
    	Builder builder = wr.accept(MediaType.APPLICATION_JSON);
    	 System.out.println (url);
    	 System.out.println ("token  " + token);
    	 System.out.println ("tenant " + tenant);
    	 builder.header("X-Auth-Token", token);
    	 builder.header("Tenant-Id", tenant);
        MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();
        searchParams = addParam(searchParams, "page", page);
        searchParams = addParam(searchParams, "pageSize", pageSize);
        searchParams = addParam(searchParams, "orderBy", orderBy);
        searchParams = addParam(searchParams, "orderType", orderType);

        return wr.queryParams(searchParams).accept(getType()).get(Products.class);
    }

    /**
     * {@inheritDoc}
     */
    public List<Attribute> loadAttributes(String pName, String token, String tenant) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_PATH_ATTRIBUTES, pName);
        Builder wr = createWebResource (url, token, tenant);
        try {
            return wr.accept(getType()).get(Product.class).getAttributes();
        } catch (Exception e) {
            throw new ResourceNotFoundException(Product.class, url);
        }
    }

    /**
     * {@inheritDoc}
     */

    public List<Metadata> loadMetadatas(String pName, String token, String tenant) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_PATH_METADATAS, pName);
        Builder wr = createWebResource (url, token, tenant);
        
        try {
            return wr.accept(getType()).get(Product.class).getMetadatas();
        } catch (Exception e) {
            throw new ResourceNotFoundException(Product.class, url);
        }
    }


}
