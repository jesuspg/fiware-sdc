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

package com.telefonica.euro_iaas.sdc.installator.impl;

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;





import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.OpenStackException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.installator.Installator;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.model.dto.NodeDto;
import com.telefonica.euro_iaas.sdc.model.dto.PuppetNode;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.Configuration;

public class InstallatorPuppetImpl implements Installator {
  
    private static Logger log = LoggerFactory.getLogger(InstallatorPuppetImpl.class);

    private HttpClient client;

    private OpenStackRegion openStackRegion;
    
    private String NODE_NOT_FOUND_PATTERN ="404";
    private String NODES_PATH ="/nodes";
    public static int MAX_TIME = 90000;
 

    public void callService(VM vm, String vdc, ProductRelease product, String action, String token)
            throws InstallatorException {
    	try {
    	    generateFilesinPuppetMaster (vm, vdc, product, action, token);
    	} catch (InstallatorException e) {
    		log.warn ("It is not possible to generate the manifests in the puppet master " + e.getMessage());
    	}
    	
    
        try {
			isRecipeExecuted(vm,product.getProduct().getName(),token);
		} catch (NodeExecutionException e) {
			log.warn ("It is not possible to generate the manifests in the puppet master " + e.getMessage());
		}

    }
    
    public void generateFilesinPuppetMaster (VM vm, String vdc, ProductRelease product, String action, String token) 
    throws InstallatorException {
    	String puppetUrl = null;
        try {
            puppetUrl = openStackRegion.getPuppetWrapperEndPoint(token);
        } catch (OpenStackException e) {
            throw new SdcRuntimeException(e);
        }
        HttpPost postInstall = new HttpPost(puppetUrl + "v2/node/"+ vm.getHostname() + "/"
                + action);

        postInstall.addHeader("Content-Type", "application/json");
        
        NodeDto nodeDto = new NodeDto(vdc,product.getProduct().getName(),product.getVersion());
        ObjectMapper mapper = new ObjectMapper();
        StringEntity input;
        
        try {
            input = new StringEntity(mapper.writeValueAsString(nodeDto));
        } catch (JsonGenerationException e2) {
            throw new SdcRuntimeException(e2);
        } catch (JsonMappingException e2) {
            throw new SdcRuntimeException(e2);
        } catch (UnsupportedEncodingException e2) {
            throw new SdcRuntimeException(e2);
        } catch (IOException e2) {
            throw new SdcRuntimeException(e2);
        }
        
        input.setContentType("application/json");
        postInstall.setEntity(input);

//        System.out.println("puppetURL: " + puppetUrl + "v2/node/"+ vm.getHostname() + "/"
//                + action);

        HttpResponse response;

        log.info("Calling puppetWrapper install");
        log.debug("connecting to puppetURL: "+"puppetURL: " + puppetUrl + "v2/node/"+ vm.getHostname() + "/"
                + action);
        try {
            response = client.execute(postInstall);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);

            if (statusCode != 200) {
                String msg=format("[puppet install] response code was: {0}", statusCode);
                log.warn(msg);
                throw new InstallatorException(format(msg));
            }
            log.debug("statusCode:"+ statusCode);
            
            log.info("Calling puppetWrapper generate");
            log.debug(puppetUrl + "v2/node/"+vm.getHostname()+"/generate");

            // generate files in puppet master
            HttpGet getGenerate = new HttpGet(puppetUrl + "v2/node/"+vm.getHostname()+"/generate");

            getGenerate.addHeader("Content-Type", "application/json");

            response = client.execute(getGenerate);
            statusCode = response.getStatusLine().getStatusCode();
            entity = response.getEntity();
            EntityUtils.consume(entity);

            if (statusCode != 200) {
                String msg=format("generate files response code was: {0}", statusCode);
                log.warn(msg);
                throw new InstallatorException(format(msg,
                        statusCode));
            }
            log.debug("statusCode:"+ statusCode);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InstallatorException(e);
        } catch (IllegalStateException e1) {
            log.error(e1.getMessage());
            throw new InstallatorException(e1);
        }
    }
    
    public void isRecipeExecuted(VM vm, String recipe, String token) throws NodeExecutionException, InstallatorException {
    	
    	boolean isExecuted = false;
        int time = 5000;
        int incremental_time=10000;
        Date fechaAhora = new Date();
        while (!isExecuted) {
        	log.info("MAX_TIME: " + MAX_TIME + " and time: " + time);
            try {
                if (time > MAX_TIME) {
                    String errorMesg = "Recipe " + recipe + " coub not be executed in " + vm.getChefClientName();
                    log.info(errorMesg);
                   // unassignRecipes(vm, recipe, token);
                    throw new NodeExecutionException(errorMesg);
                }

                Thread.sleep(time);
                
                PuppetNode node = loadNode (vm.getHostname(), token);       		
        		if (node.getCatalogTimestamp ()!=null) {
        			isExecuted = true;
        		}
                time = time +incremental_time;
            
            }  catch (InterruptedException ie) {
            	log.warn (ie.getMessage());
                throw new NodeExecutionException(ie);
            } catch (CanNotCallChefException e) {
            	log.warn (e.getMessage());
            	throw new NodeExecutionException(e);
			} 
        }
    }
    
    public PuppetNode loadNode(String hostname, String token) throws CanNotCallChefException, InstallatorException {

    	String stringNodes = "";
    	log.info("loadNode " + hostname);
    	try {
    	  stringNodes = getNodes (token);
    	} catch (Exception e) {
    		log.error(e.getMessage());
            throw new InstallatorException(e);
    	}

        PuppetNode node = new PuppetNode();
        PuppetNode node2 = node.getNode(stringNodes, hostname);
        if (node2==null) {
        	log.warn ("Node " + hostname +" does not exists");
        	throw new InstallatorException("Node " + hostname +" does not exists");
        }
        return node2;
       
    }

    public void setClient(HttpClient client) {
        this.client = client;
    }

    @Override
    public void callService(ProductInstance productInstance, VM vm, List<Attribute> attributes, String action,
            String token) throws InstallatorException, NodeExecutionException {
        // TODO Auto-generated method stub

    }

    @Override
    public void upgrade(ProductInstance productInstance, VM vm, String token) throws InstallatorException {
        // TODO Auto-generated method stub

    }

    @Override
    public void callService(ProductInstance productInstance, String action, String token) throws InstallatorException,
            NodeExecutionException {
        // TODO Auto-generated method stub

    }
    
    private String getNodes (String token) throws SdcRuntimeException{
    	String puppetServerUrl = null;
    	try {
    		puppetServerUrl = openStackRegion.getPuppetDBEndPoint(token);
		} catch (OpenStackException e) {
			 throw new SdcRuntimeException(e);
		}
    	
        String path = "/v3/nodes";
        String url = puppetServerUrl + path;
        log.debug(url);

        try {      
            HttpGet getGenerate = new HttpGet(url);
            HttpResponse resp= client.execute(getGenerate);
            String response = EntityUtils.toString(resp.getEntity());       
            log.info(response);
            return response;
        } catch (Exception e) {
          	log.warn(e.getMessage());
          	throw new SdcRuntimeException ("It is not possible to connect with puppet server Url" + e.getMessage());
        } 
    }
    public void isNodeRegistered (String hostname, String token) throws CanNotCallChefException {
        String response = "RESPONSE";
        int time = 10000;
        while (!response.contains(hostname)) {
                      
            try {
                log.info("Checking node : " + hostname + " time:" + time);
                if (time > MAX_TIME) {
                    String errorMesg = "Node  " + hostname + " is not registered in ChefServer";
                    log.info(errorMesg);
                    throw new CanNotCallChefException(errorMesg);
                }
                Thread.sleep(time);
                response = getNodes (token);      
                time += time;
            } catch (Exception e) {
            	log.warn(e.getMessage());
            	String errorMesg = "Node  " + hostname + " is not registered in ChefServer";
                log.info(errorMesg);
                throw new CanNotCallChefException(errorMesg);
            	
            } 
        }
    }

    @Override
    public void validateInstalatorData(VM vm, String token) throws InvalidInstallProductRequestException {
        if (!vm.canWorkWithInstallatorServer()) {
            String message = "The VM does not include the node hostname required to Install " + "software";
            throw new InvalidInstallProductRequestException(message);
        }
		try {
			this.isNodeRegistered(vm.getHostname(), token);
		} catch (CanNotCallChefException e) {
			String errorMesg = "Node  " + vm.getHostname() + " is not registered in ChefServer";
            log.info(errorMesg);
            throw new InvalidInstallProductRequestException(errorMesg);
		}
    }

    public void setOpenStackRegion(OpenStackRegion openStackRegion) {
        this.openStackRegion = openStackRegion;
    }

}
