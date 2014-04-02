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

package com.telefonica.euro_iaas.sdc.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;

/**
 * <p>
 * Abstract AbstractShellCommand class.
 * </p>
 * 
 * @author Sergio Arroyo
 */
public class CommandExecutorShellImpl implements CommandExecutor {

    private static Logger logger = Logger.getLogger(CommandExecutorShellImpl.class.getName());

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
        String[] outputCommand = new String[2];

        try {
            // Command is executed
            logger.log(Level.INFO, "Executing command: " + command);
            Process p = Runtime.getRuntime().exec(command);

            // Leemos la salida del comando
            outputCommand[0] = IOUtils.toString(p.getInputStream());
            outputCommand[1] = IOUtils.toString(p.getErrorStream());

            Integer exitValue = null;
            // this bucle is because sometimes the flows continues and the
            // comand
            // does not finish yet.
            while (exitValue == null) {
                try {
                    exitValue = p.exitValue();
                } catch (IllegalThreadStateException e) {
                    logger.log(Level.FINEST, "The command does not finished yet");
                }
            }

            if (!exitValue.equals(0)) {
                logger.log(Level.SEVERE, "Error executing command: " + outputCommand[1]);
                throw new ShellCommandException(outputCommand[1]);
            }
            return outputCommand;
        } catch (IOException e) {
            throw new ShellCommandException("Unexpected exception", e);
        }
    }

}
