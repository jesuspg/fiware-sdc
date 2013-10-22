/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
