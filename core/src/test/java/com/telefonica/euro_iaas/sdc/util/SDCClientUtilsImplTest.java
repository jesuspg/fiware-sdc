package com.telefonica.euro_iaas.sdc.util;

import java.io.IOException;
import java.util.ArrayList;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.dao.NodeCommandDao;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.NodeCommand;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.NodeCommandSearchCriteria;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;


import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_URL_TEMPLATE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Jesus M. Movilla
 */
public class SDCClientUtilsImplTest extends TestCase {

    private NodeCommandDao nodeCommandDao;
    private OSDao osDao;
    private SystemPropertiesProvider propertiesProvider;
    private VM host = new VM("fqn", "ip", "hostname", "domain");
    private OS os = new OS("os1", "1", "os1 description", "v1");
    private Client client;
    private WebResource webResource;
    private ClientResponse clientResponse;
    private SDCClientUtilsImpl sdcClientUtilsImpl;

    @Before
    public void setUp() throws Exception {
        sdcClientUtilsImpl = new SDCClientUtilsImpl();

        osDao = mock(OSDao.class);
        when(osDao.load(anyString())).thenReturn(os);

        ArrayList<NodeCommand> nodeCommands = new ArrayList<NodeCommand>();
        nodeCommands.add(new NodeCommand(os, "key1", "value1"));
        nodeCommands.add(new NodeCommand(os, "key2", "value2"));

        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute("key1", "value1"));
        attributes.add(new Attribute("key2", "value2"));

        nodeCommandDao = mock(NodeCommandDao.class);
        when(nodeCommandDao.findByCriteria(new NodeCommandSearchCriteria(any(OS.class)))).thenReturn(nodeCommands);

        propertiesProvider = mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty(CHEF_CLIENT_URL_TEMPLATE)).thenReturn("http://{0}:9990/sdc-client");

        // webResource = mock(WebResource.class);
        // clientResponse = mock(ClientResponse.class);

        // client = mock(Client.class);
        // when(client.resource(anyString())).thenReturn(webResource);

        // doReturn(clientResponse).when(webResource).accept(MediaType.APPLICATION_XML).put(ClientResponse.class,
        // anyList());;

        // doReturn().when(webResource).accept((MediaType)
        // anyVararg()).type(anyString()).entity(anyList()).put(anyList());
    }

    /**
     * Test the correct behavior when invoking setNodeCommands
     * 
     * @throws Exception
     */
    @Test
    public void testSetNodeCommandOk() {
        // Pending to find ut how to mock the put call to the sdc-client
        /*
         * SDCClientUtilsImpl sdcClientUtilsImpl = new SDCClientUtilsImpl();
         * sdcClientUtilsImpl.setNodeCommandDao(nodeCommandDao); sdcClientUtilsImpl.setOSDao(osDao);
         * sdcClientUtilsImpl.setClient(client); sdcClientUtilsImpl.setPropertiesProvider(propertiesProvider); try {
         * sdcClientUtilsImpl.setNodeCommands(host); fail("ShellCommanException expected"); } catch
         * (InvalidInstallProductRequestException e) { // it's ok }
         */
    }

    /**
     * Test the correct behavior when the executed command does not exists.
     * 
     * @throws Exception
     */
    public void testScriptExecutedFailsBecauseDoesNotExists() throws Exception {
        CommandExecutorShellImpl shellCommand = new CommandExecutorShellImpl();
        try {
            shellCommand.executeCommand("asdag");
            fail("ShellCommanException expected");
        } catch (ShellCommandException e) {
            // it's ok
            assertTrue(e.getCause() instanceof IOException);
        }
    }
}
