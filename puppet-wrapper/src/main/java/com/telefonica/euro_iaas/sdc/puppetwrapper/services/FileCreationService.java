package com.telefonica.euro_iaas.sdc.puppetwrapper.services;

import java.awt.image.ImagingOpException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface FileCreationService {
	
	public void generateManifestFile(String nodeName) throws ImagingOpException, IOException;
	
	public void generateSiteFile() throws FileNotFoundException, UnsupportedEncodingException, IOException;

}
