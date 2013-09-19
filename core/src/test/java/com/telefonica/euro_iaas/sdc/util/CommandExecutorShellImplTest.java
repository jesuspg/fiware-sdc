package com.telefonica.euro_iaas.sdc.util;

import java.io.IOException;

import junit.framework.TestCase;

import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;

/**
 * 
 * @author Sergio Arroyo
 * 
 */
public class CommandExecutorShellImplTest extends TestCase {

	/**
	 * Test the correct behavior when the executed command exists and is valid.
	 * 
	 * @throws Exception
	 */
	public void testScriptExecutedOk() throws Exception {
		/*
		 * CommandExecutorShellImpl shellCommand = new
		 * CommandExecutorShellImpl(); String[] result =
		 * shellCommand.executeCommand("dir \n");
		 * assertTrue(result[0].contains("TID"));
		 */
	}

	/**
	 * Test the correct behavior when the executed command exists and is
	 * invalid.
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
