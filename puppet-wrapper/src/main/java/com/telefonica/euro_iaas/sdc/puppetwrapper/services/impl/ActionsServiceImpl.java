package com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl;

import java.io.IOException;
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
    private CatalogManager catalogManager;

    @Resource
    private FileAccessService fileAccessService;

    public Node install(String group, String nodeName, String softName, String version) {

        logger.info("install group:" + group + " nodeName: " + nodeName + " soft: " + softName + " version: " + version);

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
            soft.setAction(Action.INSTALL);
        } catch (NoSuchElementException e) {
            soft = new Software();
            soft.setName(softName);
            soft.setVersion(version);
            soft.setAction(Action.INSTALL);
            node.addSoftware(soft);
        }
        
        catalogManager.addNode(node);

        logger.debug("node: " + node);

        return node;

    }

    public Node uninstall(String group, String nodeName, String softName, String version) {
        // TODO Auto-generated method stub
        return null;
    }

    public void deleteNode(String nodeName) throws IOException {
        fileAccessService.deleteNodeFiles(nodeName);
        catalogManager.removeNode(nodeName);

    }

}
