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
    public Product add(Product product, String token, String tenant) throws InsertResourceException {
        String url = getBaseHost() + ClientConstants.BASE_PRODUCT_PATH;
        try {
            Builder wr = createWebResource(url, token, tenant);
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

        Builder wr = createWebResource(url, token, tenant);

        wr.accept(getType()).type(getType()).delete(ClientResponse.class);
    }

    /**
     * {@inheritDoc}
     */

    public Product load(String pName, String token, String tenant) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_PATH, pName);
        Builder wr = createWebResource(url, token, tenant);
        try {
            return wr.accept(getType()).get(Product.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException(Product.class, url);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Product> findAll(Integer page, Integer pageSize, String orderBy, String orderType, String token,
            String tenant) {
        String url = getBaseHost() + ClientConstants.BASE_PRODUCT_PATH;

        WebResource wr = getClient().resource(url);
        Builder builder = wr.accept(MediaType.APPLICATION_JSON);
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
        Builder wr = createWebResource(url, token, tenant);
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
        Builder wr = createWebResource(url, token, tenant);

        try {
            return wr.accept(getType()).get(Product.class).getMetadatas();
        } catch (Exception e) {
            throw new ResourceNotFoundException(Product.class, url);
        }
    }

}
