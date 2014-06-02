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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.util.EntityUtils;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ChefClientDao;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.ChefClientExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.OpenStackException;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.manager.NodeManager;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;

/**
 * @author alberts
 */
public class NodeManagerImpl implements NodeManager {

    private ProductInstanceDao productInstanceDao;
    private ChefClientDao chefClientDao;
    private ChefNodeDao chefNodeDao;
    private HttpClient client;
    private OpenStackRegion openStackRegion;

    private static Logger log = LoggerFactory.getLogger(NodeManagerImpl.class);

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.manager.ChefClientManager#chefNodeDelete (java.lang.String, java.lang.String)
     */
    public void nodeDelete(String vdc, String nodeName, String token) throws NodeExecutionException {

        try {

            puppetDelete(vdc, nodeName, token);
            chefClientDelete(vdc, nodeName, token);

        } catch (ChefClientExecutionException e) {
            throw new NodeExecutionException(e);
        }

        List<ProductInstance> productInstances = null;

        // eliminacion de los productos instalados en la maquina virtual
        String hostname = nodeName.split("\\.")[0];
        try {
            productInstances = productInstanceDao.findByHostname(nodeName);

            for (int i = 0; i < productInstances.size(); i++) {
                productInstanceDao.remove(productInstances.get(i));
            }
        } catch (EntityNotFoundException enfe) {
            String errorMsg = "The hostname " + hostname + " does not have products installed " + enfe.getMessage();
            log.info(errorMsg);
        }

    }

    private void puppetDelete(String vdc, String nodeName, String token) throws NodeExecutionException {

        HttpDelete delete;
        try {
            delete = new HttpDelete(openStackRegion.getPuppetEndPoint(token) + "delete/node/" + nodeName);
        } catch (OpenStackException e2) {
            log.info(e2.getMessage());
            throw new NodeExecutionException(e2);
        }

        delete.setHeader("Content-Type", "application/json");

        HttpResponse response;

        try {
            response = client.execute(delete);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);

            if (statusCode != 200) {
                String msg = format("[puppet delete node] response code was: {0}", statusCode);
                log.info(msg);
                throw new NodeExecutionException(msg);
            }
            log.info("Node succesfully deleted from pupper master");
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new NodeExecutionException(e);
        } catch (IllegalStateException e1) {
            log.info(e1.getMessage());
            throw new NodeExecutionException(e1);
        }

    }

    private void chefClientDelete(String vdc, String chefClientName, String token) throws ChefClientExecutionException {
        ChefNode node;
        List<ProductInstance> productInstances = null;
        String hostname = null;
        try {
            // Eliminacion del nodo
            node = chefNodeDao.loadNode(chefClientName, token);
            chefNodeDao.deleteNode(node, token);
            log.info("Node " + chefClientName + " deleted from Chef Server");

            // eliminacion del chefClient
            chefClientDao.deleteChefClient(chefClientName, token);

        } catch (CanNotCallChefException e) {
            String errorMsg = "Error deleting the Node" + chefClientName + " in Chef server. Description: "
                    + e.getMessage();
            log.info(errorMsg);
            throw new ChefClientExecutionException(errorMsg, e);
        } catch (Exception e2) {
            String errorMsg = "The ChefClient " + chefClientName + " was not found in the system " + e2.getMessage();
            log.info(errorMsg);
            throw new ChefClientExecutionException(errorMsg, e2);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.manager.ChefClientManager#chefClientfindAll()
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

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.manager.ChefClientManager#chefClientload(java.lang.String, java.lang.String)
     */
    public ChefClient chefClientload(String chefClientName, String token) throws ChefClientExecutionException,
            EntityNotFoundException {

        ChefClient chefClient = new ChefClient();
        try {
            chefClient = chefClientDao.getChefClient(chefClientName, token);
        } catch (EntityNotFoundException e) {
            // String message = " An error ocurred invoing the Chef server to load ChefClient named " + chefClientName;
            // log.info(message);
            throw e;
        } catch (Exception e) {
            String message = " An error ocurred invoing the Chef server to load ChefClient named " + chefClientName;
            log.info(message);
            throw new ChefClientExecutionException(message, e);
        }
        return chefClient;
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

}
