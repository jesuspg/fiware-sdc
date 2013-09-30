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
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.model.EnvironmentInstances;
import com.telefonica.euro_iaas.sdc.client.services.EnvironmentInstanceService;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentInstanceDto;

public class EnvironmentInstanceServiceImpl extends AbstractBaseService implements EnvironmentInstanceService {

    public EnvironmentInstanceServiceImpl(Client client, String baseUrl, String mediaType) {
        setBaseHost(baseUrl);
        setType(mediaType);
        setClient(client);
    }

    @Override
    public EnvironmentInstance insert(EnvironmentInstanceDto environmentInstanceDto) {
        String url = getBaseHost() + ClientConstants.BASE_ENVIRONMENTINSTANCE_PATH;
        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).type(getType()).entity(environmentInstanceDto).post(EnvironmentInstance.class);
    }

    @Override
    public List<EnvironmentInstance> findAll(Integer page, Integer pageSize, String orderBy, String orderType) {
        String url = getBaseHost() + ClientConstants.BASE_ENVIRONMENTINSTANCE_PATH;

        WebResource wr = getClient().resource(url);
        MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();
        searchParams = addParam(searchParams, "page", page);
        searchParams = addParam(searchParams, "pageSize", pageSize);
        searchParams = addParam(searchParams, "orderBy", orderBy);
        searchParams = addParam(searchParams, "orderType", orderType);
        return wr.queryParams(searchParams).accept(getType()).get(EnvironmentInstances.class);
    }

    @Override
    public EnvironmentInstance load(Long Id) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.ACTION_ENVIRONMENTINSTANCE_INSTANCE_PATH, Id);
        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).type(getType()).entity(Id).get(EnvironmentInstance.class);
    }

    @Override
    public void delete(Long Id) {
        String url = getBaseHost() + MessageFormat.format(ClientConstants.ACTION_ENVIRONMENTINSTANCE_INSTANCE_PATH, Id);
        WebResource wr = getClient().resource(url);
        wr.accept(getType()).type(getType()).entity(Id).delete(ClientResponse.class);

    }

    @Override
    public EnvironmentInstance update(EnvironmentInstanceDto environmentInstanceDto) {
        String url = getBaseHost() + ClientConstants.BASE_ENVIRONMENTINSTANCE_PATH;

        WebResource wr = getClient().resource(url);
        return wr.accept(getType()).type(getType()).entity(environmentInstanceDto).put(EnvironmentInstance.class);
    }

}
