package com.telefonica.euro_iaas.sdc.dao;

import com.telefonica.euro_iaas.sdc.model.OSInstance;
import com.telefonica.euro_iaas.sdc.model.OSInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Unit test for SODaoJpaImpl
 * @author Sergio Arroyo
 *
 */
public class SOInstanceDaoJpaImplTest extends AbstractJpaDaoTest {

    private OSInstanceDao soInstanceDao;
    private OSDao soDao;


    protected void createSO() throws Exception {
        SODaoJpaImplTest soDaoTest = new SODaoJpaImplTest();
        soDaoTest.setSoDao(soDao);
        soDaoTest.testCreate();
    }

    /**
     * Test the create and load method
     */
    public void testCreate() throws Exception {
        createSO();
        OSInstance instance = new OSInstance(
                soDao.load(SODaoJpaImplTest.SO_NAME), Status.RUNNING,
                new VM("", ""));
        assertEquals(0, soInstanceDao.findAll().size());

        instance = soInstanceDao.create(instance);
        assertEquals(instance, soInstanceDao.load(instance.getId()));
        assertEquals(1, soInstanceDao.findAll().size());

    }


    /**
     * @param soInstanceDao the soInstanceDao to set
     */
    public void setSoInstanceDao(OSInstanceDao soInstanceDao) {
        this.soInstanceDao = soInstanceDao;
    }

    /**
     * @param soDao the soDao to set
     */
    public void setSoDao(OSDao soDao) {
        this.soDao = soDao;
    }



}
