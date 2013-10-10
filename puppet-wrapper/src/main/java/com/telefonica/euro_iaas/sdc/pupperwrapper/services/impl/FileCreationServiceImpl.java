package com.telefonica.euro_iaas.sdc.pupperwrapper.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.telefonica.euro_iaas.sdc.pupperwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.pupperwrapper.services.FileCreationService;
import com.telefonica.euro_iaas.sdc.pupperwrapper.services.FileManager;

@Service("fileService")
public class FileCreationServiceImpl implements FileCreationService {

	@Resource
	private FileManager fileManager;

	@Value("${defaultManifestsPath}")
	private String defaultManifestsPath;

	public void generateManifestFile(String nodeName) throws IOException {

		Node node = fileManager.getNode(nodeName);

		String fileContent = fileManager.generateManifestStr(nodeName);
		String path = defaultManifestsPath + node.getGroupName();

		try {

			File f = new File(path);
			f.mkdirs();
			f.createNewFile();
		} catch (IOException ex) {
			throw new IOException("Error creating manifest paths and pp file");
		}

		try {
			FileWriter fw = new FileWriter(path + "/" + node.getName() + ".pp",
					false);
			fw.write(fileContent);
			fw.close();
		} catch (IOException ex) {
			throw new IOException("Error creating manifest paths and pp file");
		}

	}

	public void generateSiteFile() throws IOException {

		String fileContent = fileManager.generateSiteStr();

		try {
			PrintWriter writer = new PrintWriter(defaultManifestsPath
					+ "site.pp", "UTF-8");
			writer.println(fileContent);
			writer.close();
		} catch (IOException ex) {
			throw new IOException("Error creating manifest paths and pp file");
		}
	}

}
