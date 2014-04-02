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

package com.telefonica.euro_iaas.sdc.puppetwrapper.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.telefonica.euro_iaas.sdc.puppetwrapper.common.Action;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.ModuleDownloaderException;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.ActionsService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileAccessService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.GitCloneService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.ModuleDownloader;

@Controller
public class PuppetController /* extends GenericController */{

    private static final Log logger = LogFactory.getLog(PuppetController.class);

    @Resource
    private ActionsService actionsService;

    @Resource
    private FileAccessService fileAccessService;

    @Resource
    private CatalogManager catalogManager;

    @Resource
    private ModuleDownloader gitCloneService;

    @Resource
    private ModuleDownloader svnExporterService;

    @RequestMapping(value = "/install/{group}/{nodeName}/{softwareName}/{version}", method = RequestMethod.POST)
    public @ResponseBody
    Node install(@PathVariable("group") String group, @PathVariable("nodeName") String nodeName,
            @PathVariable("softwareName") String softwareName, @PathVariable("version") String version,
            HttpServletRequest request) {

        logger.info("install group:" + group + " nodeName: " + nodeName + " soft: " + softwareName + " version: "
                + version);

        if (group == null || "".equals(group)) {
            logger.debug("Group is not set");
            throw new IllegalArgumentException("Group is not set");
        }

        if (nodeName == null || "".equals(nodeName)) {
            logger.debug("Node name is not set");
            throw new IllegalArgumentException("Node name is not set");
        }

        if (softwareName == null || "".equals(softwareName)) {
            logger.debug("Software Name is not set");
            throw new IllegalArgumentException("Software name is not set");
        }

        if (softwareName == null || "".equals(version)) {
            logger.debug("version is not set");
            throw new IllegalArgumentException("Version is not set");
        }

        Node node = actionsService.action(Action.INSTALL, group, nodeName, softwareName, version);

        logger.debug("node " + node);

        return node;
    }

    @RequestMapping(value = "/generate/{nodeName}", method = RequestMethod.POST)
    public @ResponseBody
    Node generateManifest(@PathVariable("nodeName") String nodeName) throws FileNotFoundException,
            UnsupportedEncodingException, IOException {

        if (nodeName == null || "".equals(nodeName)) {
            throw new IllegalArgumentException("Node name is not set");
        }
        logger.info("generating files for node:" + nodeName);

        fileAccessService.generateSiteFile();
        logger.debug("site.pp OK");

        Node node = fileAccessService.generateManifestFile(nodeName);
        logger.debug("nodes pp files OK");

        return node;
    }

    @RequestMapping(value = "/uninstall/{group}/{nodeName}/{softwareName}/{version}", method = RequestMethod.POST)
    public @ResponseBody
    Node uninstall(@PathVariable("group") String group, @PathVariable("nodeName") String nodeName,
            @PathVariable("softwareName") String softwareName, @PathVariable("version") String version,
            HttpServletRequest request) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        logger.info("install group:" + group + " nodeName: " + nodeName + " soft: " + softwareName + " version: "
                + version);

        if (group == null || "".equals(group)) {
            logger.debug("Group is not set");
            throw new IllegalArgumentException("Group is not set");
        }

        if (nodeName == null || "".equals(nodeName)) {
            logger.debug("Node name is not set");
            throw new IllegalArgumentException("Node name is not set");
        }

        if (softwareName == null || "".equals(softwareName)) {
            logger.debug("Software Name is not set");
            throw new IllegalArgumentException("Software name is not set");
        }

        if (softwareName == null || "".equals(version)) {
            logger.debug("version is not set");
            throw new IllegalArgumentException("Version is not set");
        }

        Node node = actionsService.action(Action.UNINSTALL, group, nodeName, softwareName, version);

        logger.debug("node " + node);

        return node;

    }

    @RequestMapping(value = "/delete/node/{nodeName}", method = RequestMethod.POST)
    public @ResponseBody void deleteNode(@PathVariable("nodeName") String nodeName) throws IOException {

        actionsService.deleteNode(nodeName);
    }

    @RequestMapping(value = "/delete/group/{groupName}", method = RequestMethod.POST)
    public @ResponseBody void deleteGroup(@PathVariable("groupName") String groupName) throws IOException {

        actionsService.deleteGroup(groupName);
    }

    @RequestMapping(value="/download/git/{softwareName}", method=RequestMethod.POST)
    public @ResponseBody void downloadModuleFromGit(@PathVariable("softwareName") String softwareName, 
            @RequestParam(value = "url", required = true) String url) throws  ModuleDownloaderException {

        gitCloneService.download(url, softwareName);
        
    }
    
    @RequestMapping(value="/download/svn/{softwareName}", method=RequestMethod.POST)
    public @ResponseBody void downloadModuleFromSVN(@PathVariable("softwareName") String softwareName, 
            @RequestParam(value = "url", required = true) String url) throws ModuleDownloaderException {

        svnExporterService.download(url, softwareName);
        
    }

}
