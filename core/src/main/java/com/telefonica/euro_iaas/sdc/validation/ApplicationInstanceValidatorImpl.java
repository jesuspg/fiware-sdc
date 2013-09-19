package com.telefonica.euro_iaas.sdc.validation;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.IncompatibleProductsException;
import com.telefonica.euro_iaas.sdc.exception.NotInstalledProductsException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.xmlsolutions.annotation.Requirement;

/**
 * Default implementation for ApplicationInstanceValidator.
 * 
 * @author Sergio Arroyo
 */
public class ApplicationInstanceValidatorImpl implements
		ApplicationInstanceValidator {

	private FSMValidator fsmValidator;

	/**
	 * {@inheritDoc}
	 */
	@Requirement(traceTo = { "RF001" }, status = "implemented")
@Override
	public void validateInstall(ApplicationInstance application)
			throws IncompatibleProductsException, AlreadyInstalledException,
			NotInstalledProductsException {
		// validate if previously installed
		try {
			fsmValidator.validate(application, Status.INSTALLING);
		} catch (FSMViolationException e) {
			throw new AlreadyInstalledException(application);
		}
		// validate if the products are compatible and installed
		List<ProductInstance> invalidProducts = new ArrayList<ProductInstance>();
		List<ProductRelease> uninstalledProducts = new ArrayList<ProductRelease>();
		for (ProductInstance product : application.getEnvironmentInstance()
				.getProductInstances()) {
			if (!application.getApplication().getEnvironment()
					.getProductReleases().contains(product.getProductRelease())) {
				invalidProducts.add(product);
			}
			if (product.getStatus().equals(Status.ERROR)
					|| product.getStatus().equals(Status.UNINSTALLED)
					|| product.getStatus().equals(Status.UNINSTALLING)) {
				uninstalledProducts.add(product.getProductRelease());
			}
		}
		// validate if the products are compatible
		if (!invalidProducts.isEmpty()) {
			throw new IncompatibleProductsException(application,
					invalidProducts);
		}
		// validate if the products are installed
		if (!uninstalledProducts.isEmpty()) {
			throw new NotInstalledProductsException(uninstalledProducts);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateUninstall(ApplicationInstance application)
			throws FSMViolationException {
		fsmValidator.validate(application, Status.UNINSTALLING);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateConfigure(ApplicationInstance application)
			throws FSMViolationException {
		fsmValidator.validate(application, Status.CONFIGURING);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateUpdate(ApplicationInstance application,
			ApplicationRelease newRelease)
			throws IncompatibleProductsException, NotTransitableException,
			FSMViolationException {
		fsmValidator.validate(application, Status.CONFIGURING);
		// validate the transition between two versions
		if (!application.getApplication().getTransitableReleases().contains(
				newRelease)) {
			throw new NotTransitableException();
		}
		// validate the products are compatible
		List<ProductInstance> incompatibleProducts = new ArrayList<ProductInstance>();
		for (ProductInstance product : application.getEnvironmentInstance()
				.getProductInstances()) {
			if (!newRelease.getEnvironment().getProductReleases().contains(
					product.getProductRelease())) {
				incompatibleProducts.add(product);
			}
		}
		if (!incompatibleProducts.isEmpty()) {
			throw new IncompatibleProductsException(application,
					incompatibleProducts);
		}
	}

	// //////// I.O.C /////////
	/**
	 * @param fsmValidator
	 *            the fsmValidator to set
	 */
	public void setFsmValidator(FSMValidator fsmValidator) {
		this.fsmValidator = fsmValidator;
	}

}
