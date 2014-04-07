/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.telefonica.euro_iaas.sdc.puppetwrapper.common.Action;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Software;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.ActionsService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileAccessService;

@Service("actionsService")
public class ActionsServiceImpl implements ActionsService {

    private static final Log logger = LogFactory.getLog(ActionsServiceImpl.class);

    @SuppressWarnings("restriction")
    @Resource
    protected CatalogManager catalogManager;

    @Resource
    protected FileAccessService fileAccessService;

    @Resource
    protected ProcessBuilderFactory processBuilderFactory;

    public Node action(Action action, String group, String nodeName, String softName, String version) {

        logger.info("action: " + action + "group:" + group + " nodeName: " + nodeName + " soft: " + softName
                + " version: " + version);

        Node node = null;
        try {
            node = catalogManager.getNode(nodeName);
            node.setGroupName(group);
        } catch (NoSuchElementException e) {
            node = new Node();
            node.setId(nodeName);
            node.setGroupName(group);
        }

        Software soft = null;
        try {
            soft = node.getSoftware(softName);
            soft.setVersion(version);
            soft.setAction(action);
        } catch (NoSuchElementException e) {
            soft = new Software();
            soft.setName(softName);
            soft.setVersion(version);
            soft.setAction(action);
            node.addSoftware(soft);
        }

        catalogManager.addNode(node);

        logger.debug("node: " + node);

        return node;

    }

    public void deleteNode(String nodeName) throws IOException {
        fileAccessService.deleteNodeFiles(nodeName);
        catalogManager.removeNode(nodeName);

        // generate the file again to make sure there are no empty directories
        fileAccessService.generateSiteFile();

        uregisterNode(nodeName);

    }

    private void uregisterNode(String nodeName) throws IOException {

        logger.debug("Unregistering node: " + nodeName);

        if (isNodeRegistered(nodeName)) {
            logger.debug("Node " + nodeName + " is registered -> unregistering");

            String[] cmd = { "/bin/sh", "-c", "sudo puppet cert clean "+getRealNodeName(nodeName) };
            
            Process shell = processBuilderFactory.createProcessBuilder(cmd);

            StringBuilder success = new StringBuilder();
            StringBuilder error = new StringBuilder();

            executeSystemCommand(shell, success, error);

            if ("".equals(success) && !"".equals(error)) {
                throw new IOException("Puppet cert clean has failed");
            }
        }

    }

    public String getRealNodeName(String nodeName) throws IOException {

        logger.debug("getRealNodeName for node: " + nodeName);

        String[] cmd = { "/bin/sh", "-c", "sudo puppet cert list --all | grep " + nodeName + " | gawk '{print $2}'" };

        Process shell = processBuilderFactory.createProcessBuilder(cmd);

        StringBuilder success = new StringBuilder();
        StringBuilder error = new StringBuilder();

        executeSystemCommand(shell, success, error);

        if ("".equals(success) && !"".equals(error)) {
            throw new IOException("Puppet cert clean has failed");
        }
        logger.debug("success, real name is: " + success);

        String name = success.substring(1, success.length() - 1);

        logger.debug("name: " + name);

        return name;

    }

    public boolean isNodeRegistered(String nodeName) throws IOException {

        logger.debug("isNodeRegistered node: " + nodeName);
        
        String[] cmd = { "/bin/sh", "-c", "sudo puppet cert list --all" };
        Process shell = processBuilderFactory.createProcessBuilder(cmd);

        StringBuilder successResponse = new StringBuilder();
        StringBuilder errorResponse = new StringBuilder();

        executeSystemCommand(shell, successResponse, errorResponse);

        String str = (successResponse.length() == 0 ? "" : successResponse.toString());

        if (!"".equals(str)) {
            if (!successResponse.toString().contains(nodeName)) {
                // logger.debug("registered nodes: ");
                // logger.debug(str);
                return false;
            }

        } else {
            String msg = "Puppet cert list command has failed";
            logger.debug(msg);
            throw new IOException(msg);
        }
        return true;

    }

    public void deleteGroup(String groupName) throws IOException {
        fileAccessService.deleteGoupFolder(groupName);
        catalogManager.removeNodesByGroupName(groupName);

    }

    private void executeSystemCommand(Process shell, StringBuilder successResponse, StringBuilder errorResponse)
            throws IOException {

        InputStream is = shell.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        while ((line = br.readLine()) != null) {
            System.out.println(line);
            successResponse.append(line);
        }

        InputStream isEr = shell.getErrorStream();
        InputStreamReader isrEr = new InputStreamReader(isEr);
        BufferedReader brEr = new BufferedReader(isrEr);
        String lineEr;
        while ((lineEr = brEr.readLine()) != null) {
            System.out.println(lineEr);
            errorResponse.append(lineEr);
        }

    }

    // private void executeSystemCommand(Process shell, StringBuilder
    // successResponse, StringBuilder errorResponse) throws IOException{
    //
    // try {
    // Process p = Runtime.getRuntime().exec("puppet cert list --all");
    // BufferedReader in = new BufferedReader(
    // new InputStreamReader(p.getInputStream()));
    // String line = null;
    // while ((line = in.readLine()) != null) {
    // System.out.println(line);
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

}
