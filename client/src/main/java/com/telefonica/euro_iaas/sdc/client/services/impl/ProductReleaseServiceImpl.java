/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
import com.sun.jersey.api.client.WebResource.Builder;
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
    public ProductRelease add(ProductReleaseDto releaseDto, InputStream cookbook, InputStream files, String token, String tenant) {
        try {

            MultiPart payload = new MultiPart().bodyPart(new BodyPart(releaseDto, MediaType.valueOf(getType())))
                    .bodyPart(new BodyPart(IOUtils.toByteArray(cookbook), MediaType.APPLICATION_OCTET_STREAM_TYPE))
                    .bodyPart(new BodyPart(IOUtils.toByteArray(files), MediaType.APPLICATION_OCTET_STREAM_TYPE));

            String url = getBaseHost()
                    + MessageFormat.format(ClientConstants.BASE_PRODUCT_RELEASE_PATH, releaseDto.getProductName());
            Builder wr = createWebResource(url, token,tenant);
            return wr.accept(getType()).type(MultiPartMediaTypes.MULTIPART_MIXED_TYPE)
                    .post(ProductRelease.class, payload);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ProductRelease add(ProductReleaseDto productReleaseDto, String token, String tenant) {
        String url = getBaseHost()
                + MessageFormat.format(ClientConstants.BASE_PRODUCT_RELEASE_PATH, productReleaseDto.getProductName());
        Builder wr = createWebResource(url, token,tenant);
        return wr.post(ProductRelease.class, productReleaseDto);

    }

    /**
     * {@inheritDoc}
     */
    public ProductRelease update(ProductReleaseDto releaseDto, InputStream cookbook, InputStream files, String token, String tenant) {
        try {

            MultiPart payload = new MultiPart().bodyPart(new BodyPart(releaseDto, MediaType.valueOf(getType())))
                    .bodyPart(new BodyPart(IOUtils.toByteArray(cookbook), MediaType.APPLICATION_OCTET_STREAM_TYPE))
                    .bodyPart(new BodyPart(IOUtils.toByteArray(files), MediaType.APPLICATION_OCTET_STREAM_TYPE));

            String url = getBaseHost()
                    + MessageFormat.format(ClientConstants.PRODUCT_RELEASE_PATH, releaseDto.getProductName(),
                            releaseDto.getVersion());
            Builder wr = createWebResource(url, token,tenant);
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
    
    public void delete(String pname, String version, String token, String tenant) {

        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_RELEASE_PATH, pname, version);
        Builder wr = createWebResource(url, token,tenant);

        wr.accept(getType()).type(getType()).delete(ClientResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    public ProductRelease load(String product, String version, String token, String tenant) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_RELEASE_PATH, product, version);
        Builder wr = createWebResource(url, token,tenant);
        try {
            return wr.accept(getType()).get(ProductRelease.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException(ProductRelease.class, url);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<ProductRelease> findAll(Integer page, Integer pageSize, String orderBy, String orderType,
            String productName, String osType, String token, String tenant) {
        String url;
        url = getBaseHost() + MessageFormat.format(ClientConstants.BASE_PRODUCT_RELEASE_PATH, productName);
        WebResource wr = getClient().resource(url);
    	Builder builder = wr.accept(MediaType.APPLICATION_JSON);
    	 System.out.println (url);
    	 System.out.println ("token  " + token);
    	 System.out.println ("tenant " + tenant);
    	 builder.header("X-Auth-Token", token);
    	 builder.header("Tenant-Id", tenant);
        MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();
        searchParams = addParam(searchParams, "osType", osType);
        searchParams = addParam(searchParams, "page", page);
        searchParams = addParam(searchParams, "pageSize", pageSize);
        searchParams = addParam(searchParams, "orderBy", orderBy);
        searchParams = addParam(searchParams, "orderType", orderType);

        return wr.queryParams(searchParams).accept(getType()).get(ProductReleases.class);
    }

}
