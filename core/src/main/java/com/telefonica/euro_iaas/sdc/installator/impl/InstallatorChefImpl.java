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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.installator.Installator;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.IpToVM;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;

public class InstallatorChefImpl extends BaseInstallableInstanceManagerChef implements Installator {

    private IpToVM ip2vm;
    private static Logger log = LoggerFactory.getLogger(InstallatorChefImpl.class);

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.installator.Installator#callService(com.telefonica.euro_iaas.sdc.model.dto.VM,
     * java.lang.String, com.telefonica.euro_iaas.sdc.model.ProductRelease, java.lang.String, java.lang.String)
     */
    @Override
    public void callService(VM vm, String vdc, ProductRelease productRelease, String action, String token)
            throws InstallatorException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see
     * com.telefonica.euro_iaas.sdc.installator.Installator#callService(com.telefonica.euro_iaas.sdc.model.ProductInstance
     * , com.telefonica.euro_iaas.sdc.model.dto.VM, java.util.List, java.lang.String, java.lang.String)
     */
    public void callService(ProductInstance productInstance, VM vm, List<Attribute> attributes, String action,
            String token) throws InstallatorException, NodeExecutionException {

        String process = productInstance.getProductRelease().getProduct().getName();

        String recipe = "";

        log.info("action: " + action);

        if ("install".equals(action)) {
            recipe = recipeNamingGenerator.getInstallRecipe(productInstance);
        } else if ("uninstall".equals(action)) {
            recipe = recipeNamingGenerator.getUninstallRecipe(productInstance);
        } else if ("configure".equals(action)) {
            recipe = recipeNamingGenerator.getConfigureRecipe(productInstance);
        } else if ("deployArtifact".equals(action)) {
            recipe = recipeNamingGenerator.getDeployArtifactRecipe(productInstance);
        } else if ("undeployArtifact".equals(action)) {
            recipe = recipeNamingGenerator.getUnDeployArtifactRecipe(productInstance);
        } else {
            throw new InstallatorException("Missing Action");
        }

        log.info("recipe: " + recipe);

        configureNode(vm, attributes, process, recipe, token);
        try {
            log.info("Updating node with recipe " + recipe + " in " + vm.getIp());
            if (isSdcClientInstalled()) {
                executeRecipes(vm);
            } else {
                checkRecipeExecution(vm, process, recipe, token);
            }
        } catch (NodeExecutionException e) {
            log.warn("Error in the execution of the node " + e.getMessage());
            throw new NodeExecutionException(e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * com.telefonica.euro_iaas.sdc.installator.Installator#callService(com.telefonica.euro_iaas.sdc.model.ProductInstance
     * , java.lang.String, java.lang.String)
     */
    @Override
    public void callService(ProductInstance productInstance, String action, String token) throws InstallatorException,
            NodeExecutionException {

        String process = productInstance.getProductRelease().getProduct().getName();

        VM vm = productInstance.getVm();
        String recipe = "";
        if ("uninstall".equals(action)) {
            recipe = recipeNamingGenerator.getUninstallRecipe(productInstance);
        } else {

        }

        log.info("action: " + action);
        log.info("recipe: " + recipe);

        assignRecipes(vm, recipe, token);
        try {
            if (isSdcClientInstalled()) {
                executeRecipes(vm);
            } else {
                checkRecipeExecution(vm, process, recipe, token);
            }
        } catch (NodeExecutionException e) {
            log.warn("Error in the execution of the node " + e.getMessage());
            throw new NodeExecutionException(e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * com.telefonica.euro_iaas.sdc.installator.Installator#upgrade(com.telefonica.euro_iaas.sdc.model.ProductInstance,
     * com.telefonica.euro_iaas.sdc.model.dto.VM, java.lang.String)
     */
    @Override
    public void upgrade(ProductInstance productInstance, VM vm, String token) throws InstallatorException {
        try {
            String backupRecipe = recipeNamingGenerator.getBackupRecipe(productInstance);
            log.info("backup recipe: " + backupRecipe);
            callChefUpgrade(backupRecipe, vm, token);

            String uninstallRecipe = recipeNamingGenerator.getUninstallRecipe(productInstance);
            log.info("uninstall recipe: " + uninstallRecipe);
            callChefUpgrade(uninstallRecipe, vm, token);

            String installRecipe = recipeNamingGenerator.getInstallRecipe(productInstance);
            log.info("install recipe: " + installRecipe);
            callChefUpgrade(installRecipe, vm, token);

            String restoreRecipe = recipeNamingGenerator.getRestoreRecipe(productInstance);
            log.info("restore recipe: " + restoreRecipe);
            callChefUpgrade(restoreRecipe, vm, token);
        } catch (NodeExecutionException e) {
            log.warn("Error in the execution of the node " + e.getMessage());
            throw new InstallatorException(e);
        } catch (InstallatorException ex) {
            log.warn("Error in the execution of the node " + ex.getMessage());
            throw new InstallatorException(ex);
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
     * @throws
     * @throws ShellCommandException
     */
    public void checkRecipeExecution(VM vm, String process, String recipe, String token) throws NodeExecutionException {
        boolean isExecuted = false;
        int time = 5000;
        int checkTime = 10000;
        Date fechaAhora = new Date();
        while (!isExecuted) {
            log.info("MAX_TIME: " + MAX_TIME + " and time: " + time);
            try {
                if (time > MAX_TIME) {
                    String errorMesg = "Recipe " + process + " coub not be executed in " + vm.getChefClientName();
                    log.info(errorMesg);
                    // unassignRecipes(vm, recipe, token);
                    throw new NodeExecutionException(errorMesg);
                }

                sleep(checkTime);

                ChefNode node = chefNodeDao.loadNodeFromHostname(vm.getHostname(), token);

                long last_recipeexecution_timestamp = getLastRecipeExecutionTimeStamp(node);
                log.info("last_recipeexecution_timestamp:" + last_recipeexecution_timestamp + "fechaAhora:"
                        + fechaAhora.getTime());
                if (last_recipeexecution_timestamp > fechaAhora.getTime()) {
                    isExecuted = true;
                }
                time = time + checkTime;
            } catch (EntityNotFoundException e) {
                throw new NodeExecutionException(e);
            } catch (CanNotCallChefException e) {
                throw new NodeExecutionException(e);
            } catch (InterruptedException ie) {
                throw new NodeExecutionException(ie);
            }
        }
    }

    /**
     * Sleep temporarily for the specified milliseconds.
     * 
     * @param millis
     * @throws InterruptedException
     */
    public void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    // Getting Last Sucessfully Recipe Execution Timestamp (ohai_time)
    private long getLastRecipeExecutionTimeStamp(ChefNode node) throws NodeExecutionException {
        String platform = node.getAutomaticAttributes().get("platform").toString();
        String platform_version = node.getAutomaticAttributes().get("platform_version").toString();
        log.info("platform:" + platform + " platform_version:" + platform_version);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US);
        long last_recipeexecution_timestamp = 0;

        String ohai_time_format = node.getAutomaticAttributes().get("ohai_time").toString();
        log.info("ohai_time_format:[" + ohai_time_format + "]");

        try {
            Date ohai_time_date = df.parse(ohai_time_format);
            log.info("ohai_time_date_parsed:[" + ohai_time_date.toString() + "]");
            last_recipeexecution_timestamp = ohai_time_date.getTime();
        } catch (ParseException ex) {
            String message = "Error parsing ohai_time date copming from node: " + ex.getMessage();
            log.info(message);
            throw new NodeExecutionException(message);
        }
        return last_recipeexecution_timestamp;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.telefonica.euro_iaas.sdc.installator.Installator#validateInstalatorData(com.telefonica.euro_iaas.sdc.model
     * .dto.VM, java.lang.String)
     */
    @Override
    public void validateInstalatorData(VM vm, String token) throws InvalidInstallProductRequestException,
            NodeExecutionException {
        if (isSdcClientInstalled()) {
            if (!vm.canWorkWithChef()) {
                sdcClientUtils.checkIfSdcNodeIsReady(vm.getIp());
                sdcClientUtils.setNodeCommands(vm);

                vm = ip2vm.getVm(vm.getIp(), vm.getFqn(), vm.getOsType());
                // Configure the node with the corresponding node commands
            }
        } else {
            if (!vm.canWorkWithInstallatorServer()) {
                String message = "The VM does not include the node hostname required to Install " + "software";
                throw new InvalidInstallProductRequestException(message);
            }
            isNodeRegistered(vm.getHostname(), token);
        }
    }

    /**
     * @param ip2vm
     *            the ip2vm to set
     */
    public void setIp2vm(IpToVM ip2vm) {
        this.ip2vm = ip2vm;
    }

}
