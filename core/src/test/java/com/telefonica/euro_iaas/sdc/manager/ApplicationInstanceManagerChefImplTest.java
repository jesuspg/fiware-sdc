package com.telefonica.euro_iaas.sdc.manager;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.ASSING_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.EXECUTE_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UNASSING_RECIPES_SCRIPT;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.mockito.Mockito;

import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.manager.impl.ApplicationInstanceManagerChefImpl;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.CommandExecutor;
import com.telefonica.euro_iaas.sdc.util.RecipeNamingGenerator;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class ApplicationInstanceManagerChefImplTest extends TestCase {

    private SystemPropertiesProvider propertiesProvider;
    private ApplicationInstanceDao applicationInstanceDao;
    private CommandExecutor commandExecutor;
    private RecipeNamingGenerator recipeNamingGenerator;

    VM vm;
    List<ProductInstance> products;
    Application application;
    ApplicationRelease appRelease;
    ApplicationInstance applicationInstance;

    @Override
    public void setUp() throws Exception {

        recipeNamingGenerator = mock(RecipeNamingGenerator.class);
        when(recipeNamingGenerator.getInstallRecipe(
                any(ApplicationInstance.class))).thenReturn("war::app-p1-p2");

        propertiesProvider = mock(SystemPropertiesProvider.class);

        when(propertiesProvider.getProperty(ASSING_RECIPES_SCRIPT)).thenReturn(
                "/opt/sdc/scripts/assignRecipes.sh {0} {1}");
        when(propertiesProvider.getProperty(UNASSING_RECIPES_SCRIPT))
                .thenReturn("/opt/sdc/scripts/unassignRecipes.sh {0} {1}");
        when(propertiesProvider.getProperty(EXECUTE_RECIPES_SCRIPT))
                .thenReturn("/opt/sdc/scripts/executeRecipes.sh root@{0}");

        commandExecutor = mock(CommandExecutor.class);
        when(commandExecutor.executeCommand(
                "/opt/sdc/scripts/assignRecipes.sh hostnamedomain war::app-p1-p2"))
                .thenReturn(new String[2]);
        when(commandExecutor.executeCommand(
                "/opt/sdc/scripts/executeRecipes.sh root@ip"))
                .thenReturn(new String[2]);
        when(commandExecutor.executeCommand(
                "/opt/sdc/scripts/unassignRecipes.sh hostnamedomain war::app-p1-p2"))
                .thenReturn(new String[2]);

        vm = new VM("ip", "hostname", "domain");

        Product p1 = new Product("p1","description");
        Product p2 = new Product("p2","description");
        ProductRelease pr1 = new ProductRelease(
                "version1", "releaseNotes1", null, p1, null, null);
        ProductRelease pr2 = new ProductRelease(
                "version2", "releaseNotes2", null, p2, null, null);

        products = Arrays.asList(new ProductInstance(pr1,
                ProductInstance.Status.INSTALLED, vm), new ProductInstance(pr2,
                ProductInstance.Status.INSTALLED, vm));
        application = new Application("app", "desc", "war");
        appRelease = new ApplicationRelease(
                "version", "releaseNotes", null, application,
                Arrays.asList(pr1, pr2), null);

        applicationInstance = new ApplicationInstance(appRelease, products,
                Status.INSTALLED);

        applicationInstanceDao = mock(ApplicationInstanceDao.class);
        when(applicationInstanceDao.create(Mockito
           .any(ApplicationInstance.class))).thenReturn(applicationInstance);

    }

    /**
     *
     * @throws Exception
     */
    public void testInstallWhenEverythingIsOk() throws Exception {
        // preparation
        ApplicationInstanceManagerChefImpl manager = new ApplicationInstanceManagerChefImpl();
        manager.setPropertiesProvider(propertiesProvider);
        manager.setApplicationInstanceDao(applicationInstanceDao);
        manager.setCommandExecutor(commandExecutor);
        manager.setRecipeNamingGenerator(recipeNamingGenerator);
        // execution
        ApplicationInstance installedApp = manager.install(vm, products,
                appRelease);
        // make assertions
        assertEquals(installedApp.getApplication(), appRelease);
        assertEquals(installedApp.getProducts(), products);
        assertEquals(installedApp.getStatus(), Status.INSTALLED);

        verify(recipeNamingGenerator, times(1)).getInstallRecipe(
                any(ApplicationInstance.class));
        verify(propertiesProvider, times(1)).getProperty(ASSING_RECIPES_SCRIPT);
        verify(propertiesProvider, times(1)).getProperty(
                UNASSING_RECIPES_SCRIPT);
        verify(propertiesProvider, times(1))
                .getProperty(EXECUTE_RECIPES_SCRIPT);

        verify(applicationInstanceDao, times(1)).create(
                (Mockito.any(ApplicationInstance.class)));


        verify(commandExecutor, times(1)).executeCommand(
            "/opt/sdc/scripts/assignRecipes.sh hostnamedomain war::app-p1-p2");
        verify(commandExecutor, times(1)).executeCommand(
            "/opt/sdc/scripts/executeRecipes.sh root@ip");
        verify(commandExecutor, times(1)).executeCommand(
            "/opt/sdc/scripts/unassignRecipes.sh hostnamedomain war::app-p1-p2");

    }
}
