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

import java.util.Arrays;
import java.util.List;

import com.telefonica.euro_iaas.sdc.dao.EnvironmentDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.manager.impl.EnvironmentManagerImpl;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.validation.EnvironmentValidator;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test suite for EnvironmentManagerImplTest.
 * 
 * @author Jesus M. Movilla
 */
public class EnvironmentManagerImplTest {

    private EnvironmentDao environmentDao;
    private ProductDao productDao;
    private ProductReleaseDao productReleaseDao;

    private EnvironmentValidator envValidator;

    private Product product;
    private Environment expectedEnvironment;
    private ProductRelease productRelease;
    private OS os;
    private VM host = new VM("fqn", "ip", "hostname", "domain");

    @Before
    public void setUp() throws Exception {
        os = new OS("os1", "1", "os1 description", "v1");
        host.setOsType(os.getOsType());

        product = new Product("Product::server", "description");
        productRelease = new ProductRelease("version", "releaseNotes", null, product, Arrays.asList(os), null);
        List<ProductRelease> productReleases = Arrays.asList(productRelease);

        expectedEnvironment = new Environment(productReleases);

        productDao = mock(ProductDao.class);
        when(productDao.load(any(String.class))).thenReturn(product);

        productReleaseDao = mock(ProductReleaseDao.class);
        when(productReleaseDao.load(any(Product.class), any(String.class))).thenReturn(productRelease);

        environmentDao = mock(EnvironmentDao.class);
        when(environmentDao.create(any(Environment.class))).thenReturn(expectedEnvironment);
        when(environmentDao.update(any(Environment.class))).thenReturn(expectedEnvironment);
        when(environmentDao.load(any(String.class))).thenReturn(expectedEnvironment);
        envValidator = mock(EnvironmentValidator.class);
    }

    @Test
    public void testInsertWhenEverythingIsOk() throws Exception {
        EnvironmentManagerImpl envManager = new EnvironmentManagerImpl();
        envManager.setEnvironmentDao(environmentDao);
        envManager.setProductDao(productDao);
        envManager.setProductReleaseDao(productReleaseDao);

        List<ProductRelease> productReleasesInside = Arrays.asList(productRelease);
        Environment installedEnvironment = envManager.insert(new Environment(productReleasesInside));
        // make verifications
        assertEquals(expectedEnvironment, installedEnvironment);
    }

    @Test
    public void testUpdateWhenEverithingIsOk() throws Exception {
        EnvironmentManagerImpl envManager = new EnvironmentManagerImpl();
        envManager.setEnvironmentDao(environmentDao);
        envManager.setProductDao(productDao);
        envManager.setProductReleaseDao(productReleaseDao);

        List<ProductRelease> productReleasesInside = Arrays.asList(productRelease);
        Environment updatedEnvironment = envManager.update(new Environment(productReleasesInside));

        // make verifications
        assertEquals(expectedEnvironment, updatedEnvironment);
    }

    @Test
    public void testDeleteWhenEverithingIsOk() throws Exception {
        EnvironmentManagerImpl envManager = new EnvironmentManagerImpl();
        envManager.setEnvironmentDao(environmentDao);
        envManager.setProductDao(productDao);
        envManager.setProductReleaseDao(productReleaseDao);

        List<ProductRelease> productReleasesInside = Arrays.asList(productRelease);
        Environment deletedEnvironment = new Environment(productReleasesInside);
        envManager.delete(deletedEnvironment.getName());

        List<Environment> environments = envManager.findAll();

        // make verifications
        assertEquals(0, environments.size());
        // verify(envValidator, times(1)).validateDelete(expectedEnvironment);
    }
}
