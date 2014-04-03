package com.telefonica.euro_iaas.sdc.installator.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
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

public class InstallatorChefImpl extends BaseInstallableInstanceManagerChef implements Installator {
    
    private IpToVM ip2vm;

    @Override
    public void callService(VM vm, String vdc, ProductRelease productRelease, String action)
            throws InstallatorException {
        // TODO Auto-generated method stub
        
    }
   
    @Override
    public void callService(ProductInstance productInstance, VM vm, List<Attribute> attributes, String action)
            throws InstallatorException, NodeExecutionException {

        String process = productInstance.getProductRelease().getProduct().getName();

        String recipe = "";
        
        if ("install".equals(action)) {
            recipe = recipeNamingGenerator.getInstallRecipe(productInstance);
        } else if ("uninstall".equals(action)) {
            recipe = recipeNamingGenerator.getUninstallRecipe(productInstance);
        }else if ("configure".equals(action)){
            recipe = recipeNamingGenerator.getConfigureRecipe(productInstance);
        }else if ("deployArtifact".equals(action)){
            recipe = recipeNamingGenerator.getDeployArtifactRecipe(productInstance);
        }else if ("undeployArtifact".equals(action)){
            recipe = recipeNamingGenerator.getUnDeployArtifactRecipe(productInstance);
        }else{
            throw new InstallatorException("Missing Action");
        }
        
        configureNode(vm, attributes, process, recipe);
        try {
            LOGGER.info("Updating node with recipe " + recipe + " in " + vm.getIp());
            if (isSdcClientInstalled()) {
                executeRecipes(vm);
                // unassignRecipes(vm, recipe);
            } else {
                isRecipeExecuted(vm, process, recipe);
                unassignRecipes(vm, recipe);
                //eliminate the attribute
                
            }
        } catch (NodeExecutionException e) {
            // even if execution fails want to unassign the recipe
            throw new NodeExecutionException(e.getMessage());
        }
    }

    @Override
    public void callService(ProductInstance productInstance, String action) throws InstallatorException,
            NodeExecutionException {
        VM vm = productInstance.getVm();
        String recipe="";
        if ("uninstall".equals(action)) {
           recipe = recipeNamingGenerator.getUninstallRecipe(productInstance);
        }else{
           
        }

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

    @Override
    public void upgrade(ProductInstance productInstance, VM vm) throws InstallatorException {
        try {
            String backupRecipe = recipeNamingGenerator.getBackupRecipe(productInstance);
            callChefUpgrade(backupRecipe, vm);

            String uninstallRecipe = recipeNamingGenerator.getUninstallRecipe(productInstance);
            callChefUpgrade(uninstallRecipe, vm);

            String installRecipe = recipeNamingGenerator.getInstallRecipe(productInstance);
            callChefUpgrade(installRecipe, vm);

            String restoreRecipe = recipeNamingGenerator.getRestoreRecipe(productInstance);
            callChefUpgrade(restoreRecipe, vm);
        } catch (NodeExecutionException e) {
            throw new InstallatorException(e);
        } catch (InstallatorException ex) {
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
    public void isRecipeExecuted(VM vm, String process, String recipe) throws NodeExecutionException {
        boolean isExecuted = false;
        int time = 5000;
        Date fechaAhora = new Date();
        while (!isExecuted) {
            System.out.println("MAX_TIME: " + MAX_TIME + " and time: " + time);
            try {
                if (time > MAX_TIME) {
                    String errorMesg = "Recipe " + process + " coub not be executed in " + vm.getChefClientName();
                    LOGGER.info(errorMesg);
                    unassignRecipes(vm, recipe);
                    throw new NodeExecutionException(errorMesg);
                }
               
                Thread.sleep(time);
               
                ChefNode node = chefNodeDao.loadNodeFromHostname(vm.getHostname());
                
                long last_recipeexecution_timestamp = getLastRecipeExecutionTimeStamp (node);
                LOGGER.info("last_recipeexecution_timestamp:" + last_recipeexecution_timestamp 
                                + "fechaAhora:" + fechaAhora.getTime());
                if (last_recipeexecution_timestamp > fechaAhora.getTime()) {
                    //if (isRecipeExecutedOK(process, node))
                        isExecuted = true;
                    /*else{
                        String message =" Recipe Execution failed";
                        unassignRecipes(vm, recipe);
                        throw new NodeExecutionException( new ChefRecipeExecutionException(message));
                    }*/
                }
                time += time;
            } catch (EntityNotFoundException e) {
                throw new NodeExecutionException(e);
            } catch (CanNotCallChefException e) {
                throw new NodeExecutionException(e);
            } catch (InterruptedException ie) {
                throw new NodeExecutionException(ie);
            } catch (InstallatorException ie2){
                throw new NodeExecutionException(ie2);
            }
        }
    }

   //Getting Last Sucessfully Recipe Execution Timestamp (ohai_time)
   private long getLastRecipeExecutionTimeStamp (ChefNode node) throws NodeExecutionException{
        String platform = node.getAutomaticAttributes().get("platform").toString();
        String platform_version = node.getAutomaticAttributes().get("platform_version").toString();
        LOGGER.info("platform:" + platform + " platform_version:" + platform_version);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z", Locale.US);
        long last_recipeexecution_timestamp =0;

        String ohai_time_format = node.getAutomaticAttributes().get("ohai_time").toString();
        LOGGER.info("ohai_time_format:[" + ohai_time_format +"]");

        try {
            Date ohai_time_date = df.parse(ohai_time_format);
            last_recipeexecution_timestamp = ohai_time_date.getTime();
        } catch (ParseException ex) {
            String message = "Error parsing ohai_time date copming from node: " + ex.getMessage();
            LOGGER.info(message);
            throw new NodeExecutionException (message);
        }
        return last_recipeexecution_timestamp;
    }
    
    @Override
    public void validateInstalatorData(VM vm) throws InvalidInstallProductRequestException, NodeExecutionException {
        if (isSdcClientInstalled()){
            if (!vm.canWorkWithChef()) {
                sdcClientUtils.checkIfSdcNodeIsReady(vm.getIp());
                sdcClientUtils.setNodeCommands(vm);

                vm = ip2vm.getVm(vm.getIp(), vm.getFqn(), vm.getOsType());
                // Configure the node with the corresponding node commands
            }
        } else {       
            if (!vm.canWorkWithInstallatorServer()) {
                String message = "The VM does not include the node hostname required to Install " +
                                "software";
                throw new InvalidInstallProductRequestException(message);
            }
            isNodeRegistered(vm.getHostname());
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
