package com.telefonica.euro_iaas.sdc.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;

import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;

import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.xmlsolutions.annotation.Requirement;

/**
 * Default ProductInstanceValidator implementation
 * 
 * @author Sergio Arroyo
 * 
 */
public class ProductInstanceValidatorImpl implements ProductInstanceValidator {

	private FSMValidator fsmValidator;


	/**
	 * {@inheritDoc}
	 */
	@Requirement(traceTo = { "RF000" }, status = "implemented")
	@Override
	public void validateInstall(ProductInstance product)
			throws AlreadyInstalledException {
		try {
			fsmValidator.validate(product, Status.INSTALLING);
		} catch (FSMViolationException e) {
			throw new AlreadyInstalledException(product);
		}
	}

	@Override
	public void validateDeployArtifact(ProductInstance product)
			throws FSMViolationException {
		try {
			fsmValidator.validate(product, Status.DEPLOYING_ARTEFACT);
		} catch (FSMViolationException e) {
			throw new FSMViolationException(product.getName() + " status "
					+ product.getStatus());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateUninstall(ProductInstance product)
			throws FSMViolationException {
		fsmValidator.validate(product, Status.UNINSTALLING);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateConfigure(ProductInstance product)
			throws FSMViolationException {
		fsmValidator.validate(product, Status.CONFIGURING);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateUpdate(ProductInstance product,
			ProductRelease newRelease) throws FSMViolationException,
			NotTransitableException {
		fsmValidator.validate(product, Status.UPGRADING);
		// validate the product can upgrade the new version
		List<ProductRelease> productReleases = product.getProductRelease()
				.getTransitableReleases();
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
