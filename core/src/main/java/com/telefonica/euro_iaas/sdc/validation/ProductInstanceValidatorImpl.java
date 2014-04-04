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

import java.util.List;

import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.xmlsolutions.annotation.Requirement;

/**
 * Default ProductInstanceValidator implementation
 * 
 * @author Sergio Arroyo
 */
public class ProductInstanceValidatorImpl implements ProductInstanceValidator {

    private FSMValidator fsmValidator;

    /**
     * {@inheritDoc}
     */
    @Requirement(traceTo = { "RF000" }, status = "implemented")
    @Override
    public void validateInstall(ProductInstance product) throws AlreadyInstalledException {
        try {
            fsmValidator.validate(product, Status.INSTALLING);
        } catch (FSMViolationException e) {
            throw new AlreadyInstalledException(product);
        }
    }

    @Override
    public void validateDeployArtifact(ProductInstance product) throws FSMViolationException {
        try {
            fsmValidator.validate(product, Status.DEPLOYING_ARTEFACT);
        } catch (FSMViolationException e) {
            throw new FSMViolationException(product.getName() + " status " + product.getStatus());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateUninstall(ProductInstance product) throws FSMViolationException {
        fsmValidator.validate(product, Status.UNINSTALLING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateConfigure(ProductInstance product) throws FSMViolationException {
        fsmValidator.validate(product, Status.CONFIGURING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateUpdate(ProductInstance product, ProductRelease newRelease) throws FSMViolationException,
            NotTransitableException {
        fsmValidator.validate(product, Status.UPGRADING);
        // validate the product can upgrade the new version
        List<ProductRelease> productReleases = product.getProductRelease().getTransitableReleases();
        if (!productReleases.contains(newRelease)) {
            throw new NotTransitableException();
        }

    }

    // ///////I.O.C////////
    /**
     * @param fsmValidator
     *            the fsmValidator to set
     */
    public void setFsmValidator(FSMValidator fsmValidator) {
        this.fsmValidator = fsmValidator;
    }

}
