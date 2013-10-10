package com.telefonica.euro_iaas.sdc.pupperwrapper.services;

import com.telefonica.euro_iaas.sdc.pupperwrapper.data.Node;


public interface ActionsService {

	public Node install(String group, String nodeName, String softName);
}
