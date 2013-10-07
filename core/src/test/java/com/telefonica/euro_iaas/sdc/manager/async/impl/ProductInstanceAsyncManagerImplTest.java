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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
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
    SystemPropertiesProvider propertiesProvider = mock(SystemPropertiesProvider.class);
    TaskManager taskManager = mock(TaskManager.class);
    TaskNotificator taskNotificator = mock(TaskNotificator.class);
    ProductInstanceManager productInstanceManager = mock(ProductInstanceManager.class);

    @Before
    public void setUp() {
        productInstanceAsyncManager = new ProductInstanceAsyncManagerImpl();
        productInstanceAsyncManager.setPropertiesProvider(propertiesProvider);
        productInstanceAsyncManager.setTaskManager(taskManager);
        productInstanceAsyncManager.setTaskNotificator(taskNotificator);
        productInstanceAsyncManager.setProductInstanceManager(productInstanceManager);

    }

    @Test
    public void shouldInstall() throws NodeExecutionException, AlreadyInstalledException,
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

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes)).thenReturn(productInstance);
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.SUCCESS);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes);
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
        Product product = new Product();
        productRelease.setProduct(product);

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes)).thenThrow(
                new NodeExecutionException("node execution exception"));
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(
                productInstance);

        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes);
        verify(productInstanceManager).loadByCriteria(any(ProductInstanceSearchCriteria.class));
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

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes)).thenThrow(
                new NodeExecutionException("node execution exception"));
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(null);

        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes);
        verify(productInstanceManager).loadByCriteria(any(ProductInstanceSearchCriteria.class));
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

        AlreadyInstalledException alreadyInstalledException = new AlreadyInstalledException(
                "already installed exception");
        alreadyInstalledException.setInstace(new InstallableInstance());

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes)).thenThrow(alreadyInstalledException);
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(
                productInstance);

        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes);
        verify(productInstanceManager).loadByCriteria(any(ProductInstanceSearchCriteria.class));
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

        AlreadyInstalledException alreadyInstalledException = new AlreadyInstalledException(
                "already installed exception");
        alreadyInstalledException.setInstace(new InstallableInstance());

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes)).thenThrow(alreadyInstalledException);
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(null);

        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes);
        verify(productInstanceManager).loadByCriteria(any(ProductInstanceSearchCriteria.class));
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

        InvalidInstallProductRequestException invalidInstallProductRequestException = new InvalidInstallProductRequestException(
                "invalid request exception");

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes)).thenThrow(
                invalidInstallProductRequestException);
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(null);

        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes);
        verify(productInstanceManager).loadByCriteria(any(ProductInstanceSearchCriteria.class));
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

        InvalidInstallProductRequestException invalidInstallProductRequestException = new InvalidInstallProductRequestException(
                "invalid request exception");

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes)).thenThrow(
                invalidInstallProductRequestException);
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(
                productInstance);

        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes);
        verify(productInstanceManager).loadByCriteria(any(ProductInstanceSearchCriteria.class));
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

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes)).thenThrow(
                new RuntimeException("runtime exception"));
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(null);

        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes);
        verify(productInstanceManager).loadByCriteria(any(ProductInstanceSearchCriteria.class));
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

        // when
        when(productInstanceManager.install(vm, vdc, productRelease, attributes)).thenThrow(
                new RuntimeException("runtime exception"));
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        when(productInstanceManager.loadByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(
                productInstance);

        productInstanceAsyncManager.install(vm, vdc, productRelease, attributes, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).install(vm, vdc, productRelease, attributes);
        verify(productInstanceManager).loadByCriteria(any(ProductInstanceSearchCriteria.class));
    }

    @Test
    public void shouldUninstall() throws NodeExecutionException, FSMViolationException {
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
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        productInstanceAsyncManager.uninstall(productInstance, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.SUCCESS);
        verify(productInstanceManager).uninstall(productInstance);
        verify(taskManager).updateTask(task);
    }

    @Test
    public void shouldUpdateErrorTaskInUninstallWhenFSMViolation() throws NodeExecutionException, FSMViolationException {
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

        doThrow(new FSMViolationException("error")).when(productInstanceManager).uninstall(productInstance);
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        productInstanceAsyncManager.uninstall(productInstance, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).uninstall(productInstance);
        verify(taskManager).updateTask(task);
    }

    @Test
    public void shouldUpdateErrorTaskInUninstallWhenNodeExecutionException() throws NodeExecutionException,
            FSMViolationException {
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

        doThrow(new NodeExecutionException("error")).when(productInstanceManager).uninstall(productInstance);
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        productInstanceAsyncManager.uninstall(productInstance, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).uninstall(productInstance);
        verify(taskManager).updateTask(task);
    }

    @Test
    public void shouldUpdateErrorTaskInUninstallWhenRuntimeException() throws NodeExecutionException,
            FSMViolationException {
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

        doThrow(new RuntimeException("error")).when(productInstanceManager).uninstall(productInstance);
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        productInstanceAsyncManager.uninstall(productInstance, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.ERROR);
        verify(productInstanceManager).uninstall(productInstance);
        verify(taskManager).updateTask(task);
    }

    @Test
    public void shouldUpgrade() throws NodeExecutionException, NotTransitableException, FSMViolationException {
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
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        productInstanceAsyncManager.upgrade(productInstance, productRelease, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.SUCCESS);
        verify(productInstanceManager).upgrade(productInstance, productRelease);
        verify(taskManager).updateTask(task);
    }

    @Test
    public void shouldConfigure() throws NodeExecutionException, FSMViolationException {
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
        when(propertiesProvider.getProperty(SystemPropertiesProvider.PRODUCT_INSTANCE_BASE_URL)).thenReturn("url");
        productInstanceAsyncManager.configure(productInstance, configuration, task, callback);

        // then
        assertEquals(task.getStatus(), Task.TaskStates.SUCCESS);
        verify(productInstanceManager).configure(productInstance, configuration);
        verify(taskManager).updateTask(task);
    }
}
