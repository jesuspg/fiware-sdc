/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.text.MessageFormat;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.services.ChefNodeService;
import com.telefonica.euro_iaas.sdc.client.services.SdcClientConfig;
import com.telefonica.euro_iaas.sdc.model.Task;

public class ChefNodeServiceImpl extends AbstractBaseService implements ChefNodeService {

    /**
     * @param clientConfig
     * @param baseHost
     * @param type
     */
    public ChefNodeServiceImpl(SdcClientConfig clientConfig, String baseHost, String type) {
        setSdcClientConfig(clientConfig);
        setBaseHost(baseHost);
        setType(type);
    }

    public Task delete(String vdc, String chefNodeName, String token) {

        String url = getBaseHost() + MessageFormat.format(ClientConstants.CHEFNODE_PATH, vdc, chefNodeName);
        Response response = null;
        try {
            Invocation.Builder builder = createWebResource(url, token, vdc);
            response = builder.delete();
            return response.readEntity(Task.class);
        } finally {
            if (response != null) {
                response.close();
            }
        }

    }

}
