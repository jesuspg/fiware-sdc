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

import junit.framework.TestCase;

import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;

/**
 * @author Sergio Arroyo
 */
public class CommandExecutorShellImplTest extends TestCase {

    /**
     * Test the correct behavior when the executed command exists and is valid.
     * 
     * @throws Exception
     */
    public void testScriptExecutedOk() throws Exception {
        /*
         * CommandExecutorShellImpl shellCommand = new CommandExecutorShellImpl(); String[] result =
         * shellCommand.executeCommand("dir \n"); assertTrue(result[0].contains("TID"));
         */
    }

    /**
     * Test the correct behavior when the executed command exists and is invalid.
     * 
     * @throws Exception
     */
    public void testScriptExecutedFails() throws Exception {
        CommandExecutorShellImpl shellCommand = new CommandExecutorShellImpl();
        try {
            shellCommand.executeCommand("cd -ñ");
            fail("ShellCommanException expected");
        } catch (ShellCommandException e) {
            // it's ok
        }
    }

    /**
     * Test the correct behavior when the executed command does not exists.
     * 
     * @throws Exception
     */
    public void testScriptExecutedFailsBecauseDoesNotExists() throws Exception {
        CommandExecutorShellImpl shellCommand = new CommandExecutorShellImpl();
        try {
            shellCommand.executeCommand("asdag");
            fail("ShellCommanException expected");
        } catch (ShellCommandException e) {
            // it's ok
            assertTrue(e.getCause() instanceof IOException);
        }
    }
}
