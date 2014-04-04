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
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.model.ProductInstances;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Attribute;
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

    public Task install(String vdc, ProductInstanceDto product, String callback, String token) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.INSTALL_PRODUCT_INSTANCE_PATH, vdc);
        WebResource.Builder builder = createWebResource (url, token, vdc);
        builder.entity(product);
        builder = addCallback(builder, callback);
        return builder.post(Task.class);

    }

    /**
     * {@inheritDoc}
     */

    public Task installArtifact(String vdc, String product, com.telefonica.euro_iaas.sdc.model.Artifact artifact,
            String callback, String token) {
        String url = getBaseHost() + MessageFormat.format(

        ClientConstants.INSTALL_ARTEFACT_INSTANCE_PATH, vdc, product);
        WebResource.Builder builder = createWebResource (url, token, vdc);
        builder.entity(artifact);
        builder = addCallback(builder, callback);
        return builder.post(Task.class);

    }

    public Task uninstallArtifact(String vdc, String productInstanceId, Artifact artefact, String callback, String token) {
        String url = getBaseHost() + MessageFormat.format(

        ClientConstants.UNINSTALL_ARTEFACT_INSTANCE_PATH, vdc, productInstanceId, artefact.getName());
        WebResource.Builder builder = createWebResource (url, token, vdc);
        builder = addCallback(builder, callback);
        return builder.delete(Task.class);

    }

    /**
     * {@inheritDoc}
     */

    public List<ProductInstance> findAll(String hostname, String domain, String ip, String fqn, Integer page,
            Integer pageSize, String orderBy, String orderType, Status status, String vdc, String productName, String token) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.BASE_PRODUCT_INSTANCE_PATH, vdc);
        WebResource webResource = getClient().resource(url);
    
    	 
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

        return webResource.queryParams(searchParams).accept(getType()).get(ProductInstances.class );

    }

    /**
     * {@inheritDoc}
     */

    public ProductInstance load(String vdc, String name, String token) throws ResourceNotFoundException {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.PRODUCT_INSTANCE_PATH, vdc, name);
        return this.loadUrl(url, token, vdc);
    }

    /**
     * {@inheritDoc}
     */

    public ProductInstance loadUrl(String url, String token, String tenant) throws ResourceNotFoundException {
        try {
        	Builder builder  = createWebResource (url, token, tenant);
            return builder.get(ProductInstance.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException(ProductInstance.class, url);
        }
    }

}
