package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.model.Environments;
import com.telefonica.euro_iaas.sdc.client.services.EnvironmentService;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentDto;

public class EnvironmentServiceImpl extends AbstractBaseService implements EnvironmentService {

    public EnvironmentServiceImpl(Client client, String baseUrl, String mediaType) {
        setBaseHost(baseUrl);
        setType(mediaType);
        setClient(client);
    }

    @Override
    public Environment insert(EnvironmentDto environmentDto) {
        String url = getBaseHost() + ClientConstants.BASE_ENVIRONMENT_PATH;
        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).type(getType()).entity(environmentDto).post(Environment.class);
    }

    @Override
    public List<Environment> findAll(Integer page, Integer pageSize, String orderBy, String orderType) {
        String url = getBaseHost() + ClientConstants.BASE_ENVIRONMENT_PATH;

        WebResource wr = getClient().resource(url);
        MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();
        searchParams = addParam(searchParams, "page", page);
        searchParams = addParam(searchParams, "pageSize", pageSize);
        searchParams = addParam(searchParams, "orderBy", orderBy);
        searchParams = addParam(searchParams, "orderType", orderType);
        return wr.queryParams(searchParams).accept(getType()).get(Environments.class);
    }

    @Override
    public Environment load(String name) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.ACTION_ENVIRONMENT_INSTANCE_PATH, name);
        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).type(getType()).entity(name).get(Environment.class);
    }

    @Override
    public void delete(String envName) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.ACTION_ENVIRONMENT_INSTANCE_PATH, envName);
        WebResource wr = getClient().resource(url);
        wr.accept(getType()).type(getType()).entity(envName).delete(ClientResponse.class);

    }

    @Override
    public Environment update(EnvironmentDto environmentDto) {
        String url = getBaseHost()
                + MessageFormat.format(ClientConstants.ACTION_ENVIRONMENT_INSTANCE_PATH, environmentDto.getName());
        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).type(getType()).entity(environmentDto).put(Environment.class);
    }

}
