/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.puppetwrapper.services;

import java.awt.image.ImagingOpException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;

public interface FileAccessService {

    public Node generateManifestFile(String nodeName) throws ImagingOpException, IOException;

    public void generateSiteFile() throws FileNotFoundException, UnsupportedEncodingException, IOException;

    public void deleteNodeFiles(String nodeName) throws IOException;

    public void deleteGoupFolder(String groupName) throws IOException;

}
