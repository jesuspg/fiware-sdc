package com.telefonica.euro_iaas.sdc.util;

import java.io.IOException;

import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.model.SOInstance;

/**
 * <p>SODeployer interface.</p>
 *
 * @author ju
 * @version $Id: $
 */
public interface SOStatusChecker {
    /**
     * Check if a SO is properly started
     *
     * @param so a {@link com.telefonica.euro_iaas.sdc.model.SO} object.
     * @return a {@link com.telefonica.euro_iaas.sdc.model.SOInstance} object.
     * @throws ShellCommandException 
     * @throws IOException 
     */
	public void waitOK(SOInstance soInstance) throws InterruptedException, IOException, ShellCommandException;
}
