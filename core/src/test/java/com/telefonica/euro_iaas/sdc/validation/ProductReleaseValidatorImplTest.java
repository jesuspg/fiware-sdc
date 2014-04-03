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

package com.telefonica.euro_iaas.sdc.validation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;

public class ProductReleaseValidatorImplTest {

    @Test
    public void shouldValidateDeleteWithEmptyProductInstances() throws ProductReleaseStillInstalledException {
        // given

        ProductReleaseValidatorImpl productReleaseValidator = new ProductReleaseValidatorImpl();
        ProductRelease productRelease = new ProductRelease();
        ProductInstanceDao productInstanceDao = mock(ProductInstanceDao.class);
        productReleaseValidator.setProductInstanceDao(productInstanceDao);
        List<ProductInstance> productInstances = new ArrayList<ProductInstance>(2);

        // when
        when(productInstanceDao.findByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(productInstances);
        productReleaseValidator.validateDelete(productRelease);

        // then
        verify(productInstanceDao).findByCriteria(any(ProductInstanceSearchCriteria.class));

    }

    @Test
    public void shouldValidateDeleteWithProductInstancesInstalled() {
        // given

        ProductReleaseValidatorImpl productReleaseValidator = new ProductReleaseValidatorImpl();
        ProductRelease productRelease = new ProductRelease();
        ProductInstanceDao productInstanceDao = mock(ProductInstanceDao.class);
        productReleaseValidator.setProductInstanceDao(productInstanceDao);
        List<ProductInstance> productInstances = new ArrayList<ProductInstance>(2);
        ProductInstance productInstance1 = new ProductInstance();
        productInstance1.setStatus(InstallableInstance.Status.INSTALLED);
        productInstances.add(productInstance1);

        // when
        when(productInstanceDao.findByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(productInstances);
        try {
            productReleaseValidator.validateDelete(productRelease);
            fail("not exist instances with status=installed");
        } catch (ProductReleaseStillInstalledException e) {
            // then
            assertTrue(true);
            verify(productInstanceDao).findByCriteria(any(ProductInstanceSearchCriteria.class));
        }

    }

    @Test
    public void shouldValidateDeleteWithProductInstancesWithStatusConfiguring() {
        // given

        ProductReleaseValidatorImpl productReleaseValidator = new ProductReleaseValidatorImpl();
        ProductRelease productRelease = new ProductRelease();
        ProductInstanceDao productInstanceDao = mock(ProductInstanceDao.class);
        productReleaseValidator.setProductInstanceDao(productInstanceDao);
        List<ProductInstance> productInstances = new ArrayList<ProductInstance>(2);
        ProductInstance productInstance1 = new ProductInstance();
        productInstance1.setStatus(InstallableInstance.Status.CONFIGURING);
        productInstances.add(productInstance1);

        // when
        when(productInstanceDao.findByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(productInstances);
        try {
            productReleaseValidator.validateDelete(productRelease);
            fail("not exist instances with status=configuring");
        } catch (ProductReleaseStillInstalledException e) {
            // then
            assertTrue(true);
            verify(productInstanceDao).findByCriteria(any(ProductInstanceSearchCriteria.class));
        }

    }

    @Test
    public void shouldValidateDeleteWithProductInstancesWithStatusUpgrading() {
        // given

        ProductReleaseValidatorImpl productReleaseValidator = new ProductReleaseValidatorImpl();
        ProductRelease productRelease = new ProductRelease();
        ProductInstanceDao productInstanceDao = mock(ProductInstanceDao.class);
        productReleaseValidator.setProductInstanceDao(productInstanceDao);
        List<ProductInstance> productInstances = new ArrayList<ProductInstance>(2);
        ProductInstance productInstance1 = new ProductInstance();
        productInstance1.setStatus(InstallableInstance.Status.UPGRADING);
        productInstances.add(productInstance1);

        // when
        when(productInstanceDao.findByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(productInstances);
        try {
            productReleaseValidator.validateDelete(productRelease);
            fail("not exist instances with status=upgrading");
        } catch (ProductReleaseStillInstalledException e) {
            // then
            assertTrue(true);
            verify(productInstanceDao).findByCriteria(any(ProductInstanceSearchCriteria.class));
        }

    }

    @Test
    public void shouldValidateDeleteWithProductInstancesWithStatusInstalling() {
        // given

        ProductReleaseValidatorImpl productReleaseValidator = new ProductReleaseValidatorImpl();
        ProductRelease productRelease = new ProductRelease();
        ProductInstanceDao productInstanceDao = mock(ProductInstanceDao.class);
        productReleaseValidator.setProductInstanceDao(productInstanceDao);
        List<ProductInstance> productInstances = new ArrayList<ProductInstance>(2);
        ProductInstance productInstance1 = new ProductInstance();
        productInstance1.setStatus(InstallableInstance.Status.INSTALLING);
        productInstances.add(productInstance1);

        // when
        when(productInstanceDao.findByCriteria(any(ProductInstanceSearchCriteria.class))).thenReturn(productInstances);
        try {
            productReleaseValidator.validateDelete(productRelease);
            fail("not exist instances with status=installing");
        } catch (ProductReleaseStillInstalledException e) {
            // then
            assertTrue(true);
            verify(productInstanceDao).findByCriteria(any(ProductInstanceSearchCriteria.class));
        }
    }
}
