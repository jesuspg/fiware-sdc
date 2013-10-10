package com.telefonica.euro_iaas.sdc.pupperwrapper.services;

import com.telefonica.euro_iaas.sdc.pupperwrapper.data.Node;

public interface FileManager {

	public void addNode(Node node);

	public Node getNode(String nodeName);

	public void removeNode(Node node);

	public String generateManifestStr(String nodeName);
	
	public int getNodeLength();

	public String generateSiteStr();
}
