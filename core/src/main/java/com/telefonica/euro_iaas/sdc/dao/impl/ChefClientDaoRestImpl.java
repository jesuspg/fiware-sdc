/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

/**
 * 
 */
package com.telefonica.euro_iaas.sdc.dao.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_ID;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_PASS;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_SERVER_CLIENTS_PATH;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_SERVER_URL;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ChefClientConfig;
import com.telefonica.euro_iaas.sdc.dao.ChefClientDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.util.MixlibAuthenticationDigester;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public class ChefClientDaoRestImpl implements ChefClientDao {

    SystemPropertiesProvider propertiesProvider;
    MixlibAuthenticationDigester digester;
    ChefClientConfig clientConfig;
    private static Logger LOGGER = Logger.getLogger("ChefClientDaoRestImpl");

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.dao.ChefClientDao#findAllChefClient()
     */
    public ChefClient chefClientfindByHostname(String hostname) throws EntityNotFoundException, CanNotCallChefException {

        try {
            String path = "/clients";
            LOGGER.info(propertiesProvider.getProperty(CHEF_SERVER_URL) + path);

            Map<String, String> header = getHeaders("GET", path, "");
            WebResource webResource = clientConfig.getClient().resource(propertiesProvider.getProperty(CHEF_SERVER_URL) + path);
            Builder wr = webResource.accept(MediaType.APPLICATION_JSON);
            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            String stringChefClients;
            stringChefClients = IOUtils.toString(wr.get(InputStream.class));

            if (stringChefClients == null)
                throw new EntityNotFoundException(ChefClient.class, null, "The ChefServer is empty of ChefClients");

            // JSONObject jsonChefClient = JSONObject.fromObject(stringChefClients);
            ChefClient chefClient = new ChefClient();
            String clientName = chefClient.getChefClientName(stringChefClients, hostname);

            return getChefClient(clientName);
        } catch (UniformInterfaceException e) {
            throw new CanNotCallChefException(e);
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.dao.ChefClientDao#deleteChefClient(java.lang.String)
     */
    public void deleteChefClient(String chefClientName) throws CanNotCallChefException {
        try {
        	String path = MessageFormat.format(propertiesProvider.getProperty(CHEF_SERVER_CLIENTS_PATH), chefClientName);
            // String payload = node.toJson();
            Map<String, String> header = getHeaders("DELETE", path, "");
            LOGGER.info("getChefClient " + path);
        	String chefServerUrl = propertiesProvider.getProperty(CHEF_SERVER_URL);
        	
        	LOGGER.info(chefServerUrl + path);
            WebResource webResource = clientConfig.getClient().resource(chefServerUrl + path);

            Builder wr = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            wr.delete(InputStream.class);

        } catch (UniformInterfaceException e) {
            throw new CanNotCallChefException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.dao.ChefClientDao#getChefClient(java.lang.String)
     */
    public ChefClient getChefClient(String chefClientName) throws CanNotCallChefException, EntityNotFoundException {
        
        try {
        	String path = MessageFormat.format(propertiesProvider.getProperty(CHEF_SERVER_CLIENTS_PATH), chefClientName);
        	LOGGER.info("getChefClient " + path);
        	String chefServerUrl = propertiesProvider.getProperty(CHEF_SERVER_URL);

        	LOGGER.info(chefServerUrl + path);

            Map<String, String> header = getHeaders("GET", path, "");
            WebResource webResource = clientConfig.getClient().resource(chefServerUrl + path);
            Builder wr = webResource.accept(MediaType.APPLICATION_JSON);
            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            String stringChefClient;
            stringChefClient = IOUtils.toString(wr.get(InputStream.class));

            if (stringChefClient == null)
                throw new EntityNotFoundException(ChefClient.class, null, "ChefClient " + chefClientName
                        + " is not in the ChefServer");

            JSONObject jsonChefClient = JSONObject.fromObject(stringChefClient);
            ChefClient chefClient = new ChefClient();
            chefClient.fromJson(jsonChefClient);
            return chefClient;
        } catch (UniformInterfaceException e) {
            throw new CanNotCallChefException(e);
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
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
