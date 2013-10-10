package com.telefonica.euro_iaas.sdc.pupperwrapper.services.impl;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.telefonica.euro_iaas.sdc.pupperwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.pupperwrapper.services.FileManager;

@Service("fileManager")
public class FileManagerImpl implements FileManager {

	private String eol = System.getProperty("line.separator"); 
	
	private List<Node> nodes = new ArrayList<Node>();

	public void addNode(Node node) {
		nodes.add(node);
	}

	public Node getNode(String nodeName) {

		Node result = null;
		for (Node node : nodes) {
			if (node.getName().equals(nodeName)) {
				result = node;
				break;
			}
		}
		if (result == null) {
			throw new NoSuchElementException(format(
					"The node {0} could not be found", nodeName));
		}
		return result;

	}

	public void removeNode(Node node) {
		nodes.remove(node);
	}

	public String generateManifestStr(String nodeName) {
		StringBuffer sb = new StringBuffer();
		Node node = getNode(nodeName);
		sb.append(node.generateFileStr());
		return sb.toString();
	}
	
	public int getNodeLength(){
		return nodes.size();
	}

	public String generateSiteStr() {
		
		StringBuffer sb = new StringBuffer();
		
		for(Node node:nodes){
			sb.append("import '"+ node.getGroupName()+"/*.pp'");
			sb.append(eol);		
		}
		return sb.toString();
		
	}

}
