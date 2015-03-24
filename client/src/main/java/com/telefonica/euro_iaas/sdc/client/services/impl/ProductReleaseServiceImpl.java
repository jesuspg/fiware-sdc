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

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartMediaTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ProductReleaseService;
import com.telefonica.euro_iaas.sdc.client.services.SdcClientConfig;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

/**
 * @author jesus.movilla
 */
public class ProductReleaseServiceImpl extends AbstractBaseService implements ProductReleaseService {

    private static Logger log = LoggerFactory.getLogger(ProductReleaseServiceImpl.class);

    public ProductReleaseServiceImpl(SdcClientConfig clientConfig, String baseUrl, String mediaType) {
        setBaseHost(baseUrl);
        setType(mediaType);
        this.setSdcClientConfig(clientConfig);
    }

    /**
     * {@inheritDoc}
     */
    public ProductRelease add(ProductReleaseDto releaseDto, InputStream cookbook, InputStream files, String token,
            String tenant) {
        Response response = null;
        try {

            MultiPart payload = new MultiPart().bodyPart(new BodyPart(releaseDto, MediaType.valueOf(getType())))
                    .bodyPart(new BodyPart(IOUtils.toByteArray(cookbook), MediaType.APPLICATION_OCTET_STREAM_TYPE))
                    .bodyPart(new BodyPart(IOUtils.toByteArray(files), MediaType.APPLICATION_OCTET_STREAM_TYPE));

            String url = getBaseHost()
                    + MessageFormat.format(ClientConstants.BASE_PRODUCT_RELEASE_PATH, releaseDto.getProductName());
            Invocation.Builder wr = createWebResource(url, token, tenant);
            response = wr.accept(getType()).accept(MultiPartMediaTypes.MULTIPART_MIXED_TYPE).post(Entity.text(payload));

            ProductRelease productRelease = response.readEntity(ProductRelease.class);
            return productRelease;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public ProductRelease add(ProductReleaseDto productReleaseDto, String token, String tenant) {
        String url = getBaseHost()
                + MessageFormat.format(ClientConstants.BASE_PRODUCT_RELEASE_PATH, productReleaseDto.getProductName());
        Response response = null;
        try {
            Invocation.Builder wr = createWebResource(url, token, tenant);
            response = wr.post(Entity.entity(productReleaseDto, getType()));
            ProductRelease productRelease = response.readEntity(ProductRelease.class);
            return productRelease;

        } finally {
            if (response != null) {
                response.close();
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    public ProductRelease update(ProductReleaseDto releaseDto, InputStream cookbook, InputStream files, String token,
            String tenant) {
        Response response = null;
        try {

            MultiPart payload = new MultiPart().bodyPart(new BodyPart(releaseDto, MediaType.valueOf(getType())))
                    .bodyPart(new BodyPart(IOUtils.toByteArray(cookbook), MediaType.APPLICATION_OCTET_STREAM_TYPE))
                    .bodyPart(new BodyPart(IOUtils.toByteArray(files), MediaType.APPLICATION_OCTET_STREAM_TYPE));

            String url = getBaseHost()
                    + MessageFormat.format(ClientConstants.PRODUCT_RELEASE_PATH, releaseDto.getProductName(),
                            releaseDto.getVersion());
            Invocation.Builder wr = createWebResource(url, token, tenant);
            response = wr.accept(getType()).accept("multipart/mixed").put(Entity.entity(payload, getType()));
            ProductRelease productRelease = response.readEntity(ProductRelease.class);
            return productRelease;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */

    public void delete(String pname, String version, String token, String tenant) {

        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_RELEASE_PATH, pname, version);
        Response response = null;
        try {
            Invocation.Builder wr = createWebResource(url, token, tenant);

            response = wr.accept(getType()).delete();

        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public ProductRelease load(String product, String version, String token, String tenant)
            throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_RELEASE_PATH, product, version);
        Invocation.Builder wr = createWebResource(url, token, tenant);
        Response response = null;
        try {
            response = wr.accept(getType()).get();

            ProductRelease productRelease = response.readEntity(ProductRelease.class);

            return productRelease;
        } catch (Exception e) {
            log.error("Error loading product: " + product + " version: " + version + " from: " + url, e);
            throw new ResourceNotFoundException(ProductRelease.class, url);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ProductRelease> findAll(Integer page, Integer pageSize, String orderBy, String orderType,
            String productName, String osType, String token, String tenant) {
        String url;
        url = getBaseHost() + MessageFormat.format(ClientConstants.BASE_PRODUCT_RELEASE_PATH, productName);
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
            wr.queryParam("osType", osType);

            response = wr.request().accept(getType()).get();
            List<ProductRelease> productReleases = (List<ProductRelease>) response.readEntity(ProductRelease.class);

            return productReleases;

        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
