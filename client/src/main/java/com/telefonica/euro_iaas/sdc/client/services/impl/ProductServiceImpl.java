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

package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
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
    @Override
    public Product add(Product product) throws InsertResourceException{
        String url = getBaseHost() + ClientConstants.BASE_PRODUCT_PATH;
        try {
            WebResource wr = getClient().resource(url);
            return wr.accept(getType()).type("multipart/mixed").entity(product).post(Product.class);
        } catch (Exception e) {
            throw new InsertResourceException(Product.class, url);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String pname) {

        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_PATH, pname);

        WebResource wr = getClient().resource(url);

        wr.accept(getType()).type(getType()).delete(ClientResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product load(String pName) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_PATH, pName);
        WebResource wr = getClient().resource(url);
        try {
            return wr.accept(getType()).get(Product.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException(Product.class, url);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findAll(Integer page, Integer pageSize, String orderBy, String orderType) {
        String url = getBaseHost() + ClientConstants.BASE_PRODUCT_PATH;
        
        WebResource wr = getClient().resource(url);
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
    @Override
    public List<Attribute> loadAttributes(String pName) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_PATH_ATTRIBUTES, pName);
        WebResource wr = getClient().resource(url);
        try {
            return wr.accept(getType()).get(Product.class).getAttributes();
        } catch (Exception e) {
            throw new ResourceNotFoundException(Product.class, url);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Metadata> loadMetadatas(String pName) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_PATH_METADATAS, pName);
        WebResource wr = getClient().resource(url);
        try {
            return wr.accept(getType()).get(Product.class).getMetadatas();
        } catch (Exception e) {
            throw new ResourceNotFoundException(Product.class, url);
        }
    }

}
