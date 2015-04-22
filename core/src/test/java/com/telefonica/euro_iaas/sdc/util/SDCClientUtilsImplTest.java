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

package com.telefonica.euro_iaas.sdc.util;

import static com.telefonica.euro_iaas.sdc.util.Configuration.CHEF_CLIENT_URL_TEMPLATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
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
    WebTarget webResource;
    OSDao osDao1;
    NodeCommandDao nodeCommandDao;

    @Before
    public void setUp() throws Exception {
        sdcClientUtils = new SDCClientUtilsImpl();
        propertiesProvider = mock(SystemPropertiesProvider.class);
        client = mock(Client.class);
        sdcClientUtils.setClient(client);

        webResource = mock(WebTarget.class);
        osDao1 = mock(OSDao.class);
        sdcClientUtils.setOsDao(osDao1);
        nodeCommandDao = mock(NodeCommandDao.class);
        sdcClientUtils.setNodeCommandDao(nodeCommandDao);

        when(propertiesProvider.getProperty(CHEF_CLIENT_URL_TEMPLATE)).thenReturn("http://baseurl");
        when(client.target(anyString())).thenReturn(webResource);

    }

    @Test
    public void shouldGetVM() {
        // given

        String ip = "127.0.0.1";
        String fqn = "fqn";
        String osType = "linux";
        VM vm = new VM();
        String url = "http://" + ip + ":9990/sdc-client/rest/vm";

        Invocation.Builder builder = mock(Invocation.Builder.class);
        // when

        when(client.target(url)).thenReturn(webResource);
        when(webResource.request(MediaType.APPLICATION_XML)).thenReturn(builder);
        when(builder.get(VM.class)).thenReturn(vm);

        VM resultVM = sdcClientUtils.getVM(ip, fqn, osType);
        // then
        assertNotNull(resultVM);
        assertEquals(resultVM.getFqn(), fqn);
        assertEquals(resultVM.getOsType(), osType);
        verify(client).target(url);
        verify(webResource).request(MediaType.APPLICATION_XML);
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
        Response clientResponse1 = mock(Response.class);
        List<NodeCommand> nodeCommands = new ArrayList<NodeCommand>(2);
        NodeCommand nodeCommand = new NodeCommand();
        nodeCommands.add(nodeCommand);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        OS os1 = new OS();

        // when
        when(osDao1.load("76")).thenReturn(os1);
        when(nodeCommandDao.findByCriteria(any(NodeCommandSearchCriteria.class))).thenReturn(nodeCommands);
        when(webResource.request(MediaType.APPLICATION_XML)).thenReturn(builder);
        when(builder.accept(MediaType.APPLICATION_XML)).thenReturn(builder);
        when(builder.put(Entity.xml(any(Attribute.class)))).thenReturn(clientResponse1);

        sdcClientUtils.setNodeCommands(vm);

        // then
        verify(nodeCommandDao).findByCriteria(any(NodeCommandSearchCriteria.class));
        verify(client).target(anyString());
        verify(builder).put(Entity.xml(any(Attribute.class)));
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
