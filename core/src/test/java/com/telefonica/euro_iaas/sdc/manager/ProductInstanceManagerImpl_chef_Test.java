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
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ArtifactDao;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.installator.Installator;
import com.telefonica.euro_iaas.sdc.installator.impl.InstallatorChefImpl;
import com.telefonica.euro_iaas.sdc.installator.impl.InstallatorPuppetImpl;
import com.telefonica.euro_iaas.sdc.manager.impl.ProductInstanceManagerImpl;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
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
 */
public class ProductInstanceManagerImpl_chef_Test extends TestCase {

    private SystemPropertiesProvider propertiesProvider;
    private ProductInstanceDao productInstanceDao;
    private ProductDao productDao;
    private RecipeNamingGenerator recipeNamingGenerator;
    private ChefNodeDao chefNodeDao;
    private SDCClientUtils sdcClientUtils;
    private ProductInstanceValidator piValidator;
    private Artifact artifact;
    private ProductInstanceManagerImpl manager;

    private Product product;
    private ProductInstance expectedProduct;
    private ProductRelease productRelease;
    private OS os;
    private VM host = new VM("fqn", "ip", "hostname", "domain");
    
    private String installRecipe ="Product::server";
    private String uninstallRecipe ="Product::uninstall-server";
    private String deployacrecipe ="Product::deployac";
    
    public final static String EXECUTE_COMMAND = "/opt/sdc/scripts/executeRecipes.sh root@hostnamedomain";
    public final static String ASSIGN_UNINSTALL_COMMAND = "/opt/sdc/scripts/assignRecipes.sh hostnamedomain Product::uninstall-server";

    private String jsonFilePath = "src/test/resources/Chefnode.js";
    private String jsonFromFile;
    
    @Before
    public void setUp() throws Exception {
        jsonFromFile = getFile(jsonFilePath);
        
        recipeNamingGenerator = mock(RecipeNamingGenerator.class);
        when(recipeNamingGenerator.getInstallRecipe(any(ProductInstance.class))).thenReturn(installRecipe);
        when(recipeNamingGenerator.getUninstallRecipe(any(ProductInstance.class))).thenReturn(uninstallRecipe);
        when(recipeNamingGenerator.getDeployArtifactRecipe(any(ProductInstance.class))).thenReturn(deployacrecipe);

        propertiesProvider = mock(SystemPropertiesProvider.class);
        os = new OS("os1", "1", "os1 description", "v1");
        host.setOsType(os.getOsType());

        sdcClientUtils = mock(SDCClientUtils.class);
        sdcClientUtils.execute(host);

        chefNodeDao = mock(ChefNodeDao.class);

        ChefNode chefNode = new ChefNode();
        chefNode.fromJson(JSONObject.fromObject(jsonFromFile));

        chefNode.addAttribute("dd", "dd", "dd");
        chefNode.addAttribute(installRecipe, "dd", "dd");
        chefNode.addRecipe(installRecipe);
        
        when(chefNodeDao.loadNode(any(String.class),any(String.class))).thenReturn(chefNode);
        when(chefNodeDao.updateNode((ChefNode) anyObject(),any(String.class))).thenReturn(chefNode);
        when(chefNodeDao.loadNodeFromHostname(any(String.class),any(String.class))).thenReturn(chefNode);
        Mockito.doNothing().when(chefNodeDao).isNodeRegistered(any(String.class),any(String.class)); 
               
        product = new Product("Product::server", "description");
        Metadata metadata=new Metadata("installator", "chef");
        List<Metadata>metadatas = new ArrayList<Metadata>();
        metadatas.add(metadata);
        product.setMetadatas(metadatas);
        productRelease = new ProductRelease();
        productRelease.setProduct(product);

        expectedProduct = new ProductInstance(productRelease, Status.INSTALLED, host, "vdc");

        productInstanceDao = mock(ProductInstanceDao.class);
        /*
         * when(productInstanceDao.create(any(ProductInstance.class))).thenReturn( expectedProduct);
         */
        when(productInstanceDao.update(any(ProductInstance.class))).thenReturn(expectedProduct);
        when(productInstanceDao.findUniqueByCriteria(any(ProductInstanceSearchCriteria.class))).thenThrow(
                new NotUniqueResultException());

        productDao = mock(ProductDao.class);
        when(productDao.create(any(Product.class))).thenReturn(product);
        when(productDao.update(any(Product.class))).thenReturn(product);
        when(productDao.load(any(String.class))).thenReturn(product);

        /*
         * when(productDao.findUniqueByCriteria( any(ProductSearchCriteria.class))) .thenThrow(new
         * NotUniqueResultException());
         */
        piValidator = mock(ProductInstanceValidator.class);
        
        Installator puppetInstallator= mock(InstallatorPuppetImpl.class);
        Installator chefInstallator= mock(InstallatorChefImpl.class);

        artifact = new Artifact();
        artifact.setName("artifact");
        Attributes att = new Attributes();
        att.getAttributes().add(new Attribute("key1", "value1"));
        att.getAttributes().add(new Attribute("key2", "value2"));
        artifact.setAttributes(att);
        artifact.setName("artifact");

        manager = new ProductInstanceManagerImpl();
        manager.setProductInstanceDao(productInstanceDao);
        manager.setProductDao(productDao);
//        manager.setRecipeNamingGenerator(recipeNamingGenerator);
//        manager.setChefNodeDao(chefNodeDao);
//        manager.setSdcClientUtils(sdcClientUtils);
        manager.setValidator(piValidator);
        manager.setChefInstallator(chefInstallator);
        manager.setPuppetInstallator(puppetInstallator);
    }

