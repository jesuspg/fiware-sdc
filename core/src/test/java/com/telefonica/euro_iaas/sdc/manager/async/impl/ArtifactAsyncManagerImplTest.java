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

package com.telefonica.euro_iaas.sdc.manager.async.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.manager.ArtifactManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.util.TaskNotificator;

public class ArtifactAsyncManagerImplTest {

    ArtifactAsyncManagerImpl artifactAsyncManagerImpl;
    ArtifactManager artifactManager;
    TaskNotificator taskNotificator;
    TaskManager taskManager;
    SystemPropertiesProvider propertiesProvider;

    @Before
    public void setup() {
        artifactManager = mock(ArtifactManager.class);
        taskNotificator = mock(TaskNotificator.class);
        taskManager = mock(TaskManager.class);
        propertiesProvider = mock(SystemPropertiesProvider.class);

        artifactAsyncManagerImpl = new ArtifactAsyncManagerImpl();
        artifactAsyncManagerImpl.setArtifactManager(artifactManager);
        artifactAsyncManagerImpl.setTaskNotificator(taskNotificator);
        artifactAsyncManagerImpl.setTaskManager(taskManager);
        artifactAsyncManagerImpl.setPropertiesProvider(propertiesProvider);

    }

    @Test
    public void shouldDeployArtifact() throws NodeExecutionException, FSMViolationException {
        // given
        ProductInstance productInstance = new ProductInstance();
        VM vm = new VM();
        vm.setDomain("domain");
        vm.setHostname("hostname");
        productInstance.setVm(vm);
        ProductRelease productRelease = new ProductRelease();
        Product product = new Product();
        product.setName("name");
        productRelease.setProduct(product);
        productInstance.setProductRelease(productRelease);
        Artifact artifact = new Artifact();
        Task task = new Task();
        task.setStatus(Task.TaskStates.SUCCESS);
        String urlCallback = "http://url_to_callback";

        // when
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn(
                "http://baseurl");
        artifactAsyncManagerImpl.deployArtifact(productInstance, artifact, task, urlCallback);

        // then
        verify(artifactManager).deployArtifact(productInstance, artifact);
        verify(taskNotificator).notify(urlCallback, task);
        verify(taskManager).updateTask(task);

    }

