package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.SO;

/**
 * Unit test for SODaoJpaImpl
 * @author Sergio Arroyo
 *
 */
public class SODaoJpaImplTest extends AbstractJpaDaoTest {

    private SODao soDao;

    public final static String SO_NAME = "TestSO";
    public final static String SO_DESCRIPTION = "TestDescription";
    public final static String SO_VERSION = "TestVersion";

    /**
     * Test the create and load method
     */
    public void testCreate() throws Exception {
        SO so = new SO(SO_NAME, SO_DESCRIPTION, SO_VERSION);
        assertNull(so.getId());

        SO createdSO = soDao.create(so);
        assertNotNull(createdSO.getId());
        assertEquals(so.getName(), createdSO.getName());
        assertEquals(so.getDescription(), createdSO.getDescription());
        assertEquals(so.getVersion(), createdSO.getVersion());

        SO findSo = soDao.load(createdSO.getName());
        assertEquals(createdSO, findSo);
    }

    /**
     * Test the create and load method
     */
    public void testFindAllAndUpdate() throws Exception {
        assertEquals(0, soDao.findAll().size());
        testCreate();
        List<SO> ssoo = soDao.findAll();
        assertEquals(1, ssoo.size());
        SO so = ssoo.get(0);
        so.setName("newName");
        soDao.update(so);
        assertEquals("newName", soDao.load(so.getName()).getName());
        soDao.remove(so);
        assertEquals(0, soDao.findAll().size());


    }

    /**
     * @param soDao the soDao to set
     */
    public void setSoDao(SODao soDao) {
        this.soDao = soDao;
    }


}
