package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.model.ProductReleases;
import com.telefonica.euro_iaas.sdc.client.services.ProductService;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

/**
 * Default ProductService implementation.
 *
 * @author Sergio Arroyo
 *
 */
public class ProductServiceImpl extends AbstractBaseService implements
        ProductService {

    public ProductServiceImpl(Client client, String baseUrl, String mediaType) {
        setBaseHost(baseUrl);
        setType(mediaType);
        setClient(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductRelease add(ProductReleaseDto releaseDto, InputStream cookbook,
            InputStream files) {
        try {

        MultiPart payload = new MultiPart().bodyPart(new BodyPart(
                releaseDto, MediaType.valueOf(getType())))
                .bodyPart(new BodyPart(
                        IOUtils.toByteArray(cookbook),
                        MediaType.APPLICATION_OCTET_STREAM_TYPE))
                .bodyPart(new BodyPart(
                        IOUtils.toByteArray(files),
                        MediaType.APPLICATION_OCTET_STREAM_TYPE));

        String url = getBaseHost() + ClientConstants.BASE_PRODUCT_PATH;
        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).type("multipart/mixed").entity(payload)
                .post(ProductRelease.class);
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
    public ProductRelease update(ProductReleaseDto releaseDto, InputStream cookbook,
            InputStream files) {
        try {

        MultiPart payload = new MultiPart().bodyPart(new BodyPart(
                releaseDto, MediaType.valueOf(getType())))
                .bodyPart(new BodyPart(
                        IOUtils.toByteArray(cookbook),
                        MediaType.APPLICATION_OCTET_STREAM_TYPE))
                .bodyPart(new BodyPart(
                        IOUtils.toByteArray(files),
                        MediaType.APPLICATION_OCTET_STREAM_TYPE));

        String url = getBaseHost() + MessageFormat.format(
                        ClientConstants.PRODUCT_RELEASE_PATH,
                        releaseDto.getProductName(), releaseDto.getVersion());
        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).type("multipart/mixed").entity(payload)
                .put(ProductRelease.class);
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

        String url = getBaseHost() + MessageFormat.format(
                        ClientConstants.PRODUCT_RELEASE_PATH, pname, version);;
        WebResource wr = getClient().resource(url);

        wr.accept(getType()).type(getType()).delete(ClientResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductRelease load(String product, String version)
            throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(
                ClientConstants.PRODUCT_RELEASE_PATH, product, version);
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
    public List<ProductRelease> findAll(Integer page, Integer pageSize,
            String orderBy, String orderType, String productName) {
        String url;
        if (StringUtils.isEmpty(productName)) {
            url = getBaseHost() +
                    ClientConstants.ALL_PRODUCT_RELEASE_PATH;
        } else {
            url = getBaseHost() + MessageFormat.format(
                    ClientConstants.BASE_PRODUCT_RELEASE_PATH, productName);
        }
        WebResource wr = getClient().resource(url);
        MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();
        searchParams = addParam(searchParams, "page", page);
        searchParams = addParam(searchParams, "pageSize", pageSize);
        searchParams = addParam(searchParams, "orderBy", orderBy);
        searchParams = addParam(searchParams, "orderType", orderType);

        return wr.queryParams(searchParams)
                .accept(getType()).get(ProductReleases.class);
    }

}
