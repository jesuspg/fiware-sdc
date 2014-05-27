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

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.rest.validation;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.security.core.context.SecurityContextHolder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.sdc.exception.OpenStackException;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.model.dto.OpenStackUser;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.sdc.rest.exception.UnauthorizedOperationException;
import com.telefonica.euro_iaas.sdc.util.Configuration;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class ProductInstanceResourceValidatorImpl implements ProductInstanceResourceValidator {

    private SystemPropertiesProvider systemPropertiesProvider;
    private OpenStackRegion openStackRegion;

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.rest.validation.ProductInstanceResourceValidator#validateInsert()
     */
    public void validateInsert(ProductInstanceDto product) throws UnauthorizedOperationException, OpenStackException {
        OpenStackUser user = getCredentials();

        List<String> ips = new ArrayList<String>();
        List<String> serversIds = findAllServers(user);

        for (int i = 0; i < serversIds.size(); i++) {
            String ip = findServerIP(user, serversIds.get(i));
            ips.add(ip);
        }

        if (!(ips.contains(product.getVm().getIp()))) {
            String message = " The Server with ip " + product.getVm().getIp() + " does not belong to user with token "
                    + user.getToken();
            throw new UnauthorizedOperationException(message);
        }

    }

    /**
     * Get the Credentials (FIWARE) from the Spring SecurityContext
     */
    private OpenStackUser getCredentials() {
        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
            return (OpenStackUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            return null;
        }
    }

    /**
     * Obtain the serversIds associated to a certain user
     * 
     * @param user
     * @return
     * @throws OpenStackException 
     */
    private List<String> findAllServers(OpenStackUser user) throws OpenStackException {
        // http://130.206.80.63:8774/v2/ebe6d9ec7b024361b7a3882c65a57dda/servers
        String url = this.openStackRegion.getNovaEndPoint(openStackRegion.getDefaultRegion(user.getToken()),user.getToken() ) 
                + Configuration.VERSION_PROPERTY + user.getTenantId()
                + "/servers";
        String output = getResourceOpenStack(url, user.getToken());

        return getServerIds(output);

    }

    /**
     * Obtain the ip associated to a certain serverid
     * 
     * @param user
     *            \param serverId
     * @return
     * @throws OpenStackException 
     */
    private String findServerIP(OpenStackUser user, String serverId) throws OpenStackException {
        String url = this.openStackRegion.getNovaEndPoint(openStackRegion.getDefaultRegion(user.getToken()),user.getToken() ) 
                + Configuration.VERSION_PROPERTY + user.getTenantId()
                + "/servers/" + serverId;
        String output = getResourceOpenStack(url, user.getToken());

        return getServerPublicIP(output);
    }

    public String getResourceOpenStack(String url, String token) {
        Client client = new Client();

        WebResource wr = client.resource(url);
        Builder builder = wr.accept("application/json");
        // headers
        builder = builder.header("X-Auth-Token", token);

        ClientResponse response = builder.get(ClientResponse.class);
        return response.getEntity(String.class);
    }

    /**
     * Obtains the serversId frm the response of a GET call
     * 
     * @param response
     * @return List of serverIds
     */
    public List<String> getServerIds(String response) {
        List<String> servers = new ArrayList<String>();

        JSONObject jsonNode = JSONObject.fromObject(response);
        JSONArray serversjsonArray = jsonNode.getJSONArray("servers");

        for (int i = 0; i < serversjsonArray.size(); i++) {
            JSONObject serverElement = serversjsonArray.getJSONObject(i);
            servers.add(serverElement.getString("id"));
        }

        return servers;
    }

    /**
     * Obtains the serverPublicIP from the response of a GET call
     * 
     * @param response
     * @return IP of a server
     */
    public String getServerPublicIP(String response) {
        List<String> ips = new ArrayList<String>();

        JSONObject jsonNode = JSONObject.fromObject(response);
        JSONObject serverjsonObject = jsonNode.getJSONObject("server");
        JSONObject addressesjsonObject = serverjsonObject.getJSONObject("addresses");
        JSONArray addressesjsonArray = addressesjsonObject.getJSONArray("private");

        for (int i = 0; i < addressesjsonArray.size(); i++) {
            JSONObject addressElement = addressesjsonArray.getJSONObject(i);
            ips.add(addressElement.getString("addr"));
        }

        return ips.get(1);
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }
    
    public void setOpenStackRegion (OpenStackRegion openStackRegion) {
    	this.openStackRegion = openStackRegion;
    }
}
