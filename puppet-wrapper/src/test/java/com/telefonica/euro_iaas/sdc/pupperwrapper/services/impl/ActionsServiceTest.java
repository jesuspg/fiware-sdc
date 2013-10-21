package com.telefonica.euro_iaas.sdc.pupperwrapper.services.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.sdc.puppetwrapper.common.Action;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Software;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.ActionsService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.impl.CatalogManagerMongoImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:**testContext.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ActionsServiceTest {

    @SuppressWarnings("restriction")
    @Resource
    private ActionsService actionsService;

    private CatalogManager catalogManagerMongo;
    
    @Before
    public void setUpMock() throws Exception {
        catalogManagerMongo = mock(CatalogManagerMongoImpl.class);
        
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
        
        when(catalogManagerMongo.getNode("1")).thenReturn(nodeInstall).thenThrow(new NoSuchElementException());
        when(catalogManagerMongo.getNode("2")).thenReturn(nodeInstall_2);
        when(catalogManagerMongo.getNode("3")).thenReturn(nodeUNInstall);
        when(catalogManagerMongo.getNode("4")).thenReturn(nodeUNInstall_2);
        
        
//        /*** apiKey not exists ***/
//        when(userService.getUserToken(0)).thenThrow(new NoSuchElementException());
//        
//        /*** token is not from this user ***/
//        when(userService.getUserToken(2)).thenReturn("wrongToken");
//        
//        /*** all ok ***/
//        when(userService.getUserToken(1)).thenReturn("token");
//        
//        credentialsAspect.setUserService(userService);
        
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
    public void deleteNodeTest() throws IOException {

        // install 2 nodes
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

        // delete node 1

        actionsService.deleteNode("5");

        catalogManagerMongo.getNode("5");

    }
}
