package com.telefonica.euro_iaas.sdc.puppetwrapper.services;

import java.io.IOException;

import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;

public interface ActionsService {

    public Node install(String group, String nodeName, String softName, String version);

    public Node uninstall(String group, String nodeName, String softName, String version);

    public void deleteNode(String nodeName) throws IOException;

    public void deleteGroup(String groupName) throws IOException;
}
