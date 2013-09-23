package com.telefonica.euro_iaas.sdc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
/**
 * <p>Abstract AbstractShellCommand class.</p>
 *
 * @author ju
 * @version $Id: $
 */
public abstract class AbstractShellCommand {
	
	private static Logger logger = Logger.getLogger("AbstractShellCommand");
	
	/**
	 * <p>executeCommand</p>
	 *
	 * @param command a {@link java.lang.String} object.
	 * @return an array of {@link java.lang.String} objects.
	 * @throws java.io.IOException if any.
	 */
	public String[] executeCommand (String command) throws IOException, 
	ShellCommandException
	{
		logger.log(Level.INFO, "Start executeCommand");  
		String[] outputCommand = new String[2];
		String s, sError = null;

			// Command is executed
			logger.log(Level.INFO, "Executing command: " + command); 	
			Process p = Runtime.getRuntime().exec(command);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			// Leemos la salida del comando
			logger.log(Level.INFO, "Reading the command exit"); 
			outputCommand[0] = "";
			while ((s = stdInput.readLine()) != null) {
				//logger.log(Level.INFO, "s:" + s); 
				System.out.println("s:" + s);
				outputCommand[0] = outputCommand[0] + s;
			}
			logger.log(Level.INFO, "s:" + outputCommand[0]); 
			
			outputCommand[1] = "";
			while ((sError = stdError.readLine()) != null) {
				//logger.log(Level.INFO, "sError:" + sError); 
				System.out.println("s:" + s);
				outputCommand[1] = outputCommand[1] + sError;
			}
			
			if (sError!= null)
				throw  new ShellCommandException(outputCommand[1]);
			
			logger.log(Level.INFO, "End executeCommand"); 
		
			return outputCommand;
	}


}
