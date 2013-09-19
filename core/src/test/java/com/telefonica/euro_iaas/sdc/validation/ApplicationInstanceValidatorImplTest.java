package com.telefonica.euro_iaas.sdc.validation;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.sdc.exception.IncompatibleProductsException;
import com.telefonica.euro_iaas.sdc.exception.NotInstalledProductsException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Unit test suite for ApplicationInsatnceValidatorImpl
 *
 * @author serch
 *
 */
public class ApplicationInstanceValidatorImplTest {

    private Product product = new Product("product", "description");
    private ProductRelease pRelease1 = new ProductRelease("1", "releaseNotes",
            null, product, null, null);
    private ProductRelease pRelease2 = new ProductRelease("2", "releaseNotes",
            null, product, null, null);

    private ProductInstance pInstance = new ProductInstance(pRelease1,
            Status.INSTALLED, new VM("ip"), "vdc");
    private ProductInstance pInstanceUninstalled = new ProductInstance(pRelease1,
            Status.UNINSTALLED, new VM("ip"), "vdc");
    private ProductInstance pInstance2 = new ProductInstance(pRelease2,
            Status.INSTALLED, new VM("ip"), "vdc");

    private Application app = new Application("app", "description", "type");

    private ApplicationRelease release1 = new ApplicationRelease(
            "1", "releaseNotes", null, app,
            new Environment(Arrays.asList(pRelease1)), new ArrayList<ApplicationRelease>());
    private ApplicationRelease release2 = new ApplicationRelease(
            "2", "releaseNotes", null, app,
            new Environment(Arrays.asList(pRelease1, pRelease2)), Arrays.asList(release1));
    private FSMValidator fsmValidator;

    @Before
    public void prepareMocks() throws Exception {
        fsmValidator = Mockito.mock(FSMValidator.class);
    }
///////// TEST INSTALL OPERATION /////////
    @Test
    public void testValidateInstallShouldBeOk() throws Exception {
        ApplicationInstanceValidatorImpl validator =
                new ApplicationInstanceValidatorImpl();
        validator.setFsmValidator(fsmValidator);
        ApplicationInstance application = new ApplicationInstance();
        application.setApplication(release1);
        application.setStatus(Status.INSTALLING);
        application.setEnvironmentInstance(new EnvironmentInstance(
        		release1.getEnvironment(),
        		Arrays.asList(pInstance)
        		)
        	);
        validator.validateInstall(application);

        application.setStatus(Status.UNINSTALLED);
        application.setEnvironmentInstance(new EnvironmentInstance(
        		release1.getEnvironment(),
        		Arrays.asList(pInstance)
        		)
        	);
        validator.validateInstall(application);

        application.setStatus(Status.ERROR);
        application.setEnvironmentInstance(new EnvironmentInstance(
        		release1.getEnvironment(),
        		Arrays.asList(pInstance)
        		)
        	);
        validator.validateInstall(application);
    }

    @Test(expected=NotInstalledProductsException.class)
    public void testValidateInstallShouldThrowsNotInstalledProductException() throws Exception {
        ApplicationInstanceValidatorImpl validator =
                new ApplicationInstanceValidatorImpl();
        validator.setFsmValidator(fsmValidator);

        ApplicationInstance application = new ApplicationInstance();
        application.setApplication(release1);
        application.setStatus(Status.INSTALLED);
        application.setEnvironmentInstance(new EnvironmentInstance(
        		release1.getEnvironment(),
        		Arrays.asList(pInstanceUninstalled)
        		)
        	);
        validator.validateInstall(application);
    }

    @Test(expected=IncompatibleProductsException.class)
    public void testValidateInstallShouldThrowsIncompatibleProductException() throws Exception {
        ApplicationInstanceValidatorImpl validator =
                new ApplicationInstanceValidatorImpl();
        validator.setFsmValidator(fsmValidator);
        ApplicationInstance application = new ApplicationInstance();
        application.setApplication(release1);
        application.setStatus(Status.INSTALLED);
        application.setEnvironmentInstance(new EnvironmentInstance(
        		release2.getEnvironment(),
        		Arrays.asList(pInstance2)
        		)
        	);
        validator.validateInstall(application);
    }



///////// TEST UPGRADE OPERATION /////////

    @Test
    public void testValidateUpgrade() throws Exception {
        ApplicationInstanceValidatorImpl validator =
                new ApplicationInstanceValidatorImpl();
        validator.setFsmValidator(fsmValidator);
        ApplicationInstance application = new ApplicationInstance(2l);
        application.setApplication(release2);
        application.setStatus(Status.INSTALLED);
        application.setEnvironmentInstance(new EnvironmentInstance(
        		release2.getEnvironment(),
        		Arrays.asList(pInstance)
        		)
        	);
        validator.validateUpdate(application, release1);
    }

    @Test(expected=NotTransitableException.class)
    public void testValidateUpgradeShouldThrowsNotDefinedTransitionException() throws Exception {
        ApplicationInstanceValidatorImpl validator =
                new ApplicationInstanceValidatorImpl();
        validator.setFsmValidator(fsmValidator);
        ApplicationInstance application = new ApplicationInstance(2l);
        application.setApplication(release1);
        application.setStatus(Status.INSTALLED);
        application.setEnvironmentInstance(new EnvironmentInstance(
        		release1.getEnvironment(),
        		Arrays.asList(pInstance)
        		)
        	);
        validator.validateUpdate(application, release2);
    }

    @Test(expected=IncompatibleProductsException.class)
    public void testValidateUpgradeShouldThrowsIncompatibleProductsException() throws Exception {
        ApplicationInstanceValidatorImpl validator =
                new ApplicationInstanceValidatorImpl();
        validator.setFsmValidator(fsmValidator);
        ApplicationInstance application = new ApplicationInstance(2l);
        application.setApplication(release2);
        application.setStatus(Status.INSTALLED);
        application.setEnvironmentInstance(new EnvironmentInstance(
        		release2.getEnvironment(),
        		Arrays.asList(pInstance2)
        		)
        	);
        validator.validateUpdate(application, release1);
    }
}
