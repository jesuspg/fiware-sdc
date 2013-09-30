/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

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
