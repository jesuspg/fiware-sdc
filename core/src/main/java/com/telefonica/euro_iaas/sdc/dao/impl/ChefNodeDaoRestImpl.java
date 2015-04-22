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

package com.telefonica.euro_iaas.sdc.dao.impl;

import static com.telefonica.euro_iaas.sdc.util.Configuration.CHEF_SERVER_NODES_PATH;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_ID;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_PASS;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telefonica.euro_iaas.sdc.dao.ChefClientConfig;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.util.Configuration;
import com.telefonica.euro_iaas.sdc.util.MixlibAuthenticationDigester;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.openstack.auth.exception.OpenStackException;

/**
 * Default implementation of ChefNodeManager.
 */
public class ChefNodeDaoRestImpl implements ChefNodeDao {

    MixlibAuthenticationDigester digester;
    ChefClientConfig clientConfig;
    private OpenStackRegion openStackRegion;
    private SystemPropertiesProvider propertiesProvider;

    private static Logger log = LoggerFactory.getLogger(ChefNodeDaoRestImpl.class);

    private String NODE_NOT_FOUND_PATTERN = "404";
    private String NODES_PATH = "/nodes";
    private static final int MAX_TIME = 900000;

    public ChefNode loadNodeFromHostname(String hostname, String token) throws EntityNotFoundException,
            CanNotCallChefException {
        log.info("Loading nodes " + hostname);
        String chefServerUrl = null;
        try {
            chefServerUrl = openStackRegion.getChefServerEndPoint();
        } catch (OpenStackException e) {
            throw new SdcRuntimeException(e);
        }
        try {
            String path = NODES_PATH;

            Map<String, String> header = getHeaders("GET", path, "");
            log.info(chefServerUrl + path);
            WebTarget webResource = clientConfig.getClient().target(chefServerUrl + path);
            Invocation.Builder wr = webResource.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            Response response = wr.get();

            String stringNodes = response.readEntity(String.class);

            if (stringNodes == null) {
                throw new EntityNotFoundException(ChefNode.class, null, "The ChefServer is empty of ChefNodes");
            }
            ChefNode node = new ChefNode();
            log.info(stringNodes);
            String nodeName = node.getChefNodeName(stringNodes, hostname);
            log.info("node name " + nodeName);
            return loadNode(nodeName, token);

            // } catch (UniformInterfaceException e) {
            // throw new CanNotCallChefException(e);
        } catch (Exception e) {
            log.warn("loadNodeFromHostname: ", e.getMessage());

            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */

    public ChefNode loadNode(String chefNodename, String token) throws CanNotCallChefException {
        log.info("loadNode " + chefNodename);
        String chefServerUrl = null;
        try {
            chefServerUrl = openStackRegion.getChefServerEndPoint();
            log.info("chefServerUrl " + chefServerUrl);
        } catch (OpenStackException e) {
            throw new SdcRuntimeException(e);
        }
        try {

            if (!chefNodename.startsWith("/")) {
                chefNodename = "/" + chefNodename;
            }

            String path = MessageFormat.format(Configuration.CHEF_SERVER_NODES_PATH, chefNodename);

            Map<String, String> header = getHeaders("GET", path, "");

            String url = chefServerUrl + path;
            log.info("url " + url);
            WebTarget webResource = clientConfig.getClient().target(url);
            Invocation.Builder wr = webResource.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            Response response = wr.get();

            String stringNode = response.readEntity(String.class);

            JSONObject jsonNode = JSONObject.fromObject(stringNode);

            ChefNode node = new ChefNode();
            node.fromJson(jsonNode);

            return node;
        } catch (Exception e) {
            log.warn("loadNode: ", e.getMessage());
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */

    public ChefNode updateNode(ChefNode node, String token) throws SdcRuntimeException {
        log.info("Update node " + node.getName());
        String chefServerUrl = null;
        try {
            chefServerUrl = openStackRegion.getChefServerEndPoint();
        } catch (OpenStackException e) {
            throw new SdcRuntimeException(e);
        }

        try {

            String path = MessageFormat.format(CHEF_SERVER_NODES_PATH, "/" + node.getName());
            log.info(chefServerUrl + path);
            String payload = node.toJson();
            Map<String, String> header = getHeaders("PUT", path, payload);

            WebTarget webResource = clientConfig.getClient().target(chefServerUrl).path(path);

            Invocation.Builder wr = webResource.request(MediaType.APPLICATION_JSON);
            wr = wr.accept(MediaType.APPLICATION_JSON);

            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }
            Response response = wr.put(Entity.json(payload));

            String stringNode = response.readEntity(String.class);
            JSONObject jsonNode = JSONObject.fromObject(stringNode);

            ChefNode updatedNode = new ChefNode();
            updatedNode.fromJson(jsonNode);

            return updatedNode;

        } catch (Exception ex) {
            log.warn("updateNode: ", ex.getMessage());
            throw new SdcRuntimeException(ex);
        }

    }

    public void deleteNode(ChefNode node, String token) throws CanNotCallChefException {
        String chefServerUrl = null;
        try {
            chefServerUrl = openStackRegion.getChefServerEndPoint();
        } catch (OpenStackException e) {
            throw new SdcRuntimeException(e);
        }
        try {
            String path = MessageFormat.format(CHEF_SERVER_NODES_PATH, "/" + node.getName());
            Map<String, String> header = getHeaders("DELETE", path, "");

            WebTarget webResource = clientConfig.getClient().target(chefServerUrl + path);

            Invocation.Builder wr = webResource.request(MediaType.APPLICATION_JSON);
            wr = wr.accept(MediaType.APPLICATION_JSON);

            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            wr.delete(InputStream.class);
        } catch (Exception e) {
            throw new CanNotCallChefException(e);
        }
    }

    /**
     * Checks if ChefNode is already registered in ChefServer.
     */
    public void isNodeRegistered(String hostname, String token) throws CanNotCallChefException {
        String chefServerUrl = null;
        try {
            chefServerUrl = openStackRegion.getChefServerEndPoint();
        } catch (OpenStackException e) {
            throw new SdcRuntimeException(e);
        }

        String path = "/nodes";

        String response = "RESPONSE";
        int time = 5000;
        int checkTime = 10000;
        while (!response.contains(hostname)) {

            try {
                log.info("Checking node : " + hostname + " MAX_TIME: " + MAX_TIME + " time:" + time);
                if (time > MAX_TIME) {
                    String errorMesg = "Node  " + hostname + " is not registered in ChefServer";
                    log.info(errorMesg);
                    throw new CanNotCallChefException(errorMesg);
                }
                log.info("more");
                Thread.sleep(checkTime);

                Map<String, String> header = getHeaders("GET", path, "");
                System.out.println(chefServerUrl + path);
                log.info(chefServerUrl + path);

                log.info("web resource");
                WebTarget webResource = clientConfig.getClient().target(chefServerUrl + path);
                Invocation.Builder wr = webResource.request(MediaType.APPLICATION_JSON).accept(
                        MediaType.APPLICATION_JSON);
                for (String key : header.keySet()) {
                    System.out.println(key + ":" + header.get(key));
                    wr = wr.header(key, header.get(key));
                }

                log.info("geting");
                response = IOUtils.toString(wr.get(InputStream.class));
                log.info(response);
                time = time + checkTime;

            } catch (IOException e) {
                log.warn(e.getMessage());
                throw new CanNotCallChefException(e);

            } catch (SdcRuntimeException e) {
                log.warn(e.getMessage());
                throw new CanNotCallChefException(e);

            } catch (InterruptedException e) {
                String errorMsg = e.getMessage();
                log.warn(e.getMessage());
                throw new CanNotCallChefException(errorMsg, e);
            }
        }
        log.info("Node  " + hostname + " is registered in ChefServer");
    }

    private Map<String, String> getHeaders(String method, String path, String payload) throws SdcRuntimeException {
        log.info("get headers");
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
