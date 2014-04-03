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

import java.util.ArrayList;
import java.util.Arrays;

import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;

/**
 * @author Sergio Arroyo
 */
public class ProductInstanceValidatorImplTest extends TestCase {

    private Product product = new Product("product", "description");
    private ProductRelease release1 = new ProductRelease("1", "releaseNotes", product, null,
        new ArrayList<ProductRelease>());
    private ProductRelease release2 = new ProductRelease("2", "releaseNotes", product, null,
        Arrays.asList(release1));

    private ProductInstance pInstance = new ProductInstance(release1, Status.INSTALLED, new VM("ip"), "vdc");

    private FSMValidator fsmValidator;

    @Before
    public void setUp() throws Exception {
        fsmValidator = mock(FSMValidator.class);
        Mockito.doNothing().when(fsmValidator).validate(any(InstallableInstance.class), any(Status.class));

    }

    @Test
    public void testValidateInstall() throws Exception {
        ProductInstanceValidatorImpl validator = new ProductInstanceValidatorImpl();
        validator.setFsmValidator(fsmValidator);

        ProductInstance instance = new ProductInstance(release1, Status.INSTALLED, new VM("ip"), "vdc");
        validator.validateInstall(instance);
        instance = new ProductInstance(5l);
        instance.setStatus(Status.ERROR);
        validator.validateInstall(instance);
    }

    @Test
    public void testValidateDeployAC() throws Exception {
        ProductInstanceValidatorImpl validator = new ProductInstanceValidatorImpl();
        validator.setFsmValidator(fsmValidator);

        ProductInstance instance = new ProductInstance(release1, Status.INSTALLED, new VM("ip"), "vdc");
        validator.validateDeployArtifact(instance);
        instance = new ProductInstance(5l);
        instance.setStatus(Status.ERROR);
        validator.validateInstall(instance);
    }

    /*
     * @Test(expected = ApplicationInstalledException.class) public void
     * testValidateUninstallShouldThrowsApplicationInstalledException() throws Exception { when( applicationInstanceDao
     * .findByCriteria(any(ApplicationInstanceSearchCriteria.class))) .thenReturn(applications);
     * ProductInstanceValidatorImpl validator = new ProductInstanceValidatorImpl();
     * validator.setApplicationInstanceDao(applicationInstanceDao); validator.setFsmValidator(fsmValidator);
     * ProductInstance instance = new ProductInstance(release1, Status.INSTALLED, new VM("ip"), "vdc");
     * validator.validateUninstall(instance); }
     */

    @Test
    public void testValidateUpdate() throws Exception {

        ProductInstanceValidatorImpl validator = new ProductInstanceValidatorImpl();

        validator.setFsmValidator(fsmValidator);

        ProductInstance instance = new ProductInstance(release2, Status.INSTALLED, new VM("ip"), "vdc");

        validator.validateUpdate(instance, release1);
    }

    /*
     * @Test(expected = NotTransitableException.class) public void
     * testValidateUpdateShouldThrowsNotTransitableException() throws Exception { when( applicationInstanceDao
     * .findByCriteria(any(ApplicationInstanceSearchCriteria.class))) .thenReturn(new ArrayList<ApplicationInstance>());
     * ProductInstanceValidatorImpl validator = new ProductInstanceValidatorImpl();
     * validator.setApplicationInstanceDao(applicationInstanceDao); validator.setFsmValidator(fsmValidator);
     * ProductInstance instance = new ProductInstance(release1, Status.INSTALLED, new VM("ip"), "vdc");
     * validator.validateUpdate(instance, release2); }
     * @Test(expected = ApplicationIncompatibleException.class) public void
     * testValidateUpdateShouldThrowsApplicationIncompatibleException() throws Exception { when( applicationInstanceDao
     * .findByCriteria(any(ApplicationInstanceSearchCriteria.class))) .thenReturn(applications);
     * ProductInstanceValidatorImpl validator = new ProductInstanceValidatorImpl();
     * validator.setApplicationInstanceDao(applicationInstanceDao); validator.setFsmValidator(fsmValidator);
     * ProductInstance instance = new ProductInstance(release2, Status.INSTALLED, new VM("ip"), "vdc");
     * validator.validateUpdate(instance, release1); }
     */
}
