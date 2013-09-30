/**
 * 
 */
package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.text.MessageFormat;

import javax.persistence.EntityNotFoundException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ChefClientService;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

/**
 * @author jesus.movilla
 */
public class ChefClientServiceImpl extends AbstractBaseService implements ChefClientService {

    /**
     * @param client
     * @param baseHost
     * @param type
     */
    public ChefClientServiceImpl(Client client, String baseHost, String type) {
        setClient(client);
        setBaseHost(baseHost);
        setType(type);
    }

    public Task delete(String vdc, String chefClientName) throws InvalidExecutionException {
        try {

            String url = getBaseHost() + MessageFormat.format(ClientConstants.CHEFCLIENT_PATH, vdc, chefClientName);
            WebResource wr = getClient().resource(url);
            Builder builder = wr.accept(getType()).type(getType());
            // builder = addCallback(builder, callback);
            return builder.delete(Task.class);

        } catch (UniformInterfaceException e) {
            String errorMsg = " Error deleting a ChefClient  " + chefClientName + " in vdc " + vdc
                    + " from the Chef Server. Description: " + e.getMessage();
            throw new InvalidExecutionException(errorMsg, e);
        }
    }

    public ChefClient load(String vdc, String chefClientName) throws ResourceNotFoundException {

        String url = null;
        try {
            url = getBaseHost() + MessageFormat.format(ClientConstants.CHEFCLIENT_PATH, vdc, chefClientName);

            /*
             * WebResource wr = getClient().resource(url); Builder builder = wr.accept(getType()).type(getType());
             * //builder = addCallback(builder, callback); return builder.get(c);
             */

            WebResource wr = getClient().resource(url);
            return wr.accept(getType()).get(ChefClient.class);

        } catch (EntityNotFoundException enfe) {
            String errorMsg = " Error loading a ChefClient  " + chefClientName + " in vdc " + vdc
                    + " from the Chef Server. Description: " + enfe.getMessage();
            throw new ResourceNotFoundException(ChefClient.class, url);
        } catch (Exception e) {
            String errorMsg = " Error loading a ChefClient  " + chefClientName + " in vdc " + vdc
                    + " from the Chef Server. Description: " + e.getMessage();
            throw new ResourceNotFoundException(ChefClient.class, url);
        }
    }

    public ChefClient loadByHostname(String vdc, String hostname) throws ResourceNotFoundException {

        String url = null;
        try {
            url = getBaseHost() + MessageFormat.format(ClientConstants.CHEFCLIENTHOSTNAME_PATH, vdc, hostname);

            /*
             * WebResource wr = getClient().resource(url); Builder builder = wr.accept(getType()).type(getType());
             * //builder = addCallback(builder, callback); return builder.get(c);
             */

            WebResource wr = getClient().resource(url);
            return wr.accept(getType()).get(ChefClient.class);

        } catch (EntityNotFoundException enfe) {
            String errorMsg = " Error loading a ChefClient  " + hostname + " in vdc " + vdc
                    + " from the Chef Server. Description: " + enfe.getMessage();
            throw new ResourceNotFoundException(ChefClient.class, url);
        } catch (Exception e) {
            String errorMsg = " Error loading a ChefClient  " + hostname + " in vdc " + vdc
                    + " from the Chef Server. Description: " + e.getMessage();
            throw new ResourceNotFoundException(ChefClient.class, url);
        }
    }

}
