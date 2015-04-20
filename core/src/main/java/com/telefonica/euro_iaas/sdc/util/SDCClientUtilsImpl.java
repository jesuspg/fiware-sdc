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

package com.telefonica.euro_iaas.sdc.util;

import static com.telefonica.euro_iaas.sdc.util.Configuration.CHEF_CLIENT_URL_TEMPLATE;

import java.text.MessageFormat;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.NodeCommandDao;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.NodeCommand;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.NodeCommandSearchCriteria;

public class SDCClientUtilsImpl implements SDCClientUtils {

    public static final String VM_PATH = "/rest/vm";
    public static final String CONFIG_PATH = "/rest/config";
    public static final String EXECUTE_PATH = "/rest/installable";

    private static final int MAX_TIME = 90000;

    private Client client;
    private NodeCommandDao nodeCommandDao;
    private OSDao osDao;

    /**
     * {@inheritDoc}
     */

    public VM getVM(String ip, String fqn, String osType) {
        String url = MessageFormat.format(CHEF_CLIENT_URL_TEMPLATE, ip) + VM_PATH;
        WebTarget webResource = client.target(url);
        VM vm = webResource.request(MediaType.APPLICATION_XML).get(VM.class);
        vm.setFqn(fqn);
        vm.setOsType(osType);

        return vm;
    }

    /**
     * {@inheritDoc}
     */

    public void execute(VM vm) throws NodeExecutionException {
        String url = MessageFormat.format(CHEF_CLIENT_URL_TEMPLATE, vm.getExecuteChefConectionUrl()) + EXECUTE_PATH;
        WebTarget webResource = client.target(url);
        Entity<Object> entity = Entity.entity(null, "application/x-ample");
        webResource.request(MediaType.APPLICATION_XML).post(entity);
    }

    /**
     * {@inheritDoc}
     */

    public List<Attribute> configure(VM vm, List<Attribute> configuration) {
        String url = MessageFormat.format(CHEF_CLIENT_URL_TEMPLATE, vm.getExecuteChefConectionUrl()) + CONFIG_PATH;
        WebTarget webResource = client.target(url);
        Response response = webResource.request(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE)
                .accept(MediaType.APPLICATION_XML_TYPE).put(Entity.xml(configuration));
        Attributes outputAttributes = response.readEntity(Attributes.class);
        return outputAttributes;
    }

    /**
     * {@inheritDoc}
     */

    public Attribute configureProperty(VM vm, Attribute attribute) {
        String url = MessageFormat.format(CHEF_CLIENT_URL_TEMPLATE, vm.getExecuteChefConectionUrl()) + CONFIG_PATH;
        WebTarget webResource = client.target(url);
        /*
         * return webResource.accept(MediaType.APPLICATION_XML) .type(MediaType.APPLICATION_XML).entity(configuration)
         * .put(Attributes.class);
         */
        Response response = webResource.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
                .put(Entity.xml(attribute));
        Attribute outputAttribute = response.readEntity(Attribute.class);
        return outputAttribute;

    }

    /**
     * {@inheritDoc}
     */

    public void setNodeCommands(VM vm) throws InvalidInstallProductRequestException {
        OS os;
        try {

            os = osDao.load("76");
        } catch (EntityNotFoundException e) {
            // Logger.warn (" The Operatng Sytem has not been provided");
            throw new InvalidInstallProductRequestException("The Operating Sytem has not been provided");

        }

        List<NodeCommand> nodeCommands = nodeCommandDao.findByCriteria(new NodeCommandSearchCriteria(os));

        for (NodeCommand nodeCommand : nodeCommands) {
            Attribute attribute = new Attribute(nodeCommand.getKey(), nodeCommand.getValue());
            configureProperty(vm, attribute);
        }

    }

    public void checkIfSdcNodeIsReady(String ip) throws NodeExecutionException {
        String url = MessageFormat.format(CHEF_CLIENT_URL_TEMPLATE, ip) + VM_PATH;

        WebTarget webResource = client.target(url);
        String response = "response";
        int time = 10000;
        while (!(response.contains("ip"))) {
            try {
                Thread.sleep(time);
                System.out.println("Checking node : " + ip + " time:" + time);
                if (time > MAX_TIME) {
                    String errorMesg = "SdcNode " + ip + " is not ready";
                    throw new NodeExecutionException(errorMesg);
                }
                try {
                    response = webResource.request(MediaType.APPLICATION_XML).get(String.class);
                } catch (Exception e) {
                    System.out.println("The sdc client is not ready yet. time: " + time);
                }
                time += time;
            } catch (InterruptedException e) {
                String errorMsg = e.getMessage();
                throw new NodeExecutionException(errorMsg, e);
            }

        }

    }

    /**
     * @param nodeCommandDao
     *            the nodeCommandDao to set
     */
    public void setNodeCommandDao(NodeCommandDao nodeCommandDao) {
        this.nodeCommandDao = nodeCommandDao;
    }

    /**
     * @param osDao
     *            the osDao to set
     */
    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
    }

    /**
     * @param client
     *            the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

}
