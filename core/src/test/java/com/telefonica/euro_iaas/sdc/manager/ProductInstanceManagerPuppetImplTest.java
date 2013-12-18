/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.manager;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ArtifactDao;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.manager.impl.ProductInstanceManagerPuppetImpl;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.RecipeNamingGenerator;
import com.telefonica.euro_iaas.sdc.util.SDCClientUtils;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.validation.ProductInstanceValidator;

/**
 * Unit test suite for ProductManagerPuppetImpl.
 * 
 * @author alberts
 */

// @Ignore
public class ProductInstanceManagerPuppetImplTest extends TestCase {

    private ProductInstanceManagerPuppetImpl productManager;

    private OS os;
    private Product product;
    private ProductRelease productRelease;
    private VM host;
    private SystemPropertiesProvider propertiesProvider;
    private HttpClient client;
    private ProductInstanceDao productInstanceDao;
    private ProductDao productDao;
    private RecipeNamingGenerator recipeNamingGenerator;
    private ChefNodeDao chefNodeDao;
    private ArtifactDao artifactDao;
    private SDCClientUtils sdcClientUtils;
    private ProductInstanceValidator piValidator;
    private ProductInstance expectedProduct;

    @Before
    public void setUp() throws Exception {

        product = new Product("testProduct", "description");

        host = new VM("fqn", "ip", "testName", "domain");
        
        os = new OS("os1", "1", "os1 description", "v1");
        host.setOsType(os.getOsType());

        propertiesProvider = mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty("PUPPET_MASTER_URL")).thenReturn(
                "http://130.206.82.190:8080/puppetwrapper/");

        client = (HttpClient) new HTTPClientMock();
        HttpPost post = new HttpPost(propertiesProvider.getProperty("PUPPET_MASTER_URL")
                + "install/testGroup/testName/testProduct/testVersion");

        HttpResponse response = mock(HttpResponse.class);
        response.setStatusCode(HttpStatus.SC_OK);

        productInstanceDao = mock(ProductInstanceDao.class);
        
        productRelease = new ProductRelease("version", "releaseNotes", product, Arrays.asList(os), null);
        expectedProduct = new ProductInstance(productRelease, Status.INSTALLED, host, "vdc");

        when(productInstanceDao.update(any(ProductInstance.class))).thenReturn(expectedProduct);
        when(productInstanceDao.findUniqueByCriteria(any(ProductInstanceSearchCriteria.class))).thenThrow(
                new NotUniqueResultException());

        productDao = mock(ProductDao.class);
        when(productDao.create(any(Product.class))).thenReturn(product);
        when(productDao.update(any(Product.class))).thenReturn(product);
        when(productDao.load(any(String.class))).thenReturn(product);

        /*
         * when(productDao.findUniqueByCriteria(
         * any(ProductSearchCriteria.class))) .thenThrow(new
         * NotUniqueResultException());
         */
        piValidator = mock(ProductInstanceValidator.class);

        productManager = new ProductInstanceManagerPuppetImpl();
        productManager.setPropertiesProvider(propertiesProvider);
        productManager.setProductInstanceDao(productInstanceDao);
        productManager.setProductDao(productDao);
        // productManager.setRecipeNamingGenerator(recipeNamingGenerator);
//        productManager.setSdcClientUtils(sdcClientUtils);
        productManager.setValidator(piValidator);
        productManager.setClient(client);

    }

    @Test
    public void testInstallWhenEverythingIsOk() throws Exception {

        Mockito.doThrow(new EntityNotFoundException(ProductInstance.class, "test", expectedProduct))
                .when(productInstanceDao).load(any(String.class));

        expectedProduct = new ProductInstance(productRelease, Status.INSTALLED, host, "vdc");
        when(productInstanceDao.create(any(ProductInstance.class))).thenReturn(expectedProduct);
        ProductInstance installedProduct = productManager.install(host, "vdc", productRelease, new ArrayList<Attribute>());
        // make verifications
        assertEquals(expectedProduct, installedProduct);


    }

}
