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
import java.util.StringTokenizer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.telefonica.euro_iaas.sdc.exception.InvalidNameException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductException;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.sdc.rest.auth.OpenStackAuthenticationProvider;
import com.telefonica.euro_iaas.sdc.rest.exception.UnauthorizedOperationException;
import com.telefonica.euro_iaas.sdc.util.Configuration;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.openstack.auth.exception.OpenStackException;

/**
 * @author jesus.movilla
 */
public class ProductInstanceResourceValidatorImpl implements ProductInstanceResourceValidator {

    private SystemPropertiesProvider systemPropertiesProvider;
    private OpenStackRegion openStackRegion;
    private GeneralResourceValidator generalValidator;
    private ProductResourceValidator productResourceValidator;

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.rest.validation.ProductInstanceResourceValidator#validateInsert()
     */
    public void validateInsert(ProductInstanceDto product) throws UnauthorizedOperationException, OpenStackException,
            InvalidProductException, EntityNotFoundException {
        validateFields(product);
        productResourceValidator.validateLoad(product.getProduct());
        // PaasManagerUser user = getCredentials();
        // OpenStackUser user = getCredentials();

        /*
         * List<String> ips = new ArrayList<String>(); List<String> serversIds = findAllServers(user); for (int i = 0; i
         * < serversIds.size(); i++) { String ip = findServerIP(user, serversIds.get(i)); ips.add(ip); } if
         * (!(ips.contains(product.getVm().getIp()))) { String message = " The Server with ip " +
         * product.getVm().getIp() + " does not belong to user with token " + user.getToken(); throw new
         * UnauthorizedOperationException(message); }
         */

    }

    private void validateFields(ProductInstanceDto product) throws InvalidProductException {

        if (product.getProduct() == null) {
            throw new InvalidProductException("The product is null");
        }

        if (product.getVm() == null) {
            throw new InvalidProductException("The product is null");
        }

        try {
            generalValidator.validateVesion(product.getProduct().getVersion());
        } catch (InvalidNameException e) {
            throw new InvalidProductException("The product version is not valid " + e.getMessage());
        }

        try {
            generalValidator.validateVesion(product.getProduct().getName());
        } catch (InvalidNameException e) {
            throw new InvalidProductException("The product version is not valid " + e.getMessage());
        }

        try {
            generalValidator.validateName(product.getVm().getHostname());
        } catch (InvalidNameException e) {
            throw new InvalidProductException("The hostnmae is null " + e.getMessage());
        }

        try {
            generalValidator.validateName(product.getVm().getFqn());
        } catch (InvalidNameException e) {
            throw new InvalidProductException("The fqn is null " + e.getMessage());
        }

        if (product.getAttributes() != null) {
            validateAttributesType(product.getAttributes());
        }
    }

    private void validateAttributesType(List<Attribute> attributes) throws InvalidProductException {
        String msg = "Attribute type is incorrect.";
        for (Attribute att : attributes) {
            if (att.getType() == null) {
                att.setType("Plain");
            }

            String availableTypes = systemPropertiesProvider
                    .getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES);

            StringTokenizer st2 = new StringTokenizer(availableTypes, "|");
            boolean error = true;
            while (st2.hasMoreElements()) {
                if (att.getType().equals(st2.nextElement())) {
                    error = false;
                    break;
                }
            }
            if (error) {
                throw new InvalidProductException(msg);
            }
        }
    }

    public String getToken() {
        PaasManagerUser user = OpenStackAuthenticationProvider.getCredentials();
        if (user == null) {
            return "";
        } else {
            return user.getToken();
        }

    }

    /**
     * Obtain the serversIds associated to a certain user
     * 
     * @param user
     * @return
     * @throws OpenStackException
     */
    private List<String> findAllServers(PaasManagerUser user) throws OpenStackException {
        // http://130.206.80.63:8774/v2/ebe6d9ec7b024361b7a3882c65a57dda/servers

        String url = this.openStackRegion.getNovaEndPoint(openStackRegion.getDefaultRegion()) + user.getTenantId()
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
    private String findServerIP(PaasManagerUser user, String serverId) throws OpenStackException {
        String url = this.openStackRegion.getNovaEndPoint(openStackRegion.getDefaultRegion())
                + Configuration.VERSION_PROPERTY + user.getTenantId() + "/servers/" + serverId;
        String output = getResourceOpenStack(url, user.getToken());

        return getServerPublicIP(output);
    }

    public String getResourceOpenStack(String url, String token) {
        Client client = ClientBuilder.newClient();

        WebTarget wr = client.target(url);
        Invocation.Builder builder = wr.request("application/json");
        // headers
        builder = builder.header("X-Auth-Token", token);

        Response response = builder.get();
        return response.readEntity(String.class);
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

    /**
     * @param openStackRegion
     */
    public void setOpenStackRegion(OpenStackRegion openStackRegion) {
        this.openStackRegion = openStackRegion;
    }

    /**
     * @param generalValidator
     *            the generalValidator to set
     */
    public void setGeneralValidator(GeneralResourceValidator generalValidator) {
        this.generalValidator = generalValidator;
    }

    public void setProductResourceValidator(ProductResourceValidator productResourceValidator) {
        this.productResourceValidator = productResourceValidator;
    }

}
