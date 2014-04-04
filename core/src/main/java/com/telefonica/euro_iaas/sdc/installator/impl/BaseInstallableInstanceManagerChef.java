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

package com.telefonica.euro_iaas.sdc.installator.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.RecipeNamingGenerator;
import com.telefonica.euro_iaas.sdc.util.SDCClientUtils;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * Provides some methods to work with deployable units (products and
 * applications).
 * 
 * @author Sergio Arroyo
 */
public class BaseInstallableInstanceManagerChef {
    
    protected String INSTALL = "install";
    protected String UNINSTALL = "uninstall";
    protected String CONFIGURE = "configure";
    protected String DEPLOY_ARTIFACT="deployArtifact";
    protected String UNDEPLOY_ARTIFACT="undeployArtifact";

    protected SystemPropertiesProvider propertiesProvider;
    protected RecipeNamingGenerator recipeNamingGenerator;
    protected ChefNodeDao chefNodeDao;
    protected SDCClientUtils sdcClientUtils;

    protected int MAX_TIME = 90000;

    protected static Logger LOGGER = Logger.getLogger("BaseInstallableInstanceManager");

    protected void callChefUpgrade(String recipe, VM vm) throws InstallatorException, NodeExecutionException {
        assignRecipes(vm, recipe);
        try {
            executeRecipes(vm);
            // unassignRecipes(vm, recipe);
        } catch (NodeExecutionException e) {
            // unassignRecipes(vm, recipe);
            // even if execution fails want to unassign the recipe
            throw new NodeExecutionException(e.getMessage());
        }
    }
    
    /**
     * Tell Chef the previously assigned recipes are ready to be installed.
     * 
     * @param osInstance
     * @throws ShellCommandException
     */
    public void executeRecipes(VM vm) throws NodeExecutionException {
        // tell Chef the assigned recipes shall be installed:
        sdcClientUtils.execute(vm);
    }

    /**
     * Tell Chef the previously assigned recipes are ready to be installed.
     * 
     * @param osInstance
     * @throws ShellCommandException
     */
    public void assignRecipes(VM vm, String recipe) throws InstallatorException {
        try {
            // tell Chef the assigned recipes shall be installed:
            ChefNode node = chefNodeDao.loadNode(vm.getChefClientName());
            node.addRecipe(recipe);

            chefNodeDao.updateNode(node);
        } catch (CanNotCallChefException e) {
            throw new InstallatorException(e);
        }
    }

    /**
     * Tell Chef the previously assigned recipes are ready to be installed.
     * 
     * @param osInstance
     * @throws InstallatorException
     * @throws ShellCommandException
     */
    public void unassignRecipes(VM vm, String recipe) throws InstallatorException {
        // tell Chef the assigned recipes shall be deleted:
        ChefNode node = null;
        try {
            node = chefNodeDao.loadNodeFromHostname(vm.getHostname());
        } catch (EntityNotFoundException e) {
            String message = " Node with hostname " + vm.getHostname() + " is not registered in Chef Server";
            LOGGER.info(message);
            throw new InstallatorException(message, e);
        } catch (CanNotCallChefException e) {
            throw new InstallatorException(e);
        }
        try {
            node.removeRecipe(recipe);
            chefNodeDao.updateNode(node);
        } catch (CanNotCallChefException e) {
            throw new InstallatorException(e);
        }
    }

    /**
     * Add override attributes for the configured values.
     * 
     * @param vm
     *            the chef node
     * @param attributes
     *            the new attributes
     * @param recipe
     *            the recipe for that new attributes
     * @throws InstallatorException
     */
    public void configureNode(VM vm, List<Attribute> attributes, String process, String recipe)
            throws InstallatorException, InstallatorException {
        // tell Chef the assigned recipes shall be deleted:
        // ChefNode node = chefNodeDao.loadNode(vm.getChefClientName());
        ChefNode node = null;
        try {
            node = chefNodeDao.loadNodeFromHostname(vm.getHostname());
            node.addRecipe(recipe);
            
            if (attributes != null) {
                for (Attribute attr : attributes) {
                    node.addAttribute(process, attr.getKey(), attr.getValue());
                }
            }
        } catch (EntityNotFoundException e) {
            String message = " Node with hostname " + vm.getHostname() + " is not registered in Chef Server";
            LOGGER.info(message);
            throw new InstallatorException(message, e);
        } catch (CanNotCallChefException e) {
            throw new InstallatorException(e);
        }
        try {
            chefNodeDao.updateNode(node);
        } catch (CanNotCallChefException e) {
            throw new InstallatorException(e);
        }
    }

 
    /**
     * Checks if the Node is already registered in ChefServer.
     * @param hostname
     */
    public void isNodeRegistered(String hostname) {
        try {
            chefNodeDao.isNodeRegistered(hostname);
        } catch (CanNotCallChefException e) {
            throw new SdcRuntimeException(e);
        }
    }

    
    protected boolean isSdcClientInstalled () {
        String sdcClient = propertiesProvider.getProperty(SystemPropertiesProvider.SDCCLIENT_INSTALLED_IN_NODES);
        return Boolean.parseBoolean(sdcClient);
    }
    
    // //////////// I.O.C. //////////////
    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param recipeNamingGenerator
     *            the recipeNamingGenerator to set
     */
    public void setRecipeNamingGenerator(RecipeNamingGenerator recipeNamingGenerator) {
        this.recipeNamingGenerator = recipeNamingGenerator;
    }

    /**
     * @param chefNodeDao
     *            the chefNodeDao to set
     */
    public void setChefNodeDao(ChefNodeDao chefNodeDao) {
        this.chefNodeDao = chefNodeDao;
    }

    /**
     * @param sdcClientUtils
     *            the sdcClientUtils to set
     */
    public void setSdcClientUtils(SDCClientUtils sdcClientUtils) {
        this.sdcClientUtils = sdcClientUtils;
    }

}
