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
import java.util.Arrays;

import junit.framework.TestCase;

import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.sdc.dao.ArtifactDao;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.installator.Installator;
import com.telefonica.euro_iaas.sdc.installator.impl.InstallatorChefImpl;
import com.telefonica.euro_iaas.sdc.manager.impl.ArtifactManagerChefImpl;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
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
public class ArtifactManagerChefImplTest extends TestCase {

    private SystemPropertiesProvider propertiesProvider;
    private ProductInstanceDao productInstanceDao;
    private ProductDao productDao;
    private RecipeNamingGenerator recipeNamingGenerator;
    private ChefNodeDao chefNodeDao;
    private ArtifactDao artifactDao;
    private SDCClientUtils sdcClientUtils;
    private ProductInstanceValidator piValidator;
    private Artifact artifact;
    private Installator installator;

    private Product product;
    private ProductInstance expectedProduct;
    private ProductRelease productRelease;
    private OS os;
    private VM host = new VM("fqn", "ip", "hostname", "domain");
    
    private String installRecipe ="Product::server";
    private String uninstallRecipe ="Product::uninstall-server";
    private String deployacrecipe ="Product::deployac";
    private String undeployacrecipe ="Product::test";
    
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
        when(recipeNamingGenerator.getUnDeployArtifactRecipe(any(ProductInstance.class))).thenReturn(undeployacrecipe);
        
        propertiesProvider = mock(SystemPropertiesProvider.class);
        os = new OS("os1", "1", "os1 description", "v1");
        host.setOsType(os.getOsType());

        sdcClientUtils = mock(SDCClientUtils.class);
        sdcClientUtils.execute(host);

        chefNodeDao = mock(ChefNodeDao.class);
        artifactDao = mock(ArtifactDao.class);

        ChefNode chefNode = new ChefNode();
        chefNode.fromJson(JSONObject.fromObject(jsonFromFile));
        
        chefNode.addAttribute("dd", "dd", "dd");
        chefNode.addAttribute(deployacrecipe, "dd", "dd");
        chefNode.addAttribute(undeployacrecipe, "dd", "dd");
        
        chefNode.addRecipe(deployacrecipe);
        chefNode.addRecipe(undeployacrecipe);
        when (chefNodeDao.loadNode(any(String.class),any(String.class))).thenReturn(chefNode);
       // when(chefNodeDao.loadNode(host.getChefClientName(),any(String.class))).thenReturn(chefNode);
        when(chefNodeDao.loadNodeFromHostname(any(String.class),any(String.class))).thenReturn(chefNode);
        when(chefNodeDao.updateNode((ChefNode) anyObject(),any(String.class))).thenReturn(chefNode);
        Mockito.doNothing().when(chefNodeDao).isNodeRegistered(any(String.class),any(String.class)); 
        
        product = new Product("Product::server", "description");
        productRelease = new ProductRelease("version", "releaseNotes", product, Arrays.asList(os), null);

        expectedProduct = new ProductInstance(productRelease, Status.INSTALLED, host, "vdc");

        productInstanceDao = mock(ProductInstanceDao.class);
        when(productInstanceDao.create(any(ProductInstance.class))).thenReturn(expectedProduct);
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
        
        installator=mock(InstallatorChefImpl.class);

