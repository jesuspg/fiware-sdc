package com.telefonica.euro_iaas.sdc.puppetwrapper.services;

import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;

public interface CatalogManager {

    public void addNode(Node node);

    public Node getNode(String nodeName);

    public void removeNode(String nodeName);

    public String generateManifestStr(String nodeName);

    public int getNodeLength();

    public String generateSiteStr();
}
