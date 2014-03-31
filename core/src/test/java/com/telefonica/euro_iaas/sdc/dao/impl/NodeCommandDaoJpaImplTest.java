/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.NodeCommandDao;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.model.NodeCommand;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.NodeCommandSearchCriteria;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class NodeCommandDaoJpaImplTest {
    // Need to be revisited

    @Autowired
    OSDao osDao;

    @Autowired
    NodeCommandDao nodeCommandDao;

    public void createNodeCommand() throws Exception {

        NodeCommand nodeCommand = new NodeCommand();
        OS so = new OS("Pru", "1", "Prueba I Description", "Prueba I Version");

        try {
            so = osDao.load(so.getName());
            System.out.println("The OS " + so.getName() + " already exists");
        } catch (EntityNotFoundException e) {
            System.out.println("The Product " + so.getName() + " does not exist");
            so = osDao.create(so);
        }

        nodeCommand.setOS(so);
        nodeCommand.setKey("hostname_command");
        nodeCommand.setValue("hostname");

        NodeCommand creatednodeCommand = nodeCommandDao.create(nodeCommand);
        assertEquals(creatednodeCommand, nodeCommand);

    }

    @Test
    public void testCreateAndFindByCriteria() throws Exception {

        System.out.println("testCreateAndFindByCriteria.Start");
        createNodeCommand();
        System.out.println("NodeCommand created");

        NodeCommand nodeCommand = nodeCommandDao.findAll().get(0);

        NodeCommandSearchCriteria criteria = new NodeCommandSearchCriteria(nodeCommand.getOS());
        assertEquals(1, nodeCommandDao.findByCriteria(criteria).size());
        // Assert.assertEquals(nodeCommandDao.findByCriteria(criteria),
        // nodeCommand);
    }

    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
    }

    public void setNodeCommandDao(NodeCommandDao nodeCommandDao) {
        this.nodeCommandDao = nodeCommandDao;
    }
}
