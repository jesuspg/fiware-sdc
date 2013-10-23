/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.puppetwrapper.services;

import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;

public interface CatalogManager {

    
    public void addNode(Node node);

    public Node getNode(String nodeName);

    public void removeNode(String nodeName);
    
    public int getNodeLength();

    public String generateManifestStr(String nodeName);

    public String generateSiteStr();

    public void removeNodesByGroupName(String groupName);
}
