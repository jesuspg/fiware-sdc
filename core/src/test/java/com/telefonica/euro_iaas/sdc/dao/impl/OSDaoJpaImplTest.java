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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.model.OS;

/**
 * Unit test for SODaoJpaImpl
 * 
 * @author Sergio Arroyo
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class OSDaoJpaImplTest {

    @Autowired
    OSDao osDao;

    /**
     * Test the create and load method
     */
    @Test
    public void testCreate() throws Exception {
        OS so = new OS("Win", "test", "desc", "1");
        assertNull(so.getId());

        OS createdSO = osDao.create(so);
        assertNotNull(createdSO.getId());
        assertEquals(so.getName(), createdSO.getName());
        assertEquals(so.getDescription(), createdSO.getDescription());
        assertEquals(so.getVersion(), createdSO.getVersion());

        OS findSo = osDao.load(createdSO.getOsType());
        assertEquals(createdSO, findSo);
    }

    /**
     * Test the create and load method
     */
    @Test
    public void testFindAllAndUpdate() throws Exception {
        // given
        OS so = new OS("OSx", "test2", "desc", "2");
        osDao.create(so);

        // when
        List<OS> ssoo = osDao.findAll();
        OS so0 = ssoo.get(0);
        so0.setName("newName");
        osDao.update(so0);
        long updatedOS = so0.getId();

        // then
        assertEquals("newName", osDao.load(updatedOS).getName());

    }

    /**
     * @param osDao
     *            the osDao to set
     */
    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
    }

}
