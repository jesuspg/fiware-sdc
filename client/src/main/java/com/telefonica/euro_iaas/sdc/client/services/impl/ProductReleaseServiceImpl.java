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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.MultiPartMediaTypes;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.model.ProductReleases;
import com.telefonica.euro_iaas.sdc.client.services.ProductReleaseService;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

/**
 * @author jesus.movilla
 */
public class ProductReleaseServiceImpl extends AbstractBaseService implements ProductReleaseService {

    public ProductReleaseServiceImpl(Client client, String baseUrl, String mediaType) {
        setBaseHost(baseUrl);
        setType(mediaType);
        setClient(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductRelease add(ProductReleaseDto releaseDto, InputStream cookbook, InputStream files) {
        try {

            MultiPart payload = new MultiPart().bodyPart(new BodyPart(releaseDto, MediaType.valueOf(getType())))
                    .bodyPart(new BodyPart(IOUtils.toByteArray(cookbook), MediaType.APPLICATION_OCTET_STREAM_TYPE))
                    .bodyPart(new BodyPart(IOUtils.toByteArray(files), MediaType.APPLICATION_OCTET_STREAM_TYPE));

            String url = getBaseHost()
                    + MessageFormat.format(ClientConstants.BASE_PRODUCT_RELEASE_PATH, releaseDto.getProductName());
            WebResource wr = getClient().resource(url);
            return wr.accept(getType()).type(MultiPartMediaTypes.MULTIPART_MIXED_TYPE)
                    .post(ProductRelease.class, payload);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductRelease add(ProductReleaseDto productReleaseDto) {
        String url = getBaseHost()
                + MessageFormat.format(ClientConstants.BASE_PRODUCT_RELEASE_PATH, productReleaseDto.getProductName());
        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).type(getType()).post(ProductRelease.class, productReleaseDto);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductRelease update(ProductReleaseDto releaseDto, InputStream cookbook, InputStream files) {
        try {

            MultiPart payload = new MultiPart().bodyPart(new BodyPart(releaseDto, MediaType.valueOf(getType())))
                    .bodyPart(new BodyPart(IOUtils.toByteArray(cookbook), MediaType.APPLICATION_OCTET_STREAM_TYPE))
                    .bodyPart(new BodyPart(IOUtils.toByteArray(files), MediaType.APPLICATION_OCTET_STREAM_TYPE));

            String url = getBaseHost()
                    + MessageFormat.format(ClientConstants.PRODUCT_RELEASE_PATH, releaseDto.getProductName(),
                            releaseDto.getVersion());
            WebResource wr = getClient().resource(url);
            return wr.accept(getType()).type("multipart/mixed").entity(payload).put(ProductRelease.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String pname, String version) {

        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_RELEASE_PATH, pname, version);
        WebResource wr = getClient().resource(url);

        wr.accept(getType()).type(getType()).delete(ClientResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductRelease load(String product, String version) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_RELEASE_PATH, product, version);
        WebResource wr = getClient().resource(url);
        try {
            return wr.accept(getType()).get(ProductRelease.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException(ProductRelease.class, url);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductRelease> findAll(Integer page, Integer pageSize, String orderBy, String orderType,
            String productName, String osType) {
        String url;
        url = getBaseHost() + MessageFormat.format(ClientConstants.BASE_PRODUCT_RELEASE_PATH, productName);
        WebResource wr = getClient().resource(url);
        MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();
        searchParams = addParam(searchParams, "osType", osType);
        searchParams = addParam(searchParams, "page", page);
        searchParams = addParam(searchParams, "pageSize", pageSize);
        searchParams = addParam(searchParams, "orderBy", orderBy);
        searchParams = addParam(searchParams, "orderType", orderType);

        return wr.queryParams(searchParams).accept(getType()).get(ProductReleases.class);
    }
}
