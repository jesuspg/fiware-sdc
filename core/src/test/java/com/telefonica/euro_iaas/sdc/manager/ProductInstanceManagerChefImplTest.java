package com.telefonica.euro_iaas.sdc.manager;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.manager.impl.ProductInstanceManagerChefImpl;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.RecipeNamingGenerator;
import com.telefonica.euro_iaas.sdc.util.SDCClientUtils;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.validation.ProductInstanceValidator;

/**
 * Unit test suite for ProductManagerChefImpl.
 *
 * @author Sergio Arroyo
 *
 */
public class ProductInstanceManagerChefImplTest {

    private SystemPropertiesProvider propertiesProvider;
    private ProductInstanceDao productInstanceDao;
    private RecipeNamingGenerator recipeNamingGenerator;
    private ChefNodeDao chefNodeDao;
    private SDCClientUtils sdcClientUtils;
    private ProductInstanceValidator piValidator;

    private Product product;
    private ProductInstance expectedProduct;
    private ProductRelease productRelease;
    private OS os;
    private VM host = new VM("hostname", "domain");

    public final static String EXECUTE_COMMAND =
        "/opt/sdc/scripts/executeRecipes.sh root@hostnamedomain";
    public final static String ASSIGN_UNINSTALL_COMMAND =
        "/opt/sdc/scripts/assignRecipes.sh hostnamedomain Product::uninstall-server";



    @Before
    public void setUp() throws Exception {
        recipeNamingGenerator = mock(RecipeNamingGenerator.class);
        when(recipeNamingGenerator.getInstallRecipe(
                any(ProductInstance.class))).thenReturn("Product::server");
        when(recipeNamingGenerator.getUninstallRecipe(
                any(ProductInstance.class))).thenReturn("Product::uninstall-server");

        propertiesProvider = mock(SystemPropertiesProvider.class);

        sdcClientUtils = mock(SDCClientUtils.class);
        sdcClientUtils.execute(host);


        chefNodeDao = mock(ChefNodeDao.class);
        when(chefNodeDao.loadNode(host))
                .thenReturn(new ChefNode());
        when(chefNodeDao.updateNode((ChefNode)anyObject()))
        .thenReturn(new ChefNode());


        os = new OS("os1", "os1 description", "v1");
        product = new Product("Product::server", "description");
        productRelease = new ProductRelease(
                "version", "releaseNotes", null, product, Arrays.asList(os), null);

        expectedProduct = new ProductInstance(
                productRelease, Status.INSTALLED, host);

        productInstanceDao = mock(ProductInstanceDao.class);
        when(productInstanceDao.create(any(ProductInstance.class))).thenReturn(
                expectedProduct);
        when(productInstanceDao.update(any(ProductInstance.class))).thenReturn(
                expectedProduct);
        when(productInstanceDao.findUniqueByCriteria(
                any(ProductInstanceSearchCriteria.class)))
                .thenThrow(new NotUniqueResultException());
        piValidator = mock(ProductInstanceValidator.class);
    }

    @Test
    public void testInstallWhenEverithingIsOk() throws Exception {
        ProductInstanceManagerChefImpl manager = new ProductInstanceManagerChefImpl();
        manager.setProductInstanceDao(productInstanceDao);
        manager.setPropertiesProvider(propertiesProvider);
        manager.setRecipeNamingGenerator(recipeNamingGenerator);
        manager.setChefNodeDao(chefNodeDao);
        manager.setSdcClientUtils(sdcClientUtils);
        manager.setValidator(piValidator);


        ProductInstance installedProduct = manager.install(
                host, productRelease, new ArrayList<Attribute>());
        // make verifications
        assertEquals(expectedProduct, installedProduct);

        verify(recipeNamingGenerator, times(1)).getInstallRecipe(
                any(ProductInstance.class));
        // only one prodcut will be installed, the other one causes error.



        verify(productInstanceDao, times(1)).create(
                any(ProductInstance.class));
        verify(productInstanceDao, times(1)).findUniqueByCriteria(
                any(ProductInstanceSearchCriteria.class));
        verify(productInstanceDao, times(1)).update(any(ProductInstance.class));

        verify(chefNodeDao, times(2)).loadNode(host);
        verify(chefNodeDao, times(2)).updateNode((ChefNode)anyObject());
        verify(piValidator, times(1)).validateInstall(expectedProduct);


    }

    @Test
    public void testUninstallWhenEverithingIsOk() throws Exception {
        ProductInstanceManagerChefImpl manager = new ProductInstanceManagerChefImpl();
        manager.setProductInstanceDao(productInstanceDao);
        manager.setPropertiesProvider(propertiesProvider);
        manager.setRecipeNamingGenerator(recipeNamingGenerator);
        manager.setChefNodeDao(chefNodeDao);
        manager.setSdcClientUtils(sdcClientUtils);
        manager.setValidator(piValidator);


        manager.uninstall(expectedProduct);

        verify(recipeNamingGenerator, times(1)).getUninstallRecipe(
                any(ProductInstance.class));
        // only one prodcut will be installed, the other one causes error.
        verify(productInstanceDao, times(0)).create(any(ProductInstance.class));
        verify(productInstanceDao, times(2)).update(any(ProductInstance.class));

        verify(chefNodeDao, times(2)).loadNode(host);
        verify(chefNodeDao, times(2)).updateNode((ChefNode)anyObject());
        verify(sdcClientUtils, times(2)).execute(host);
        verify(piValidator, times(1)).validateUninstall(expectedProduct);

    }
}
