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
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ws.rs.HttpMethod;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallPuppetException;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.installator.Installator;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.AttributeDto;
import com.telefonica.euro_iaas.sdc.model.dto.NodeDto;
import com.telefonica.euro_iaas.sdc.model.dto.PuppetNode;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.HttpsClient;
import com.telefonica.fiware.commons.openstack.auth.exception.OpenStackException;

/**
 * Class to dela with installs through puppetwrapper
 */
public class InstallatorPuppetImpl implements Installator {

    private static final Object IPALL = "IPALL";

    private static Logger log = LoggerFactory.getLogger(InstallatorPuppetImpl.class);

    private HttpClient client;
    private HttpsClient httpsClient;

    private OpenStackRegion openStackRegion;

    public static int MAX_TIME = 360000;

    public void callService(VM vm, String vdc, ProductRelease product, String action, String token)
            throws InstallatorException, NodeExecutionException {
        try {
            generateFilesinPuppetMaster(vm, vdc, product, action, token);
        } catch (InstallatorException e) {
            log.warn("It is not possible to generate the manifests in the puppet master " + e.getMessage());
            throw new InstallatorException(e.getMessage());
        }

        try {
            checkRecipeExecution(vm, product.getProduct().getName(), token);
        } catch (NodeExecutionException e) {
            // even if execution fails want to unassign the recipe
            log.debug(e.getMessage());
            throw new NodeExecutionException(e.getMessage());
        }

        try {
            checkRecipeExecution(vm, product.getProduct().getName(), token);
        } catch (NodeExecutionException e) {
            log.warn("It is not possible execute the module " + product.getProduct().getName() + " in node "
                    + vm.getHostname());
        }

    }

    public void generateFilesinPuppetMaster(VM vm, String vdc, ProductRelease product, String action, String token)
            throws InstallatorException {
        String puppetUrl = null;
        try {
            callPuppetMaster(vm, vdc, product, action, token, null);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new InstallatorException(e.getMessage());
        }

    }

