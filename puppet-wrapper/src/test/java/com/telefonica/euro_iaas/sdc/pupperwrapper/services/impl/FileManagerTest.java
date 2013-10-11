package com.telefonica.euro_iaas.sdc.pupperwrapper.services.impl;

import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.sdc.puppetwrapper.constants.Action;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Node;
import com.telefonica.euro_iaas.sdc.puppetwrapper.data.Software;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:**testContext.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class FileManagerTest {
	
	@Resource
	private FileManager fileManager;
	
	@Test (expected=NoSuchElementException.class)
	public void getNodeTest_notfound() {
		Node node = fileManager.getNode("test");
		
	}
	@Test
	public void getNodeTest() {
		Node node = new Node();
		node.setName("test");
		node.setGroupName("group");
		fileManager.addNode(node);
		Node node1 = fileManager.getNode("test");
		assertTrue(node1.getName().equals("test"));
	}
	
	@Test
	public void testAddNode(){
		int length=fileManager.getNodeLength();
		assertTrue(length==0);
		Node node = new Node();
		node.setName("test");
		node.setGroupName("group");
		fileManager.addNode(node);
		length=fileManager.getNodeLength();
		assertTrue(length==1);
	}
	
	@Test
	public void testRemoveNode(){
		int length=fileManager.getNodeLength();
		assertTrue(length==0);
		Node node = new Node();
		node.setName("test");
		node.setGroupName("group");
		fileManager.addNode(node);
		length=fileManager.getNodeLength();
		assertTrue(length==1);
		
		fileManager.removeNode(node);
		length=fileManager.getNodeLength();
		assertTrue(length==0);
	}
	
	@Test
	public void generateFileStrTest_onlyNode(){
		Node node = new Node();
		node.setName("test");
		node.setGroupName("group");
		fileManager.addNode(node);
		
		String str = fileManager.generateManifestStr("test");
		assertTrue(str.length()>0);
		assertTrue(str.contains("{"));
		assertTrue(str.contains("node"));
	}
	
	@Test
	public void generateFileStrTest_nodeAndSoft(){
		Node node = new Node();
		node.setName("test");
		node.setGroupName("group");

		Software soft = new Software();
		soft.setName("testSoft");
		soft.setAction(Action.INSTALL);
		
		node.addSoftware(soft);
		
		fileManager.addNode(node);
		
		String str = fileManager.generateManifestStr("test");
		assertTrue(str.length()>0);
		assertTrue(str.contains("{"));
		assertTrue(str.contains("node"));
		assertTrue(str.contains("class"));
		assertTrue(str.contains("install"));
	}

	@Test
	public void generateSiteFile(){
		Node node = new Node();
		node.setName("test");
		node.setGroupName("group");
		
		Node node2 = new Node();
		node2.setName("test2");
		node2.setGroupName("group2");
		
		fileManager.addNode(node);
		fileManager.addNode(node2);
		
		String str= fileManager.generateSiteStr();
		
		assertTrue(str.length()>0);
		assertTrue(str.contains("import 'group/*.pp'"));
		assertTrue(str.contains("import 'group2/*.pp'"));
	}

}
