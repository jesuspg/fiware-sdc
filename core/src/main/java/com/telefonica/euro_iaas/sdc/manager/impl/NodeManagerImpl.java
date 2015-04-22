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
package com.telefonica.euro_iaas.sdc.manager.impl;

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.HttpMethod;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telefonica.euro_iaas.sdc.dao.ChefClientDao;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.ChefClientExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.installator.impl.InstallatorChefImpl;
import com.telefonica.euro_iaas.sdc.installator.impl.InstallatorPuppetImpl;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.manager.NodeManager;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.model.dto.NodeDto;
import com.telefonica.euro_iaas.sdc.util.HttpsClient;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.openstack.auth.exception.OpenStackException;

/**
 * @author alberts
 */
public class NodeManagerImpl implements NodeManager {

    private ProductInstanceDao productInstanceDao;
    private ChefClientDao chefClientDao;
    private ChefNodeDao chefNodeDao;
    private HttpClient client;
    private HttpsClient httpsClient;
    private OpenStackRegion openStackRegion;

    private static Logger log = LoggerFactory.getLogger(NodeManagerImpl.class);
    private static Logger puppetLog = LoggerFactory.getLogger(InstallatorPuppetImpl.class);
    private static Logger chefLog = LoggerFactory.getLogger(InstallatorChefImpl.class);

    private static int HTTP_OK = 200;
    private static int HTTP_NOT_FOUND = 404;
    private static int HTTP_NO_IMPLEMENTED = 405;

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.manager.ChefClientManager#chefNodeDelete (java.lang.String, java.lang.String)
     */
    public void nodeDelete(String vdc, String nodeName, String token) throws NodeExecutionException {

        log.info("deleting node");
        boolean errorChef = false;
        boolean errorPuppet = false;
        try {
            puppetDelete(vdc, nodeName, token);
        } catch (Exception e) {
            log.warn("The node cannot be deleted in Puppet master");
            errorPuppet = true;
        }

        try {
            chefClientDelete(vdc, nodeName, token);
        } catch (Exception e2) {
            log.warn("The node cannot be deleted in Chef-server");
            errorChef = true;
        }

        if (errorPuppet && errorChef) {
            throw new NodeExecutionException("Error to delete the node " + nodeName);
        }

    }

    /**
     * It deletes the productInstances which correspond to the node.
     * 
     * @param nodeName
     * @throws EntityNotFoundException
     */
    public void deleteProductsInNode(String nodeName) throws EntityNotFoundException {
        List<ProductInstance> productInstances = null;

        String hostname = nodeName.split("\\.")[0];
        productInstances = productInstanceDao.findByHostname(hostname);

        for (int i = 0; i < productInstances.size(); i++) {
            productInstanceDao.remove(productInstances.get(i));
        }

    }

    private void puppetDelete(String vdc, String nodeName, String token) throws NodeExecutionException {

        puppetLog.info("deleting node " + nodeName + " from puppet master");

        String deleteUrl = null;
        try {
            deleteUrl = openStackRegion.getPuppetWrapperEndPoint() + "v2/node/" + nodeName;
        } catch (OpenStackException e2) {
            puppetLog.warn(e2.getMessage());
        }

        if (deleteUrl != null) {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put(HttpsClient.HEADER_AUTH, token);
            headers.put(HttpsClient.HEADER_TENNANT, vdc);

            try {
                int statusCode;
                statusCode = httpsClient.doHttps(HttpMethod.DELETE, deleteUrl, "", headers);

                if (statusCode == HTTP_OK || statusCode == HTTP_NOT_FOUND) { // 404 means node
                    // didn't exist in
                    // puppet
                    log.info("Node deleted");
                } else {
                    String msg = format("[puppet delete node] response code was: {0}", statusCode);
                    puppetLog.info(msg);
                    throw new NodeExecutionException(msg);
                }
                puppetLog.info("Node succesfully deleted from pupper master");
            } catch (IOException e) {
                puppetLog.info(e.getMessage());
                throw new NodeExecutionException(e);
            } catch (IllegalStateException e1) {
                puppetLog.info(e1.getMessage());
                throw new NodeExecutionException(e1);
            } catch (KeyManagementException | NoSuchAlgorithmException e2) {
                puppetLog.info(e2.getMessage());
                throw new NodeExecutionException(e2);
            }
        }

    }