    @Override
    public void callService(ProductInstance productInstance, VM vm, List<Attribute> attributes, String action,
            String token) throws InstallatorException, NodeExecutionException {

        try {
            callPuppetMaster(vm, productInstance.getVdc(), productInstance.getProductRelease(), action, token,
                    attributes);
        } catch (KeyManagementException | NoSuchAlgorithmException e1) {
            throw new InstallatorException(e1.getMessage());
        }
        try {
            checkRecipeExecution(vm, productInstance.getProductRelease().getProduct().getName(), token);
        } catch (NodeExecutionException e) {
            String str = "It is not possible execute the module "
                    + productInstance.getProductRelease().getProduct().getName() + " in node " + vm.getHostname();
            log.warn(str);
            throw e;
        }

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

    private void callPuppetMaster(VM vm, String vdc, ProductRelease product, String action, String token,
            List<Attribute> attributes) throws InstallatorException, KeyManagementException, NoSuchAlgorithmException {
        String puppetUrl = null;

        try {
            puppetUrl = openStackRegion.getPuppetWrapperEndPoint();
        } catch (OpenStackException e) {
            throw new SdcRuntimeException(e);
        }

        List<AttributeDto> newAttributes = null;
        if (attributes != null) {
            newAttributes = formatAttributesForPuppet(attributes);
        }

        // HttpPost postInstall = new HttpPost(puppetUrl + "v2/node/" +
        // vm.getHostname() + "/" + action);
        //
        // postInstall.addHeader("Content-Type", "application/json");
        // postInstall.setHeader("X-Auth-Token", token);
        // postInstall.setHeader("Tenant-Id", vdc);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HttpsClient.HEADER_AUTH, token);
        headers.put(HttpsClient.HEADER_TENNANT, vdc);

        NodeDto nodeDto = new NodeDto(vdc, product.getProduct().getName(), product.getVersion(), newAttributes);
        ObjectMapper mapper = new ObjectMapper();
        StringEntity input;
        String payload = "";

        try {
            input = new StringEntity(mapper.writeValueAsString(nodeDto));
            payload = mapper.writeValueAsString(nodeDto);
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
        // postInstall.setEntity(input);

        // System.out.println("puppetURL: " + puppetUrl + "v2/node/"+
        // vm.getHostname() + "/"
        // + action);

        HttpResponse response;

        log.info("Calling puppetWrapper " + action);
        log.info("connecting to puppetURL: " + "puppetURL: " + puppetUrl + "v2/node/" + vm.getHostname() + "/" + action);
        log.info("payload: " + payload);
        try {
            int statusCode = httpsClient.doHttps(HttpMethod.POST, puppetUrl + "v2/node/" + vm.getHostname() + "/"
                    + action, payload, headers);

            if (statusCode != 200) {
                String msg = format("[puppet " + action + "] response code was: {0}", statusCode);
                log.warn(msg);
                throw new InstallatorException(format(msg));
            }
            log.debug("statusCode:" + statusCode);

            log.info("Calling puppetWrapper generate");
            log.info(puppetUrl + "v2/node/" + vm.getHostname() + "/generate");

            statusCode = httpsClient.doHttps(HttpMethod.GET, puppetUrl + "v2/node/" + vm.getHostname() + "/generate",
                    "", headers);

            if (statusCode != 200) {
                String msg = format("generate files response code was: {0}", statusCode);
                log.warn(msg);
                throw new InstallatorException(format(msg, statusCode));
            }
            log.debug("statusCode:" + statusCode);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InstallatorException(e);
        } catch (IllegalStateException e1) {
            log.error(e1.getMessage());
            throw new InstallatorException(e1);
        }
    }

    public List<AttributeDto> formatAttributesForPuppet(List<Attribute> attributes) {
        List<AttributeDto> newAtts = new ArrayList<AttributeDto>();

        for (Attribute att : attributes) {
            AttributeDto attDto = new AttributeDto();
            attDto.setKey(att.getKey());
            attDto.setDescription(att.getDescription());
            if (att.getType().equals(IPALL)) {
                String newValue = "[";
                StringTokenizer st = new StringTokenizer(att.getValue(), ",");
                while (st.hasMoreElements()) {
                    newValue = newValue + "'" + st.nextElement() + "', ";
                }
                newValue = newValue.substring(0, newValue.length() - 2);
                newValue = newValue + "]";
                attDto.setValue(newValue);
            } else {
                attDto.setValue(att.getValue());
            }
            newAtts.add(attDto);
        }
        return newAtts;
    }

    public void checkRecipeExecution(VM vm, String module, String token) throws NodeExecutionException,
            InstallatorException {

        boolean isExecuted = false;
        int time = 5000;
        int incremental_time = 10000;
        while (!isExecuted) {
            log.info("MAX_TIME: " + MAX_TIME + " and time: " + time);
            try {
                if (time > MAX_TIME) {
                    String errorMesg = "Module " + module + " could not be executed in " + vm.getHostname();
                    log.info(errorMesg);
                    // unassignRecipes(vm, recipe, token);
                    throw new NodeExecutionException(errorMesg);
                }

                sleep(incremental_time);

                PuppetNode node = loadNode(vm.getHostname(), token);
                log.debug("Get time catalog " + node.getCatalogTimestamp());
                if (node.getCatalogTimestamp() != null && !node.getCatalogTimestamp().equals("null")) {
                    isExecuted = true;
                }
                time = time + incremental_time;

            } catch (InterruptedException ie) {
                log.warn(ie.getMessage());
                throw new NodeExecutionException(ie);
            } catch (CanNotCallPuppetException e) {
                log.warn(e.getMessage());
                throw new NodeExecutionException(e);
            }
        }
    }

    public PuppetNode loadNode(String hostname, String token) throws CanNotCallPuppetException, InstallatorException {

        String stringNodes = "";
        log.info("loadNode " + hostname);
        try {
            stringNodes = getNodes(token);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InstallatorException(e);
        }

        PuppetNode node = new PuppetNode();
        PuppetNode node2 = node.getNode(stringNodes, hostname);
        if (node2 == null) {
            log.warn("Node " + hostname + " does not exists");
            throw new InstallatorException("Node " + hostname + " does not exists");
        }
        return node2;

    }

    public void setClient(HttpClient client) {
        this.client = client;
    }

    private String getNodes(String token) throws SdcRuntimeException {
        String puppetServerUrl = null;
        try {
            puppetServerUrl = openStackRegion.getPuppetDBEndPoint();
        } catch (OpenStackException e) {
            throw new SdcRuntimeException(e);
        }

        String path = "/v3/nodes";
        String url = puppetServerUrl + path;
        log.debug(url);

        try {
            HttpGet getGenerate = new HttpGet(url);
            HttpResponse resp = client.execute(getGenerate);
            String response = EntityUtils.toString(resp.getEntity());
            return response;
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new SdcRuntimeException("It is not possible to connect with puppet server in "+url+". Error: " + e
                    .getMessage());
        }
    }

    public void isNodeRegistered(String hostname, String token) throws CanNotCallPuppetException {
        String response = "RESPONSE";
        int time = 10000;
        int check_time = 10000;
        while (!response.contains(hostname)) {

            try {
                log.info("Checking node : " + hostname + " time:" + time);
                if (time > MAX_TIME) {
                    String errorMesg = "Node  " + hostname + " is not registered in puppet master";
                    log.info(errorMesg);
                    throw new CanNotCallPuppetException(errorMesg);
                }
                sleep(check_time);
                response = getNodes(token);
                time = time + check_time;
            } catch (Exception e) {
                log.warn(e.getMessage());
                String errorMesg = "Node  " + hostname + "  is not registered the puppet master " + e.getMessage();
                log.info(errorMesg);
                throw new CanNotCallPuppetException(errorMesg);

            }
        }
        log.debug("Node  " + hostname + " is registered in puppet master");
    }

    @Override
    public void validateInstalatorData(VM vm, String token) throws InvalidInstallProductRequestException {
        if (!vm.canWorkWithInstallatorServer()) {
            String message = "The VM does not include the node hostname required to Install " + "software";
            throw new InvalidInstallProductRequestException(message);
        }
        try {
            this.isNodeRegistered(vm.getHostname(), token);
        } catch (CanNotCallPuppetException e) {
            String errorMesg = "Node  " + vm.getHostname() + " is not registered in the puppet master "
                    + e.getMessage();
            log.info(errorMesg);
            throw new SdcRuntimeException(errorMesg);
        }
    }

    public void setOpenStackRegion(OpenStackRegion openStackRegion) {
        this.openStackRegion = openStackRegion;
    }

    public void setHttpsClient(HttpsClient httpsClient) {
        this.httpsClient = httpsClient;
    }

    /**
     * Sleep temporarily for the specified milliseconds.
     * 
     * @param millis
     * @throws InterruptedException
     */
    public void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }
}
