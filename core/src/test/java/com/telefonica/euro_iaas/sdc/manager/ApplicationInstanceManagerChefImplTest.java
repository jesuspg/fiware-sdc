/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.manager.impl.ApplicationInstanceManagerChefImpl;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.RecipeNamingGenerator;
import com.telefonica.euro_iaas.sdc.util.SDCClientUtils;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.validation.ApplicationInstanceValidator;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApplicationInstanceManagerChefImplTest {

    private SystemPropertiesProvider propertiesProvider;
    private ApplicationInstanceDao applicationInstanceDao;
    private RecipeNamingGenerator recipeNamingGenerator;
    private ChefNodeDao chefNodeDao;
    private SDCClientUtils sdcClientUtils;
    private ApplicationInstanceValidator aiValidator;

    VM vm;
    List<ProductInstance> products;
    EnvironmentInstance envInstance;
    Application application;
    ApplicationRelease appRelease;
    ApplicationInstance applicationInstance;

    @Before
    public void setUp() throws Exception {

        recipeNamingGenerator = mock(RecipeNamingGenerator.class);
        when(recipeNamingGenerator.getInstallRecipe(any(ApplicationInstance.class))).thenReturn("war::app-p1-p2");

        propertiesProvider = mock(SystemPropertiesProvider.class);

        vm = new VM("ip", "hostname", "domain");

        sdcClientUtils = mock(SDCClientUtils.class);
        sdcClientUtils.execute(vm);

        Product p1 = new Product("p1", "description");
        Product p2 = new Product("p2", "description");
        ProductRelease pr1 = new ProductRelease("version1", "releaseNotes1", null, p1, null, null);
        ProductRelease pr2 = new ProductRelease("version2", "releaseNotes2", null, p2, null, null);

        Environment env1 = new Environment(Arrays.asList(pr1, pr2));

        products = Arrays.asList(new ProductInstance(pr1, ProductInstance.Status.INSTALLED, vm, "vdc"),
                new ProductInstance(pr2, ProductInstance.Status.INSTALLED, vm, "vdc"));
        application = new Application("app", "desc", "war");
        appRelease = new ApplicationRelease("version", "releaseNotes", null, application, env1, null);

        envInstance = new EnvironmentInstance(env1, products);
        applicationInstance = new ApplicationInstance(appRelease, envInstance, Status.INSTALLED, vm, "vdc");

        applicationInstanceDao = mock(ApplicationInstanceDao.class);
        when(applicationInstanceDao.create(Mockito.any(ApplicationInstance.class))).thenReturn(applicationInstance);
        when(applicationInstanceDao.update(Mockito.any(ApplicationInstance.class))).thenReturn(applicationInstance);
        when(applicationInstanceDao.findUniqueByCriteria(Mockito.any(ApplicationInstanceSearchCriteria.class)))
                .thenThrow(new NotUniqueResultException());

        chefNodeDao = mock(ChefNodeDao.class);
        when(chefNodeDao.loadNode(vm.getChefClientName())).thenReturn(new ChefNode());
        when(chefNodeDao.updateNode((ChefNode) anyObject())).thenReturn(new ChefNode());

        aiValidator = mock(ApplicationInstanceValidator.class);
    }

    /**
     * @throws Exception
     */
    @Test
    public void testInstallWhenEverythingIsOk() throws Exception {
        // preparation
        ApplicationInstanceManagerChefImpl manager = new ApplicationInstanceManagerChefImpl();
        manager.setPropertiesProvider(propertiesProvider);
        manager.setApplicationInstanceDao(applicationInstanceDao);
        manager.setRecipeNamingGenerator(recipeNamingGenerator);
        manager.setChefNodeDao(chefNodeDao);
        manager.setSdcClientUtils(sdcClientUtils);
        manager.setValidator(aiValidator);
        // execution
        ApplicationInstance installedApp = manager.install(vm, "vdc", envInstance, appRelease,
                new ArrayList<Attribute>());
        // make assertions
        Assert.assertEquals(installedApp.getApplication(), appRelease);
        Assert.assertEquals(installedApp.getEnvironmentInstance(), envInstance);
        Assert.assertEquals(installedApp.getStatus(), Status.INSTALLED);

        verify(recipeNamingGenerator, times(1)).getInstallRecipe(any(ApplicationInstance.class));

        verify(applicationInstanceDao, times(1)).create((Mockito.any(ApplicationInstance.class)));

        // verify(chefNodeDao, times(1)).loadNode(vm.getChefClientName());
        // verify(chefNodeDao, times(1)).updateNode((ChefNode) anyObject());
        verify(sdcClientUtils, times(2)).execute(vm);
        verify(aiValidator, times(1)).validateInstall(Mockito.any(ApplicationInstance.class));

    }
}
