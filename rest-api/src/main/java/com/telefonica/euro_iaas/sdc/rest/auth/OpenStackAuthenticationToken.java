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

package com.telefonica.euro_iaas.sdc.rest.auth;

import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telefonica.euro_iaas.sdc.rest.exception.AuthenticationConnectionException;

/**
 * Class to obtain a valid token from the OpenStack.
 */
public class OpenStackAuthenticationToken {
    /**
     * http code 200.
     */
    private static final int CODE_200 = 200;
    /**
     * http code 201.
     */
    private static final int CODE_203 = 203;

    /**
     * The log.
     */
    // private Timer timer;
    /**
     * The token ID.
     */
    private String token;
    /**
     * The tenant ID.
     */
    private String tenantId;
    /**
     * The url of the keystone service.
     */
    private String url;
    /**
     * The tenant name.
     */
    private String tenant;
    /**
     * The user of the keystone admin.
     */
    private String user;
    /**
     * The pass of the keystone admin.
     */
    private String pass;

    /**
     * Rest client.
     */
    private Client client;
    /**
     * The log.
     */
    private static Logger log = LoggerFactory.getLogger(OpenStackAuthenticationToken.class);

    /**
     * The default constructor of the class OpenStackAuthenticationToken.
     * 
     * @param params
     *            The array of params with the values of url, tenant, user and password, together with the httpClient
     *            and threshold.
     */
    OpenStackAuthenticationToken(ArrayList<Object> params) {
        initialize(params);
    }

    /**
     * Function to initialize the data structure.
     * 
     * @param params
     *            List of parameters to initialize.
     */
    public void initialize(ArrayList<Object> params) {
        this.token = "";
        this.tenantId = "";
        this.url = (String) params.get(0);
        this.tenant = (String) params.get(1);
        this.user = (String) params.get(2);
        this.pass = (String) params.get(3);
        this.client = (Client) params.get(4);

    }

    /**
     * Request to OpenStack a valid token/tenantId.
     * 
     * @return The new credential (tenant id and token).
     */
    public String[] getCredentials() {
        String[] credential = new String[2];

        log.info("generate new valid token for admin");

        try {
            Response response;

            WebTarget wr = this.client.target(url);

            String payload = "{\"auth\": {\"tenantName\": \"" + tenant + "\", \""
                    + "passwordCredentials\":{\"username\": \"" + user + "\"," + " \"password\": \"" + pass + "\"}}}";

            Invocation.Builder builder = wr.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

            response = builder.post(Entity.entity(payload, MediaType.APPLICATION_JSON));

            if ((response.getStatus() == CODE_200) || (response.getStatus() == CODE_203)) {

                JSONObject jsonObject = JSONObject.fromObject(response.readEntity(String.class));
                jsonObject = (JSONObject) jsonObject.get("access");

                if (jsonObject.containsKey("token")) {

                    JSONObject tokenObject = (JSONObject) jsonObject.get("token");
                    token = (String) tokenObject.get("id");
                    tenantId = (String) ((JSONObject) tokenObject.get("tenant")).get("id");

                    credential[0] = token;
                    credential[1] = tenantId;
                    log.info("generated new token for tenantId:" + tenantId);

                }
            } else {
                String exceptionMessage = "Failed : HTTP error code : (" + url + ")" + response.getStatus()
                        + " message: " + response;
                log.error(exceptionMessage);
                throw new AuthenticationConnectionException(exceptionMessage);

            }
        } catch (Exception ex) {
            throw new AuthenticationConnectionException("Error in authentication: " + ex);
        }

        return credential;

    }

}
