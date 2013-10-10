package com.telefonica.euro_iaas.sdc.pupperwrapper.services.impl;

import java.util.NoSuchElementException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.telefonica.euro_iaas.sdc.pupperwrapper.constants.Action;
import com.telefonica.euro_iaas.sdc.pupperwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.pupperwrapper.data.Software;
import com.telefonica.euro_iaas.sdc.pupperwrapper.services.FileManager;
import com.telefonica.euro_iaas.sdc.pupperwrapper.services.ActionsService;

@Service("actionsService")
public class ActionsServiceImpl implements ActionsService {
	
	@SuppressWarnings("restriction")
	@Resource
	private FileManager fileManager;
	
	public Node install(String group, String nodeName, String softName){
		
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
		
		return node;
		
	}

}
