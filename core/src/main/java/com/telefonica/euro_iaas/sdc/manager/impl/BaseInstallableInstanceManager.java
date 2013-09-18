package com.telefonica.euro_iaas.sdc.manager.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.ASSING_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_ATTRIBUTES_LEFT_LIMITER;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_ATTRIBUTES_RIGHT_LIMITER;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_ATTRIBUTES_SEPARATOR;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_ATTRIBUTES_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_ROLE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.EXECUTE_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UNASSING_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UPDATE_ATTRIBUTES_SCRIPT;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.CommandExecutor;
import com.telefonica.euro_iaas.sdc.util.RecipeNamingGenerator;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
/**
 * Provides some methods to work with deployable units
 * (products and applications).
 * @author Sergio Arroyo
 *
 */
public class BaseInstallableInstanceManager {

    protected SystemPropertiesProvider propertiesProvider;
    protected RecipeNamingGenerator recipeNamingGenerator;
    private CommandExecutor commandExecutor;


    protected void callChef(String recipe, VM vm) throws ShellCommandException {
        assignRecipes(vm, recipe);
        try {
            executeRecipes(vm);
            unassignRecipes(vm, recipe);
        } catch (ShellCommandException e) {
            unassignRecipes(vm, recipe);
            //even if execution fails  want to unassign the recipe
            throw new ShellCommandException(e.getMessage());
        }
    }

    /**
     * Tell Chef the previously assigned recipes are ready to be installed.
     *
     * @param osInstance
     * @throws ShellCommandException
     */
    public void executeRecipes(VM vm)
            throws ShellCommandException {
        // tell Chef the assigned recipes shall be installed:
        String command = MessageFormat.format(propertiesProvider
                .getProperty(EXECUTE_RECIPES_SCRIPT),
                vm.getExecuteChefConectionUrl());
        commandExecutor.executeCommand(command);
    }

    /**
     * Tell Chef the previously assigned recipes are ready to be installed.
     *
     * @param osInstance
     * @throws ShellCommandException
     */
    public void assignRecipes(VM vm, String recipe)
            throws ShellCommandException {
        // tell Chef the assigned recipes shall be installed:
        String command = MessageFormat.format(propertiesProvider
                .getProperty(ASSING_RECIPES_SCRIPT), vm.getChefClientName(),
                recipe);
        commandExecutor.executeCommand(command);
    }

    /**
     * Tell Chef the previously assigned recipes are ready to be installed.
     *
     * @param osInstance
     * @throws ShellCommandException
     */
    public void unassignRecipes(VM vm, String recipe)
            throws ShellCommandException {
        // tell Chef the assigned recipes shall be installed:
        String command = MessageFormat.format(propertiesProvider
                .getProperty(UNASSING_RECIPES_SCRIPT), vm.getChefClientName(),
                recipe);
        commandExecutor.executeCommand(command);
    }

    /**
     *
     * @param populatedRoleTemplate
     */
    public void updateAttributes(String roleName, String fileName, VM vm)
    throws ShellCommandException {
        String command = MessageFormat.format(propertiesProvider
                .getProperty(UPDATE_ATTRIBUTES_SCRIPT), roleName, fileName,
                vm.getHostname(), vm.getDomain(),
                vm.getExecuteChefConectionUrl());
        commandExecutor.executeCommand(command);
    }


    public String populateRoleTemplate(VM vm, String installableName,
            List<Attribute> attributes, String roleName, String process) {
        String attributeList = populateAttributeTemplate(attributes);
        String roleTemplate = propertiesProvider.getProperty(
                CHEF_ROLE_TEMPLATE);

        return MessageFormat.format(roleTemplate, vm.getHostname(),
                installableName, attributeList, roleName, process);
    }

    /**
     * Get the template with the placeholders replaced by the correct values
     * @param attributes the attributes
     * @return
     */
    private String populateAttributeTemplate(List<Attribute> attributes) {
        String attributeList = "";
        String attributeTemplate = propertiesProvider.getProperty(
                CHEF_ATTRIBUTES_TEMPLATE);
        String attributeSeparator = propertiesProvider.getProperty(
                CHEF_ATTRIBUTES_SEPARATOR);
        Integer i = attributes.size();

        for (Attribute attribute : attributes) {
            String value = attribute.getValue();
            if (!isList(value)) {
                value = '"' + value + '"';
            }
            attributeList = attributeList
            + MessageFormat.format(attributeTemplate,
                    attribute.getKey(), value);
            i = i -1;
            if (i > 0) {
                attributeList = attributeList + attributeSeparator;
            }
        }
        attributeList = propertiesProvider.getProperty(CHEF_ATTRIBUTES_LEFT_LIMITER)
        + attributeList + propertiesProvider.getProperty(CHEF_ATTRIBUTES_RIGHT_LIMITER);

        return attributeList;
    }

    private Boolean isList (String attributeValue) {
         Pattern p = Pattern.compile("\\[.*\\]");
         Matcher m = p.matcher(attributeValue);
         return  m.matches();
    }

    public File createRoleFile(String roleTemplate, String filename) {
        try {
            File tmpFile = File.createTempFile(filename, "");
            // Write to temp file
            BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile));
            out.write(roleTemplate);
            out.close();
            return tmpFile;
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
        }
    }


    ////////////// I.O.C. //////////////
    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(
            SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param commandExecutor the commandExecutor to set
     */
    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    /**
     * @param recipeNamingGenerator the recipeNamingGenerator to set
     */
    public void setRecipeNamingGenerator(RecipeNamingGenerator recipeNamingGenerator) {
        this.recipeNamingGenerator = recipeNamingGenerator;
    }


}
