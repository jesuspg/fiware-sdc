package com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.telefonica.euro_iaas.sdc.puppetwrapper.controllers.PuppetController;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileCreationService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileManager;

@Service("fileService")
public class FileCreationServiceImpl implements FileCreationService {

	private static final Log logger = LogFactory.getLog(FileCreationServiceImpl.class);

	@Resource
	private FileManager fileManager;

	private String defaultManifestsPath;

	public Node generateManifestFile(String nodeName) throws IOException {
		
		logger.info("creating Manifest file for node: "+nodeName);

		Node node = fileManager.getNode(nodeName);

		String fileContent = fileManager.generateManifestStr(nodeName);
		String path = defaultManifestsPath + node.getGroupName();

		try {

			File f = new File(path);
			f.mkdirs();
			f.createNewFile();
		} catch (IOException ex) {
			logger.debug("Error creating manifest paths and pp file", ex);
			throw new IOException("Error creating manifest paths and pp file");
		}

		try {
			FileWriter fw = new FileWriter(path + "/" + node.getName() + ".pp",
					false);
			fw.write(fileContent);
			fw.close();
		} catch (IOException ex) {
			logger.debug("Error creating manifest paths and pp file", ex);
			throw new IOException("Error creating manifest paths and pp file");
		}
		
		logger.debug("Manifest file created");
		
		return node;

	}

	public void generateSiteFile() throws IOException{

		logger.info("Generate site.pp");
		
		String fileContent = fileManager.generateSiteStr();

		logger.debug("site content: "+ fileContent);
		logger.debug("defaultManifestsPath: "+defaultManifestsPath);
		
		try {
			PrintWriter writer = new PrintWriter(defaultManifestsPath
					+ "site.pp", "UTF-8");
			writer.println(fileContent);
			writer.close();
		} catch (IOException ex) {
			logger.debug("Error creating site.pp file", ex);
			throw new IOException("Error creating site.pp file");
		}
		
		logger.debug("Site.pp file created");
	}
	
	@Value(value= "${defaultManifestsPath}" )
	public void setDefaultManifestsPath(String defaultManifestsPath) {
		this.defaultManifestsPath = defaultManifestsPath;
	}

}
