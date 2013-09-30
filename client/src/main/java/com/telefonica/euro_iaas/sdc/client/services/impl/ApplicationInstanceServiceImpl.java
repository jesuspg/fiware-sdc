/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.text.MessageFormat;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.model.ApplicationInstances;
import com.telefonica.euro_iaas.sdc.client.services.ApplicationInstanceService;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationInstanceDto;

/**
 * Default implementation of ApplicationInstanceService
 * 
 * @author Sergio Arroyo
 */
public class ApplicationInstanceServiceImpl extends AbstractInstallableService implements ApplicationInstanceService {

    /**
     * @param client
     * @param baseHost
     * @param type
     */
    public ApplicationInstanceServiceImpl(Client client, String baseHost, String type) {
        setClient(client);
        setBaseHost(baseHost);
        setType(type);
        setUpgradePath(ClientConstants.UPGRADE_APPLICATION_INSTANCE_PATH);
        setUninstallPath(ClientConstants.APPLICATION_INSTANCE_PATH);
        setConfigPath(ClientConstants.APPLICATION_INSTANCE_PATH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task install(String vdc, ApplicationInstanceDto application, String callback) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.INSTALL_APPLICATION_INSTANCE_PATH, vdc);
        WebResource wr = getClient().resource(url);
        Builder builder = wr.accept(getType()).type(getType()).entity(application);
        builder = addCallback(builder, callback);
        return builder.post(Task.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findAll(String hostname, String domain, String ip, String fqn, Integer page,
            Integer pageSize, String orderBy, String orderType, List<Status> status, String vdc, String applicationName) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.BASE_APPLICATION_INSTANCE_PATH, vdc);
        WebResource wr = getClient().resource(url);

        MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();
        searchParams = addParam(searchParams, "hostname", hostname);
        searchParams = addParam(searchParams, "domain", domain);
        searchParams = addParam(searchParams, "ip", ip);
        searchParams = addParam(searchParams, "fqn", fqn);
        searchParams = addParam(searchParams, "page", page);
        searchParams = addParam(searchParams, "pageSize", pageSize);
        searchParams = addParam(searchParams, "orderBy", orderBy);
        searchParams = addParam(searchParams, "orderType", orderType);
        searchParams = addParam(searchParams, "status", status);
        searchParams = addParam(searchParams, "applicationName", applicationName);

        return wr.queryParams(searchParams).accept(getType()).get(ApplicationInstances.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance load(String vdc, String name) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.APPLICATION_INSTANCE_PATH, vdc, name);
        return this.load(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance load(String url) {
        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).get(ApplicationInstance.class);
    }

}
