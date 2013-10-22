/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.pupperwrapper.services.impl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.sdc.puppetwrapper.common.Action;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Software;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.ActionsService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:**testContext.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ActionsServiceTest {

    @SuppressWarnings("restriction")
    @Resource
    private ActionsService actionsService;

    @SuppressWarnings("restriction")
    @Resource
    private CatalogManager fileManager;

    @Test
    public void install() {

        actionsService.install("testGroup", "testNode", "testSoft", "1.0.0");

        Node node = fileManager.getNode("testNode");
        Software soft = node.getSoftware("testSoft");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getName().equals("testNode"));
        assertTrue(soft.getName().equals("testSoft"));
        assertTrue(soft.getVersion().equals("1.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

    }

    @Test
    public void install_Modification_Soft() {

        actionsService.install("testGroup", "testNode", "testSoft", "1.0.0");

        Node node = fileManager.getNode("testNode");
        Software soft = node.getSoftware("testSoft");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getName().equals("testNode"));
        assertTrue(soft.getName().equals("testSoft"));
        assertTrue(soft.getVersion().equals("1.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

        actionsService.install("testGroup", "testNode", "testSoft2", "2.0.0");
        node = fileManager.getNode("testNode");
        soft = node.getSoftware("testSoft2");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getName().equals("testNode"));
        assertTrue(soft.getName().equals("testSoft2"));
        assertTrue(soft.getVersion().equals("2.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

    }

    @Test(expected = NoSuchElementException.class)
    public void deleteNodeTest() throws IOException {

        // install 2 nodes
        actionsService.install("testGroup", "testNode", "testSoft", "1.0.0");

        Node node = fileManager.getNode("testNode");
        Software soft = node.getSoftware("testSoft");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getName().equals("testNode"));
        assertTrue(soft.getName().equals("testSoft"));
        assertTrue(soft.getVersion().equals("1.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

        actionsService.install("testGroup", "testNode2", "testSoft2", "2.0.0");
        node = fileManager.getNode("testNode2");
        soft = node.getSoftware("testSoft2");

        assertTrue(node != null);
        assertTrue(soft != null);
        assertTrue(node.getGroupName().equals("testGroup"));
        assertTrue(node.getName().equals("testNode2"));
        assertTrue(soft.getName().equals("testSoft2"));
        assertTrue(soft.getVersion().equals("2.0.0"));
        assertTrue(soft.getAction().equals(Action.INSTALL));

        // delete node 1

        actionsService.deleteNode("testNode");

        fileManager.getNode("testNode");

    }
}
