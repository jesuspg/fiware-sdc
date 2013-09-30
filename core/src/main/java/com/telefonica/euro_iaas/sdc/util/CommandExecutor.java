package com.telefonica.euro_iaas.sdc.util;

import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;

public interface CommandExecutor {

    /**
     * <p>
     * Execute the given command.
     * </p>
     * 
     * @param command
     *            the given command
     * @return the std and error output.
     * @throws ShellCommandException
     *             if the command does not exists or if return an error.
     */
    String[] executeCommand(String command) throws ShellCommandException;

}
