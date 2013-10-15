package com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl;

import java.util.NoSuchElementException;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.telefonica.euro_iaas.sdc.puppetwrapper.common.Action;
import com.telefonica.euro_iaas.sdc.puppetwrapper.controllers.PuppetController;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Software;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.ActionsService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileManager;

@Service("actionsService")
public class ActionsServiceImpl implements ActionsService {
	
	private static final Log logger = LogFactory.getLog(ActionsServiceImpl.class);
	
	@SuppressWarnings("restriction")
	@Resource
	private FileManager fileManager;
	
	public Node install(String group, String nodeName, String softName){
		
		logger.info("install group:"+group+ " nodeName: "+nodeName+" soft: "+softName);
		
		Node node=null;
		try{
			node=fileManager.getNode(nodeName);
			node.setGroupName(group);
		}catch (NoSuchElementException e){
			node=new Node();
			node.setName(nodeName);
			node.setGroupName(group);
			fileManager.addNode(node);
		}
		
		Software soft=null;
		try{
			soft=node.getSoftware(softName);
			soft.setAction(Action.INSTALL);
		}catch (NoSuchElementException e){
			soft=new Software();
			soft.setName(softName);
			soft.setAction(Action.INSTALL);
			node.addSoftware(soft);
		}
		
		logger.debug("node: "+node);
		
		return node;
		
	}

}