    @Test
    public void testInstallWhenEverithingIsOk() throws Exception {

        Mockito.doThrow(new EntityNotFoundException(ProductInstance.class, "test", expectedProduct))
                .when(productInstanceDao).load(any(String.class));

        expectedProduct = new ProductInstance(productRelease, Status.INSTALLED, host, "vdc");
        when(productInstanceDao.create(any(ProductInstance.class))).thenReturn(expectedProduct);
        ProductInstance installedProduct = manager.install(host, "vdc", productRelease, new ArrayList<Attribute>(), "token");
        // make verifications
        assertEquals(expectedProduct, installedProduct);

        //verify(recipeNamingGenerator, times(1)).getInstallRecipe(any(ProductInstance.class));
        // only one prodcut will be installed, the other one causes error.

        verify(productInstanceDao, times(1)).create(any(ProductInstance.class));
        // verify(productInstanceDao, times(1)).findUniqueByCriteria(
        // any(ProductInstanceSearchCriteria.class));
        verify(productInstanceDao, times(2)).update(any(ProductInstance.class));

        // verify(chefNodeDao, times(1)).loadNode(host.getChefClientName());
        // verify(chefNodeDao, times(1)).updateNode((ChefNode) anyObject());
        verify(piValidator, times(1)).validateInstall(expectedProduct);
    }

    @Test
    public void testUninstallWhenEverithingIsOk() throws Exception {

        Mockito.doThrow(new EntityNotFoundException(ProductInstance.class, "test", expectedProduct))
                .when(productInstanceDao).load(any(String.class));
        manager.uninstall(expectedProduct, "token");

//        verify(recipeNamingGenerator, times(1)).getUninstallRecipe(any(ProductInstance.class));
        // only one prodcut will be installed, the other one causes error.
        verify(productInstanceDao, times(0)).create(any(ProductInstance.class));
        verify(productInstanceDao, times(2)).update(any(ProductInstance.class));

        // verify(chefNodeDao, times(1)).loadNode(host.getChefClientName());
        // verify(chefNodeDao, times(1)).updateNode((ChefNode) anyObject());
        //verify(sdcClientUtils, times(2)).execute(host);
        verify(piValidator, times(1)).validateUninstall(expectedProduct);
        assertEquals("Result", expectedProduct.getStatus(), Status.UNINSTALLED);
    }

    @Test
    public void testInstallAndUninstall() throws Exception {
        Mockito.doThrow(new EntityNotFoundException(ProductInstance.class, "test", expectedProduct))
                .when(productInstanceDao).load(any(String.class));

        expectedProduct = new ProductInstance(productRelease, Status.INSTALLED, host, "vdc");
        when(productInstanceDao.create(any(ProductInstance.class))).thenReturn(expectedProduct);
        ProductInstance installedProduct = manager.install(host, "vdc", productRelease, new ArrayList<Attribute>(), "token");

        // make verifications
        assertEquals(installedProduct.getStatus(), Status.INSTALLED);

        manager.uninstall(installedProduct, "token");
        assertEquals(installedProduct.getStatus(), Status.UNINSTALLED);

    }

//    @Test
//    public void createProductInstance() throws Exception {
//
//        ProductInstance installedProduct = manager.createProductInstance(productRelease, host, "vdc",
//                new ArrayList<Attribute>());
//
//    }

    @Test
    public void testUnInstallAndInstall() throws Exception {

        expectedProduct = new ProductInstance(productRelease, Status.INSTALLED, host, "vdc");
        when(productInstanceDao.load(any(String.class))).thenReturn(expectedProduct);

        when(productInstanceDao.update(any(ProductInstance.class))).thenReturn(expectedProduct);

        manager.uninstall(expectedProduct, "token");
        assertEquals(expectedProduct.getStatus(), Status.UNINSTALLED);

        when(productInstanceDao.load(any(String.class))).thenReturn(expectedProduct);

        expectedProduct = manager.install(host, "vdc", productRelease, new ArrayList<Attribute>(), "token");
        assertEquals(expectedProduct.getStatus(), Status.INSTALLED);

    }

    @Test
    public void testInstallProductNotValidStatus() throws Exception {

        expectedProduct = new ProductInstance(productRelease, Status.UNINSTALLING, host, "vdc");

        when(productInstanceDao.load(any(String.class))).thenReturn(expectedProduct);
        boolean thrown = false;
        try {
            ProductInstance installedProduct = manager.install(host, "vdc", productRelease, new ArrayList<Attribute>(), "token");
        } catch (InvalidInstallProductRequestException e) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    @Test
    public void testProductAlreadyInstalled() throws Exception {

        expectedProduct = new ProductInstance(productRelease, Status.INSTALLED, host, "vdc");

        when(productInstanceDao.load(any(String.class))).thenReturn(expectedProduct);

        boolean thrown = false;
        try {
            ProductInstance installedProduct = manager.install(host, "vdc", productRelease, new ArrayList<Attribute>(), "token");
        } catch (AlreadyInstalledException e) {
            thrown = true;
        }

        assertTrue(thrown);
    }
    
    private String getFile(String file) throws IOException {
        File f = new File(file);
        System.out.println(f.isFile() + " " + f.getAbsolutePath());

        InputStream dd = new FileInputStream(f);

        BufferedReader reader = new BufferedReader(new InputStreamReader(dd));
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        while ((actualString = reader.readLine()) != null) {
          ruleFile.append(actualString).append("\n");
        }
        return ruleFile.toString();
      }
}
