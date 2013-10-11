package com.telefonica.euro_iaas.sdc.pupperwrapper.services.impl;

import static org.junit.Assert.assertTrue;

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
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.ActionsService;
import com.telefonica.euro_iaas.sdc.puppetwrapper.services.FileManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:**testContext.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)

public class ActionsServiceTest {
	
	@SuppressWarnings("restriction")
	@Resource
	private ActionsService actionsService;
	
	@SuppressWarnings("restriction")
	@Resource
	private FileManager fileManager;
	
	@Test
	public void install(){
		
		actionsService.install("testGroup","testNode", "testSoft");
		
		Node node = fileManager.getNode("testNode");
		Software soft = node.getSoftware("testSoft");
		
		assertTrue(node!=null);
		assertTrue(soft!=null);
		assertTrue(node.getGroupName().equals("testGroup"));
		assertTrue(node.getName().equals("testNode"));
		assertTrue(soft.getName().equals("testSoft"));
		assertTrue(soft.getAction().equals(Action.INSTALL));
		
		
		
	}
	
	@Test
	public void install_Modification_Soft(){
		
		actionsService.install("testGroup","testNode", "testSoft");
		
		Node node = fileManager.getNode("testNode");
		Software soft = node.getSoftware("testSoft");
		
		assertTrue(node!=null);
		assertTrue(soft!=null);
		assertTrue(node.getGroupName().equals("testGroup"));
		assertTrue(node.getName().equals("testNode"));
		assertTrue(soft.getName().equals("testSoft"));
		assertTrue(soft.getAction().equals(Action.INSTALL));
		
		actionsService.install("testGroup","testNode", "testSoft2");
		node = fileManager.getNode("testNode");
		soft = node.getSoftware("testSoft2");
		
		assertTrue(node!=null);
		assertTrue(soft!=null);
		assertTrue(node.getGroupName().equals("testGroup"));
		assertTrue(node.getName().equals("testNode"));
		assertTrue(soft.getName().equals("testSoft2"));
		assertTrue(soft.getAction().equals(Action.INSTALL));
		
		
	}
	

}
