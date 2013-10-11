/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.RecipeNamingGenerator;
import com.telefonica.euro_iaas.sdc.util.SDCClientUtils;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * Provides some methods to work with deployable units (products and applications).
 * 
 * @author Sergio Arroyo
 */
public class BaseInstallableInstanceManager {

    protected SystemPropertiesProvider propertiesProvider;
    protected RecipeNamingGenerator recipeNamingGenerator;
    private ChefNodeDao chefNodeDao;
    protected SDCClientUtils sdcClientUtils;

    protected void callChef(String recipe, VM vm) throws CanNotCallChefException, NodeExecutionException {
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

    protected void callChef(String process, String recipe, VM vm, List<Attribute> attributes)
            throws CanNotCallChefException, NodeExecutionException {
        // System.out.println("Attributre " + attributes);
        configureNode(vm, attributes, process, recipe);
        try {
            System.out.println("Executing recipe " + recipe + " in " + vm.getIp());
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
    public void assignRecipes(VM vm, String recipe) throws CanNotCallChefException {
        // tell Chef the assigned recipes shall be installed:
        ChefNode node = chefNodeDao.loadNode(vm.getChefClientName());
        node.addRecipe(recipe);

        chefNodeDao.updateNode(node);
    }

    /**
     * Tell Chef the previously assigned recipes are ready to be installed.
     * 
     * @param osInstance
     * @throws ShellCommandException
     */
    public void unassignRecipes(VM vm, String recipe) throws CanNotCallChefException {
        // tell Chef the assigned recipes shall be deleted:
        ChefNode node = chefNodeDao.loadNode(vm.getChefClientName());
        node.removeRecipe(recipe);
        chefNodeDao.updateNode(node);
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
     */
    public void configureNode(VM vm, List<Attribute> attributes, String process, String recipe)
            throws CanNotCallChefException {
        // tell Chef the assigned recipes shall be deleted:
        ChefNode node = chefNodeDao.loadNode(vm.getChefClientName());
        node.addRecipe(recipe);
        if (attributes != null) {
            for (Attribute attr : attributes) {
                node.addAttribute(process, attr.getKey(), attr.getValue());
            }
        }
        chefNodeDao.updateNode(node);
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
