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

package com.telefonica.euro_iaas.sdc.keystoneutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.logging.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.exception.OpenStackException;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.keystoneutils.RegionCache;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;


/**
 * This class implements OpenStackRegion interface.<br>
 * {@inheritDoc}
 */
public class OpenStackRegionImpl implements OpenStackRegion {

    private Client client;

    private static Logger log = Logger.getLogger("OpenStackRegionImpl");

    /**
     * Default constructor. It creates a jersey client.
     */
    public OpenStackRegionImpl() {
        client = Client.create();
    }

    /**
     * the properties configuration.
     */
    private SystemPropertiesProvider systemPropertiesProvider;

    @Override
    public String getEndPointByNameAndRegionName(String type, String regionName, String token)
            throws OpenStackException {

        RegionCache regionCache = new RegionCache();
        String url = regionCache.getUrl(regionName, type);

        String tokenadmin = this.getTokenAdmin();
        if (url != null) {
        	log.info ("Get url for " + type + " in region " +regionName + " : " + url);
            return url;
        } else {
            String responseJSON = callToKeystone(token, tokenadmin);

            String result = parseEndpoint(token, responseJSON, type, regionName);
            if (result == null) {
                throw new OpenStackException("region not found");
            }
            regionCache.putUrl(regionName, type, result);

            return result;
        }
    }

    public String getTokenAdmin() throws OpenStackException {

        ClientResponse response = getEndPointsThroughTokenRequest();
        return parseToken(response.getEntity(String.class));

    }

    @Override
    public String getPuppetWrapperEndPoint( String token) throws OpenStackException  {
    	String defaulRegion = this.getDefaultRegion(token);
    	log.info ("Get url for puppet for default region " + defaulRegion);
    	String url;
        try {
            url = getEndPointByNameAndRegionName("puppetwrapper", defaulRegion, token);
        } catch (OpenStackException e) {
            String msn = "It is not possible to obtain the Puppet Wrapper endpoint";
            log.info(msn);
            throw new OpenStackException (msn);
            		
        }
        return url;
    }
    
    public String getChefServerEndPoint( String token) throws OpenStackException  {
    	
    	String defaulRegion = this.getDefaultRegion(token);
    	log.info ("Get url for chef-server for default region " + defaulRegion);
    	String url;
        try {
            url = getEndPointByNameAndRegionName("chef-server", defaulRegion, token);
        } catch (OpenStackException e) {
            String msn = "It is not possible to obtain the Puppet Master endpoint";
            log.info(msn);
            throw new OpenStackException (msn);
            		
        }
        return url;
    }
    
    public String getWebdavPoint(String token) throws OpenStackException {
    	String defaulRegion = this.getDefaultRegion(token);
    	log.info ("Get url for webdav for default region " + defaulRegion);
    	String url;
        try {
            url = getEndPointByNameAndRegionName("webdav", defaulRegion, token);
        } catch (OpenStackException e) {
            String msn = "It is not possible to obtain the Puppet Master endpoint";
            log.info(msn);
            throw new OpenStackException (msn);
            		
        }
        return url;
    }
    
    public String getDefaultRegion (String token) throws OpenStackException {
    	log.info("Get defautl region for token " + token);
        
        List<String> regions = null;
        try {
            regions = getRegionNames (token);
            log.info("regions " + regions + " " + regions.size());
        } catch (OpenStackException e) {
            String msn = "It is not possible to obtain the SDC endpoint";
            log.info(msn);
            throw new OpenStackException (msn);
        }
        
        return regions.get(0);
    }

    @Override
    public List<String> getRegionNames(String token) throws OpenStackException {
    	log.info("getRegionNames" );
        String tokenAdmin = this.getTokenAdmin();
        log.info("token admin" + tokenAdmin);
        String responseJSON = callToKeystone(token, tokenAdmin);
        return parseRegionName(responseJSON, "nova");

    }

    private String callToKeystone(String token, String tokenAdmin) throws OpenStackException {
        ClientResponse response = getJSONWithEndpoints(token, tokenAdmin);
        return response.getEntity(String.class);

    }

    private ClientResponse getJSONWithEndpoints(String token, String tokenadmin) throws OpenStackException {
        String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_URL) + "tokens/" + token
                + "/endpoints";

