package com.telefonica.euro_iaas.sdc.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;


import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.model.Application;

import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.VM;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

/**
 * 
 * @author Sergio Arroyo
 * 
 */
public class ProductInstanceValidatorImplTest extends TestCase {

	private Product product = new Product("product", "description");
	private ProductRelease release1 = new ProductRelease("1", "releaseNotes",
			null, product, null, new ArrayList<ProductRelease>());
	private ProductRelease release2 = new ProductRelease("2", "releaseNotes",
			null, product, null, Arrays.asList(release1));

	private ProductInstance pInstance = new ProductInstance(release1,
			Status.INSTALLED, new VM("ip"), "vdc");
	private Application app = new Application("app", "description", "type");


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
		
		ProductInstance instance = new ProductInstance(release1,
				Status.INSTALLED, new VM("ip"), "vdc");
		validator.validateInstall(instance);
		instance = new ProductInstance(5l);
		instance.setStatus(Status.ERROR);
		validator.validateInstall(instance);
	}

	@Test
	public void testValidateDeployAC() throws Exception {
		ProductInstanceValidatorImpl validator = new ProductInstanceValidatorImpl();
		validator.setFsmValidator(fsmValidator);

		ProductInstance instance = new ProductInstance(release1,
				Status.INSTALLED, new VM("ip"), "vdc");
		validator.validateDeployArtifact(instance);
		instance = new ProductInstance(5l);
		instance.setStatus(Status.ERROR);
		validator.validateInstall(instance);
	}

/*	@Test(expected = ApplicationInstalledException.class)
	public void testValidateUninstallShouldThrowsApplicationInstalledException()
			throws Exception {
		when(
				applicationInstanceDao
						.findByCriteria(any(ApplicationInstanceSearchCriteria.class)))
				.thenReturn(applications);
		ProductInstanceValidatorImpl validator = new ProductInstanceValidatorImpl();
		validator.setApplicationInstanceDao(applicationInstanceDao);
		validator.setFsmValidator(fsmValidator);

		ProductInstance instance = new ProductInstance(release1,
				Status.INSTALLED, new VM("ip"), "vdc");
		validator.validateUninstall(instance);
	}*/

	@Test
	public void testValidateUpdate() throws Exception {
		
		ProductInstanceValidatorImpl validator = new ProductInstanceValidatorImpl();
		
		validator.setFsmValidator(fsmValidator);

		ProductInstance instance = new ProductInstance(release2,
				Status.INSTALLED, new VM("ip"), "vdc");

		validator.validateUpdate(instance, release1);
	}

/*	@Test(expected = NotTransitableException.class)
	public void testValidateUpdateShouldThrowsNotTransitableException()
			throws Exception {
		when(
				applicationInstanceDao
						.findByCriteria(any(ApplicationInstanceSearchCriteria.class)))
				.thenReturn(new ArrayList<ApplicationInstance>());
		ProductInstanceValidatorImpl validator = new ProductInstanceValidatorImpl();
		validator.setApplicationInstanceDao(applicationInstanceDao);
		validator.setFsmValidator(fsmValidator);

		ProductInstance instance = new ProductInstance(release1,
				Status.INSTALLED, new VM("ip"), "vdc");

		validator.validateUpdate(instance, release2);
	}

	@Test(expected = ApplicationIncompatibleException.class)
	public void testValidateUpdateShouldThrowsApplicationIncompatibleException()
			throws Exception {
		when(
				applicationInstanceDao
						.findByCriteria(any(ApplicationInstanceSearchCriteria.class)))
				.thenReturn(applications);
		ProductInstanceValidatorImpl validator = new ProductInstanceValidatorImpl();
		validator.setApplicationInstanceDao(applicationInstanceDao);
		validator.setFsmValidator(fsmValidator);

		ProductInstance instance = new ProductInstance(release2,
				Status.INSTALLED, new VM("ip"), "vdc");

		validator.validateUpdate(instance, release1);
	}*/
}
