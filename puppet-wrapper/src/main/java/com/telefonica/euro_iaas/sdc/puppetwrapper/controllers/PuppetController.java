package com.telefonica.euro_iaas.sdc.puppetwrapper.controllers;

import java.awt.image.ImagingOpException;
import java.io.IOException;

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
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileCreationService;

@Controller
public class PuppetController /*extends GenericController*/{
	
	private static final Log logger = LogFactory.getLog(PuppetController.class);

	@Resource
	private ActionsService actionsService;

	@Resource
	private FileCreationService fileService;

	@RequestMapping("/install/{group}/{nodeName}/{softwareName}")
	@ResponseBody
	public Node install(@PathVariable("group") String group,
			@PathVariable("nodeName") String nodeName,
			@PathVariable ("softwareName") String softwareName,
			HttpServletRequest request) {
		
		logger.info("install group:"+group+ " nodeName: "+nodeName+" soft: "+softwareName);
		
		if (group==null || "".equals(group)){
			logger.debug("Group is not set");
			throw new IllegalArgumentException("Group is not set");
		}
		
		if (nodeName==null || "".equals(nodeName)){
			logger.debug("Node name is not set");
			throw new IllegalArgumentException("Node name is not set");
		}
		
		if (softwareName==null || "".equals(softwareName)){
			logger.debug("Software Name is not set");
			throw new IllegalArgumentException("Software name is not set");
		}
		
		Node node= actionsService.install(group, nodeName, softwareName);
		
		logger.debug("node "+node);
		
		return node;
	}

	@RequestMapping("/generate/{nodeName}")
	@ResponseBody
	public void generateManifest(@PathVariable String nodeName)
			throws ImagingOpException, IOException {

		if (nodeName==null || "".equals(nodeName)){
			throw new IllegalArgumentException("Node name is not set");
		}
		logger.info("generating files for node:" + nodeName);
		
		fileService.generateSiteFile();
		logger.debug("site.pp OK");
		
		fileService.generateManifestFile(nodeName);
		logger.debug("nodes pp files OK");
	}
	
	@RequestMapping("/test")
	public @ResponseBody String test() {
		return "test";
	}

}
