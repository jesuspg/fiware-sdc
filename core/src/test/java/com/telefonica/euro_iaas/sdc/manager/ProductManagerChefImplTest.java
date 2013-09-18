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
import java.util.List;

import junit.framework.TestCase;

import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.manager.impl.ProductInstanceManagerChefImpl;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
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

    private Product product;
    private Product invalidProduct;
    private ProductInstance expectedProduct;
    private OS os;
    private VM host = new VM("hostname", "description");

    @Override
    public void setUp() throws Exception {
        propertiesProvider = mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty(INSTALL_RECIPE_TEMPLATE))
                .thenReturn("install-template");
        when(propertiesProvider.getProperty(UNINSTALL_RECIPE_TEMPLATE))
                .thenReturn("uninstall-template");
        when(propertiesProvider.getProperty(ASSING_RECIPES_SCRIPT)).thenReturn(
                "ls");
        when(propertiesProvider.getProperty(UNASSING_RECIPES_SCRIPT))
                .thenReturn("ls");
        when(propertiesProvider.getProperty(EXECUTE_RECIPES_SCRIPT))
                .thenReturn("ls");

        os = new OS("os1", "os1 description", "v1");
        product = new Product("Product", "Version", Arrays.asList(os));
        invalidProduct = new Product("Product2", "Version2", Arrays
                .asList(new OS("os2", "os2Desc", "v2")));
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

        List<ProductInstance> installedProducts = manager.install(host, Arrays
                .asList(product, invalidProduct));
        // make verifications
        assertEquals(2, installedProducts.size());
        assertEquals(expectedProduct, installedProducts.get(0));

        verify(propertiesProvider, times(1)).getProperty(
                INSTALL_RECIPE_TEMPLATE);
        verify(propertiesProvider, times(installedProducts.size()))
                .getProperty(ASSING_RECIPES_SCRIPT);
        verify(propertiesProvider, times(installedProducts.size()))
                .getProperty(UNASSING_RECIPES_SCRIPT);
        // only one prodcut will be installed, the other one causes error.

        verify(propertiesProvider, times(1))
                .getProperty(EXECUTE_RECIPES_SCRIPT);
        verify(propertiesProvider, times(0)).getProperty(
                UNINSTALL_RECIPE_TEMPLATE);

        verify(productInstanceDao, times(installedProducts.size())).create(
                any(ProductInstance.class));
        verify(productInstanceDao, times(0)).update(any(ProductInstance.class));
    }

    public void testUninstallWhenEverithingIsOk() throws Exception {
        ProductInstanceManagerChefImpl manager = new ProductInstanceManagerChefImpl();
        manager.setProductInstanceDao(productInstanceDao);
        manager.setPropertiesProvider(propertiesProvider);

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
    }
}
