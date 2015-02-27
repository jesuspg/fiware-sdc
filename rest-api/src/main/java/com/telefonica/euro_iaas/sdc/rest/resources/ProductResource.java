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

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.rest.exception.APIException;

/**
 * Provides a rest api to works with Product.
 * 
 * @author Sergio Arroyo
 */
public interface ProductResource {

    /**
     * Insert a product int SDC Database.
     *
     * @param product: the product
     * @return product: the created product
     * @throws APIException
     *             if there is any error.
     */
    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Product insert(Product product) throws APIException;

    /**
     * Retrieve all Products available created in the system.
     * 
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @return the created OS instances.
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Product> findAll(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
                    @QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType);

    /**
     * Retrieve the selected Product.
     * 
     * @param name
     *            the product name
     * @return the product.
     * @throws APIException
     *             if the product does not exists
     */
    @GET
    @Path("/{pName}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Product load(@PathParam("pName") String name) throws APIException;

    /**
     * Retrieve the common attributes for the selected product.
     * 
     * @param name
     *            the product name
     * @return the attributes.
     * @throws APIException
     *             if the product does not exists
     */
    @GET
    @Path("/{pName}/attributes/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Attribute> loadAttributes(@PathParam("pName") String name) throws APIException;

    /**
     * Add an attribute for the selected product.
     *
     * @param name
     *            the product name
     * @param attribute
     *            the the attribute to be added
     * @throws APIException
     *             if the product does not exists
     */
    @POST
    @Path("/{pName}/attributes/")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void insertAttribute(@PathParam("pName") String name, Attribute attribute) throws APIException;

    /**
     * Retrieve the attribute attribute-name for the selected product.
     *
     * @param name
     *            the product name
     * @param attributeName
     *            the product attribute name
     * @throws APIException
     *             if the product does not exists
     */
    @PUT
    @Path("/{pName}/attributes/{attributeName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void updateAttribute(@PathParam("pName") String name, @PathParam("attributeName") String attributeName,
        Attribute attribute) throws APIException;

    /**
     * Delete the attribute attribute-name for the selected product.
     *
     * @param name
     *            the product name
     * @param attributeName
     *            the product attribute name
     * @throws APIException
     *             if the product does not exists
     */
    @DELETE
    @Path("/{pName}/attributes/{attributeName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void deleteAttribute(@PathParam("pName") String name, @PathParam("attributeName") String attributeName)
        throws APIException;

    /**
     * Retrieve the attribute attributeName for the selected product.
     *
     * @param name
     *            the product name
     * @param attributeName
     *            the product attribute name
     * @return the attributes.
     * @throws APIException
     *             if the product does not exists
     */
    @GET
    @Path("/{pName}/attributes/{attributeName}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Attribute loadAttribute(@PathParam("pName") String name, @PathParam("attributeName") String attributeName)
        throws APIException;

    /**
     * Retrieve the metadatas for the selected product.
     * 
     * @param name
     *            the product name
     * @return the metadatas.
     * @throws APIException
     *             if the product does not exists
     */
    @GET
    @Path("/{pName}/metadatas/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<Metadata> loadMetadatas(@PathParam("pName") String name) throws APIException;

    /**
     * Add the metadata metadata-name for the selected product.
     *
     * @param name
     *            the product name
     * @param metadata
     *            the metadata to be added
     * @throws APIException
     *             if the product does not exists
     */
    @POST
    @Path("/{pName}/metadatas/")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void insertMetadata(@PathParam("pName") String name, Metadata metadata) throws APIException;

    /**
     * Update the metadata metadata-name for the selected product.
     *
     * @param name
     *            the product name
     * @param metadataName
     *            the product metadata name
     * @throws APIException
     *             if the product does not exists
     */
    @PUT
    @Path("/{pName}/metadatas/{metadataName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void updateMetadata(@PathParam("pName") String name, @PathParam("metadataName") String metadataName,
        Metadata metadata) throws APIException;

    /**
     * Delete the metadata metadata-name for the selected product.
     *
     * @param name
     *            the product name
     * @param metadataName
     *            the product metadata name
     * @throws APIException
     *             if the product does not exists
     */
    @DELETE
    @Path("/{pName}/metadatas/{metadataName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void deleteMetadata(@PathParam("pName") String name, @PathParam("metadataName") String metadataName)
        throws APIException;

    /**
     * Retrieve the metadata metadata-name for the selected product.
     *
     * @param name
     *            the product name
     * @param metadataName
     *            the product metadata name
     * @return the attributes.
     * @throws APIException
     *             if the product does not exists
     */
    @GET
    @Path("/{pName}/metadatas/{metadataName}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Metadata loadMetadata(@PathParam("pName") String name,
        @PathParam("metadataName") String metadataName) throws APIException;

    /**
     * Delete the Product in SDC Database.
     * 
     * @param name
     * @throws ProductReleaseNotFoundException
     * @throws ProductReleaseStillInstalledException
     */
    @DELETE
    @Path("/{pName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void delete(@PathParam("pName") String name) throws ProductReleaseNotFoundException,
        ProductReleaseStillInstalledException;
}
