/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
