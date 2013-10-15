/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.OS;

/**
 * Unit test for SODaoJpaImpl
 * 
 * @author Sergio Arroyo
 */
public class SODaoJpaImplTest extends AbstractJpaDaoTest {

    private OSDao soDao;

    public final static String SO_NAME = "TestSO";
    public final static String SO_OSTYPE = "OSTypeSO";
    public final static String SO_DESCRIPTION = "TestDescription";
    public final static String SO_VERSION = "TestVersion";

    /**
     * Test the create and load method
     */
    public void testCreate() throws Exception {
        OS so = new OS(SO_OSTYPE, SO_NAME, SO_DESCRIPTION, SO_VERSION);
        assertNull(so.getId());

        OS createdSO = soDao.create(so);
        assertNotNull(createdSO.getId());
        assertEquals(so.getName(), createdSO.getName());
        assertEquals(so.getDescription(), createdSO.getDescription());
        assertEquals(so.getVersion(), createdSO.getVersion());

        OS findSo = soDao.load(createdSO.getOsType());
        assertEquals(createdSO, findSo);
    }

    /**
     * Test the create and load method
     */
    public void testFindAllAndUpdate() throws Exception {
        assertEquals(0, soDao.findAll().size());
        testCreate();
        List<OS> ssoo = soDao.findAll();
        assertEquals(1, ssoo.size());
        OS so = ssoo.get(0);
        so.setName("newName");
        soDao.update(so);
        assertEquals("newName", soDao.load(so.getOsType()).getName());
        soDao.remove(so);
        assertEquals(0, soDao.findAll().size());

    }

    /**
     * @param soDao
     *            the soDao to set
     */
    public void setSoDao(OSDao soDao) {
        this.soDao = soDao;
    }

}
