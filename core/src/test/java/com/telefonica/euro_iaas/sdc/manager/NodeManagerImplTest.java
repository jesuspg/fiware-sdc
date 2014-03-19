package com.telefonica.euro_iaas.sdc.manager;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ChefClientDao;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.manager.impl.NodeManagerImpl;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;

public class NodeManagerImplTest {
    
    private ProductInstanceDao productInstanceDao;
    private ChefClientDao chefClientDao;
    private ChefNodeDao chefNodeDao;
    
    private NodeManagerImpl nodeManager;
    
    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws CanNotCallChefException{
        
        productInstanceDao= mock(ProductInstanceDao.class);
        chefClientDao=mock(ChefClientDao.class);
        chefNodeDao=mock(ChefNodeDao.class);
        
        nodeManager= new NodeManagerImpl();
        nodeManager.setChefClientDao(chefClientDao);
        nodeManager.setChefNodeDao(chefNodeDao);
        nodeManager.setProductInstanceDao(productInstanceDao);
        
    }
    
    @Test
    public void deleteNodeTestOK() throws NodeExecutionException, CanNotCallChefException, EntityNotFoundException{
        
        when(chefNodeDao.loadNode("testOk")).thenReturn(new ChefNode());
        
        List<ProductInstance> productInstances=new ArrayList<ProductInstance>();
        productInstances.add(new ProductInstance());
        
        when(productInstanceDao.findByHostname(anyString())).thenReturn(productInstances);
        
        nodeManager.nodeDelete("test", "testOk");
        
    }
    
    @Test
    public void deleteNodeTestEntityNotFound() throws EntityNotFoundException{
        
        List<ProductInstance> productInstances=new ArrayList<ProductInstance>();
        productInstances.add(new ProductInstance());
        
        when(productInstanceDao.findByHostname(anyString())).thenThrow(EntityNotFoundException.class);
        
    }

    @Test(expected=NodeExecutionException.class)
    public void deleteNodeTestNodeException_1() throws NodeExecutionException, CanNotCallChefException {
        
        when(chefNodeDao.loadNode("testError")).thenThrow(CanNotCallChefException.class);
        
        nodeManager.nodeDelete("test", "testError");
        
    }
    
    @Test(expected=NodeExecutionException.class)
    public void deleteNodeTestNodeException_2() throws NodeExecutionException, Exception {
        
        when(chefNodeDao.loadNode("testError")).thenThrow(Exception.class);
        
        nodeManager.nodeDelete("test", "testError");
        
    }
    
}
