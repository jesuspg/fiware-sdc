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

package com.telefonica.euro_iaas.sdc.installator;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.installator.impl.InstallatorChefImpl;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.RecipeNamingGenerator;
import com.telefonica.euro_iaas.sdc.util.SDCClientUtils;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class InstallatorChefTest {

    private OS os;
    private VM host = new VM("fqn", "ip", "hostname", "domain");
    private ProductInstance productInstance;
    private ProductRelease productRelease;
    private Product product;
    private InstallatorChefImpl installator;
    private RecipeNamingGenerator recipeNamingGenerator;
    private ChefNodeDao chefNodeDao;
    private SystemPropertiesProvider propertiesProvider;
    private SDCClientUtils sdcClientUtils;
    private ChefNode chefNode;

    private String jsonFilePath = "src/test/resources/chefNodeOhaiTimeDate.js";
    private String jsonFromFile = "{\"name\":\"test\"}";

    private String initProbeRecipe = "probe::0.1_init";
    private String installProbeRecipe = "probe::0.1_install";
    private String installRecipe = "Product::server";
    private String uninstallRecipe = "Product::uninstall-server";
    private String deployacrecipe = "Product::deployac";
    private String configurerecipe = "Product::configure";
    private String undeployacrecipe = "Product::undeployac";

    @Before
    public void setup() throws CanNotCallChefException, EntityNotFoundException, IOException, NodeExecutionException,
            InterruptedException {
        os = new OS("os1", "1", "os1 description", "v1");
        host.setOsType(os.getOsType());

        product = new Product("Product", "description");
        Metadata metadata = new Metadata("installator", "chef");
        List<Metadata> metadatas = new ArrayList<Metadata>();
        metadatas.add(metadata);
        product.setMetadatas(metadatas);
        productRelease = new ProductRelease();
        productRelease.setProduct(product);

        productInstance = new ProductInstance(productRelease, Status.INSTALLED, host, "vdc");
        productInstance.setProductRelease(productRelease);

        sdcClientUtils = mock(SDCClientUtils.class);
        sdcClientUtils.execute(host);

        // jsonFromFile = getFile(jsonFilePath);
        chefNode = new ChefNode();
        chefNode.setName("a");
        // chefNode.fromJson(JSONObject.fromObject(jsonFromFile));

        chefNode.addAttribute("dd", "dd", "dd");
        chefNode.addAttribute(installRecipe, "dd", "dd");
        chefNode.addAttribute("action", "action_probe", "init");
        chefNode.addAutomaticAttritube("platform", "value");
        chefNode.addAutomaticAttritube("platform_version", "value");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z", Locale.US);
        Date future = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(future);
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.YEAR, 2020);
        future = calendar.getTime();

        chefNode.addAutomaticAttritube("ohai_time", df.format(future));
        // chefNode.addRecipe(initProbeRecipe);
        chefNode.addRecipe(installRecipe);
        // chefNode.addRecipe(installProbeRecipe);

        chefNodeDao = mock(ChefNodeDao.class);
        when(chefNodeDao.loadNode(any(String.class), any(String.class))).thenReturn(chefNode);
        when(chefNodeDao.updateNode((ChefNode) anyObject(), any(String.class))).thenReturn(chefNode);
        when(chefNodeDao.loadNodeFromHostname(any(String.class), any(String.class))).thenReturn(chefNode);
        Mockito.doNothing().when(chefNodeDao).isNodeRegistered(any(String.class), any(String.class));

        recipeNamingGenerator = mock(RecipeNamingGenerator.class);
        when(recipeNamingGenerator.getInstallRecipe(any(ProductInstance.class))).thenReturn(installRecipe);
        when(recipeNamingGenerator.getUninstallRecipe(any(ProductInstance.class))).thenReturn(uninstallRecipe);
        when(recipeNamingGenerator.getDeployArtifactRecipe(any(ProductInstance.class))).thenReturn(deployacrecipe);
        when(recipeNamingGenerator.getConfigureRecipe(any(ProductInstance.class))).thenReturn(configurerecipe);
        when(recipeNamingGenerator.getUnDeployArtifactRecipe(any(ProductInstance.class))).thenReturn(undeployacrecipe);

        propertiesProvider = mock(SystemPropertiesProvider.class);

        installator = spy(new InstallatorChefImpl());
        installator.setRecipeNamingGenerator(recipeNamingGenerator);
        installator.setChefNodeDao(chefNodeDao);
        installator.setPropertiesProvider(propertiesProvider);
        installator.setSdcClientUtils(sdcClientUtils);
        doNothing().when(installator).sleep(10000);

    }

    @Test
    public void installTest_OK() throws InstallatorException, NodeExecutionException {

        installator.callService(productInstance, host, new ArrayList<Attribute>(), "install", "token");
    }

    @Test
    public void uninstallTest_OK() throws InstallatorException, NodeExecutionException {

        installator.callService(productInstance, host, new ArrayList<Attribute>(), "uninstall", "token");
    }

    @Test
    public void configureTest_OK() throws InstallatorException, NodeExecutionException {

        installator.callService(productInstance, host, new ArrayList<Attribute>(), "configure", "token");
    }

    @Test
    public void deployArtifactTest_OK() throws InstallatorException, NodeExecutionException {

        installator.callService(productInstance, host, new ArrayList<Attribute>(), "deployArtifact", "token");
    }

    @Test
    public void undeployArtifactTest_OK() throws InstallatorException, NodeExecutionException {

        installator.callService(productInstance, host, new ArrayList<Attribute>(), "undeployArtifact", "token");
    }

    @Test(expected = InstallatorException.class)
    public void installTest_fail() throws InstallatorException, NodeExecutionException {

        installator.callService(productInstance, host, new ArrayList<Attribute>(), "NOT EXISTS", "token");

    }

    @Test
    public void unnstallTest_OK_1() throws InstallatorException, NodeExecutionException {

        installator.callService(productInstance, "uninstall", "token");
    }
    
    @Test
    public void checkRecipeExecution_OK() throws InstallatorException, NodeExecutionException { 
    	installator.checkRecipeExecution(host, "Product", installRecipe, "token");
    }

    @Test(expected = NodeExecutionException.class)
    public void checkRecipeExecution_fail() throws InstallatorException, NodeExecutionException {
        	
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z", Locale.US);
        Date past = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(past);
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.YEAR, 2000);
        past = calendar.getTime();
        
        chefNode.addAutomaticAttritube("ohai_time", df.format(past));
        
        installator.checkRecipeExecution(host, "Product", installRecipe, "token");
    }
}