    private void chefClientDelete(String vdc, String chefClientName, String token) throws ChefClientExecutionException {
        chefLog.info("deleting node " + chefClientName + " from chef server");

        ChefNode node;
        List<ProductInstance> productInstances = null;
        String hostname = null;
        try {
            // Eliminacion del nodo
            node = chefNodeDao.loadNode(chefClientName, token);
            chefNodeDao.deleteNode(node, token);
            chefLog.info("Node " + chefClientName + " deleted from Chef Server");
            chefClientDao.deleteChefClient(chefClientName, token);
        } catch (CanNotCallChefException e) {
            String errorMsg = "Error deleting the Node" + chefClientName + " in Chef server. Description: "
                    + e.getMessage();
            chefLog.warn(errorMsg);
            throw new ChefClientExecutionException(errorMsg, e);
        } catch (Exception e2) {
            String errorMsg = "The ChefClient " + chefClientName + " was not found in the system " + e2.getMessage();
            chefLog.info(errorMsg);
            throw new ChefClientExecutionException(errorMsg, e2);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.manager.ChefClientManager#chefClientfindAll ()
     */
    public ChefClient chefClientfindByHostname(String hostname, String token) throws EntityNotFoundException,
            ChefClientExecutionException {

        ChefClient chefClient;
        try {
            chefClient = chefClientDao.chefClientfindByHostname(hostname, token);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            String message = " An error ocurred invoing the Chef server to load " + "ChefClient whose hostname is  "
                    + hostname;
            log.info(message);
            throw new ChefClientExecutionException(message, e);
        }
        return chefClient;
    }

    /**
     * It obtains the chefClient from Chef-server.
     * 
     * @param chefClientName
     * @param token
     * @return
     * @throws ChefClientExecutionException
     * @throws EntityNotFoundException
     */
    public ChefClient chefClientload(String chefClientName, String token) throws ChefClientExecutionException,
            EntityNotFoundException {

        ChefClient chefClient = new ChefClient();
        try {
            chefClient = chefClientDao.getChefClient(chefClientName, token);
        } catch (EntityNotFoundException e) {
            String men = "The node is not in the chef-server: " + chefClientName + " : " + e.getMessage();
            log.warn(men);
            throw new EntityNotFoundException(ChefClient.class, men, chefClientName);
        } catch (Exception e) {
            String message = " An error ocurred invoing the Chef server to load ChefClient named " + chefClientName;
            log.info(message);
            throw new ChefClientExecutionException(message, e);
        }
        return chefClient;
    }

    /**
     * It obtains the node from puppet master
     * 
     * @param nodeName
     * @param token
     * @param vdc
     * @return
     * @throws ChefClientExecutionException
     * @throws EntityNotFoundException
     */
    public NodeDto puppetClientload(String nodeName, String token, String vdc) throws EntityNotFoundException {

        puppetLog.info("Getting node " + nodeName + " from puppet master");

        String url = null;
        NodeDto node = null;
        try {
            url = openStackRegion.getPuppetWrapperEndPoint() + "v2/node/" + nodeName;
        } catch (OpenStackException e2) {
            puppetLog.warn(e2.getMessage());
        }

        if (url != null) {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put(HttpsClient.HEADER_AUTH, token);
            headers.put(HttpsClient.HEADER_TENNANT, vdc);

            try {
                int statusCode;
                statusCode = httpsClient.doHttps(HttpMethod.GET, url, "", headers);

                // We considered no implemented
                if (statusCode == HTTP_OK || statusCode == HTTP_NO_IMPLEMENTED) {
                    log.info("Node obtained " + nodeName);
                    node = new NodeDto();
                    node.setSoftwareName(nodeName);

                } else {
                    String msg = format("[puppet get node] response code was: {0}", statusCode);
                    puppetLog.info(msg);
                    throw new EntityNotFoundException(ChefClient.class, msg, nodeName);
                }
            } catch (Exception e) {
                String msg = format("[puppet get node] response code was: {0}", e.getMessage());
                puppetLog.info(e.getMessage());
                throw new EntityNotFoundException(ChefClient.class, msg, nodeName);
            }
        }
        return node;
    }

    /**
     * @param chefClientDao
     *            the chefClientDao to set
     */
    public void setChefClientDao(ChefClientDao chefClientDao) {
        this.chefClientDao = chefClientDao;
    }

    /**
     * @param chefNodeDao
     *            the chefNodeDao to set
     */
    public void setChefNodeDao(ChefNodeDao chefNodeDao) {
        this.chefNodeDao = chefNodeDao;
    }

    /**
     * @param productInstanceDao
     *            the productInstanceDao to set
     */
    public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }

    /**
     * @param client
     *            the client to set
     */
    public void setClient(HttpClient client) {
        this.client = client;
    }

    public void setOpenStackRegion(OpenStackRegion openStackRegion) {
        this.openStackRegion = openStackRegion;
    }

    public void setHttpsClient(HttpsClient httpsClient) {
        this.httpsClient = httpsClient;
    }

}
