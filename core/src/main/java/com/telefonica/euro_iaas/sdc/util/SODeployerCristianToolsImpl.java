package com.telefonica.euro_iaas.sdc.util;

import com.telefonica.euro_iaas.sdc.model.SO;
import com.telefonica.euro_iaas.sdc.model.SOInstance;
import com.telefonica.euro_iaas.sdc.model.Image;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;

/**
 * <p>SODeployerCristianToolsImpl class.</p>
 *
 * @author Jesus M. Movilla
 * @version $Id: $
 */
public class SODeployerCristianToolsImpl extends AbstractShellCommand implements SODeployer {

    private static Logger logger = Logger.getLogger("SODeployerCrisitanToolsImpl");

    /** {@inheritDoc} */
    @Override
    public SOInstance deploySO(SO so) {
        // TODO Auto-generated method stub
        SOInstance soInstance = new SOInstance();
        try
        {
            soInstance.setSo(so);
            soInstance.setDate(new Date());
            soInstance.setStatus(SOInstance.Status.INSTALLING);
            String hostname = generateHostname(so);
            createImage(so, hostname);

            soInstance.setSo(so);
            soInstance.setDate(new Date());
            soInstance.setStatus(SOInstance.Status.RUNNING);
            soInstance.setHostname (hostname);
        } catch (IOException ioe)
        {
            logger.log (Level.SEVERE, ioe.getMessage());
        } catch (ShellCommandException sce)
        {
            logger.log (Level.SEVERE, sce.getMessage());
        }
        return soInstance;
    }
    
    public Image stopImage (Image image) {
    	try{
    		haltImage(image);
    		image.setUrl("http://piolin3.hi.inet/images/" + image.getSo().getHostname() + ".img");
    		
    	} catch (IOException ioe)
        {
            logger.log (Level.SEVERE, ioe.getMessage());
        } catch (ShellCommandException sce)
        {
            logger.log (Level.SEVERE, sce.getMessage());
        }
        return image;
    	
    }
    
    private void createImage(SO so, String hostname) throws IOException, ShellCommandException
    {
        String[] output = new String[2];
        String command = "/home/images/kvm/newVm.py " + so.getName() + " " + hostname;
        output = executeCommand (command);
    }

    private String generateHostname(SO so)
    {
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("yyMMddHHmmssZ");
            String s = formatter.format(date);
            String hostname = so.getName() + "-" + s.substring(0, s.length() - 5);
            logger.log (Level.INFO, "hostname = " + hostname);

            return hostname;
    }
    
    private void haltImage(Image imagen) throws IOException, ShellCommandException
    {
        String[] output = new String[2];
        String command = "/home/images/kvm/haltVM.py " + imagen.getSo().getHostname();
        output = executeCommand (command);
        for(String s : output)
        	System.out.println("TRONQUITO:: "+s);
    }

}
