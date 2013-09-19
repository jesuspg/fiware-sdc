package com.telefonica.euro_iaas.sdc.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.ApplicationInstanceStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.InvalidEnvironmentInstanceException;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test suite for EnvironmentnsatnceValidatorImpl
 * 
 * @author Jesus M. Movilla
 * 
 */
public class EnvironmentInstanceValidatorImplTest {

	private Product product = new Product("product", "description");
	private ProductRelease pRelease1 = new ProductRelease("1", "releaseNotes",
			null, product, null, null);
	private ProductRelease pRelease2 = new ProductRelease("2", "releaseNotes",
			null, product, null, null);
	List<ProductRelease> productReleasesInstalled = Arrays.asList(pRelease1,
			pRelease2);
	List<ProductRelease> productReleases1 = Arrays.asList(pRelease1);

	Environment envInstalled = new Environment(productReleasesInstalled);
	Environment envUnInstalled = new Environment(productReleases1);

	private ProductInstance pInstance = new ProductInstance(pRelease1,
			Status.INSTALLED, new VM("ip"), "vdc");
	private ProductInstance pInstanceUninstalled = new ProductInstance(
			pRelease1, Status.UNINSTALLED, new VM("ip"), "vdc");
	private ProductInstance pInstance2 = new ProductInstance(pRelease2,
			Status.INSTALLED, new VM("ip"), "vdc");

	private List<ProductInstance> piInstalled = Arrays.asList(pInstance,
			pInstance2);
	private List<ProductInstance> piUnInstalled = Arrays
			.asList(pInstanceUninstalled);

	private EnvironmentInstance envInstanceInstalled = new EnvironmentInstance(
			envInstalled, piInstalled);
	private EnvironmentInstance envInstanceUnInstalled = new EnvironmentInstance(
			envUnInstalled, piUnInstalled);

	private Application app = new Application("app", "description", "type");

	private ApplicationRelease release1 = new ApplicationRelease("1",
			"releaseNotes", null, app,
			new Environment(Arrays.asList(pRelease1)),
			new ArrayList<ApplicationRelease>());
	private ApplicationRelease release2 = new ApplicationRelease("2",
			"releaseNotes", null, app, new Environment(Arrays.asList(pRelease1,
					pRelease2)), Arrays.asList(release1));

	private ApplicationInstance application;

	private ApplicationInstanceDao applicationInstanceDao;

	@Before
	public void prepareMocks() throws Exception {

		application = new ApplicationInstance();
		application.setApplication(release1);
		application.setStatus(Status.INSTALLING);
		application.setEnvironmentInstance(envInstanceInstalled);
		List<ApplicationInstance> applicationInstances = Arrays
				.asList(application);

		applicationInstanceDao = mock(ApplicationInstanceDao.class);
		when(applicationInstanceDao.findAll()).thenReturn(applicationInstances);

	}

	// /////// TEST INSERT OPERATION /////////
	@Test
	public void testValidateInsertShouldBeOk() throws Exception {
		EnvironmentInstanceValidatorImpl validator = new EnvironmentInstanceValidatorImpl();

		validator.setApplicationInstanceDao(applicationInstanceDao);
		validator.validateInsert(envInstanceInstalled);
	}

	@Test
	public void testValidateInsertShouldNotBeOk() throws Exception {
		EnvironmentInstanceValidatorImpl validator = new EnvironmentInstanceValidatorImpl();
		try {
			validator.setApplicationInstanceDao(applicationInstanceDao);
			validator.validateInsert(envInstanceUnInstalled);
			fail("The test failed");
		} catch (InvalidEnvironmentInstanceException e) {

		}

	}

	// /////// TEST UPDATE OPERATION /////////
	@Test
	public void testValidateUpdate() throws Exception {
		EnvironmentInstanceValidatorImpl validator = new EnvironmentInstanceValidatorImpl();

		validator.setApplicationInstanceDao(applicationInstanceDao);
		validator.validateUpdate(envInstanceInstalled);
	}

	@Test(expected = InvalidEnvironmentInstanceException.class)
	public void testValidateUpdateShouldThrowsInvalidEnvironmentInstanceException()
			throws Exception {

		EnvironmentInstanceValidatorImpl validator = new EnvironmentInstanceValidatorImpl();
		validator.setApplicationInstanceDao(applicationInstanceDao);
		validator.validateUpdate(envInstanceUnInstalled);
	}

	// /////// TEST DELETE OPERATION /////////
	@Test
	public void testValidateDelete() throws Exception {

		EnvironmentInstanceValidatorImpl validator = new EnvironmentInstanceValidatorImpl();
		validator.setApplicationInstanceDao(applicationInstanceDao);

		EnvironmentInstance envInstanceDeleted = new EnvironmentInstance();
		envInstanceDeleted.setEnvironment(envInstalled);
		envInstanceDeleted.setProductInstances(piInstalled);

		validator.validateDelete(envInstanceDeleted);
	}

	@Test(expected = ApplicationInstanceStillInstalledException.class)
	public void testValidateDeleteShouldThrowsInvalidEnvironmentInstanceException()
			throws Exception {

		EnvironmentInstanceValidatorImpl validator = new EnvironmentInstanceValidatorImpl();

		validator.setApplicationInstanceDao(applicationInstanceDao);

		validator.validateDelete(envInstanceInstalled);
	}

}
