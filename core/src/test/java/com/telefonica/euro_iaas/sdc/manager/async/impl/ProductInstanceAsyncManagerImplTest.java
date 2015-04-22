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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.util.TaskNotificator;

public class ProductInstanceAsyncManagerImplTest {
    ProductInstanceAsyncManagerImpl productInstanceAsyncManager;
 
    TaskManager taskManager = mock(TaskManager.class);
    TaskNotificator taskNotificator = mock(TaskNotificator.class);
    ProductInstanceManager productInstanceManager = mock(ProductInstanceManager.class);
    ProductDao productDao = mock(ProductDao.class);
//    Product product;

    @Before
    public void setUp() throws EntityNotFoundException {
    	SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        productInstanceAsyncManager = new ProductInstanceAsyncManagerImpl();
        productInstanceAsyncManager.setTaskManager(taskManager);
        productInstanceAsyncManager.setTaskNotificator(taskNotificator);
        productInstanceAsyncManager.setProductInstanceManager(productInstanceManager);
        productInstanceAsyncManager.setSystemPropertiesProvider(systemPropertiesProvider);
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("");
    }

    @Test
    public void shouldInstall() throws NodeExecutionException, AlreadyInstalledException,
            InvalidInstallProductRequestException, EntityNotFoundException {
        // given
        VM vm = new VM();
        String vdc = "virtualDataCenter";
        ProductRelease productRelease = new ProductRelease();
        List<Attribute> attributes = new ArrayList<Attribute>(2);
        Task task = new Task();
        String callback = "http://localhost/callback";
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(vm);
        productInstance.setProductRelease(productRelease);
        Metadata metadata=new Metadata("installator", "chef");
        Product product = new Product();
        List<Metadata>metadatas = new ArrayList<Metadata>();
        metadatas.add(metadata);
        product.setMetadatas(metadatas);
        productRelease.setProduct(product);

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes, "token")).thenReturn(productInstance);
        when(productDao.load(Mockito.anyString())).thenReturn(product);
        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, "token", task, callback);
        

        // then
        assertEquals(task.getStatus(), Task.TaskStates.SUCCESS);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes,  "token");
    }
    
    @Test
    public void shouldUpdateErrorTaskWhenInstallAndNodeExecutionException() throws NotUniqueResultException,
            EntityNotFoundException, NodeExecutionException, AlreadyInstalledException,
            InvalidInstallProductRequestException {
     // given
        VM vm = new VM();
        String vdc = "virtualDataCenter";
        ProductRelease productRelease = new ProductRelease();
        List<Attribute> attributes = new ArrayList<Attribute>(2);
        Task task = new Task();
        String callback = "http://localhost/callback";
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(vm);
        productInstance.setProductRelease(productRelease);
        Metadata metadata=new Metadata("installator", "chef");
        Product product = new Product();
        List<Metadata>metadatas = new ArrayList<Metadata>();
        metadatas.add(metadata);
        product.setMetadatas(metadatas);
        productRelease.setProduct(product);

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes, "token")).thenReturn(productInstance);
        when(productDao.load(Mockito.anyString())).thenReturn(product);
        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, "token",task, callback);
        

        // then
        assertEquals(task.getStatus(), Task.TaskStates.SUCCESS);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes, "token");
    }
    
    @Test
    public void shouldUpdateErrorTaskWhenInstallAndNodeExecutionExceptionAndProductInstallNotExist()
            throws NotUniqueResultException, EntityNotFoundException, NodeExecutionException,
            AlreadyInstalledException, InvalidInstallProductRequestException {
        // given
        VM vm = new VM();
        String vdc = "virtualDataCenter";
        ProductRelease productRelease = new ProductRelease();
        List<Attribute> attributes = new ArrayList<Attribute>(2);
        Task task = new Task();
        String callback = "http://localhost/callback";
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(vm);
        productInstance.setProductRelease(productRelease);
        Product product = new Product();
        productRelease.setProduct(product);
        List<Metadata>metadatas = new ArrayList<Metadata>();
        Metadata metadata=new Metadata("installator", "chef");
        metadatas.add(metadata);
        product.setMetadatas(metadatas);

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes,"token")).thenThrow(
                new NodeExecutionException("node execution exception"));
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(null);

        when(productDao.load(Mockito.anyString())).thenReturn(product);
        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, "token",task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes,"token");
       
    }
    

    @Test
    public void shouldUpdateErrorTaskWhenInstallAndAlreadyInstalled() throws NotUniqueResultException,
            EntityNotFoundException, NodeExecutionException, AlreadyInstalledException,
            InvalidInstallProductRequestException {
        // given
        VM vm = new VM();
        String vdc = "virtualDataCenter";
        ProductRelease productRelease = new ProductRelease();
        List<Attribute> attributes = new ArrayList<Attribute>(2);
        Task task = new Task();
        String callback = "http://localhost/callback";
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(vm);
        productInstance.setProductRelease(productRelease);
        Product product = new Product();
        productRelease.setProduct(product);
        List<Metadata>metadatas = new ArrayList<Metadata>();
        Metadata metadata=new Metadata("installator", "chef");
        metadatas.add(metadata);
        product.setMetadatas(metadatas);

        AlreadyInstalledException alreadyInstalledException = new AlreadyInstalledException(
                "already installed exception");
        alreadyInstalledException.setInstace(new InstallableInstance());

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes, "token")).thenThrow(alreadyInstalledException);
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(
                productInstance);

        when(productDao.load(Mockito.anyString())).thenReturn(product);
        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, "token", task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes, "token");
       
    }
    
    @Test
    public void shouldUpdateErrorTaskWhenInstallAndAlreadyInstalledAndInstanceNotExist()
            throws NotUniqueResultException, EntityNotFoundException, NodeExecutionException,
            AlreadyInstalledException, InvalidInstallProductRequestException {
        // given
        VM vm = new VM();
        String vdc = "virtualDataCenter";
        ProductRelease productRelease = new ProductRelease();
        List<Attribute> attributes = new ArrayList<Attribute>(2);
        Task task = new Task();
        String callback = "http://localhost/callback";
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(vm);
        productInstance.setProductRelease(productRelease);
        Product product = new Product();
        productRelease.setProduct(product);
        List<Metadata>metadatas = new ArrayList<Metadata>();
        Metadata metadata=new Metadata("installator", "chef");
        metadatas.add(metadata);
        product.setMetadatas(metadatas);

        AlreadyInstalledException alreadyInstalledException = new AlreadyInstalledException(
                "already installed exception");
        alreadyInstalledException.setInstace(new InstallableInstance());

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes, "token")).thenThrow(alreadyInstalledException);
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(null);

        when(productDao.load(Mockito.anyString())).thenReturn(product);
        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, "token",task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes,"token");
       
    }
    
    @Test
    public void shouldUpdateErrorTaskWhenInstallAndInvalidInstallProductRequestAndInstanceNotExist()
            throws NotUniqueResultException, EntityNotFoundException, NodeExecutionException,
            AlreadyInstalledException, InvalidInstallProductRequestException {
        // given
        VM vm = new VM();
        String vdc = "virtualDataCenter";
        ProductRelease productRelease = new ProductRelease();
        List<Attribute> attributes = new ArrayList<Attribute>(2);
        Task task = new Task();
        String callback = "http://localhost/callback";
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(vm);
        productInstance.setProductRelease(productRelease);
        Product product = new Product();
        productRelease.setProduct(product);
        List<Metadata>metadatas = new ArrayList<Metadata>();
        Metadata metadata=new Metadata("installator", "chef");
        metadatas.add(metadata);
        product.setMetadatas(metadatas);

        InvalidInstallProductRequestException invalidInstallProductRequestException = new InvalidInstallProductRequestException(
                "invalid request exception");

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes, "token")).thenThrow(
                invalidInstallProductRequestException);
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(null);

        when(productDao.load(Mockito.anyString())).thenReturn(product);
        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes,"token", task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes, "token");
       
    }
    
    @Test
    public void shouldUpdateErrorTaskWhenInstallAndInvalidInstallProductRequest() throws NotUniqueResultException,
            EntityNotFoundException, NodeExecutionException, AlreadyInstalledException,
            InvalidInstallProductRequestException {
        // given
        VM vm = new VM();
        String vdc = "virtualDataCenter";
        ProductRelease productRelease = new ProductRelease();
        List<Attribute> attributes = new ArrayList<Attribute>(2);
        Task task = new Task();
        String callback = "http://localhost/callback";
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(vm);
        productInstance.setProductRelease(productRelease);
        Product product = new Product();
        productRelease.setProduct(product);
        List<Metadata>metadatas = new ArrayList<Metadata>();
        Metadata metadata=new Metadata("installator", "chef");
        metadatas.add(metadata);
        product.setMetadatas(metadatas);

        InvalidInstallProductRequestException invalidInstallProductRequestException = new InvalidInstallProductRequestException(
                "invalid request exception");

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes, "token")).thenThrow(
                invalidInstallProductRequestException);
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(
                productInstance);

        when(productDao.load(Mockito.anyString())).thenReturn(product);
        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes,  "token", task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes,  "token");
       
    }
    
    @Test
    public void shouldUpdateErrorTaskWhenInstallAndRuntimeExceptionAndInstanceNotExist()
            throws NotUniqueResultException, EntityNotFoundException, NodeExecutionException,
            AlreadyInstalledException, InvalidInstallProductRequestException {
        // given
        VM vm = new VM();
        String vdc = "virtualDataCenter";
        ProductRelease productRelease = new ProductRelease();
        List<Attribute> attributes = new ArrayList<Attribute>(2);
        Task task = new Task();
        String callback = "http://localhost/callback";
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(vm);
        productInstance.setProductRelease(productRelease);
        Product product = new Product();
        productRelease.setProduct(product);
        List<Metadata>metadatas = new ArrayList<Metadata>();
        Metadata metadata=new Metadata("installator", "chef");
        metadatas.add(metadata);
        product.setMetadatas(metadatas);

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes,  "token")).thenThrow(
                new RuntimeException("runtime exception"));
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(null);

        when(productDao.load(Mockito.anyString())).thenReturn(product);
        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes,  "token", task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes,  "token");
     
    }
    
    @Test
    public void shouldUpdateErrorTaskWhenInstallAndRuntimeException() throws NotUniqueResultException,
            EntityNotFoundException, NodeExecutionException, AlreadyInstalledException,
            InvalidInstallProductRequestException {
        // given
        VM vm = new VM();
        String vdc = "virtualDataCenter";
        ProductRelease productRelease = new ProductRelease();
        List<Attribute> attributes = new ArrayList<Attribute>(2);
        Task task = new Task();
        String callback = "http://localhost/callback";
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(vm);
        productInstance.setProductRelease(productRelease);
        Product product = new Product();
        productRelease.setProduct(product);
        List<Metadata>metadatas = new ArrayList<Metadata>();
        Metadata metadata=new Metadata("installator", "chef");
        metadatas.add(metadata);
        product.setMetadatas(metadatas);

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes,  "token")).thenThrow(
                new RuntimeException("runtime exception"));
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(
                productInstance);

        when(productDao.load(Mockito.anyString())).thenReturn(product);
        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, "token", task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes,  "token");
     
    }
    
    @Test
    public void shouldUninstall() throws NodeExecutionException, FSMViolationException, EntityNotFoundException {
        // given
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(new VM());
        ProductRelease productRelease = new ProductRelease();
        Product product = new Product();
        productRelease.setProduct(product);
        productInstance.setProductRelease(productRelease);
        Task task = new Task();
        String callback = "http://callback";

        // when
        productInstanceAsyncManager.uninstall(productInstance, "token", task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.SUCCESS);
        verify(productInstanceManager).uninstall(productInstance,  "token");
        verify(taskManager).updateTask(task);
    }
    
    @Test
    public void shouldUpdateErrorTaskInUninstallWhenFSMViolation() throws NodeExecutionException, FSMViolationException, EntityNotFoundException {
        // given
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(new VM());
        ProductRelease productRelease = new ProductRelease();
        Product product = new Product();
        productRelease.setProduct(product);
        productInstance.setProductRelease(productRelease);
        Task task = new Task();
        String callback = "http://callback";

        // when

        doThrow(new FSMViolationException("error")).when(productInstanceManager).uninstall(productInstance,  "token");
        productInstanceAsyncManager.uninstall(productInstance, "token", task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).uninstall(productInstance,  "token");
        verify(taskManager).updateTask(task);
    }
    
    @Test
    public void shouldUpdateErrorTaskInUninstallWhenNodeExecutionException() throws NodeExecutionException,
            FSMViolationException, EntityNotFoundException {
        // given
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(new VM());
        ProductRelease productRelease = new ProductRelease();
        Product product = new Product();
        productRelease.setProduct(product);
        productInstance.setProductRelease(productRelease);
        Task task = new Task();
        String callback = "http://callback";

        // when

        doThrow(new NodeExecutionException("error")).when(productInstanceManager).uninstall(productInstance,  "token");
        productInstanceAsyncManager.uninstall(productInstance,  "token", task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).uninstall(productInstance,  "token");
        verify(taskManager).updateTask(task);
    }
    
    @Test
    public void shouldUpdateErrorTaskInUninstallWhenRuntimeException() throws NodeExecutionException,
            FSMViolationException, EntityNotFoundException {
        // given
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(new VM());
        ProductRelease productRelease = new ProductRelease();
        Product product = new Product();
        productRelease.setProduct(product);
        productInstance.setProductRelease(productRelease);
        Task task = new Task();
        String callback = "http://callback";

        // when

        doThrow(new RuntimeException("error")).when(productInstanceManager).uninstall(productInstance,  "token");
        productInstanceAsyncManager.uninstall(productInstance,  "token", task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).uninstall(productInstance,  "token");
        verify(taskManager).updateTask(task);
    }
    
    @Test
    public void shouldUpgrade() throws NodeExecutionException, NotTransitableException, FSMViolationException, InstallatorException, EntityNotFoundException {
        // given
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(new VM());
        ProductRelease productRelease = new ProductRelease();
        Product product = new Product();
        productRelease.setProduct(product);
        productInstance.setProductRelease(productRelease);
        Task task = new Task();
        String callback = "http://callback";

        // when
        productInstanceAsyncManager.upgrade(productInstance, productRelease,  "token", task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.SUCCESS);
        verify(productInstanceManager).upgrade(productInstance, productRelease,  "token");
        verify(taskManager).updateTask(task);
    }
    
    @Test
    public void shouldConfigure() throws NodeExecutionException, FSMViolationException, InstallatorException {
        // given
        ProductInstance productInstance = new ProductInstance();
        productInstance.setVm(new VM());
        ProductRelease productRelease = new ProductRelease();
        Product product = new Product();
        productRelease.setProduct(product);
        productInstance.setProductRelease(productRelease);
        Task task = new Task();
        String callback = "http://callback";
        List<Attribute> configuration = new ArrayList<Attribute>(2);

        // when
        productInstanceAsyncManager.configure(productInstance, configuration, "token", task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.SUCCESS);
        verify(productInstanceManager).configure(productInstance, configuration,  "token");
        verify(taskManager).updateTask(task);
    }
    
}
