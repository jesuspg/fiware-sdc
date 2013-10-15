package com.telefonica.euro_iaas.sdc.puppetwrapper.services;

import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;


public interface ActionsService {

	public Node install(String group, String nodeName, String softName, String version);
}
