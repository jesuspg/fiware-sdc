package com.telefonica.euro_iaas.sdc.manager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.sdc.dao.EnvironmentDao;
import com.telefonica.euro_iaas.sdc.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.manager.impl.EnvironmentInstanceManagerImpl;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.EnvironmentInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.validation.EnvironmentInstanceValidator;

public class EnvironmentInstanceManagerImplTest{

    private EnvironmentDao environmentDao;
    private EnvironmentInstanceDao environmentInstanceDao;
    private ProductInstanceDao productInstanceDao;
    private EnvironmentInstanceValidator envInstanceValidator;


    VM vm;
    List<ProductInstance> products;
    Environment environment;
    EnvironmentInstance envInstance;
    Application application;
    ApplicationRelease appRelease;
    ApplicationInstance applicationInstance;
    ProductInstance pi1, pi2;
    ProductRelease pr1, pr2;
    Product p1, p2;

    @Before
    public void setUp() throws Exception {

        vm = new VM("ip", "hostname", "domain");

         p1 = new Product("p1","description");
         p2 = new Product("p2","description");
         pr1 = new ProductRelease(
                "version1", "releaseNotes1", null, p1, null, null);
         pr2 = new ProductRelease(
                "version2", "releaseNotes2", null, p2, null, null);

        environment = new Environment (Arrays.asList(pr1, pr2));
        
        pi1 = new ProductInstance(pr1,
                ProductInstance.Status.INSTALLED, vm, "vdc");
        pi2 = new ProductInstance(pr2,
                ProductInstance.Status.INSTALLED, vm, "vdc");
        
        products = Arrays.asList(pi1, pi2);
        
        envInstance = new EnvironmentInstance(environment, products);
        
        environmentInstanceDao = mock(EnvironmentInstanceDao.class);
        when(environmentInstanceDao.create(Mockito
           .any(EnvironmentInstance.class))).thenReturn(envInstance);
        when(environmentInstanceDao.load(Mockito
                .any(Long.class))).thenReturn(envInstance);
        
        environmentDao = mock(EnvironmentDao.class);
        when(environmentDao.create(Mockito
           .any(Environment.class))).thenReturn(environment);
        when(environmentDao.update(Mockito
                .any(Environment.class))).thenReturn(environment);
        when(environmentDao.load(Mockito.any(String.class)))
        		.thenReturn(environment);
        
        productInstanceDao = mock(ProductInstanceDao.class);
        when(productInstanceDao.create(pi1)).thenReturn(pi1);
        when(productInstanceDao.create(pi2)).thenReturn(pi2);
        when(productInstanceDao.load(pi1.getId())).thenReturn(pi1);
        when(productInstanceDao.load(pi2.getId())).thenReturn(pi2);

        envInstanceValidator = mock(EnvironmentInstanceValidator.class);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testInsertWhenEverythingIsOk() throws Exception {
        // preparation
        EnvironmentInstanceManagerImpl envInstanceManager = new EnvironmentInstanceManagerImpl();
        envInstanceManager.setEnvironmentDao(environmentDao);
        envInstanceManager.setEnvironmentInstanceDao(environmentInstanceDao);
        envInstanceManager.setProductInstanceDao(productInstanceDao);
        envInstanceManager.setValidator(envInstanceValidator);
        
        // execution
        EnvironmentInstance installedEnvInstance = envInstanceManager.insert(
        		new EnvironmentInstance (environment, products));
        		
        // make assertions
        Assert.assertEquals(installedEnvInstance, envInstance);
        Assert.assertEquals(installedEnvInstance.getEnvironment(), environment);
        Assert.assertEquals(installedEnvInstance.getProductInstances(), products);
        //Assert.assertEquals(installedEnv.getId(), envInstance.getId());

    }
    
    /**
    *
    * @throws Exception
    */
   @Test
   public void testDeleteWhenEverythingIsOk() throws Exception {
       // preparation
       EnvironmentInstanceManagerImpl envInstanceManager = new EnvironmentInstanceManagerImpl();
       envInstanceManager.setEnvironmentDao(environmentDao);
       envInstanceManager.setEnvironmentInstanceDao(environmentInstanceDao);
       envInstanceManager.setProductInstanceDao(productInstanceDao);
       envInstanceManager.setValidator(envInstanceValidator);
       
       // execution
       envInstanceManager.delete(envInstance.getId());
       
       List<EnvironmentInstance> envInstances = envInstanceManager.findAll();
       		
       // make assertions
       Assert.assertEquals(0, envInstances.size());

   }
    
}
