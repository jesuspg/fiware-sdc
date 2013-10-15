/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.util;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_URL_TEMPLATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.NodeCommandDao;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.NodeCommand;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.NodeCommandSearchCriteria;

public class SDCClientUtilsImplTest {

    SDCClientUtilsImpl sdcClientUtils;
    SystemPropertiesProvider propertiesProvider;
    Client client;
    WebResource webResource;
    OSDao osDao1;
    NodeCommandDao nodeCommandDao;

    @Before
    public void setUp() throws Exception {
        sdcClientUtils = new SDCClientUtilsImpl();
        propertiesProvider = mock(SystemPropertiesProvider.class);
        client = mock(Client.class);
        sdcClientUtils.setClient(client);
        sdcClientUtils.setPropertiesProvider(propertiesProvider);
        webResource = mock(WebResource.class);
        osDao1 = mock(OSDao.class);
        sdcClientUtils.setOsDao(osDao1);
        nodeCommandDao = mock(NodeCommandDao.class);
        sdcClientUtils.setNodeCommandDao(nodeCommandDao);

        when(propertiesProvider.getProperty(CHEF_CLIENT_URL_TEMPLATE)).thenReturn("http://baseurl");
        when(client.resource(anyString())).thenReturn(webResource);

    }

    @Test
    public void shouldGetVM() {
        // given

        String ip = "127.0.0.1";
        String fqn = "fqn";
        String osType = "linux";
        VM vm = new VM();

        String url = "http://baseurl/rest/vm";
        WebResource.Builder builder = mock(WebResource.Builder.class);
        // when

        when(client.resource(url)).thenReturn(webResource);
        when(webResource.accept(MediaType.APPLICATION_XML)).thenReturn(builder);
        when(builder.get(VM.class)).thenReturn(vm);

        VM resultVM = sdcClientUtils.getVM(ip, fqn, osType);
        // then
        assertNotNull(resultVM);
        assertEquals(resultVM.getFqn(), fqn);
        assertEquals(resultVM.getOsType(), osType);
        verify(client).resource(url);
        verify(webResource).accept(MediaType.APPLICATION_XML);
        verify(builder).get(VM.class);
    }

    @Test(expected = InvalidInstallProductRequestException.class)
    public void shouldThrowExceptionInSetCommandWithInvalidOsType() throws EntityNotFoundException,
            InvalidInstallProductRequestException {
        // given
        SDCClientUtilsImpl sdcClientUtils = new SDCClientUtilsImpl();
        NodeCommandDao nodeCommandDao1 = mock(NodeCommandDao.class);
        String osType = "76";
        VM vm = new VM("fqn", "ip", "hostname", "domain", osType);
        OSDao osDao1 = mock(OSDao.class);
        sdcClientUtils.setOsDao(osDao1);
        sdcClientUtils.setNodeCommandDao(nodeCommandDao1);
        // when
        when(osDao1.load(osType)).thenThrow(new EntityNotFoundException(OS.class, "", ""));

        sdcClientUtils.setNodeCommands(vm);

        // then
    }

    @Test
    public void shouldSetCommandWithOS76WithoutNodeCommands() throws InvalidInstallProductRequestException,
            EntityNotFoundException {
        // given
        SDCClientUtilsImpl sdcClientUtils = new SDCClientUtilsImpl();
        NodeCommandDao nodeCommandDao1 = mock(NodeCommandDao.class);
        String osType = "76";
        VM vm = new VM("fqn", "ip", "hostname", "domain", osType);
        OSDao osDao1 = mock(OSDao.class);
        sdcClientUtils.setOsDao(osDao1);
        sdcClientUtils.setNodeCommandDao(nodeCommandDao1);
        List<NodeCommand> nodeCommands = new ArrayList<NodeCommand>(2);
        // when
        OS os1 = new OS();
        when(osDao1.load("76")).thenReturn(os1);
        when(nodeCommandDao1.findByCriteria(any(NodeCommandSearchCriteria.class))).thenReturn(nodeCommands);

        sdcClientUtils.setNodeCommands(vm);

        // then
        verify(nodeCommandDao1).findByCriteria(any(NodeCommandSearchCriteria.class));
    }

    @Test
    public void shouldSetCommandWithOS76WithNodeCommands() throws InvalidInstallProductRequestException,
            EntityNotFoundException {
        // given

        String osType = "76";
        VM vm = new VM("fqn", "ip", "hostname", "domain", osType);
        ClientResponse clientResponse1 = mock(ClientResponse.class);
        List<NodeCommand> nodeCommands = new ArrayList<NodeCommand>(2);
        NodeCommand nodeCommand = new NodeCommand();
        nodeCommands.add(nodeCommand);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        OS os1 = new OS();

        // when
        when(osDao1.load("76")).thenReturn(os1);
        when(nodeCommandDao.findByCriteria(any(NodeCommandSearchCriteria.class))).thenReturn(nodeCommands);
        when(webResource.accept(MediaType.APPLICATION_XML)).thenReturn(builder);
        when(builder.type(MediaType.APPLICATION_XML)).thenReturn(builder);
        when(builder.put(eq(ClientResponse.class), any(Attribute.class))).thenReturn(clientResponse1);

        sdcClientUtils.setNodeCommands(vm);

        // then
        verify(nodeCommandDao).findByCriteria(any(NodeCommandSearchCriteria.class));
        verify(client).resource(anyString());
        verify(builder).put(eq(ClientResponse.class), any(Attribute.class));
    }

    @Test
    public void shouldSetCommandWithOS7777WithoutNodeCommands() throws InvalidInstallProductRequestException,
            EntityNotFoundException {
        // given
        SDCClientUtilsImpl sdcClientUtils = new SDCClientUtilsImpl();
        NodeCommandDao nodeCommandDao1 = mock(NodeCommandDao.class);
        String osType = "7777";
        VM vm = new VM("fqn", "ip", "hostname", "domain", osType);
        OSDao osDao1 = mock(OSDao.class);
        sdcClientUtils.setOsDao(osDao1);
        sdcClientUtils.setNodeCommandDao(nodeCommandDao1);
        List<NodeCommand> nodeCommands = new ArrayList<NodeCommand>(2);
        // when
        OS os1 = new OS();
        when(osDao1.load("76")).thenReturn(os1);
        when(nodeCommandDao1.findByCriteria(any(NodeCommandSearchCriteria.class))).thenReturn(nodeCommands);

        sdcClientUtils.setNodeCommands(vm);

        // then
        verify(osDao1).load("76");
        verify(nodeCommandDao1).findByCriteria(any(NodeCommandSearchCriteria.class));
    }
}