        artifact = new Artifact();
        artifact.setName("artifact");
        Attributes att = new Attributes();
        att.getAttributes().add(new Attribute("key1", "value1"));
        att.getAttributes().add(new Attribute("key2", "value2"));
        artifact.setAttributes(att);
        artifact.setName("artifact");
    }

    @Test
    public void testDeployACWhenEverithingIsOk() throws Exception {
        ArtifactManagerChefImpl manager = new ArtifactManagerChefImpl();
        manager.setProductInstanceDao(productInstanceDao);
        manager.setPropertiesProvider(propertiesProvider);
        manager.setRecipeNamingGenerator(recipeNamingGenerator);
        manager.setChefNodeDao(chefNodeDao);
        manager.setSdcClientUtils(sdcClientUtils);
        manager.setValidator(piValidator);
        manager.setArtifactDao(artifactDao);
        manager.setInstallator(installator);

        product = new Product("Product::test", "description");
        productRelease = new ProductRelease("version", "releaseNotes", product, 
            Arrays.asList(os), null);

        expectedProduct = new ProductInstance(productRelease, Status.INSTALLED, host, "vdc");

        artifact = new Artifact();
        artifact.setName("artifact2");
        Attributes att = new Attributes();
        att.getAttributes().add(new Attribute("key1", "value1"));
        att.getAttributes().add(new Attribute("key2", "value2"));
        artifact.setAttributes(att);

        manager.deployArtifact(expectedProduct, artifact, "token");

        //verify(recipeNamingGenerator, times(1)).getDeployArtifactRecipe(any(ProductInstance.class));
        // only one prodcut will be installed, the other one causes error.
        verify(productInstanceDao, times(0)).create(any(ProductInstance.class));
        verify(productInstanceDao, times(1)).update(any(ProductInstance.class));

        // verify(chefNodeDao, times(1)).loadNode(host.getChefClientName());
        // verify(chefNodeDao, times(1)).updateNode((ChefNode) anyObject());
        verify(piValidator, times(1)).validateDeployArtifact(expectedProduct);

        assertEquals("Result", expectedProduct.getStatus(), Status.INSTALLED);
        assertEquals("Result", expectedProduct.getArtifacts().size(), 1);

        artifact = new Artifact();
        artifact.setName("artifact3");
        att = new Attributes();
        att.getAttributes().add(new Attribute("key1", "value1"));
        att.getAttributes().add(new Attribute("key2", "value2"));
        artifact.setAttributes(att);

        manager.deployArtifact(expectedProduct, artifact, "token");
        assertEquals("Result", expectedProduct.getStatus(), Status.INSTALLED);
        assertEquals("Result", expectedProduct.getArtifacts().size(), 1);

    }

    @Test
    public void testDeployACNotArtefact() throws Exception {
        ArtifactManagerChefImpl manager = new ArtifactManagerChefImpl();
        manager.setProductInstanceDao(productInstanceDao);
        manager.setPropertiesProvider(propertiesProvider);
        manager.setRecipeNamingGenerator(recipeNamingGenerator);
        manager.setChefNodeDao(chefNodeDao);
        manager.setSdcClientUtils(sdcClientUtils);
        manager.setValidator(piValidator);
        manager.setArtifactDao(artifactDao);
        manager.setInstallator(installator);

        product = new Product("Product::test", "description");
        productRelease = new ProductRelease("version", "releaseNotes", product, 
            Arrays.asList(os), null);

        expectedProduct = new ProductInstance(productRelease, Status.INSTALLED, host, "vdc");

        artifact = new Artifact();
        artifact.setName("artifact2");

        manager.deployArtifact(expectedProduct, artifact, "token");

//        verify(recipeNamingGenerator, times(1)).getDeployArtifactRecipe(any(ProductInstance.class));
        verify(productInstanceDao, times(0)).create(any(ProductInstance.class));
        verify(productInstanceDao, times(1)).update(any(ProductInstance.class));
        verify(piValidator, times(1)).validateDeployArtifact(expectedProduct);

        assertEquals("Result", expectedProduct.getStatus(), Status.INSTALLED);
        assertEquals("Result", expectedProduct.getArtifacts().size(), 1);

    }

    @Test
    public void testUndeployAC() throws Exception {
        ArtifactManagerChefImpl manager = new ArtifactManagerChefImpl();
        manager.setProductInstanceDao(productInstanceDao);
        manager.setPropertiesProvider(propertiesProvider);
        manager.setRecipeNamingGenerator(recipeNamingGenerator);
        manager.setChefNodeDao(chefNodeDao);
        manager.setSdcClientUtils(sdcClientUtils);
        manager.setValidator(piValidator);
        manager.setArtifactDao(artifactDao);
        manager.setInstallator(installator);

        product = new Product("Product::test", "description");
        productRelease = new ProductRelease("version", "releaseNotes", product, 
            Arrays.asList(os), null);

        expectedProduct = new ProductInstance(productRelease, Status.INSTALLED, host, "vdc");

        artifact = new Artifact();
        artifact.setName("artifact2");
        expectedProduct.addArtifact(artifact);

        manager.undeployArtifact(expectedProduct, "artifact2", "token");

        assertEquals("Result", expectedProduct.getStatus(), Status.INSTALLED);
        assertEquals("Result", expectedProduct.getArtifacts().size(), 0);

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
