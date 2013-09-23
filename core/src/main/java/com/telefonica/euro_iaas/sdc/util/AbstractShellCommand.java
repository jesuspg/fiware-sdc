package com.telefonica.euro_iaas.sdc.util;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.ASSING_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.EXECUTE_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UNASSING_RECIPES_SCRIPT;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * <p>
 * Abstract AbstractShellCommand class.
 * </p>
 *
 * @author Sergio Arroyo
 */
public class AbstractShellCommand {

    private static Logger logger = Logger.getLogger(AbstractShellCommand.class
            .getName());

    protected SystemPropertiesProvider propertiesProvider;

    /**
     * <p>
     * executeCommand
     * </p>
     *
     * @param command
     *            a {@link java.lang.String} object.
     * @return an array of {@link java.lang.String} objects.
     * @throws java.io.IOException
     *             if any.
     */
    public String[] executeCommand(String command) throws ShellCommandException {
        logger.log(Level.INFO, "Start executeCommand");
        String[] outputCommand = new String[2];

        try {
            // Command is executed
            logger.log(Level.INFO, "Executing command: " + command);
            Process p = Runtime.getRuntime().exec(command);

            // Leemos la salida del comando
            outputCommand[0] = IOUtils.toString(p.getInputStream());
            outputCommand[1] = IOUtils.toString(p.getErrorStream());

            Integer exitValue = null;
            //this bucle is because sometimes the flows continues and the comand
            //does not finish yet.
            while (exitValue == null) {
                try {
                    exitValue = p.exitValue();
                } catch (IllegalThreadStateException e) {
                    logger.log(Level.FINEST, "The command does not finished yet");
                }
            }

            if (!exitValue.equals(0)) {
                logger.log(Level.SEVERE,
                        "Error executing command: " + outputCommand[1]);
                throw new ShellCommandException(outputCommand[1]);
            }
            logger.log(Level.INFO, "End executeCommand");
            return outputCommand;
        } catch (IOException e) {
            throw new ShellCommandException("Unexpected exception", e);
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
        executeCommand(command);
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
        executeCommand(command);
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
        executeCommand(command);
    }


    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(
            SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

}
