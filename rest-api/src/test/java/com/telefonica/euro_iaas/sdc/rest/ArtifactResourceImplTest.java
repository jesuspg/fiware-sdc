/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
        doNothing().when(artifactAsyncManager).deployArtifact(any(ProductInstance.class), any(Artifact.class),
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
