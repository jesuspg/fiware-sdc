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

package com.telefonica.euro_iaas.sdc.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
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
