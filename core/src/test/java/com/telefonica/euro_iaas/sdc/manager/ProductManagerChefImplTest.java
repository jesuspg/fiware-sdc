package com.telefonica.euro_iaas.sdc.manager;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.ASSING_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.EXECUTE_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.INSTALL_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UNASSING_RECIPES_SCRIPT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UNINSTALL_RECIPE_TEMPLATE;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import junit.framework.TestCase;

import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.manager.impl.ProductInstanceManagerChefImpl;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.CommandExecutor;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * Unit test suite for ProductManagerChefImpl.
 *
 * @author Sergio Arroyo
 *
 */
public class ProductManagerChefImplTest extends TestCase {

    private SystemPropertiesProvider propertiesProvider;
    private ProductInstanceDao productInstanceDao;
    private CommandExecutor commandExecutor;


    private Product product;
    private ProductInstance expectedProduct;
    private OS os;
    private VM host = new VM("hostname", "domain");

    public final static String EXECUTE_COMMAND =
        "/opt/sdc/scripts/executeRecipes.sh root@hostnamedomain";
    public final static String ASSIGN_COMMAND =
        "/opt/sdc/scripts/assignRecipes.sh hostnamedomain Product::server";
    public final static String UNASSIGN_COMMAND =
        "/opt/sdc/scripts/unassignRecipes.sh hostnamedomain Product::server";

    public final static String ASSIGN_UNINSTALL_COMMAND =
        "/opt/sdc/scripts/assignRecipes.sh hostnamedomain Product::uninstall-server";
    public final static String UNASSIGN_UNINSTALL_COMMAND =
        "/opt/sdc/scripts/unassignRecipes.sh hostnamedomain Product::uninstall-server";

    @Override
    public void setUp() throws Exception {
        propertiesProvider = mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty(INSTALL_RECIPE_TEMPLATE))
                .thenReturn("{0}");
        when(propertiesProvider.getProperty(UNINSTALL_RECIPE_TEMPLATE))
                .thenReturn("{0}::uninstall");
        when(propertiesProvider.getProperty(ASSING_RECIPES_SCRIPT)).thenReturn(
                "/opt/sdc/scripts/assignRecipes.sh {0} {1}");
        when(propertiesProvider.getProperty(UNASSING_RECIPES_SCRIPT))
                .thenReturn("/opt/sdc/scripts/unassignRecipes.sh {0} {1}");
        when(propertiesProvider.getProperty(EXECUTE_RECIPES_SCRIPT))
                .thenReturn("/opt/sdc/scripts/executeRecipes.sh root@{0}");

        commandExecutor = mock(CommandExecutor.class);
        when(commandExecutor.executeCommand(ASSIGN_COMMAND))
                .thenReturn(new String[2]);
        when(commandExecutor.executeCommand(
                EXECUTE_COMMAND))
                .thenReturn(new String[2]);
        when(commandExecutor.executeCommand(UNASSIGN_COMMAND))
                .thenReturn(new String[2]);


        os = new OS("os1", "os1 description", "v1");
        product = new Product("Product::server", "Version", Arrays.asList(os));

        expectedProduct = new ProductInstance(product, Status.INSTALLED, host);

        productInstanceDao = mock(ProductInstanceDao.class);
        when(productInstanceDao.create(any(ProductInstance.class))).thenReturn(
                expectedProduct);
        when(productInstanceDao.update(any(ProductInstance.class))).thenReturn(
                expectedProduct);
    }

    public void testInstallWhenEverithingIsOk() throws Exception {
        ProductInstanceManagerChefImpl manager = new ProductInstanceManagerChefImpl();
        manager.setProductInstanceDao(productInstanceDao);
        manager.setPropertiesProvider(propertiesProvider);
        manager.setCommandExecutor(commandExecutor);

        ProductInstance installedProduct = manager.install(host, product);
        // make verifications
        assertEquals(expectedProduct, installedProduct);

        verify(propertiesProvider, times(1)).getProperty(
                INSTALL_RECIPE_TEMPLATE);
        verify(propertiesProvider, times(1))
                .getProperty(ASSING_RECIPES_SCRIPT);
        verify(propertiesProvider, times(1))
                .getProperty(UNASSING_RECIPES_SCRIPT);
        // only one prodcut will be installed, the other one causes error.

        verify(propertiesProvider, times(1))
                .getProperty(EXECUTE_RECIPES_SCRIPT);
        verify(propertiesProvider, times(0)).getProperty(
                UNINSTALL_RECIPE_TEMPLATE);

        verify(productInstanceDao, times(1)).create(
                any(ProductInstance.class));
        verify(productInstanceDao, times(0)).update(any(ProductInstance.class));

        verify(commandExecutor, times(1)).executeCommand(ASSIGN_COMMAND);
        verify(commandExecutor, times(1)).executeCommand(EXECUTE_COMMAND);
        verify(commandExecutor, times(1)).executeCommand(UNASSIGN_COMMAND);

    }

    public void testUninstallWhenEverithingIsOk() throws Exception {
        ProductInstanceManagerChefImpl manager = new ProductInstanceManagerChefImpl();
        manager.setProductInstanceDao(productInstanceDao);
        manager.setPropertiesProvider(propertiesProvider);
        manager.setCommandExecutor(commandExecutor);

        manager.uninstall(expectedProduct);

        verify(propertiesProvider, times(0)).getProperty(
                INSTALL_RECIPE_TEMPLATE);
        verify(propertiesProvider, times(1)).getProperty(ASSING_RECIPES_SCRIPT);
        // only one prodcut will be installed, the other one causes error.

        verify(propertiesProvider, times(1))
                .getProperty(EXECUTE_RECIPES_SCRIPT);
        verify(propertiesProvider, times(1)).getProperty(
                UNINSTALL_RECIPE_TEMPLATE);
        verify(propertiesProvider, times(1)).getProperty(
                UNASSING_RECIPES_SCRIPT);

        verify(productInstanceDao, times(0)).create(any(ProductInstance.class));
        verify(productInstanceDao, times(1)).update(any(ProductInstance.class));

        verify(commandExecutor, times(1)).executeCommand(ASSIGN_UNINSTALL_COMMAND);
        verify(commandExecutor, times(1)).executeCommand(EXECUTE_COMMAND);
        verify(commandExecutor, times(1)).executeCommand(UNASSIGN_UNINSTALL_COMMAND);
    }
}
