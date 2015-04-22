/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.manager;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.installator.Installator;
import com.telefonica.euro_iaas.sdc.installator.impl.InstallatorPuppetImpl;
import com.telefonica.euro_iaas.sdc.manager.impl.ProductInstanceManagerImpl;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.validation.ProductInstanceValidator;

/**
 * Unit test suite for ProductManagerPuppetImpl.
 * 
 * @author alberts
 */

// @Ignore
public class ProductInstanceManagerImpl_puppet_Test extends TestCase {

    private ProductInstanceManagerImpl productManager;

    private OS os;
    private Product product;
    private ProductRelease productRelease;
    private VM host;
    private SystemPropertiesProvider propertiesProvider;
    private ProductInstanceDao productInstanceDao;
    private ProductDao productDao;
    private ProductInstanceValidator piValidator;
    private ProductInstance expectedProduct;

    @Before
    public void setUp() throws Exception {

        product = new Product("testProduct", "description");
        Metadata metadata=new Metadata("installator", "puppet");
        List<Metadata>metadatas = new ArrayList<Metadata>();
        metadatas.add(metadata);
        product.setMetadatas(metadatas);

        host = new VM("fqn", "ip", "testName", "domain");
        
        os = new OS("os1", "1", "os1 description", "v1");
        host.setOsType(os.getOsType());

        propertiesProvider = mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty("PUPPET_MASTER_URL")).thenReturn(
                "http://130.206.82.190:8080/puppetwrapper/");

        
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
        
        Installator installator= mock(InstallatorPuppetImpl.class);
        

        /*
         * when(productDao.findUniqueByCriteria(
         * any(ProductSearchCriteria.class))) .thenThrow(new
         * NotUniqueResultException());
         */
        piValidator = mock(ProductInstanceValidator.class);

        productManager = new ProductInstanceManagerImpl();
        productManager.setProductInstanceDao(productInstanceDao);
        productManager.setProductDao(productDao);
        // productManager.setRecipeNamingGenerator(recipeNamingGenerator);
//        productManager.setSdcClientUtils(sdcClientUtils);
        productManager.setValidator(piValidator);
        productManager.setPuppetInstallator(installator);

    }

    @Test
    public void testInstallWhenEverythingIsOk() throws Exception {

        Mockito.doThrow(new EntityNotFoundException(ProductInstance.class, "test", expectedProduct))
                .when(productInstanceDao).load(any(String.class));

        expectedProduct = new ProductInstance(productRelease, Status.INSTALLED, host, "vdc");
        when(productInstanceDao.create(any(ProductInstance.class))).thenReturn(expectedProduct);
        ProductInstance installedProduct = productManager.install(host, "vdc", productRelease, new ArrayList<Attribute>(), "token");
        // make verifications
        assertEquals(expectedProduct, installedProduct);


    }

}
