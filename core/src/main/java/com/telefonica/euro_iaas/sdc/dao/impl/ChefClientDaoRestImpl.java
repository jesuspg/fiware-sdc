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

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.dao.impl;

import static com.telefonica.euro_iaas.sdc.util.Configuration.CHEF_SERVER_CLIENTS_PATH;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_ID;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_PASS;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telefonica.euro_iaas.sdc.dao.ChefClientConfig;
import com.telefonica.euro_iaas.sdc.dao.ChefClientDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.util.MixlibAuthenticationDigester;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.openstack.auth.exception.OpenStackException;

/**
 * @author jesus.movilla
 */
public class ChefClientDaoRestImpl implements ChefClientDao {

    SystemPropertiesProvider propertiesProvider;
    MixlibAuthenticationDigester digester;
    ChefClientConfig clientConfig;
    private static Logger log = LoggerFactory.getLogger(ChefClientDaoRestImpl.class);
    private OpenStackRegion openStackRegion;

    /**
     * It gets the chefclient by the hostname
     * 
     * @param hostname
     * @param token
     * @return
     * @throws EntityNotFoundException
     * @throws CanNotCallChefException
     */
    public ChefClient chefClientfindByHostname(String hostname, String token) throws EntityNotFoundException,
            CanNotCallChefException {

        try {
            String path = "/clients";
            String chefServer = null;
            try {
                chefServer = openStackRegion.getChefServerEndPoint();
            } catch (OpenStackException e) {
                throw new SdcRuntimeException(e);
            }
            log.info(chefServer + path);

            Map<String, String> header = getHeaders("GET", path, "");
            WebTarget webResource = clientConfig.getClient().target(chefServer + path);
            Invocation.Builder wr = webResource.request(MediaType.APPLICATION_JSON);
            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            String stringChefClients;
            stringChefClients = IOUtils.toString(wr.get(InputStream.class));

            if (stringChefClients == null)
                throw new EntityNotFoundException(ChefClient.class, null, "The ChefServer is empty of ChefClients");

            // JSONObject jsonChefClient = JSONObject.fromObject(stringChefClients);
            ChefClient chefClient = new ChefClient();
            String clientName = chefClient.getChefClientName(stringChefClients, hostname);

            return getChefClient(clientName, token);
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * It deletes the chefclient by the name
     * 
     * @param chefClientName
     *            the chefClientName to be deleted
     * @param token
     * @throws CanNotCallChefException
     */
    public void deleteChefClient(String chefClientName, String token) throws CanNotCallChefException {
        String chefServerUrl = null;
        try {
            chefServerUrl = openStackRegion.getChefServerEndPoint();
        } catch (OpenStackException e) {
            throw new SdcRuntimeException(e);
        }
        if (!chefClientName.startsWith("/")) {
            chefClientName = "/" + chefClientName;
        }
        String path = MessageFormat.format(CHEF_SERVER_CLIENTS_PATH, chefClientName);
        // String payload = node.toJson();
        Map<String, String> header = getHeaders("DELETE", path, "");

        log.info(chefServerUrl + path);
        WebTarget webResource = clientConfig.getClient().target(chefServerUrl + path);

        Invocation.Builder wr = webResource.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        for (String key : header.keySet()) {
            wr = wr.header(key, header.get(key));
        }

        wr.delete(InputStream.class);

    }

    /**
     * It get the chef-client
     * 
     * @param chefClientName
     * @param token
     * @return
     * @throws CanNotCallChefException
     * @throws EntityNotFoundException
     */
    public ChefClient getChefClient(String chefClientName, String token) throws CanNotCallChefException,
            EntityNotFoundException {
        String chefServerUrl = null;
        try {
            chefServerUrl = openStackRegion.getChefServerEndPoint();
        } catch (OpenStackException e) {
            throw new SdcRuntimeException(e);
        }
        if (!chefClientName.startsWith("/")) {
            chefClientName = "/" + chefClientName;
        }

        try {

            String path = MessageFormat.format(CHEF_SERVER_CLIENTS_PATH, chefClientName);
            log.info(chefServerUrl + path);

            Map<String, String> header = getHeaders("GET", path, "");
            WebTarget webResource = clientConfig.getClient().target(chefServerUrl + path);
            Invocation.Builder wr = webResource.request(MediaType.APPLICATION_JSON);
            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            String stringChefClient;
            stringChefClient = IOUtils.toString(wr.get(InputStream.class));

            if (stringChefClient == null)
                throw new EntityNotFoundException(ChefClient.class, null, "ChefClient " + chefClientName
                        + " is not in the ChefServer");

            JSONObject jsonChefClient = JSONObject.fromObject(stringChefClient);
            ChefClient chefClient = new ChefClient();
            chefClient.fromJson(jsonChefClient);
            return chefClient;
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
        } catch (Exception e) {
            throw new EntityNotFoundException(ChefClient.class, chefClientName, chefClientName);
        }
    }

    private Map<String, String> getHeaders(String method, String path, String payload) {

        return digester.digest(method, path, payload, new Date(), propertiesProvider.getProperty(CHEF_CLIENT_ID),
                propertiesProvider.getProperty(CHEF_CLIENT_PASS));
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param digester
     *            the digester to set
     */
    public void setDigester(MixlibAuthenticationDigester digester) {
        this.digester = digester;
    }

    /**
     * @param clientConfig
     *            the client to set
     */
    public void setClientConfig(ChefClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public void setOpenStackRegion(OpenStackRegion openStackRegion) {
        this.openStackRegion = openStackRegion;
    }

}
