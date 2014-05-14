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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.telefonica.euro_iaas.sdc.puppetwrapper.common.Action;
import com.telefonica.euro_iaas.sdc.puppetwrapper.common.URLValue;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.ModuleDownloaderException;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.ActionsService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileAccessService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.ModuleDownloader;

@Controller
@RequestMapping(value = "/v2")
public class PuppetControllerv2 extends GenericController {

    private static final Log logger = LogFactory.getLog(PuppetControllerv2.class);
    
    public static final String gitRepoSource="git";
    public static final String svnRepoSource="svn";

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

    @RequestMapping(value = "/node/{nodeName}/install", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public @ResponseBody
    Node install(@RequestBody String group, @PathVariable String nodeName, @RequestBody String softwareName,
            @RequestBody String version, HttpServletRequest request) {

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

        if (version == null || "".equals(version)) {
            logger.debug("version is not set");
            throw new IllegalArgumentException("Version is not set");
        }

        Node node = actionsService.action(Action.INSTALL, group, nodeName, softwareName, version);

        logger.debug("node " + node);

        return node;
    }

    @RequestMapping(value = "/node/{nodeName}/generate", method = RequestMethod.POST)
    public @ResponseBody
    Node generateManifest(@PathVariable("nodeName") String nodeName) throws FileNotFoundException,
            UnsupportedEncodingException, IOException {

        if (nodeName == null || "".equals(nodeName)) {
            throw new IllegalArgumentException("Node name is not set");
        }
        logger.info("generating files for node:" + nodeName);

        Node node = fileAccessService.generateManifestFile(nodeName);
        logger.debug("nodes pp files OK");

        fileAccessService.generateSiteFile();
        logger.debug("site.pp OK");

        return node;
    }

    @RequestMapping(value = "/node/{nodeName}/uninstall", method = RequestMethod.POST)
    public @ResponseBody
    Node uninstall(@RequestBody String group, @PathVariable String nodeName, @RequestBody String softwareName,
            @RequestBody String version, HttpServletRequest request) {

        logger.info("uninstall group:" + group + " nodeName: " + nodeName + " soft: " + softwareName + " version: "
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

        if (version == null || "".equals(version)) {
            logger.debug("version is not set");
            throw new IllegalArgumentException("Version is not set");
        }

        Node node = actionsService.action(Action.UNINSTALL, group, nodeName, softwareName, version);

        logger.debug("node " + node);

        return node;

    }

    @RequestMapping(value = "/node/{nodeName}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteNode(@PathVariable("nodeName") String nodeName) throws IOException {
        
        if (nodeName == null || "".equals(nodeName)) {
            logger.debug("Node name is not set");
            throw new IllegalArgumentException("Node name is not set");
        }

        logger.info("Deleting node: " + nodeName);
        actionsService.deleteNode(nodeName);
        logger.info("Node: " + nodeName + " deleted.");
    }


    @RequestMapping(value = "/module/{softwareName}/download",  method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void downloadModule(@PathVariable("softwareName") String softwareName, @RequestBody URLValue url,
            @RequestBody String repoSource) throws ModuleDownloaderException {
        
        if (softwareName == null || "".equals(softwareName)) {
            logger.debug("Software name is not set");
            throw new IllegalArgumentException("Software name is not set");
        }
        
        if(url==null || "".equals(url.getUrl())){
            logger.debug("Url is not set");
            throw new IllegalArgumentException("Url is not set");
        }
        
        if(repoSource==null || "".equals(repoSource)){
            logger.debug("repoSource is not set");
            throw new IllegalArgumentException("repoSource is not set");
        }
        
        if(gitRepoSource.equals(repoSource)){
            gitCloneService.download(url.getUrl(), softwareName);
        }else if(svnRepoSource.equals(repoSource)){
            svnExporterService.download(url.getUrl(), softwareName);
        }else{
            throw new ModuleDownloaderException("RepoSource parameter is incorrect");
        }

    }

    @RequestMapping(value = "/module/{moduleName}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteModule(@PathVariable("moduleName") String moduleName) throws IOException {
        
        if (moduleName == null || "".equals(moduleName)) {
            logger.debug("Module name is not set");
            throw new IllegalArgumentException("Module name is not set");
        }

        logger.info("Deleting module: " + moduleName);
        actionsService.deleteModule(moduleName);
        logger.info("Module: " + moduleName + " deleted.");
    }
    

    public void setActionsService(ActionsService actionsService) {
        this.actionsService = actionsService;
    }

    public void setFileAccessService(FileAccessService fileAccessService) {
        this.fileAccessService = fileAccessService;
    }

    public void setCatalogManager(CatalogManager catalogManager) {
        this.catalogManager = catalogManager;
    }

    public void setGitCloneService(ModuleDownloader gitCloneService) {
        this.gitCloneService = gitCloneService;
    }

    public void setSvnExporterService(ModuleDownloader svnExporterService) {
        this.svnExporterService = svnExporterService;
    }
    


}
