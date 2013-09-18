package com.telefonica.euro_iaas.sdc.dao;

import com.telefonica.euro_iaas.sdc.model.SOInstance;
import com.telefonica.euro_iaas.sdc.model.SOInstance.Status;

/**
 * Unit test for SODaoJpaImpl
 * @author Sergio Arroyo
 *
 */
public class SOInstanceDaoJpaImplTest extends AbstractJpaDaoTest {

    private SOInstanceDao soInstanceDao;
    private SODao soDao;


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
        SOInstance instance = new SOInstance(
                soDao.load(SODaoJpaImplTest.SO_NAME), Status.RUNNING);
        assertEquals(0, soInstanceDao.findAll().size());

        instance = soInstanceDao.create(instance);
        assertEquals(instance, soInstanceDao.load(instance.getId()));
        assertEquals(1, soInstanceDao.findAll().size());

    }


    /**
     * @param soInstanceDao the soInstanceDao to set
     */
    public void setSoInstanceDao(SOInstanceDao soInstanceDao) {
        this.soInstanceDao = soInstanceDao;
    }

    /**
     * @param soDao the soDao to set
     */
    public void setSoDao(SODao soDao) {
        this.soDao = soDao;
    }



}
