/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.manager.impl;

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.properties.PropertiesProvider;
import com.telefonica.euro_iaas.sdc.dao.ChefClientDao;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.ChefClientExecutionException;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.manager.NodeManager;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * @author alberts
 */
public class NodeManagerImpl implements NodeManager {

    private ProductInstanceDao productInstanceDao;
    private ChefClientDao chefClientDao;
    private ChefNodeDao chefNodeDao;
    private SystemPropertiesProvider propertiesProvider;
    private HttpClient client;


    private static Logger log = Logger.getLogger("NodeManagerImpl");

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telefonica.euro_iaas.sdc.manager.ChefClientManager#chefNodeDelete
     * (java.lang.String, java.lang.String)
     */
    public void nodeDelete(String vdc, String nodeName) throws NodeExecutionException {

        try {
            
            puppetDelete(vdc,nodeName);
            chefClientDelete(vdc,nodeName);
        
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
    
    private void puppetDelete(String vdc, String nodeName) throws NodeExecutionException {
        
        HttpDelete delete = new HttpDelete(propertiesProvider.getProperty(SystemPropertiesProvider.PUPPET_MASTER_URL)
                + "delete/node/"+nodeName);
        
        System.out.println("puppetURL: "+propertiesProvider.getProperty(SystemPropertiesProvider.PUPPET_MASTER_URL)
                + "delete/node/"+nodeName);
        
        delete.setHeader("Content-Type", "application/json");

        HttpResponse response;

        try {
            response = client.execute(delete);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);

            if (statusCode != 200) {
                String msg=format("[puppet delete node] response code was: {0}", statusCode);
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

    private void chefClientDelete(String vdc, String chefClientName) throws ChefClientExecutionException {
        ChefNode node;
        List<ProductInstance> productInstances = null;
        String hostname = null;
        try {
            // Eliminacion del nodo
            node = chefNodeDao.loadNode(chefClientName);
            chefNodeDao.deleteNode(node);
            log.info("Node " + chefClientName + " deleted from Chef Server");

            // eliminacion del chefClient
            chefClientDao.deleteChefClient(chefClientName);

            
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
    public ChefClient chefClientfindByHostname(String hostname) throws EntityNotFoundException,
            ChefClientExecutionException {

        ChefClient chefClient = new ChefClient();
        try {
            chefClient = chefClientDao.chefClientfindByHostname(hostname);
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
    public ChefClient chefClientload(String chefClientName) throws ChefClientExecutionException,
            EntityNotFoundException {

        ChefClient chefClient = new ChefClient();
        try {
            chefClient = chefClientDao.getChefClient(chefClientName);
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
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }
    
    /**
     * @param client
     *            the client to set
     */
    public void setClient(HttpClient client) {
        this.client = client;
    }

}
