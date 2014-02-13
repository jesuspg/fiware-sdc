package com.telefonica.euro_iaas.sdc.pupperwrapper.services.tests;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.sdc.puppetwrapper.common.Action;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Software;

public class CatalogManagerMongoTest {

    private CatalogManagerMongoImpl4Test catalogManagerMongo;

    private MongoTemplate mongoTemplateMock;

    @Before
    public void setUp() {
        
        mongoTemplateMock = mock(MongoTemplate.class);
        
        catalogManagerMongo = new CatalogManagerMongoImpl4Test();
        catalogManagerMongo.setMongoTemplate(mongoTemplateMock);

        final Node node = new Node();
        node.setId("test");
        node.setGroupName("group");
        
        Software soft=new Software();
        soft.setName("test");
        soft.setVersion("1.0.0");
        soft.setAction(Action.INSTALL);
        node.addSoftware(soft);
        
        final Node node2 = new Node();
        node2.setId("test2");
        node2.setGroupName("group2");
        
        final Node node3 = new Node();
        node3.setId("test3");
        node3.setGroupName("group2");

//        Query query = mock(Query.class);
        Query query = new Query(Criteria.where("id").is("test"));
        when(mongoTemplateMock.findOne(query, Node.class)).thenReturn(node);
        
        query = new Query(Criteria.where("id").is("test3"));
        when(mongoTemplateMock.findOne(query, Node.class)).thenThrow(new NoSuchElementException());

        when(mongoTemplateMock.findAll(Node.class)).thenReturn(new ArrayList<Node>() {
            {
                add(node);
                add(node2);
                add(node3);
            }
        }).thenReturn(new ArrayList<Node>() {
            {
                add(node);
                add(node2);
                add(node3);
            }
        }).thenReturn(new ArrayList<Node>(){
            {
                add(node);
                add(node2);
            }
        });

    }

    @Test(expected = NoSuchElementException.class)
    public void getNodeTest_notfound() {
        Node node = catalogManagerMongo.getNode("test3");

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
        assertTrue(length == 2);
        Node node = new Node();
        node.setId("test");
        node.setGroupName("group");
        catalogManagerMongo.addNode(node);
        length = catalogManagerMongo.getNodeLength();
        assertTrue(length == 3);
    }

    @Test
    public void testRemoveNode() {
        int length = catalogManagerMongo.getNodeLength();
        assertTrue(length == 2);
        Node node = new Node();
        node.setId("test");
        node.setGroupName("group");
        catalogManagerMongo.addNode(node);
        length = catalogManagerMongo.getNodeLength();
        assertTrue(length == 3);

        catalogManagerMongo.removeNode(node.getId());
        length = catalogManagerMongo.getNodeLength();
        assertTrue(length == 2);
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
      

        String str = catalogManagerMongo.generateManifestStr("test");
        assertTrue(str.length() > 0);
        assertTrue(str.contains("{"));
        assertTrue(str.contains("node"));
        assertTrue(str.contains("class"));
        assertTrue(str.contains("install"));
        assertTrue(str.contains("version"));
    }

    @Test
    public void generateSiteFileTest() {

        String str = catalogManagerMongo.generateSiteStr();

        assertTrue(str.length() > 0);
        assertTrue(str.contains("import 'group/*.pp'"));
        assertTrue(str.contains("import 'group2/*.pp'"));
        
    }

    @Test
    public void removeNodesByGroupNameTest() {

        catalogManagerMongo.removeNodesByGroupName("group");

        assertTrue(catalogManagerMongo.getNodeLength() == 2);

    }
}
