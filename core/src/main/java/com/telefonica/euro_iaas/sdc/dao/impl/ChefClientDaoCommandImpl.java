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

import java.text.MessageFormat;

import com.telefonica.euro_iaas.sdc.dao.ChefClientDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.util.CommandExecutor;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

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
    public void deleteChefClient(String chefClientName, String token) {
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
    public ChefClient getChefClient(String chefClientName, String token) throws CanNotCallChefException,
            EntityNotFoundException {
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
    public ChefClient chefClientfindByHostname(String hostname, String token) {
        // TODO Auto-generated method stub
        return null;
    }

}
