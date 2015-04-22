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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

/**
 * Provides some methods to work with deployable units (products and applications).
 * 
 * @author Sergio Arroyo
 */
public class BaseInstallableInstanceManagerChef {

    protected String INSTALL = "install";
    protected String UNINSTALL = "uninstall";
    protected String CONFIGURE = "configure";
    protected String DEPLOY_ARTIFACT = "deployArtifact";
    protected String UNDEPLOY_ARTIFACT = "undeployArtifact";

    protected SystemPropertiesProvider propertiesProvider;
    protected RecipeNamingGenerator recipeNamingGenerator;
    protected ChefNodeDao chefNodeDao;
    protected SDCClientUtils sdcClientUtils;

    protected int MAX_TIME = 1200000;

    protected static Logger log = LoggerFactory.getLogger(BaseInstallableInstanceManagerChef.class);

    protected void callChefUpgrade(String recipe, VM vm, String token) throws InstallatorException,
            NodeExecutionException {
        assignRecipes(vm, recipe, token);
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
    public void assignRecipes(VM vm, String recipe, String token) throws InstallatorException {
        try {
            // tell Chef the assigned recipes shall be installed:
            ChefNode node = chefNodeDao.loadNode(vm.getChefClientName(), token);
            node.addRecipe(recipe);

            chefNodeDao.updateNode(node, token);
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
    public void unassignRecipes(VM vm, String recipe, String token) throws InstallatorException {
        // tell Chef the assigned recipes shall be deleted:
        ChefNode node = null;
        try {
            node = chefNodeDao.loadNodeFromHostname(vm.getHostname(), token);
        } catch (EntityNotFoundException e) {
            String message = " Node with hostname " + vm.getHostname() + " is not registered in Chef Server";
            log.info(message);
            throw new InstallatorException(message, e);
        } catch (CanNotCallChefException e) {
            throw new InstallatorException(e);
        }
        try {
            node.removeRecipe(recipe);
            chefNodeDao.updateNode(node, token);
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
    public void configureNode(VM vm, List<Attribute> attributes, String process, String recipe, String token)
            throws InstallatorException, InstallatorException {
        // tell Chef the assigned recipes shall be deleted:
        // ChefNode node = chefNodeDao.loadNode(vm.getChefClientName());
        ChefNode node = null;
        try {
            node = chefNodeDao.loadNodeFromHostname(vm.getHostname(), token);
            node.addRecipe(recipe);

            if (attributes != null) {
                for (Attribute attr : attributes) {
                    node.addAttribute(process, attr.getKey(), attr.getValue());
                }
            }
        } catch (EntityNotFoundException e) {
            String message = " Node with hostname " + vm.getHostname() + " is not registered in Chef Server";
            log.error(message);
            throw new InstallatorException(message, e);
        } catch (CanNotCallChefException e) {
            throw new InstallatorException(e);
        }
        try {
            chefNodeDao.updateNode(node, token);
        } catch (CanNotCallChefException e) {
            log.error(e.getMessage());
            throw new InstallatorException(e);
        }
    }

    /**
     * Checks if the Node is already registered in ChefServer.
     * 
     * @param hostname
     */
    public void isNodeRegistered(String hostname, String token) throws SdcRuntimeException {
        try {
            chefNodeDao.isNodeRegistered(hostname, token);
        } catch (CanNotCallChefException e) {
            log.error(e.getMessage());
            throw new SdcRuntimeException(e);
        }
    }

    protected boolean isSdcClientInstalled() {
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
