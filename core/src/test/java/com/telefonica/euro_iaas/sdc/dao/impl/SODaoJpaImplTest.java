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
public class SODaoJpaImplTest {

    @Autowired
    OSDao osDao;

    public final static String SO_NAME = "TestSO";
    public final static String SO_OSTYPE = "OSTypeSO";
    public final static String SO_DESCRIPTION = "TestDescription";
    public final static String SO_VERSION = "TestVersion";

    /**
     * Test the create and load method
     */
    @Test
    public void testCreate() throws Exception {
        OS so = new OS(SO_OSTYPE, SO_NAME, SO_DESCRIPTION, SO_VERSION);
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
        assertEquals(0, osDao.findAll().size());
        testCreate();
        List<OS> ssoo = osDao.findAll();
        assertEquals(1, ssoo.size());
        OS so = ssoo.get(0);
        so.setName("newName");
        osDao.update(so);
        assertEquals("newName", osDao.load(so.getOsType()).getName());
        osDao.remove(so);
        assertEquals(0, osDao.findAll().size());

    }

    /**
     * @param osDao
     *            the osDao to set
     */
    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
    }

}
