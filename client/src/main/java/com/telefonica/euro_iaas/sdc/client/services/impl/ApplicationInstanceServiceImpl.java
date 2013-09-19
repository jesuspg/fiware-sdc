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
 * @author Sergio Arroyo
 *
 */
public class ApplicationInstanceServiceImpl extends AbstractInstallableService
    implements ApplicationInstanceService {

    /**
     * @param client
     * @param baseHost
     * @param type
     */
    public ApplicationInstanceServiceImpl(Client client, String baseHost,
            String type) {
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
    public Task install(String vdc, ApplicationInstanceDto application,
            String callback) {
        String url = getBaseHost() + MessageFormat.format(
                ClientConstants.INSTALL_APPLICATION_INSTANCE_PATH, vdc);
        WebResource wr = getClient().resource(url);
        Builder builder = wr.accept(getType()).type(getType()).entity(application);
        builder = addCallback(builder, callback);
        return builder.post(Task.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findAll(Integer page, Integer pageSize,
            String orderBy, String orderType, List<Status> status, String vdc,
            String applicationName) {
        String url = getBaseHost() + MessageFormat.format(
                ClientConstants.APPLICATION_INSTANCE_PATH, vdc);
        WebResource wr = getClient().resource(url);
        MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();
        searchParams.add("page", page.toString());
        searchParams.add("pageSize", pageSize.toString());
        searchParams.add("orderBy", orderBy);
        searchParams.add("orderType", orderType);
        searchParams.add("status", status.toString());
        searchParams.add("applicationName", applicationName);

        return wr.queryParams(searchParams).accept(getType()).get(
                ApplicationInstances.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance load(String vdc, Long id) {
        String url = getBaseHost() + MessageFormat.format(
                ClientConstants.APPLICATION_INSTANCE_PATH, vdc, id);
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