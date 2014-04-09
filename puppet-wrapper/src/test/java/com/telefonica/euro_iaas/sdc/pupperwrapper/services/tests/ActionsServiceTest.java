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

package com.telefonica.euro_iaas.sdc.pupperwrapper.services.tests;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.puppetwrapper.common.Action;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Software;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileAccessService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl.CatalogManagerMongoImpl;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl.FileAccessServiceImpl;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl.ProcessBuilderFactory;

public class ActionsServiceTest {

    private ActionServiceImpl4Test actionsService;

    private CatalogManager catalogManagerMongo;

    private ProcessBuilderFactory processBuilderFactory;

    @Before
    public void setUpMock() throws Exception {
        catalogManagerMongo = mock(CatalogManagerMongoImpl.class);

        FileAccessService fileAccessService = mock(FileAccessServiceImpl.class);

        processBuilderFactory = mock(ProcessBuilderFactory.class);

        actionsService = new ActionServiceImpl4Test();
        actionsService.setCatalogManager(catalogManagerMongo);
        actionsService.setFileAccessService(fileAccessService);
        actionsService.setProcessBuilderFactory(processBuilderFactory);

        Node nodeInstall = new Node();
        nodeInstall.setGroupName("testGroup");
        nodeInstall.setId("1");
        Software soft = new Software();
        soft.setName("testSoft");
        soft.setAction(Action.INSTALL);
        soft.setVersion("1.0.0");
        nodeInstall.addSoftware(soft);

        Node nodeInstall_2 = new Node();
        nodeInstall_2.setGroupName("testGroup");
        nodeInstall_2.setId("2");
        Software soft_2 = new Software();
        soft_2.setName("testSoft2");
        soft_2.setAction(Action.INSTALL);
        soft_2.setVersion("2.0.0");
        nodeInstall_2.addSoftware(soft_2);

        Node nodeUNInstall = new Node();
        nodeUNInstall.setGroupName("testGroup");
        nodeUNInstall.setId("3");
        Software softUN = new Software();
        softUN.setName("testSoft");
        softUN.setAction(Action.UNINSTALL);
        softUN.setVersion("1.0.0");
        nodeUNInstall.addSoftware(softUN);

        Node nodeUNInstall_2 = new Node();
        nodeUNInstall_2.setGroupName("testGroup");
        nodeUNInstall_2.setId("4");
        Software softUN_2 = new Software();
        softUN_2.setName("testSoft2");
        softUN_2.setAction(Action.UNINSTALL);
        softUN_2.setVersion("2.0.0");
        nodeUNInstall_2.addSoftware(softUN_2);

        when(catalogManagerMongo.getNode("1")).thenReturn(nodeInstall).thenReturn(nodeInstall)
                .thenThrow(new NoSuchElementException());
        when(catalogManagerMongo.getNode("2")).thenReturn(nodeInstall_2);
        when(catalogManagerMongo.getNode("3")).thenReturn(nodeUNInstall);
        when(catalogManagerMongo.getNode("4")).thenReturn(nodeUNInstall_2);

    }