        WebResource webResource = client.resource(url);
        log.info("url " + url);
        WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON);
        builder.header("X-Auth-Token", tokenadmin);

        ClientResponse response = builder.get(ClientResponse.class);

        int code = response.getStatus();
        log.info("code " + code);

        if (code != 200) {
            String message = "Failed : HTTP (url:" + url + ") error code : " + code + " body: "
                    + response.getEntity(String.class);

            if (code == 501) {
                log.info(message);
                response = getEndPointsThroughTokenRequest();
            } else {
                log.info(message);
                throw new OpenStackException(message);
            }

        }
        return response;
    }

    private String parseEndpoint(String token, String response, String type, String regionName) throws OpenStackException {

        JSONObject jsonObject = JSONObject.fromObject(response);

        String url = null;
        Map<String, String> urlMap = new HashMap<String, String>();
        if (jsonObject.containsKey("endpoints")) {

            JSONArray endpointsArray = jsonObject.getJSONArray("endpoints");

            boolean notFound = true;
            Iterator it = endpointsArray.iterator();
            JSONObject endpointJson = null;
            while (notFound && it.hasNext()) {

                endpointJson = JSONObject.fromObject(it.next());
                String name1 = endpointJson.get("type").toString();
                String regionName1 = endpointJson.get("region").toString();

                if (type.equals(name1)) {
                    
                    url = endpointJson.get("publicURL").toString();
                    urlMap.put(regionName1, url);
                    if ((regionName != null) && (regionName.equals(regionName1))) {
                        notFound = false;
                    }
                }
            }
            if (!notFound) {
                return url;
            }
            // return default regionName

        } else {
            if (jsonObject.containsKey("access")) {
                JSONArray servicesArray = jsonObject.getJSONObject("access").getJSONArray("serviceCatalog");

                boolean notFound = true;
                Iterator it = servicesArray.iterator();
                JSONObject serviceJSON;
                while (notFound && it.hasNext()) {

                    serviceJSON = JSONObject.fromObject(it.next());
                    String name1 = serviceJSON.get("type").toString();

                    if (type.equals(name1)) {
                        JSONArray endpointsArray = serviceJSON.getJSONArray("endpoints");
                        Iterator it2 = endpointsArray.iterator();

                        while (notFound && it2.hasNext()) {
                            JSONObject endpointJson = JSONObject.fromObject(it2.next());

                            String regionName1 = endpointJson.get("region").toString();
                            url = endpointJson.get("publicURL").toString();
                            if (regionName.equals(regionName1)) {
                                notFound = false;
                            }
                            urlMap.put(regionName1, url);

                        }

                    }
                }
                if (!notFound) {
                    return url;
                }

            }

        }
       ;
        return urlMap.get( this.getDefaultRegion(token));
    }

    /**
     * Parse region name, with compatibility with essex,grizzly.
     * 
     * @param response
     * @param name
     * @return
     */
    private List<String> parseRegionName(String response, String name) {

        List<String> names = new ArrayList<String>(2);

        JSONObject jsonObject = JSONObject.fromObject(response);

        if (jsonObject.containsKey("endpoints")) {

            JSONArray endpointsArray = jsonObject.getJSONArray("endpoints");

            Iterator it = endpointsArray.iterator();
            JSONObject endpointJson;
            while (it.hasNext()) {

                endpointJson = JSONObject.fromObject(it.next());
                String name1 = endpointJson.get("name").toString();
                String regionName1 = endpointJson.get("region").toString();

                if (name.equals(name1)) {
                    if (!names.contains(regionName1)) {
                        names.add(regionName1);
                    }
                }
            }
        } else {
            if (jsonObject.containsKey("access")) {

                JSONArray servicesArray = jsonObject.getJSONObject("access").getJSONArray("serviceCatalog");

                boolean notFound = true;
                Iterator it = servicesArray.iterator();
                JSONObject serviceJSON;
                while (notFound && it.hasNext()) {

                    serviceJSON = JSONObject.fromObject(it.next());
                    String name1 = serviceJSON.get("name").toString();

                    if (name.equals(name1)) {
                        JSONArray endpointsArray = serviceJSON.getJSONArray("endpoints");

                        Iterator it2 = endpointsArray.iterator();
                        while (it2.hasNext()) {
                            JSONObject endpointJson = JSONObject.fromObject(it2.next());

                            String regionName1 = endpointJson.get("region").toString();
                            if (!names.contains(regionName1)) {
                                names.add(regionName1);
                            }

                        }
                        notFound = false;

                    }
                }
            }
        }
        return names;
    }

    /**
     * Parse region name, with compatibility with essex,grizzly.
     * 
     * @param response
     * @param name
     * @return
     */
    private String parseToken(String response) {

        String token = null;

        JSONObject jsonObject = JSONObject.fromObject(response);
        jsonObject = (JSONObject) jsonObject.get("access");

        if (jsonObject.containsKey("token")) {

            JSONObject tokenObject = (JSONObject) jsonObject.get("token");
            token = (String) tokenObject.get("id");

        }
        return token;
    }

    public ClientResponse getEndPointsThroughTokenRequest() throws OpenStackException {
        String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_URL) + "tokens";
        log.info("actionUri: " + url);

        ClientResponse response;

        WebResource wr = client.resource(url);

        String adminUser = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_USER);
        String adminPass = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_PASS);
        String adminTenant = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_TENANT);

        String payload = "{\"auth\": {\"tenantName\": \"" + adminTenant + "\", \""
                + "passwordCredentials\":{\"username\": \"" + adminUser + "\"," + " \"password\": \"" + adminPass
                + "\"}}}";

        WebResource.Builder builder = wr.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
                .entity(payload);

        response = builder.post(ClientResponse.class);

        int httpCode = response.getStatus();
        if (httpCode != 200) {
            String message = "Error calling OpenStack for valid token. " + "Status " + httpCode + ": "
                    + response.getEntity(String.class);
            log.info(message);
            throw new OpenStackException(message);
        }

        return response;

    }

    public SystemPropertiesProvider getSystemPropertiesProvider() {
        return systemPropertiesProvider;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getNovaEndPoint(String regionName, String token) throws OpenStackException {

        String url = getEndPointByNameAndRegionName("compute", regionName, token);
        log.info("getNovaEndPoint " + regionName + " " + token + " " + url);

        Integer index = url.lastIndexOf("/");
        url = url.substring(0, index + 1);

        return url;

    }

}
