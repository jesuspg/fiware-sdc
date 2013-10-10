package com.telefonica.euro_iaas.sdc.pupperwrapper.controllers;

import java.awt.image.ImagingOpException;
import java.io.IOException;
import java.util.NoSuchElementException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.telefonica.euro_iaas.sdc.pupperwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.pupperwrapper.services.ActionsService;
import com.telefonica.euro_iaas.sdc.pupperwrapper.services.FileCreationService;

@Controller
public class PuppetController /*extends GenericController*/{

	@SuppressWarnings("restriction")
	@Resource
	private ActionsService actionsService;

	@SuppressWarnings("restriction")
	@Resource
	private FileCreationService fileService;

	@RequestMapping("/install/{group}/{nodeName}/{softwareName}")
	@ResponseBody
	public Node install(@PathVariable("group") String group,
			@PathVariable("nodeName") String nodeName,
			@PathVariable ("softwareName") String softwareName,
			HttpServletRequest request) {
		
		System.out.println(request.getHeader("accept"));
		
		if (group==null || "".equals(group)){
			throw new IllegalArgumentException("Group is not set");
		}
		
		if (nodeName==null || "".equals(nodeName)){
			throw new IllegalArgumentException("Node name is not set");
		}
		
		if (softwareName==null || "".equals(softwareName)){
			throw new IllegalArgumentException("Software name is not set");
		}

		Node node= actionsService.install(group, nodeName, softwareName);
		System.out.println("OK "+group);
		return node;
	}

	@RequestMapping("/generate/{nodeName}")
	@ResponseBody
	public void generateManifest(@PathVariable String nodeName)
			throws ImagingOpException, IOException {

		if (nodeName==null || "".equals(nodeName)){
			throw new IllegalArgumentException("Node name is not set");
		}
		
		fileService.generateSiteFile();
		fileService.generateManifestFile(nodeName);
	}
	
	@RequestMapping("/test")
	public @ResponseBody String test() {
		return "test";
	}

}
