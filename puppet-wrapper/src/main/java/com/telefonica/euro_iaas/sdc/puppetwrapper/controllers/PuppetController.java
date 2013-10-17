package com.telefonica.euro_iaas.sdc.puppetwrapper.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.ActionsService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileAccessService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;

@Controller
public class PuppetController /* extends GenericController */{

    private static final Log logger = LogFactory.getLog(PuppetController.class);

    @Resource
    private ActionsService actionsService;

    @Resource
    private FileAccessService fileAccessService;

    @Resource
    private CatalogManager catalogManager;

    @RequestMapping("/install/{group}/{nodeName}/{softwareName}/{version}")
    @ResponseBody
    public Node install(@PathVariable("group") String group, @PathVariable("nodeName") String nodeName,
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

        Node node = actionsService.install(group, nodeName, softwareName, version);

        logger.debug("node " + node);

        return node;
    }

    @RequestMapping("/generate/{nodeName}")
    @ResponseBody
    public Node generateManifest(@PathVariable("nodeName") String nodeName) throws FileNotFoundException,
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

    @RequestMapping("/delete/node/{nodeName}")
    @ResponseBody
    public void deleteNode(@PathVariable("nodeName") String nodeName) throws IOException {

        actionsService.deleteNode(nodeName);
    }

    @RequestMapping("/test")
    public @ResponseBody
    String test() {
        return "test";
    }

}