    @Test
    public void install() {

        actionsService.action(Action.INSTALL, "testGroup", "1", "testSoft", "1.0.0");

        Node node = catalogManagerMongo.getNode("1");
        Software soft = node.getSoftware("testSoft");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getId().equals("1"));
        assertTrue(soft.getName().equals("testSoft"));
        assertTrue(soft.getVersion().equals("1.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

    }

    @Test
    public void install_Modification_Soft() {

        actionsService.action(Action.INSTALL, "testGroup", "1", "testSoft", "1.0.0");

        Node node = catalogManagerMongo.getNode("1");
        Software soft = node.getSoftware("testSoft");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getId().equals("1"));
        assertTrue(soft.getName().equals("testSoft"));
        assertTrue(soft.getVersion().equals("1.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

        actionsService.action(Action.INSTALL, "testGroup", "2", "testSoft2", "2.0.0");
        node = catalogManagerMongo.getNode("2");
        soft = node.getSoftware("testSoft2");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getId().equals("2"));
        assertTrue(soft.getName().equals("testSoft2"));
        assertTrue(soft.getVersion().equals("2.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

    }

    @Test
    public void uninstallTest() {

        actionsService.action(Action.UNINSTALL, "testGroup", "3", "testSoft", "1.0.0");

        Node node = catalogManagerMongo.getNode("3");
        Software soft = node.getSoftware("testSoft");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getId().equals("3"));
        assertTrue(soft.getName().equals("testSoft"));
        assertTrue(soft.getVersion().equals("1.0.0"));
        assertTrue(soft.getAction().equals(Action.UNINSTALL));

    }

    @Test
    public void uninstall_Modification_Soft() {

        actionsService.action(Action.INSTALL, "testGroup", "1", "testSoft", "1.0.0");

        Node node = catalogManagerMongo.getNode("1");
        Software soft = node.getSoftware("testSoft");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getId().equals("1"));
        assertTrue(soft.getName().equals("testSoft"));
        assertTrue(soft.getVersion().equals("1.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

        actionsService.action(Action.UNINSTALL, "testGroup", "4", "testSoft2", "2.0.0");
        node = catalogManagerMongo.getNode("4");
        soft = node.getSoftware("testSoft2");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getId().equals("4"));
        assertTrue(soft.getName().equals("testSoft2"));
        assertTrue(soft.getVersion().equals("2.0.0"));
        assertTrue(soft.getAction().equals(Action.UNINSTALL));

    }

    @Test(expected = NoSuchElementException.class)
    public void deleteNodeTest_OK() throws IOException {

        Process shell = mock(Process.class);
        Process shell2 = mock(Process.class);

        String[] cmd={anyString()};
        // call to puppet cert list --all
        when(
                processBuilderFactory.createProcessBuilder(cmd)).thenReturn(shell);

        String str = "Node 1 is registered";
        String strdelete = "Node 1 unregistered";
        when(shell.getInputStream()).thenReturn(new ByteArrayInputStream(str.getBytes("UTF-8"))).thenReturn(
                new ByteArrayInputStream(strdelete.getBytes("UTF-8")));

        String strEr = " ";
        when(shell.getErrorStream()).thenReturn(new ByteArrayInputStream(strEr.getBytes("UTF-8")));

        // call to puppet cert list --all |grep nodename|gawk. .
        when(
                processBuilderFactory.createProcessBuilder(cmd))
                .thenReturn(shell2);

        String str2 = "Node1.novalocal";
        when(shell2.getInputStream()).thenReturn(new ByteArrayInputStream(str2.getBytes("UTF-8")));

        String strEr2 = " ";
        when(shell2.getErrorStream()).thenReturn(new ByteArrayInputStream(strEr2.getBytes("UTF-8")));

        // install 2 nodes
        actionsService.action(Action.INSTALL, "testGroup", "1", "testSoft", "1.0.0");

        Node node = catalogManagerMongo.getNode("1");
        Software soft = node.getSoftware("testSoft");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getId().equals("1"));
        assertTrue(soft.getName().equals("testSoft"));
        assertTrue(soft.getVersion().equals("1.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

        actionsService.action(Action.INSTALL, "testGroup", "2", "testSoft2", "2.0.0");
        node = catalogManagerMongo.getNode("2");
        soft = node.getSoftware("testSoft2");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getId().equals("2"));
        assertTrue(soft.getName().equals("testSoft2"));
        assertTrue(soft.getVersion().equals("2.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

        // delete node 1

        actionsService.deleteNode("1");

        catalogManagerMongo.getNode("1");

    }

    @Test(expected = IOException.class)
    public void deleteNodeTest_Exception() throws IOException {

        Process shell = mock(Process.class);

        String[] cmd={anyString()};
        when(
                processBuilderFactory.createProcessBuilder(cmd)).thenReturn(shell);

        String str = "";
        String strdelete = "";
        when(shell.getInputStream()).thenReturn(new ByteArrayInputStream(str.getBytes("UTF-8"))).thenReturn(
                new ByteArrayInputStream(strdelete.getBytes("UTF-8")));

        String strEr = " ";
        when(shell.getErrorStream()).thenReturn(new ByteArrayInputStream(strEr.getBytes("UTF-8")));

        // install 2 nodes
        actionsService.action(Action.INSTALL, "testGroup", "1", "testSoft", "1.0.0");

        Node node = catalogManagerMongo.getNode("1");
        Software soft = node.getSoftware("testSoft");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getId().equals("1"));
        assertTrue(soft.getName().equals("testSoft"));
        assertTrue(soft.getVersion().equals("1.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

        actionsService.action(Action.INSTALL, "testGroup", "2", "testSoft2", "2.0.0");
        node = catalogManagerMongo.getNode("2");
        soft = node.getSoftware("testSoft2");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getId().equals("2"));
        assertTrue(soft.getName().equals("testSoft2"));
        assertTrue(soft.getVersion().equals("2.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

        // delete node 1

        actionsService.deleteNode("1");

        catalogManagerMongo.getNode("1");

    }

    @Test
    public void isNodeRegistered_NO() throws IOException {

        Process shell = mock(Process.class);

        String[] cmd = { anyString() };
        when(processBuilderFactory.createProcessBuilder(cmd)).thenReturn(shell);

        String str = "Node 3 is registered";
        when(shell.getInputStream()).thenReturn(new ByteArrayInputStream(str.getBytes("UTF-8")));

        String strEr = " ";
        when(shell.getErrorStream()).thenReturn(new ByteArrayInputStream(strEr.getBytes("UTF-8")));

        Assert.assertFalse(actionsService.isNodeRegistered("1"));

    }

    @Test
    public void isNodeRegistered_YES() throws IOException {

        Process shell = mock(Process.class);

        String[] cmd = { anyString() };

        when(processBuilderFactory.createProcessBuilder(cmd)).thenReturn(shell);

        String str = "Node 1 is registered";
        when(shell.getInputStream()).thenReturn(new ByteArrayInputStream(str.getBytes("UTF-8")));

        String strEr = " ";
        when(shell.getErrorStream()).thenReturn(new ByteArrayInputStream(strEr.getBytes("UTF-8")));

        Assert.assertTrue(actionsService.isNodeRegistered("1"));

    }

    @Test(expected = IOException.class)
    public void isNodeRegistered_Exception() throws IOException {

        Process shell = mock(Process.class);

        String[] cmd = { anyString() };
        when(processBuilderFactory.createProcessBuilder(cmd)).thenReturn(shell);

        String str = "";
        when(shell.getInputStream()).thenReturn(new ByteArrayInputStream(str.getBytes("UTF-8")));

        String strEr = " ";
        when(shell.getErrorStream()).thenReturn(new ByteArrayInputStream(strEr.getBytes("UTF-8")));

        Assert.assertTrue(actionsService.isNodeRegistered("1"));

    }

    @Test()
    public void getRealNodeNameTest() throws IOException {

        Process shell = mock(Process.class);

        String[] cmd = { anyString() };

        when(processBuilderFactory.createProcessBuilder(cmd)).thenReturn(shell);

        String str = "\"testnodename.openstacklocal\"";
        when(shell.getInputStream()).thenReturn(new ByteArrayInputStream(str.getBytes("UTF-8")));

        String strEr = " ";
        when(shell.getErrorStream()).thenReturn(new ByteArrayInputStream(strEr.getBytes("UTF-8")));

        Assert.assertTrue("testnodename.openstacklocal".equals(actionsService.getRealNodeName("testnodename")));

    }

}
