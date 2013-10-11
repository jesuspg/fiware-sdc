/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.manager.ChefNodeManager;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;

public class ChefNodeManagerImpl implements ChefNodeManager {

    private ChefNodeDao chefNodeDao;
    private ProductInstanceDao productInstanceDao;

    private static Logger log = Logger.getLogger("ChefNodeManagerImpl");

    public void chefNodeDelete(String vdc, String chefNodename) throws NodeExecutionException {
        ChefNode node;

        List<ProductInstance> productInstances = null;

        try {
            productInstances = productInstanceDao.findByHostname(chefNodename);
            String domain = productInstances.get(0).getVm().getDomain();

            node = chefNodeDao.loadNode(chefNodename + domain);
            chefNodeDao.deleteNode(node);
            log.info("Node " + chefNodename + " deleted from Chef Server");

            for (int i = 0; i < productInstances.size(); i++) {
                productInstanceDao.remove(productInstances.get(i));
            }
        } catch (CanNotCallChefException e) {
            String errorMsg = "Error deleting the Node" + chefNodename + " in Chef server. Description: "
                    + e.getMessage();
            log.info(errorMsg);
            throw new NodeExecutionException(errorMsg);
        } catch (EntityNotFoundException e1) {
            String errorMsg = "The Hostname was not found in the system " + e1.getMessage();
            log.info(errorMsg);
            throw new NodeExecutionException(errorMsg);
        }
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
