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
        
        actionsService=new ActionServiceImpl4Test();
        actionsService.setCatalogManager(catalogManagerMongo);
        actionsService.setFileAccessService(fileAccessService);
        actionsService.setProcessBuilderFactory(processBuilderFactory);
        
        Node nodeInstall=new Node();
        nodeInstall.setGroupName("testGroup");
        nodeInstall.setId("1");
        Software soft = new Software();
        soft.setName("testSoft");
        soft.setAction(Action.INSTALL);
        soft.setVersion("1.0.0");
        nodeInstall.addSoftware(soft);
        
        Node nodeInstall_2=new Node();
        nodeInstall_2.setGroupName("testGroup");
        nodeInstall_2.setId("2");
        Software soft_2 = new Software();
        soft_2.setName("testSoft2");
        soft_2.setAction(Action.INSTALL);
        soft_2.setVersion("2.0.0");
        nodeInstall_2.addSoftware(soft_2);
        
        Node nodeUNInstall=new Node();
        nodeUNInstall.setGroupName("testGroup");
        nodeUNInstall.setId("3");
        Software softUN = new Software();
        softUN.setName("testSoft");
        softUN.setAction(Action.UNINSTALL);
        softUN.setVersion("1.0.0");
        nodeUNInstall.addSoftware(softUN);
        
        Node nodeUNInstall_2=new Node();
        nodeUNInstall_2.setGroupName("testGroup");
        nodeUNInstall_2.setId("4");
        Software softUN_2 = new Software();
        softUN_2.setName("testSoft2");
        softUN_2.setAction(Action.UNINSTALL);
        softUN_2.setVersion("2.0.0");
        nodeUNInstall_2.addSoftware(softUN_2);
        
        when(catalogManagerMongo.getNode("1")).thenReturn(nodeInstall).thenReturn(nodeInstall).thenThrow(new NoSuchElementException());
        when(catalogManagerMongo.getNode("2")).thenReturn(nodeInstall_2);
        when(catalogManagerMongo.getNode("3")).thenReturn(nodeUNInstall);
        when(catalogManagerMongo.getNode("4")).thenReturn(nodeUNInstall_2);
        
        
    }

    @Test
    public void install() {

        actionsService.action(Action.INSTALL,"testGroup", "1", "testSoft", "1.0.0");

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

        actionsService.action(Action.INSTALL,"testGroup", "1", "testSoft", "1.0.0");

        Node node = catalogManagerMongo.getNode("1");
        Software soft = node.getSoftware("testSoft");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getId().equals("1"));
        assertTrue(soft.getName().equals("testSoft"));
        assertTrue(soft.getVersion().equals("1.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

        actionsService.action(Action.INSTALL,"testGroup", "2", "testSoft2", "2.0.0");
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

        actionsService.action(Action.UNINSTALL,"testGroup", "3", "testSoft", "1.0.0");

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

        actionsService.action(Action.INSTALL,"testGroup", "1", "testSoft", "1.0.0");

        Node node = catalogManagerMongo.getNode("1");
        Software soft = node.getSoftware("testSoft");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getId().equals("1"));
        assertTrue(soft.getName().equals("testSoft"));
        assertTrue(soft.getVersion().equals("1.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

        actionsService.action(Action.UNINSTALL,"testGroup", "4", "testSoft2", "2.0.0");
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
        
        when(processBuilderFactory.createProcessBuilder(anyString(),anyString(),anyString(),anyString())).thenReturn(shell);
        
        String str= "Node 1 is registered";
        String strdelete="Node 1 unregistered";
        when(shell.getInputStream()).thenReturn(new ByteArrayInputStream(str.getBytes("UTF-8"))).thenReturn(new ByteArrayInputStream(strdelete.getBytes("UTF-8")));

        String strEr= " ";
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
    
    @Test(expected = IOException.class)
    public void deleteNodeTest_Exception() throws IOException {
        
        Process shell = mock(Process.class);
        
        when(processBuilderFactory.createProcessBuilder(anyString(),anyString(),anyString(),anyString())).thenReturn(shell);
        
        String str= "";
        String strdelete="";
        when(shell.getInputStream()).thenReturn(new ByteArrayInputStream(str.getBytes("UTF-8"))).thenReturn(new ByteArrayInputStream(strdelete.getBytes("UTF-8")));

        String strEr= " ";
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
        
        when(processBuilderFactory.createProcessBuilder(anyString(),anyString(),anyString(),anyString())).thenReturn(shell);
        
        String str= "Node 3 is registered";
        when(shell.getInputStream()).thenReturn(new ByteArrayInputStream(str.getBytes("UTF-8")));

        String strEr= " ";
        when(shell.getErrorStream()).thenReturn(new ByteArrayInputStream(strEr.getBytes("UTF-8")));

        Assert.assertFalse(actionsService.isNodeRegistered("1"));
        

    }

    @Test
    public void isNodeRegistered_YES() throws IOException {
        
        Process shell = mock(Process.class);
        
        when(processBuilderFactory.createProcessBuilder(anyString(),anyString(),anyString(),anyString())).thenReturn(shell);
        
        String str= "Node 1 is registered";
        when(shell.getInputStream()).thenReturn(new ByteArrayInputStream(str.getBytes("UTF-8")));

        String strEr= " ";
        when(shell.getErrorStream()).thenReturn(new ByteArrayInputStream(strEr.getBytes("UTF-8")));

        Assert.assertTrue(actionsService.isNodeRegistered("1"));
        

    }
    
    @Test(expected=IOException.class)
    public void isNodeRegistered_Exception() throws IOException {
        
        Process shell = mock(Process.class);
        
        when(processBuilderFactory.createProcessBuilder(anyString(),anyString(),anyString(),anyString())).thenReturn(shell);
        
        String str= "";
        when(shell.getInputStream()).thenReturn(new ByteArrayInputStream(str.getBytes("UTF-8")));

        String strEr= " ";
        when(shell.getErrorStream()).thenReturn(new ByteArrayInputStream(strEr.getBytes("UTF-8")));

        Assert.assertTrue(actionsService.isNodeRegistered("1"));
        

    }

    
}
