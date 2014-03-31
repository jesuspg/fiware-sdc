/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.text.MessageFormat;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.services.ChefNodeService;
import com.telefonica.euro_iaas.sdc.model.Task;

public class ChefNodeServiceImpl extends AbstractBaseService implements ChefNodeService {

    /**
     * @param client
     * @param baseHost
     * @param type
     */
    public ChefNodeServiceImpl(Client client, String baseHost, String type) {
        setClient(client);
        setBaseHost(baseHost);
        setType(type);
    }

    public Task delete(String vdc, String chefNodeName, String token) throws InvalidExecutionException {
        try {

            String url = getBaseHost() + MessageFormat.format(ClientConstants.CHEFNODE_PATH, vdc, chefNodeName);
            WebResource.Builder builder= createWebResource (url, token, vdc);
            return builder.delete(Task.class);

        } catch (UniformInterfaceException e) {
            String errorMsg = " Error deleting a chefnode " + chefNodeName + " in vdc " + vdc + ". Description: "
                    + e.getMessage();
            throw new InvalidExecutionException(errorMsg);
        }

    }

}
