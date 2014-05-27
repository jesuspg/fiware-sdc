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

package com.telefonica.euro_iaas.sdc.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.manager.async.ArtifactAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.ProductInstanceAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ArtifactDto;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.rest.resources.ArtifactResourceImpl;

public class ArtifactResourceImplTest {
    public static String PRODUCT_NAME = "Product::server";
    public static String PRODUCT_VERSION = "Product::version";
    public static String VDC = "vdc";
    public static String KEY1 = "key1";
    public static String VALUE1 = "value1";
    public static String HREF = "href";

    ArtifactResourceImpl artifactResource = null;

    @Before
    public void setUp() throws Exception {
        artifactResource = new ArtifactResourceImpl();
        TaskManager taskManager = mock(TaskManager.class);
        ProductInstanceAsyncManager productInstanceAsyncManager = mock(ProductInstanceAsyncManager.class);
        ArtifactAsyncManager artifactAsyncManager = mock(ArtifactAsyncManager.class);

        artifactResource.setTaskManager(taskManager);
        artifactResource.setProductInstanceAsyncManager(productInstanceAsyncManager);
        artifactResource.setArtifactAsyncManager(artifactAsyncManager);

        Product product = new Product(PRODUCT_NAME, "description");
        OS os = new OS("os1", "1", "os1 description", "v1");
        VM vm = new VM("ip", "hostname", "domain");
        ProductRelease productRelease = new ProductRelease(PRODUCT_VERSION, "releaseNotes", 
            product, Arrays.asList(os), null);
        List<ProductRelease> lProductRelease = new ArrayList<ProductRelease>();
        ProductInstance productIns = new ProductInstance(productRelease, Status.INSTALLED, vm, VDC);
        lProductRelease.add(productRelease);
        Task task = new Task();
        task.setHref("href");

        when(taskManager.createTask(any(Task.class))).thenReturn(task);
        when(productInstanceAsyncManager.load(any(String.class), any(String.class))).thenReturn(productIns);
        doNothing().when(artifactAsyncManager).deployArtifact(any(ProductInstance.class), any(Artifact.class), any(String.class),
                any(Task.class), any(String.class));

    }

    @Test
    public void testInsert() throws Exception {
        Attribute att = new Attribute(KEY1, VALUE1, "description1");
        List<Attribute> atts = new ArrayList<Attribute>();
        atts.add(att);
        String callback = "";
        ArtifactDto artifactDto = new ArtifactDto("artifact", atts);
        Task task = artifactResource.install(VDC, "productIntanceName", artifactDto, callback);
        assertEquals(task.getHref(), HREF);

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testList() throws Exception {

    }

}
