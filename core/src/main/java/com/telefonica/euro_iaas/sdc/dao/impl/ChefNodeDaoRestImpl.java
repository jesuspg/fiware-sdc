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

package com.telefonica.euro_iaas.sdc.dao.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_ID;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_PASS;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_SERVER_NODES_PATH;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_SERVER_URL;

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
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.util.MixlibAuthenticationDigester;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
/**
 * Default implementation of ChefNodeManager.
 * 
 * @author Sergio Arroyo
 */
public class ChefNodeDaoRestImpl implements ChefNodeDao {

    SystemPropertiesProvider propertiesProvider;
    MixlibAuthenticationDigester digester;
    ChefClientConfig clientConfig;

    
    private static Logger LOGGER = LoggerFactory.getLogger(ChefNodeDaoRestImpl.class);
    
    private String NODE_NOT_FOUND_PATTERN ="404";
    private String NODES_PATH ="/nodes";
    private static final int MAX_TIME = 90000;
    
    public ChefNode loadNodeFromHostname(String hostname) throws EntityNotFoundException, 
        CanNotCallChefException {
    	LOGGER.info ("Loading nodes" + hostname );
        try {
            String path = NODES_PATH;

            Map<String, String> header = getHeaders("GET", path, "");
            LOGGER.info (propertiesProvider.getProperty(CHEF_SERVER_URL) + path);
            WebResource webResource = clientConfig.getClient().resource(propertiesProvider.getProperty(CHEF_SERVER_URL) + path);
            Builder wr = webResource.accept(MediaType.APPLICATION_JSON);
            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }
            String stringNodes;
            stringNodes = IOUtils.toString(wr.get(InputStream.class));
            
            if (stringNodes == null) {
                throw new EntityNotFoundException(ChefNode.class, null, 
                    "The ChefServer is empty of ChefNodes");
            }           
            ChefNode node = new ChefNode();
            LOGGER.info (stringNodes);
            String nodeName = node.getChefNodeName(stringNodes, hostname);
            return loadNode(nodeName);
         } catch (UniformInterfaceException e) {
             throw new CanNotCallChefException(e);
         } catch (IOException e) {
             throw new SdcRuntimeException(e);
         }
     }
    /**
     * {@inheritDoc}
     */
  
    public ChefNode loadNode(String chefNodename) throws CanNotCallChefException {
        try {
        	
        	if (!chefNodename.startsWith("/")) {
        		chefNodename = "/"+chefNodename;
        	}
        	
            String  path = MessageFormat.format(propertiesProvider.getProperty(CHEF_SERVER_NODES_PATH), chefNodename);
            LOGGER.info (propertiesProvider.getProperty(CHEF_SERVER_URL) + path);
            LOGGER.info("getting hedares");
            Map<String, String> header = getHeaders("GET", path, "");
            LOGGER.info("obtainaing webresource");
            WebResource webResource = clientConfig.getClient().resource(propertiesProvider.getProperty(CHEF_SERVER_URL) + path);
            Builder wr = webResource.accept(MediaType.APPLICATION_JSON);
            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }
            LOGGER.info("getting input string");
            InputStream inputStream = wr.get(InputStream.class);
            String stringNode;
            stringNode = IOUtils.toString(inputStream);
            JSONObject jsonNode = JSONObject.fromObject(stringNode);
            LOGGER.info (stringNode);
    
            ChefNode node = new ChefNode();
            node.fromJson(jsonNode);
            return node;
        } catch (UniformInterfaceException e) {
        	LOGGER.warn(e.getMessage());
            throw new CanNotCallChefException(e);
        } catch (IOException e) {
        	LOGGER.warn(e.getMessage());
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
  
    public ChefNode updateNode(ChefNode node) throws CanNotCallChefException {
    	LOGGER.info("Update node " + node.getName() );
        try {
            String path = MessageFormat.format(propertiesProvider.getProperty(CHEF_SERVER_NODES_PATH), "/"+node.getName());
            LOGGER.info (propertiesProvider.getProperty(CHEF_SERVER_URL) + path);
            String payload = node.toJson();
            Map<String, String> header = getHeaders("PUT", path, payload);

            WebResource webResource = clientConfig.getClient().resource(propertiesProvider.getProperty(CHEF_SERVER_URL) + path);

            Builder wr = webResource.accept(MediaType.APPLICATION_JSON);
            wr = wr.type(MediaType.APPLICATION_JSON);
            wr = wr.entity(payload);

            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }
            
            InputStream inputStream = wr.put(InputStream.class);
            String stringNode;
            stringNode = IOUtils.toString(inputStream);
            JSONObject jsonNode = JSONObject.fromObject(stringNode);

            ChefNode updatedNode = new ChefNode();
            updatedNode.fromJson(jsonNode);
            return updatedNode;
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
        } catch (UniformInterfaceException e) {
            throw new CanNotCallChefException(e);
        }
    }

    public void deleteNode(ChefNode node) throws CanNotCallChefException {
    	
        try {
            String path = MessageFormat.format(propertiesProvider.getProperty(CHEF_SERVER_NODES_PATH), "/"+node.getName());
            Map<String, String> header = getHeaders("DELETE", path, "");

            WebResource webResource = clientConfig.getClient().resource(propertiesProvider.getProperty(CHEF_SERVER_URL) + path);

            Builder wr = webResource.accept(MediaType.APPLICATION_JSON);
            wr = wr.type(MediaType.APPLICATION_JSON);

            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            wr.delete(InputStream.class);
        } catch (UniformInterfaceException e) {
            throw new CanNotCallChefException(e);
        }
    }
    
    /**
     * Checks if ChefNode is already registered in ChefServer.
     */
    public void isNodeRegistered (String hostname) throws CanNotCallChefException {
    	
        String path = "/nodes";

        String response = "RESPONSE";
        int time = 10000;
        while (!response.contains(hostname)) {
                      
            try {
                LOGGER.info("Checking node : " + hostname + " time:" + time);
                if (time > MAX_TIME) {
                    String errorMesg = "Node  " + hostname + " is not registered in ChefServer";
                    LOGGER.info(errorMesg);
                    throw new CanNotCallChefException(errorMesg);
                }
                Thread.sleep(time);
                
                Map<String, String> header = getHeaders("GET", path, "");
                LOGGER.info(propertiesProvider.getProperty(CHEF_SERVER_URL) + path);

                LOGGER.info("web resource");
                WebResource webResource = clientConfig.getClient().resource(propertiesProvider.getProperty(CHEF_SERVER_URL) + path);
                Builder wr = webResource.accept(MediaType.APPLICATION_JSON);
                for (String key : header.keySet()) {
                    System.out.println(key + ":" + header.get(key));
                    wr = wr.header(key, header.get(key));
                }
                
                LOGGER.info("geting");
                response = IOUtils.toString(wr.get(InputStream.class));
                LOGGER.info(response);
                time += time;
            } catch (UniformInterfaceException e) {
            	LOGGER.warn(e.getMessage());
                throw new CanNotCallChefException(e);
            } catch (IOException e) {
            	LOGGER.warn(e.getMessage());
                throw new CanNotCallChefException(e);
                
            } catch (InterruptedException e) {
                String errorMsg = e.getMessage();
                LOGGER.warn(e.getMessage());
                throw new CanNotCallChefException(errorMsg, e);
            }
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

}
