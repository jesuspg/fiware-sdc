package com.telefonica.euro_iaas.sdc.util;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_URL_TEMPLATE;

import java.text.MessageFormat;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

public class SDCClientUtilsImpl implements SDCClientUtils {

    public final static String VM_PATH = "/rest/vm";
    public final static String CONFIG_PATH = "/rest/config";
    public final static String EXECUTE_PATH = "/rest/installable";

    private SystemPropertiesProvider propertiesProvider;
    private Client client;

    /**
     * {@inheritDoc}
     */
    @Override
    public VM getVM(String ip) {
        String url = MessageFormat.format(
                propertiesProvider.getProperty(CHEF_CLIENT_URL_TEMPLATE), ip)
                + VM_PATH;
        WebResource webResource = client.resource(url);
        VM vm = webResource.accept(MediaType.APPLICATION_XML).get(VM.class);

        return vm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(VM vm) throws NodeExecutionException {
        try {
            String url = MessageFormat.format(
                    propertiesProvider.getProperty(CHEF_CLIENT_URL_TEMPLATE),
                    vm.getExecuteChefConectionUrl()) + EXECUTE_PATH;
            WebResource webResource = client.resource(url);
            webResource.accept(MediaType.APPLICATION_XML).post();
        } catch (UniformInterfaceException e) {
            throw new NodeExecutionException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attribute> configure(VM vm, List<Attribute> configuration) {
        String url = MessageFormat.format(
                propertiesProvider.getProperty(CHEF_CLIENT_URL_TEMPLATE),
                vm.getExecuteChefConectionUrl())
                + CONFIG_PATH;
        WebResource webResource = client.resource(url);
        return webResource.accept(MediaType.APPLICATION_XML)
                .type(MediaType.APPLICATION_XML).entity(configuration)
                .put(Attributes.class);
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(
            SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param client
     *            the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

}
