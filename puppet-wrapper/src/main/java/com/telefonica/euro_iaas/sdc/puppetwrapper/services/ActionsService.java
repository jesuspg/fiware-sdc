package com.telefonica.euro_iaas.sdc.puppetwrapper.services;

import java.io.IOException;

import com.telefonica.euro_iaas.sdc.puppetwrapper.common.Action;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;

public interface ActionsService {

    public Node action(Action action, String group, String nodeName, String softName, String version);

    public void deleteNode(String nodeName) throws IOException;

    public void deleteGroup(String groupName) throws IOException;
}
