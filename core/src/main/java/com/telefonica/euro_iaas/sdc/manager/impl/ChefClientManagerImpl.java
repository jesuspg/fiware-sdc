/**
 * 
 */
package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ChefClientDao;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.ChefClientExecutionException;
import com.telefonica.euro_iaas.sdc.manager.ChefClientManager;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;

/**
 * @author jesus.movilla
 */
public class ChefClientManagerImpl implements ChefClientManager {

    private ProductInstanceDao productInstanceDao;
    private ChefClientDao chefClientDao;
    private ChefNodeDao chefNodeDao;

    private static Logger log = Logger.getLogger("ChefNodeManagerImpl");

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

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.manager.ChefClientManager#chefNodeDelete(java.lang.String, java.lang.String)
     */
    public void chefClientDelete(String vdc, String chefClientName) throws ChefClientExecutionException {
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

            // eliminacion de los productos instalados en la maquina virtual
            hostname = chefClientName.split("\\.")[0];
            productInstances = productInstanceDao.findByHostname(hostname);

            for (int i = 0; i < productInstances.size(); i++) {
                productInstanceDao.remove(productInstances.get(i));
            }
        } catch (EntityNotFoundException enfe) {
            String errorMsg = "The hostname " + hostname + " does not have products installed " + enfe.getMessage();
            log.info(errorMsg);
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

}
