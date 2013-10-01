/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.dao.impl;

import java.text.MessageFormat;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ChefClientDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.util.CommandExecutor;

/**
 * @author jesus.movilla
 */
public class ChefClientDaoCommandImpl implements ChefClientDao {

    private CommandExecutor commandExecutor;
    private String CHEF_CLIENT_DELETE_COMMAND = "knife client delete -y {0}";
    private String CHEF_CLIENT_GET_COMMAND = "knife client show {0}";

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.dao.ChefClientDao#deleteChefClient(java.lang.String)
     */
    public void deleteChefClient(String chefClientName) {
        String command = MessageFormat.format(CHEF_CLIENT_DELETE_COMMAND, chefClientName);

        try {
            commandExecutor.executeCommand(command);
        } catch (ShellCommandException sce) {
            throw new RuntimeException(sce);
        }

    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.dao.ChefClientDao#getChefClient(java.lang.String)
     */
    public ChefClient getChefClient(String chefClientName) throws CanNotCallChefException, EntityNotFoundException {
        String command = MessageFormat.format(CHEF_CLIENT_GET_COMMAND, chefClientName);
        String[] output = new String[2];
        String chefClientString;

        try {
            output = commandExecutor.executeCommand(command);

            if (output[0] == null)
                throw new EntityNotFoundException(ChefClient.class, null, "ChefClient " + chefClientName
                        + " is not in the ChefServer");

            chefClientString = output[0].toString();
            // JSONObject jsonChefClient = JSONObject.fromObject(chefClientString);

            ChefClient chefClient = new ChefClient();
            // chefClient.fromJson(jsonChefClient);
            chefClient.fromKnifeCommand(chefClientString);
            return chefClient;
        } catch (ShellCommandException sce) {
            throw new RuntimeException(sce);
        }
    }

    /**
     * @param commandExecutor
     *            the commandExecutor to set
     */
    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.dao.ChefClientDao#chefClientfindByHostname(java.lang.String)
     */
    public ChefClient chefClientfindByHostname(String hostname) {
        // TODO Auto-generated method stub
        return null;
    }

}
