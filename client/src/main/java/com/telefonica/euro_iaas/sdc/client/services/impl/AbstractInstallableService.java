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
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.client.services.SdcClientConfig;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;

/**
 * Provides some commons methods to work with installable instances.
 * 
 * @author Sergio Arroyo
 */
public abstract class AbstractInstallableService extends AbstractBaseService {

    private String upgradePath;
    private String configPath;
    private String uninstallPath;
    private SdcClientConfig clientConfig;

    /**
     * See {@link ApplicationInstanceService#upgrade(String, Long, String, String)} or
     * {@link ProductInstanceService#upgrade(String, Long, String, String)}
     */
    public Task upgrade(String vdc, String name, String version, String callback, String token) {
        String url = getBaseHost() + MessageFormat.format(upgradePath, vdc, name, version);
        Response response = null;
        try {
            Invocation.Builder builder = createWebResource(url, token, vdc);
            builder = addCallback(builder, callback);
            response = builder.put(Entity.json(null));
            return response.readEntity(Task.class);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * See {@link ApplicationInstanceService#configure(String, Long, String, Attributes)} or
     * {@link ProductInstanceService#configure(String, Long, String, Attributes)}
     */
    public Task configure(String vdc, String name, String callback, List<Attribute> arguments, String token) {
        String url = getBaseHost() + MessageFormat.format(configPath, vdc, name);
        Response response = null;
        try {
            Invocation.Builder builder = createWebResource(url, token, vdc);
            builder = addCallback(builder, callback);
            Attributes attributes = new Attributes();
            attributes.addAll(arguments);
            // create Attributes object because Jersey can not write List<Attribute>
            response = builder.put(Entity.entity(attributes, getType()));
            return response.readEntity(Task.class);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * See {@link ApplicationInstanceService#uninstall(String, Long, String)} or
     * {@link ProductInstanceService#uninstall(String, Long, String)}
     */
    public Task uninstall(String vdc, String name, String callback, String token) {
        String url = getBaseHost() + MessageFormat.format(uninstallPath, vdc, name);
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

    protected Invocation.Builder addCallback(Invocation.Builder resource, String callback) {
        if (!StringUtils.isEmpty(callback)) {
            resource = resource.header("callback", callback);
        }
        return resource;
    }

    /**
     * @return the upgradePath
     */
    public String getUpgradePath() {
        return upgradePath;
    }

    /**
     * @param upgradePath
     *            the upgradePath to set
     */
    public void setUpgradePath(String upgradePath) {
        this.upgradePath = upgradePath;
    }

    /**
     * @return the configPath
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * @param configPath
     *            the configPath to set
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    /**
     * @return the uninstallPath
     */
    public String getUninstallPath() {
        return uninstallPath;
    }

    /**
     * @param uninstallPath
     *            the uninstallPath to set
     */
    public void setUninstallPath(String uninstallPath) {
        this.uninstallPath = uninstallPath;
    }

}
