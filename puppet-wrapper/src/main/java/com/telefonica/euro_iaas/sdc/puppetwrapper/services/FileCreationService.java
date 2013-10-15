package com.telefonica.euro_iaas.sdc.puppetwrapper.services;

import java.awt.image.ImagingOpException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;

public interface FileCreationService {
	
	public Node generateManifestFile(String nodeName) throws ImagingOpException, IOException;
	
	public void generateSiteFile() throws FileNotFoundException, UnsupportedEncodingException, IOException;

}
