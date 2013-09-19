package com.telefonica.euro_iaas.sdc.validation;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.ApplicationIncompatibleException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationInstalledException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;

/**
 *
 * @author Sergio Arroyo
 *
 */
public class ProductInstanceValidatorImplTest {

    private Product product = new Product("product", "description");
    private ProductRelease release1 = new ProductRelease("1", "releaseNotes",
            null, product, null, new ArrayList<ProductRelease>());
    private ProductRelease release2 = new ProductRelease("2", "releaseNotes",
            null, product, null, Arrays.asList(release1));

    private ProductInstance pInstance = new ProductInstance(release1,
            Status.INSTALLED, new VM("ip"));
    private Application app = new Application("app", "description", "type");

    private ApplicationRelease appRelease1 = new ApplicationRelease(
            "1", "releaseNotes", null, app,
            Arrays.asList(release2), new ArrayList<ApplicationRelease>());


    private ApplicationInstance appInstance1  = new ApplicationInstance();

    private List<ApplicationInstance> applications =
            Arrays.asList(appInstance1);

    private FSMValidator fsmValidator;
    private ApplicationInstanceDao applicationInstanceDao;

    @Before
    public void prepareMocks() throws Exception {
        fsmValidator = mock(FSMValidator.class);
        applicationInstanceDao = mock(ApplicationInstanceDao.class);

        appInstance1.setApplication(appRelease1);
        appInstance1.setStatus(Status.INSTALLED);
        appInstance1.setProducts(Arrays.asList(pInstance));
    }

    @Test
    public void validateInstall() throws Exception {
        ProductInstanceValidatorImpl validator =
                new ProductInstanceValidatorImpl();
        validator.setFsmValidator(fsmValidator);

        ProductInstance instance = new ProductInstance(
                release1, Status.INSTALLED, new VM("ip"));
        validator.validateInstall(instance);
        instance = new ProductInstance(5l);
        instance.setStatus(Status.ERROR);
        validator.validateInstall(instance);
    }

    @Test
    public void validateUninstall() throws Exception {
        when(applicationInstanceDao.findByCriteria(
                any(ApplicationInstanceSearchCriteria.class)))
                .thenReturn(new ArrayList<ApplicationInstance>());
        ProductInstanceValidatorImpl validator =
                new ProductInstanceValidatorImpl();
        validator.setFsmValidator(fsmValidator);
        validator.setApplicationInstanceDao(applicationInstanceDao);
        ProductInstance instance = new ProductInstance(
                release1, Status.INSTALLED, new VM("ip"));
        validator.validateUninstall(instance);
    }

    @Test(expected = ApplicationInstalledException.class)
    public void validateUninstallShouldThrowsApplicationInstalledException() throws Exception {
        when(applicationInstanceDao.findByCriteria(
                any(ApplicationInstanceSearchCriteria.class)))
                .thenReturn(applications);
        ProductInstanceValidatorImpl validator =
                new ProductInstanceValidatorImpl();
        validator.setApplicationInstanceDao(applicationInstanceDao);
        validator.setFsmValidator(fsmValidator);

        ProductInstance instance = new ProductInstance(
                release1, Status.INSTALLED, new VM("ip"));
        validator.validateUninstall(instance);
    }

    @Test
    public void validateUpdate() throws Exception {
        when(applicationInstanceDao.findByCriteria(
                any(ApplicationInstanceSearchCriteria.class)))
                .thenReturn(new ArrayList<ApplicationInstance>());
        ProductInstanceValidatorImpl validator =
                new ProductInstanceValidatorImpl();
        validator.setApplicationInstanceDao(applicationInstanceDao);
        validator.setFsmValidator(fsmValidator);

        ProductInstance instance = new ProductInstance(
                release2, Status.INSTALLED, new VM("ip"));

        validator.validateUpdate(instance, release1);
    }

    @Test(expected = NotTransitableException.class)
    public void validateUpdateShouldThrowsNotTransitableException() throws Exception {
        when(applicationInstanceDao.findByCriteria(
                any(ApplicationInstanceSearchCriteria.class)))
                .thenReturn(new ArrayList<ApplicationInstance>());
        ProductInstanceValidatorImpl validator =
                new ProductInstanceValidatorImpl();
        validator.setApplicationInstanceDao(applicationInstanceDao);
        validator.setFsmValidator(fsmValidator);

        ProductInstance instance = new ProductInstance(
                release1, Status.INSTALLED, new VM("ip"));

        validator.validateUpdate(instance, release2);
    }

    @Test(expected = ApplicationIncompatibleException.class)
    public void validateUpdateShouldThrowsApplicationIncompatibleException() throws Exception {
        when(applicationInstanceDao.findByCriteria(
                any(ApplicationInstanceSearchCriteria.class)))
                .thenReturn(applications);
        ProductInstanceValidatorImpl validator =
                new ProductInstanceValidatorImpl();
        validator.setApplicationInstanceDao(applicationInstanceDao);
        validator.setFsmValidator(fsmValidator);

        ProductInstance instance = new ProductInstance(
                release2, Status.INSTALLED, new VM("ip"));

        validator.validateUpdate(instance, release1);
    }
}
