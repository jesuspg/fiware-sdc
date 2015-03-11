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

package com.telefonica.euro_iaas.sdc.manager.async.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
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


    @Before
    public void setup() {
        artifactManager = mock(ArtifactManager.class);
        taskNotificator = mock(TaskNotificator.class);
        taskManager = mock(TaskManager.class);
        SystemPropertiesProvider systemPropertiesProvider = mock (SystemPropertiesProvider.class);

        artifactAsyncManagerImpl = new ArtifactAsyncManagerImpl();
        artifactAsyncManagerImpl.setArtifactManager(artifactManager);
        artifactAsyncManagerImpl.setTaskNotificator(taskNotificator);
        artifactAsyncManagerImpl.setTaskManager(taskManager);
        artifactAsyncManagerImpl.setSystemPropertiesProvider(systemPropertiesProvider);


    }

    @Test
    public void shouldDeployArtifact() throws NodeExecutionException, FSMViolationException, InstallatorException {
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
        artifactAsyncManagerImpl.deployArtifact(productInstance, artifact, "token", task, urlCallback);

        // then
        verify(artifactManager).deployArtifact(productInstance, artifact, "token");
        verify(taskNotificator).notify(urlCallback, task);
        verify(taskManager).updateTask(task);

    }

    @Test
    public void shouldUpdateErrorTaskWhenDeployArtifactAndProducesFMViolation() throws NodeExecutionException,
            FSMViolationException, InstallatorException {
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


        when(artifactManager.deployArtifact(productInstance, artifact, "token")).thenThrow(new FSMViolationException("error"));
        artifactAsyncManagerImpl.deployArtifact(productInstance, artifact, "token", task, urlCallback);

        // then
        verify(artifactManager).deployArtifact(productInstance, artifact, "token");
        verify(taskNotificator).notify(urlCallback, task);
        verify(taskManager).updateTask(task);
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);

    }

    @Test
    public void shouldUpdateErrorTaskWhenDeployArtifactAndProducesNodeExecutionError() throws NodeExecutionException,
            FSMViolationException, InstallatorException {
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

        when(artifactManager.deployArtifact(productInstance, artifact, "token")).thenThrow(new NodeExecutionException("error"));
        artifactAsyncManagerImpl.deployArtifact(productInstance, artifact, "token", task, urlCallback);

        // then
        verify(artifactManager).deployArtifact(productInstance, artifact, "token");
        verify(taskNotificator).notify(urlCallback, task);
        verify(taskManager).updateTask(task);
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);

    }

    @Test
    public void shouldUnDeployArtifactAndTaskSuccess() throws NodeExecutionException, FSMViolationException, InstallatorException {
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
        artifactAsyncManagerImpl.undeployArtifact(productInstance, artifactName, "token", task, urlCallback);

        // then
        verify(artifactManager).undeployArtifact(productInstance, artifactName, "token");
        verify(taskNotificator).notify(urlCallback, task);
        verify(taskManager).updateTask(task);
        assertEquals(task.getStatus(), Task.TaskStates.SUCCESS);

    }

    @Test
    public void shouldUpdateErrorTaskWhenUnDeployArtifactAndProducesNodeExecutionException()
            throws NodeExecutionException, FSMViolationException, InstallatorException {
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

        when(artifactManager.undeployArtifact(productInstance, artifactName, "token")).thenThrow(
                new NodeExecutionException("error"));

        artifactAsyncManagerImpl.undeployArtifact(productInstance, artifactName, "token", task, urlCallback);

        // then
        verify(artifactManager).undeployArtifact(productInstance, artifactName,"token");
        verify(taskNotificator).notify(urlCallback, task);
        verify(taskManager).updateTask(task);
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);

    }

    @Test
    public void shouldUpdateErrorTaskWhenUnDeployArtifactAndProducesFSMViaolation() throws NodeExecutionException,
            FSMViolationException, InstallatorException {
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

        when(artifactManager.undeployArtifact(productInstance, artifactName,"token")).thenThrow(
                new FSMViolationException("error"));

        artifactAsyncManagerImpl.undeployArtifact(productInstance, artifactName, "token", task, urlCallback);

        // then
        verify(artifactManager).undeployArtifact(productInstance, artifactName,"token");
        verify(taskNotificator).notify(urlCallback, task);
        verify(taskManager).updateTask(task);
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);

    }
}
