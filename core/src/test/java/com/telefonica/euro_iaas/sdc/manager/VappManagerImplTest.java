package com.telefonica.euro_iaas.sdc.manager;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.mockito.Mockito;

import com.telefonica.euro_iaas.sdc.dao.SODao;
import com.telefonica.euro_iaas.sdc.model.SO;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.SOSearchCriteria;
/**
 * Unit test suite for VappManagerImpl.
 *
 * @author Sergio Arroyo
 *
 */
public class VappManagerImplTest extends TestCase {

    public void testFindAllSSOO() throws Exception {
        SODao soDao = Mockito.mock(SODao.class);
        Mockito.when(soDao.findAll()).thenReturn(new ArrayList<SO>());
        ImageManagerImpl manager = new ImageManagerImpl();
        manager.setSoDao(soDao);
        SOSearchCriteria criteria = new SOSearchCriteria();
        List<SO> ssoo = manager.findAllSSOO(criteria);
        assertEquals(0, ssoo.size());
        Mockito.verify(soDao).findAll();
    }

}
