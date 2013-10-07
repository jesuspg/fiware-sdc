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
