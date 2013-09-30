package com.telefonica.euro_iaas.sdc.validation;

import java.util.Arrays;
import java.util.List;

import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Mockito.mock;

public class ApplicationReleaseValidatorImplTest {

    private ApplicationReleaseValidatorImpl applicationreleaseValidator;
    private ApplicationRelease applicationRelease;
    private ApplicationInstance applicationInstance;
    private List<ProductInstance> productInstances;

    private ApplicationInstanceDao applicationInstanceDao;
    private ProductReleaseDao productReleaseDao;

    @Before
    public void setUp() {
        applicationreleaseValidator = new ApplicationReleaseValidatorImpl();
        applicationRelease = new ApplicationRelease();

        Application application = new Application();
        application.setName("abcd");
        application.setDescription("descritpion");

        applicationRelease.setApplication(application);
        applicationRelease.setVersion("0.1.1");

        Product product = new Product();
        product.setName("tomcat");

        ProductRelease productRelease = new ProductRelease();
        productRelease.setProduct(product);
        productRelease.setVersion("1.0.0");

        List<ProductRelease> supportedProducts = Arrays.asList(productRelease);
        applicationRelease.setEnvironment(new Environment(supportedProducts));

        Attribute privateAttribute = new Attribute("ssl_port", "8443", "The ssl listen port");
        Attribute privateAttributeII = new Attribute("port", "8080", "The listen port");

        List<Attribute> privateAttributes = Arrays.asList(privateAttribute, privateAttributeII);
        applicationRelease.setPrivateAttributes(privateAttributes);

        // ***** ProductInstances
        ProductInstance productInstance = new ProductInstance();
        productInstance.setProductRelease(productRelease);
        productInstance.setVm(new VM("localhost", "ip", "domain"));

        productInstances = Arrays.asList(productInstance);

        // *********** ApplicationInstance

        applicationInstance = new ApplicationInstance();
        applicationInstance.setApplication(applicationRelease);
        applicationInstance.setEnvironmentInstance(new EnvironmentInstance(new Environment(applicationRelease
                .getEnvironment().getProductReleases()), productInstances));

        applicationInstance.setStatus(Status.INSTALLED);

        applicationInstanceDao = mock(ApplicationInstanceDao.class);
        productReleaseDao = mock(ProductReleaseDao.class);

        applicationreleaseValidator = new ApplicationReleaseValidatorImpl();
        applicationreleaseValidator.setProductReleaseDao(productReleaseDao);
        applicationreleaseValidator.setApplicationInstanceDao(applicationInstanceDao);
    }

    @Test
    public void testValidateDeleteKO() throws Exception {
        /*
         * applicationInstanceDao.create(applicationInstance); try {
         * applicationreleaseValidator.validateDelete(applicationRelease); Assert.fail(); } catch
         * (ApplicationReleaseStillInstalledException e) { }
         */
    }

    @Test
    public void testValidateInsertOK() throws Exception {
        // applicationreleaseValidator.validateInsert(applicationRelease);

    }

    @Test
    public void testValidateInsertKO() throws Exception {
        // applicationreleaseValidator.validateInsert(applicationRelease);
        // applicationreleaseValidator.validateInsert(applicationRelease);

    }

    @Test
    public void testValidateDeleteOK() throws Exception {

        // applicationreleaseValidator.validateDelete(applicationRelease);
    }

}
