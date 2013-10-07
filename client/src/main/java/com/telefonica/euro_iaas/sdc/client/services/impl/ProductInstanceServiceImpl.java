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
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.model.ProductInstances;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;

/**
 * ProductInstanceService implementation.
 * 
 * @author Sergio Arroyo
 */
public class ProductInstanceServiceImpl extends AbstractInstallableService implements ProductInstanceService {

    /**
     * @param client
     * @param baseHost
     * @param type
     */
    public ProductInstanceServiceImpl(Client client, String baseHost, String type) {
        setClient(client);
        setBaseHost(baseHost);
        setType(type);
        setUpgradePath(ClientConstants.UPGRADE_PRODUCT_INSTANCE_PATH);
        setUninstallPath(ClientConstants.PRODUCT_INSTANCE_PATH);
        setConfigPath(ClientConstants.PRODUCT_INSTANCE_PATH);
    }

    /**
     * {@inheritDoc}
     */

    public Task install(String vdc, ProductInstanceDto product, String callback) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.INSTALL_PRODUCT_INSTANCE_PATH, vdc);
        WebResource wr = getClient().resource(url);
        WebResource.Builder builder = wr.accept(getType()).type(getType()).entity(product);
        builder = addCallback(builder, callback);
        return builder.post(Task.class);

    }

    /**
     * {@inheritDoc}
     */

    public Task installArtifact(String vdc, String product, com.telefonica.euro_iaas.sdc.model.Artifact artifact,
            String callback) {
        String url = getBaseHost() + MessageFormat.format(

        ClientConstants.INSTALL_ARTEFACT_INSTANCE_PATH, vdc, product);
        WebResource wr = getClient().resource(url);
        WebResource.Builder builder = wr.accept(getType()).type(getType()).entity(artifact);
        builder = addCallback(builder, callback);
        return builder.post(Task.class);

    }

    public Task uninstallArtifact(String vdc, String productInstanceId, Artifact artefact, String callback) {
        String url = getBaseHost() + MessageFormat.format(

        ClientConstants.UNINSTALL_ARTEFACT_INSTANCE_PATH, vdc, productInstanceId, artefact.getName());
        WebResource wr = getClient().resource(url);
        WebResource.Builder builder = wr.accept(getType()).type(getType());
        builder = addCallback(builder, callback);
        return builder.delete(Task.class);

    }

    /**
     * {@inheritDoc}
     */

    public List<ProductInstance> findAll(String hostname, String domain, String ip, String fqn, Integer page,
            Integer pageSize, String orderBy, String orderType, Status status, String vdc, String productName) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.BASE_PRODUCT_INSTANCE_PATH, vdc);
        WebResource wr = getClient().resource(url);
        MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();
        searchParams = addParam(searchParams, "hostname", hostname);
        searchParams = addParam(searchParams, "domain", domain);
        searchParams = addParam(searchParams, "ip", ip);
        searchParams = addParam(searchParams, "fqn", fqn);
        searchParams = addParam(searchParams, "page", page);
        searchParams = addParam(searchParams, "pageSize", pageSize);
        searchParams = addParam(searchParams, "orderBy", orderBy);
        searchParams = addParam(searchParams, "orderType", orderType);
        searchParams = addParam(searchParams, "status", status);
        searchParams = addParam(searchParams, "product", productName);

        return wr.queryParams(searchParams).accept(getType()).get(ProductInstances.class);

    }

    /**
     * {@inheritDoc}
     */

    public ProductInstance load(String vdc, String name) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_INSTANCE_PATH, vdc, name);
        return this.load(url);
    }

    /**
     * {@inheritDoc}
     */

    public ProductInstance load(String url) throws ResourceNotFoundException {
        try {
            WebResource wr = getClient().resource(url);
            return wr.accept(getType()).get(ProductInstance.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException(ProductInstance.class, url);
        }
    }

}
