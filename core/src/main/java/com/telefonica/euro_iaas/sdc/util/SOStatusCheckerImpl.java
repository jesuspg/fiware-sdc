package com.telefonica.euro_iaas.sdc.util;

import java.io.IOException;

import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.model.SOInstance;

/**
 * <p>Abstract SOStatusCheckerImpl class.</p>
 *
 * @author cjc
 * @version $Id: $
 */
public class SOStatusCheckerImpl extends AbstractShellCommand implements SOStatusChecker {

	@Override
	public void waitOK(SOInstance soInstance) throws InterruptedException, IOException, ShellCommandException {
		//Wait 3 minutes to complete cloning and booting
		Thread.sleep(3*60*1000);
		String out;
		do {
			System.out.println("Esperando 1 segundo");
			Thread.sleep(1000);
			String command = "ssh -l root "+soInstance.getHostname()+" cat /tmp/status";
			String[] result = executeCommand(command);
			System.out.println(result);
			out = result[0];
			System.out.println("valor de /tmp/status: "+out);
		}
		while(!out.equals("0"));
	}

}
