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

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.InsertResourceException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.model.Products;
import com.telefonica.euro_iaas.sdc.client.services.ProductService;
import com.telefonica.euro_iaas.sdc.client.services.SdcClientConfig;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;

/**
 * Default ProductService implementation.
 * 
 * @author Sergio Arroyo
 */
public class ProductServiceImpl extends AbstractBaseService implements ProductService {

    public ProductServiceImpl(SdcClientConfig clientConfig, String baseUrl, String mediaType) {
        setBaseHost(baseUrl);
        setType(mediaType);
        setSdcClientConfig(clientConfig);
    }

    /**
     * {@inheritDoc}
     */
    public Product add(Product product, String token, String tenant) throws InsertResourceException {
        String url = getBaseHost() + ClientConstants.BASE_PRODUCT_PATH;
        Response response = null;
        try {
            Invocation.Builder wr = createWebResource(url, token, tenant);

            response = wr.accept(getType()).accept(getType()).post(Entity.entity(product, getType()));

            return response.readEntity(Product.class);
        } catch (Exception e) {
            throw new InsertResourceException(Product.class, url);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */

    public void delete(String pname, String token, String tenant) {

        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_PATH, pname);
        Response response = null;
        try {
            Invocation.Builder wr = createWebResource(url, token, tenant);

            response = wr.accept(getType()).accept(getType()).delete();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */

    public Product load(String pName, String token, String tenant) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_PATH, pName);
        Invocation.Builder wr = createWebResource(url, token, tenant);
        Response response = null;
        try {
            response = wr.accept(getType()).get();
            return response.readEntity(Product.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException(Product.class, url);

        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Product> findAll(Integer page, Integer pageSize, String orderBy, String orderType, String token,
            String tenant) {
        String url = getBaseHost() + ClientConstants.BASE_PRODUCT_PATH;
        Response response = null;
        try {

            WebTarget wr = getClient().target(url);
            Invocation.Builder builder = wr.request().accept(MediaType.APPLICATION_JSON);
            builder.header("X-Auth-Token", token);
            builder.header("Tenant-Id", tenant);

            wr.queryParam("page", page);
            wr.queryParam("pageSize", pageSize);
            wr.queryParam("orderBy", orderBy);
            wr.queryParam("orderType", orderType);
            response = builder.get();
            return response.readEntity(Products.class);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Attribute> loadAttributes(String pName, String token, String tenant) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_PATH_ATTRIBUTES, pName);
        Invocation.Builder wr = createWebResource(url, token, tenant);
        Response response = null;
        try {
            response = wr.accept(getType()).get();
            return response.readEntity(Product.class).getAttributes();
        } catch (Exception e) {
            throw new ResourceNotFoundException(Product.class, url);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */

    public List<Metadata> loadMetadatas(String pName, String token, String tenant) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_PATH_METADATAS, pName);
        Invocation.Builder wr = createWebResource(url, token, tenant);
        Response response = null;
        try {
            response = wr.accept(getType()).get();
            return response.readEntity(Product.class).getMetadatas();
        } catch (Exception e) {
            throw new ResourceNotFoundException(Product.class, url);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

}
