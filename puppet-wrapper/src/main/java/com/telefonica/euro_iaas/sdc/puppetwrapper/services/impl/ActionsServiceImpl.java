/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

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
    protected CatalogManager catalogManager;

    @Resource
    protected FileAccessService fileAccessService;

    public Node action(Action action, String group, String nodeName, String softName, String version) {

        logger.info("action: "+action+ "group:" + group + " nodeName: " + nodeName + " soft: " + softName + " version: " + version);

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

    }

    public void deleteGroup(String groupName) throws IOException {
        fileAccessService.deleteGoupFolder(groupName);
        catalogManager.removeNodesByGroupName(groupName);
        
    }

}
