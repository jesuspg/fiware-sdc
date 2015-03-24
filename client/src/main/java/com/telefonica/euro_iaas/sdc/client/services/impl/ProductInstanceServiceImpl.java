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
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.model.ProductInstances;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.client.services.SdcClientConfig;
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

    private static Logger log = LoggerFactory.getLogger(ProductInstanceServiceImpl.class.getName());

    /**
     * @param clientConfig
     * @param baseHost
     * @param type
     */
    public ProductInstanceServiceImpl(SdcClientConfig clientConfig, String baseHost, String type) {
        setSdcClientConfig(clientConfig);
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
        log.info("Install " + vdc + " procut " + product);
        String url = getBaseHost() + MessageFormat.format(ClientConstants.INSTALL_PRODUCT_INSTANCE_PATH, vdc);
        log.info(url);
        Response response = null;
        try {
            Invocation.Builder builder = createWebResource(url, token, vdc);
            builder = addCallback(builder, callback);
            response = builder.post(Entity.entity(product, getType()));
            return response.readEntity(Task.class);

        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */

    public Task installArtifact(String vdc, String product, com.telefonica.euro_iaas.sdc.model.Artifact artifact,
            String callback, String token) {
        String url = getBaseHost() + MessageFormat.format(

        ClientConstants.INSTALL_ARTEFACT_INSTANCE_PATH, vdc, product);
        Response response = null;
        try {
            Invocation.Builder builder = createWebResource(url, token, vdc);
            builder = addCallback(builder, callback);
            response = builder.post(Entity.entity(artifact, getType()));
            return response.readEntity(Task.class);
        } finally {
            if (response != null) {
                response.close();
            }
        }

    }

    public Task uninstallArtifact(String vdc, String productInstanceId, Artifact artefact, String callback, String token) {
        String url = getBaseHost()
                + MessageFormat.format(ClientConstants.UNINSTALL_ARTEFACT_INSTANCE_PATH, vdc, productInstanceId,
                        artefact.getName());
        Response response = null;
        try {
            Invocation.Builder builder = createWebResource(url, token, vdc);
            builder = addCallback(builder, callback);
            response = builder.delete();
            return response.readEntity(Task.class);
        } finally {
            if (response != null) {
                response.close();
            }

        }
    }

    /**
     * {@inheritDoc}
     */

    public List<ProductInstance> findAll(String hostname, String domain, String ip, String fqn, Integer page,
            Integer pageSize, String orderBy, String orderType, Status status, String vdc, String productName,
            String token) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.BASE_PRODUCT_INSTANCE_PATH, vdc);
        Response response = null;
        try {
            WebTarget webResource = getClient().target(url);

            webResource.queryParam("hostname", hostname);
            webResource.queryParam("domain", domain);
            webResource.queryParam("ip", ip);
            webResource.queryParam("fqn", fqn);
            webResource.queryParam("page", page);
            webResource.queryParam("pageSize", pageSize);
            webResource.queryParam("orderBy", orderBy);
            webResource.queryParam("orderType", orderType);
            webResource.queryParam("status", status);
            webResource.queryParam("product", productName);

            response = webResource.request(getType()).accept(getType()).get();
            return response.readEntity(ProductInstances.class);
        } finally {
            if (response != null) {
                response.close();
            }

        }
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
        Response response = null;
        try {
            Invocation.Builder builder = createWebResource(url, token, tenant);
            response = builder.get();
            return response.readEntity(ProductInstance.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException(ProductInstance.class, url);
        } finally {
            if (response != null) {
                response.close();
            }

        }
    }

}
