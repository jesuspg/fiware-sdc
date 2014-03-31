/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl;

import static java.text.MessageFormat.format;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileAccessService;

@Service("fileAccessService")
public class FileAccessServiceImpl implements FileAccessService {

    private static final Log logger = LogFactory.getLog(FileAccessServiceImpl.class);

    @Resource
    protected CatalogManager catalogManager;

    private String defaultManifestsPath;

    public Node generateManifestFile(String nodeName) throws IOException {

        logger.info("creating Manifest file for node: " + nodeName);

        Node node = catalogManager.getNode(nodeName);

        String fileContent = catalogManager.generateManifestStr(nodeName);
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
            FileWriter fw = new FileWriter(path + "/" + node.getId() + ".pp", false);
            fw.write(fileContent);
            fw.close();
        } catch (IOException ex) {
            logger.debug("Error creating manifest paths and pp file", ex);
            throw new IOException("Error creating manifest paths and pp file");
        }

        logger.debug("Manifest file created");

        return node;

    }

    public void generateSiteFile() throws IOException {

        logger.info("Generate site.pp");

        String fileContent = catalogManager.generateSiteStr();

        logger.debug("site content: " + fileContent);
        logger.debug("defaultManifestsPath: " + defaultManifestsPath);

        try {
            PrintWriter writer = new PrintWriter(defaultManifestsPath + "site.pp", "UTF-8");
            writer.println(fileContent);
            writer.close();
        } catch (IOException ex) {
            logger.debug("Error creating site.pp file", ex);
            throw new IOException("Error creating site.pp file");
        }

        logger.debug("Site.pp file created");
    }

    @Value(value = "${defaultManifestsPath}")
    public void setDefaultManifestsPath(String defaultManifestsPath) {
        this.defaultManifestsPath = defaultManifestsPath;
    }

    public void deleteNodeFiles(String nodeName) throws IOException {

        Node node = catalogManager.getNode(nodeName);

        String path = defaultManifestsPath + node.getGroupName();

        File file = new File(path + "/" + node.getId() + ".pp");

        if (!file.delete()) {
            logger.info(format("File {0} could not be deleted. Did it exist?", path + "/" +node.getId() + ".pp"));
        }

    }

    public void deleteGoupFolder(String groupName) throws IOException {
        
        File path = new File(defaultManifestsPath + groupName);
        
        FileUtils.deleteDirectory(path);        
    }

}
