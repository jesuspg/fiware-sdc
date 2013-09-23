package com.telefonica.euro_iaas.sdc.util;

import java.io.IOException;

import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;

import junit.framework.TestCase;

/**
 *
 * @author Sergio Arroyo
 *
 */
public class AbstractShellCommandTest extends TestCase {

    /**
     * Test the correct behavior when the executed command exists and is valid.
     *
     * @throws Exception
     */
    public void testScriptExecutedOk() throws Exception {
        AbstractShellCommand shellCommand = new AbstractShellCommand();
        String[] result = shellCommand.executeCommand("env");
        assertTrue(result[0].contains("PATH"));

    }

    /**
     * Test the correct behavior when the executed command exists and is
     * invalid.
     *
     * @throws Exception
     */
    public void testScriptExecutedFails() throws Exception {
        AbstractShellCommand shellCommand = new AbstractShellCommand();
        try {
            shellCommand.executeCommand("ls -ñ");
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
        AbstractShellCommand shellCommand = new AbstractShellCommand();
        try {
            shellCommand.executeCommand("asdag");
            fail("ShellCommanException expected");
        } catch (ShellCommandException e) {
            // it's ok
            assertTrue(e.getCause() instanceof IOException);
        }
    }
}
