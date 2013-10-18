package com.telefonica.euro_iaas.sdc.pupperwrapper.services.impl;

import static org.junit.Assert.*;

import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.sdc.puppetwrapper.common.Action;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Software;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileAccessService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:**testContext.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class FileAccessServiceTest {

    @Resource
    private FileAccessService fileAccessService;

    @Resource
    private CatalogManager catalogManager;

    @Value("${defaultManifestsPath}")
    private String defaultManifestsPath;

    @Test
    public void generateManifestFileTest() throws ImagingOpException, IOException {

        Node node = new Node();
        node.setId("test");
        node.setGroupName("group");

        Node node2 = new Node();
        node2.setId("test2");
        node2.setGroupName("group");

        Software soft = new Software();
        soft.setName("testSoft");
        soft.setVersion("1.0.0");
        soft.setAction(Action.INSTALL);

        node.addSoftware(soft);

        Software soft2 = new Software();
        soft2.setName("testSoft2");
        soft.setVersion("2.0.0");
        soft2.setAction(Action.INSTALL);

        node2.addSoftware(soft2);

        catalogManager.addNode(node);
        catalogManager.addNode(node2);

        fileAccessService.generateManifestFile("test");
        fileAccessService.generateManifestFile("test2");

        File f = new File(defaultManifestsPath + node.getGroupName() + "/" + node.getId() + ".pp");
        assertTrue(f.exists());

        File f2 = new File(defaultManifestsPath + node2.getGroupName() + "/" + node2.getId() + ".pp");
        assertTrue(f2.exists());
    }

    @Test
    public void generateSiteFileTest() throws IOException {

        Node node = new Node();
        node.setId("test");
        node.setGroupName("group");

        Node node2 = new Node();
        node2.setId("test2");
        node2.setGroupName("group2");

        Software soft = new Software();
        soft.setName("testSoft");
        soft.setAction(Action.INSTALL);

        node.addSoftware(soft);

        Software soft2 = new Software();
        soft2.setName("testSoft2");
        soft2.setAction(Action.INSTALL);

        node2.addSoftware(soft2);

        catalogManager.addNode(node);
        catalogManager.addNode(node2);

        fileAccessService.generateSiteFile();

        File f = new File(defaultManifestsPath + "site.pp");
        assertTrue(f.exists());

    }

    @Test
    public void deleteNodeTest() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        // creating structure
        Node node = new Node();
        node.setId("test");
        node.setGroupName("group");

        Node node2 = new Node();
        node2.setId("test2");
        node2.setGroupName("group2");

        Software soft = new Software();
        soft.setName("testSoft");
        soft.setAction(Action.INSTALL);

        node.addSoftware(soft);

        Software soft2 = new Software();
        soft2.setName("testSoft2");
        soft2.setAction(Action.INSTALL);

        node2.addSoftware(soft2);

        catalogManager.addNode(node);
        catalogManager.addNode(node2);

        fileAccessService.generateSiteFile();
        fileAccessService.generateManifestFile(node.getId());
        fileAccessService.generateManifestFile(node2.getId());

        File f = new File(defaultManifestsPath + "site.pp");
        assertTrue(f.exists());
        f = new File(defaultManifestsPath + node.getGroupName() + "/" + node.getId() + ".pp");
        assertTrue(f.exists());

        f = new File(defaultManifestsPath + node2.getGroupName() + "/" + node2.getId() + ".pp");
        assertTrue(f.exists());

        // deleting

        fileAccessService.deleteNodeFiles(node.getId());

        f = new File(defaultManifestsPath + "site.pp");
        assertTrue(f.exists());

        f = new File(defaultManifestsPath + node.getGroupName() + "/" + node.getId() + ".pp");
        assertFalse(f.exists());

        f = new File(defaultManifestsPath + node2.getGroupName() + "/" + node2.getId() + ".pp");
        assertTrue(f.exists());

    }

}
