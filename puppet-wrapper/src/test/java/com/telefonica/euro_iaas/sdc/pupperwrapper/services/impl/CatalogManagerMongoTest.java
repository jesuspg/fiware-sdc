package com.telefonica.euro_iaas.sdc.pupperwrapper.services.impl;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.NoSuchElementException;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.sdc.puppetwrapper.common.Action;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Software;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.CatalogManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:**testContext.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CatalogManagerMongoTest {

    @Resource
    private CatalogManager catalogManagerMongo;

    @Resource
    MongoTemplate mongoTemplate;

    @Before
    public void setUp() {
        mongoTemplate.remove(new Query(), "nodes");

    }

    @Test(expected = NoSuchElementException.class)
    public void getNodeTest_notfound() {
        Node node = catalogManagerMongo.getNode("test");

    }

    @Test
    public void getNodeTest() {
        Node node = new Node();
        node.setId("test");
        node.setGroupName("group");
        catalogManagerMongo.addNode(node);
        Node node1 = catalogManagerMongo.getNode("test");
        assertTrue(node1.getId().equals("test"));
    }

    @Test
    public void testAddNode() {
        int length = catalogManagerMongo.getNodeLength();
        assertTrue(length == 0);
        Node node = new Node();
        node.setId("test");
        node.setGroupName("group");
        catalogManagerMongo.addNode(node);
        length = catalogManagerMongo.getNodeLength();
        assertTrue(length == 1);
    }

    @Test
    public void testRemoveNode() {
        int length = catalogManagerMongo.getNodeLength();
        assertTrue(length == 0);
        Node node = new Node();
        node.setId("test");
        node.setGroupName("group");
        catalogManagerMongo.addNode(node);
        length = catalogManagerMongo.getNodeLength();
        assertTrue(length == 1);

        catalogManagerMongo.removeNode(node.getId());
        length = catalogManagerMongo.getNodeLength();
        assertTrue(length == 0);
    }

    @Test
    public void generateFileStrTest_onlyNode() {
        Node node = new Node();
        node.setId("test");
        node.setGroupName("group");
        catalogManagerMongo.addNode(node);

        String str = catalogManagerMongo.generateManifestStr("test");
        assertTrue(str.length() > 0);
        assertTrue(str.contains("{"));
        assertTrue(str.contains("node"));
    }

    @Test
    public void generateFileStrTest_nodeAndSoft() {
        Node node = new Node();
        node.setId("test");
        node.setGroupName("group");

        Software soft = new Software();
        soft.setName("testSoft");
        soft.setVersion("1.0.0");
        soft.setAction(Action.INSTALL);

        node.addSoftware(soft);

        catalogManagerMongo.addNode(node);

        String str = catalogManagerMongo.generateManifestStr("test");
        assertTrue(str.length() > 0);
        assertTrue(str.contains("{"));
        assertTrue(str.contains("node"));
        assertTrue(str.contains("class"));
        assertTrue(str.contains("install"));
        assertTrue(str.contains("version"));
    }

    @Test
    public void generateSiteFile() {
        Node node = new Node();
        node.setId("test");
        node.setGroupName("group");

        Node node2 = new Node();
        node2.setId("test2");
        node2.setGroupName("group2");

        catalogManagerMongo.addNode(node);
        catalogManagerMongo.addNode(node2);

        String str = catalogManagerMongo.generateSiteStr();

        assertTrue(str.length() > 0);
        assertTrue(str.contains("import 'group/*.pp'"));
        assertTrue(str.contains("import 'group2/*.pp'"));
    }
    
    @Test
    public void removeNodesByGroupNameTest(){
        
        Node node = new Node();
        node.setId("test");
        node.setGroupName("group");

        Node node2 = new Node();
        node2.setId("test2");
        node2.setGroupName("group");

        catalogManagerMongo.addNode(node);
        catalogManagerMongo.addNode(node2);
        
        assertTrue(catalogManagerMongo.getNodeLength()==2);
        
        catalogManagerMongo.removeNodesByGroupName("group");
        
        assertTrue(catalogManagerMongo.getNodeLength()==0);
        
    }
}
