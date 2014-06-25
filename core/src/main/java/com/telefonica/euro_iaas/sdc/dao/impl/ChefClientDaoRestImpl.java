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
package com.telefonica.euro_iaas.sdc.dao.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_ID;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_PASS;
import static com.telefonica.euro_iaas.sdc.util.Configuration.CHEF_SERVER_CLIENTS_PATH;


import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ChefClientConfig;
import com.telefonica.euro_iaas.sdc.dao.ChefClientDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.OpenStackException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.util.MixlibAuthenticationDigester;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class ChefClientDaoRestImpl implements ChefClientDao {

    SystemPropertiesProvider propertiesProvider;
    MixlibAuthenticationDigester digester;
    ChefClientConfig clientConfig;
    private static Logger log = LoggerFactory.getLogger(ChefClientDaoRestImpl.class);
    private OpenStackRegion openStackRegion;

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.dao.ChefClientDao#findAllChefClient()
     */
    public ChefClient chefClientfindByHostname(String hostname, String token) throws EntityNotFoundException, CanNotCallChefException {

        try {
            String path = "/clients";
            String chefServer = null;
			try {
				chefServer = openStackRegion.getChefServerEndPoint(token);
			} catch (OpenStackException e) {
				 throw new SdcRuntimeException(e);
			}
            log.info(chefServer+ path);

            Map<String, String> header = getHeaders("GET", path, "");
            WebResource webResource = clientConfig.getClient().resource(chefServer + path);
            Builder wr = webResource.accept(MediaType.APPLICATION_JSON);
            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            String stringChefClients;
            stringChefClients = IOUtils.toString(wr.get(InputStream.class));

            if (stringChefClients == null)
                throw new EntityNotFoundException(ChefClient.class, null, "The ChefServer is empty of ChefClients");

            // JSONObject jsonChefClient = JSONObject.fromObject(stringChefClients);
            ChefClient chefClient = new ChefClient();
            String clientName = chefClient.getChefClientName(stringChefClients, hostname);

            return getChefClient(clientName, token);
        } catch (UniformInterfaceException e) {
            throw new CanNotCallChefException(e);
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.dao.ChefClientDao#deleteChefClient(java.lang.String)
     */
    public void deleteChefClient(String chefClientName, String token) throws CanNotCallChefException {
    	String chefServerUrl = null;
		try {
			chefServerUrl = openStackRegion.getChefServerEndPoint(token);
		} catch (OpenStackException e) {
			 throw new SdcRuntimeException(e);
		}
    	if (!chefClientName.startsWith("/")) {
    		chefClientName = "/"+chefClientName;
    	}
        try {
        	String path = MessageFormat.format(CHEF_SERVER_CLIENTS_PATH, chefClientName);
            // String payload = node.toJson();
            Map<String, String> header = getHeaders("DELETE", path, "");

        	
        	log.info(chefServerUrl + path);
            WebResource webResource = clientConfig.getClient().resource(chefServerUrl + path);

            Builder wr = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            wr.delete(InputStream.class);

        } catch (UniformInterfaceException e) {
            throw new CanNotCallChefException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.dao.ChefClientDao#getChefClient(java.lang.String)
     */
    public ChefClient getChefClient(String chefClientName, String token) throws CanNotCallChefException, EntityNotFoundException {
    	String chefServerUrl = null;
		try {
			chefServerUrl = openStackRegion.getChefServerEndPoint(token);
		} catch (OpenStackException e) {
			 throw new SdcRuntimeException(e);
		}
    	if (!chefClientName.startsWith("/")) {
    		chefClientName = "/"+chefClientName;
    	}
        
        try {

        	String path = MessageFormat.format(CHEF_SERVER_CLIENTS_PATH, chefClientName);
        	log.info(chefServerUrl + path);


            Map<String, String> header = getHeaders("GET", path, "");
            WebResource webResource = clientConfig.getClient().resource(chefServerUrl + path);
            Builder wr = webResource.accept(MediaType.APPLICATION_JSON);
            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            String stringChefClient;
            stringChefClient = IOUtils.toString(wr.get(InputStream.class));

            if (stringChefClient == null)
                throw new EntityNotFoundException(ChefClient.class, null, "ChefClient " + chefClientName
                        + " is not in the ChefServer");

            JSONObject jsonChefClient = JSONObject.fromObject(stringChefClient);
            ChefClient chefClient = new ChefClient();
            chefClient.fromJson(jsonChefClient);
            return chefClient;
        } catch (UniformInterfaceException e) {
            throw new CanNotCallChefException(e);
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
        }
    }

    private Map<String, String> getHeaders(String method, String path, String payload) {

        return digester.digest(method, path, payload, new Date(), propertiesProvider.getProperty(CHEF_CLIENT_ID),
                propertiesProvider.getProperty(CHEF_CLIENT_PASS));
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param digester
     *            the digester to set
     */
    public void setDigester(MixlibAuthenticationDigester digester) {
        this.digester = digester;
    }

    /**
     * @param client
     *            the client to set
     */
    public void setClientConfig(ChefClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }
    public void setOpenStackRegion (OpenStackRegion openStackRegion) {
    	this.openStackRegion = openStackRegion;
    }
    
   

}