    @Test
    public void shouldUpdateErrorTaskWhenDeployArtifactAndProducesFMViolation() throws NodeExecutionException,
            FSMViolationException {
        // given
        ProductInstance productInstance = new ProductInstance();
        VM vm = new VM();
        vm.setDomain("domain");
        vm.setHostname("hostname");
        productInstance.setVm(vm);
        ProductRelease productRelease = new ProductRelease();
        Product product = new Product();
        product.setName("name");
        productRelease.setProduct(product);
        productInstance.setProductRelease(productRelease);
        Artifact artifact = new Artifact();
        Task task = new Task();
        String urlCallback = "http://url_to_callback";

        // when
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn(
                "http://baseurl");

        when(artifactManager.deployArtifact(productInstance, artifact)).thenThrow(new FSMViolationException("error"));
        artifactAsyncManagerImpl.deployArtifact(productInstance, artifact, task, urlCallback);

        // then
        verify(artifactManager).deployArtifact(productInstance, artifact);
        verify(taskNotificator).notify(urlCallback, task);
        verify(taskManager).updateTask(task);
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);

    }

    @Test
    public void shouldUpdateErrorTaskWhenDeployArtifactAndProducesNodeExecutionError() throws NodeExecutionException,
            FSMViolationException {
        // given
        ProductInstance productInstance = new ProductInstance();
        VM vm = new VM();
        vm.setDomain("domain");
        vm.setHostname("hostname");
        productInstance.setVm(vm);
        ProductRelease productRelease = new ProductRelease();
        Product product = new Product();
        product.setName("name");
        productRelease.setProduct(product);
        productInstance.setProductRelease(productRelease);
        Artifact artifact = new Artifact();
        Task task = new Task();
        String urlCallback = "http://url_to_callback";

        // when
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn(
                "http://baseurl");

        when(artifactManager.deployArtifact(productInstance, artifact)).thenThrow(new NodeExecutionException("error"));
        artifactAsyncManagerImpl.deployArtifact(productInstance, artifact, task, urlCallback);

        // then
        verify(artifactManager).deployArtifact(productInstance, artifact);
        verify(taskNotificator).notify(urlCallback, task);
        verify(taskManager).updateTask(task);
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);

    }

    @Test
    public void shouldUnDeployArtifactAndTaskSuccess() throws NodeExecutionException, FSMViolationException {
        // given
        ProductInstance productInstance = new ProductInstance();
        VM vm = new VM();
        vm.setDomain("domain");
        vm.setHostname("hostname");
        productInstance.setVm(vm);
        ProductRelease productRelease = new ProductRelease();
        Product product = new Product();
        product.setName("name");
        productRelease.setProduct(product);
        productInstance.setProductRelease(productRelease);
        String artifactName = "artifactName";
        Task task = new Task();
        task.setStatus(Task.TaskStates.SUCCESS);
        String urlCallback = "http://url_to_callback";

        // when
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn(
                "http://baseurl");
        artifactAsyncManagerImpl.undeployArtifact(productInstance, artifactName, task, urlCallback);

        // then
        verify(artifactManager).undeployArtifact(productInstance, artifactName);
        verify(taskNotificator).notify(urlCallback, task);
        verify(taskManager).updateTask(task);
        assertEquals(task.getStatus(), Task.TaskStates.SUCCESS);

    }

    @Test
    public void shouldUpdateErrorTaskWhenUnDeployArtifactAndProducesNodeExecutionException()
            throws NodeExecutionException, FSMViolationException {
        // given
        ProductInstance productInstance = new ProductInstance();
        VM vm = new VM();
        vm.setDomain("domain");
        vm.setHostname("hostname");
        productInstance.setVm(vm);
        ProductRelease productRelease = new ProductRelease();
        Product product = new Product();
        product.setName("name");
        productRelease.setProduct(product);
        productInstance.setProductRelease(productRelease);
        String artifactName = "artifactName";
        Task task = new Task();
        task.setStatus(Task.TaskStates.SUCCESS);
        String urlCallback = "http://url_to_callback";

        // when

        when(artifactManager.undeployArtifact(productInstance, artifactName)).thenThrow(
                new NodeExecutionException("error"));
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn(
                "http://baseurl");
        artifactAsyncManagerImpl.undeployArtifact(productInstance, artifactName, task, urlCallback);

        // then
        verify(artifactManager).undeployArtifact(productInstance, artifactName);
        verify(taskNotificator).notify(urlCallback, task);
        verify(taskManager).updateTask(task);
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);

    }

    @Test
    public void shouldUpdateErrorTaskWhenUnDeployArtifactAndProducesFSMViaolation() throws NodeExecutionException,
            FSMViolationException {
        // given
        ProductInstance productInstance = new ProductInstance();
        VM vm = new VM();
        vm.setDomain("domain");
        vm.setHostname("hostname");
        productInstance.setVm(vm);
        ProductRelease productRelease = new ProductRelease();
        Product product = new Product();
        product.setName("name");
        productRelease.setProduct(product);
        productInstance.setProductRelease(productRelease);
        String artifactName = "artifactName";
        Task task = new Task();
        task.setStatus(Task.TaskStates.SUCCESS);
        String urlCallback = "http://url_to_callback";

        // when

        when(artifactManager.undeployArtifact(productInstance, artifactName)).thenThrow(
                new FSMViolationException("error"));
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn(
                "http://baseurl");
        artifactAsyncManagerImpl.undeployArtifact(productInstance, artifactName, task, urlCallback);

        // then
        verify(artifactManager).undeployArtifact(productInstance, artifactName);
        verify(taskNotificator).notify(urlCallback, task);
        verify(taskManager).updateTask(task);
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);

    }
}
