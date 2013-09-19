package com.telefonica.euro_iaas.sdc.validation;

import java.util.Arrays;
import java.util.List;

import com.telefonica.euro_iaas.sdc.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.sdc.dao.EnvironmentDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class EnvironmentValidatorImplTest {

	private EnvironmentValidatorImpl environmentValidator;
	private List<ProductRelease> productReleases;

	private ProductInstanceDao productInstanceDao;
	private ApplicationReleaseDao applicationReleaseDao;

	private EnvironmentDao environmentDao;

	private Product product;
	private ProductRelease productRelease;
	private Environment expectedEnvironment;

	@Before
	public void setUp() throws Exception {
		environmentValidator = new EnvironmentValidatorImpl();

		// ***** Product
		product = new Product();
		product.setName("abcd");
		product.setDescription("descritpion");

		// ***** ProductReleases
		productRelease = new ProductRelease();
		productRelease.setProduct(product);
		productRelease.setVersion("0.1.1");
		Attribute privateAttribute = new Attribute("ssl_port", "8443",
				"The ssl listen port");
		Attribute privateAttributeII = new Attribute("port", "8080",
				"The listen port");

		List<Attribute> privateAttributes = Arrays.asList(privateAttribute,
				privateAttributeII);
		productRelease.setPrivateAttributes(privateAttributes);

		// ************Environment
		productReleases = Arrays.asList(productRelease);
		Environment environment = new Environment(productReleases);

		environmentDao = mock(EnvironmentDao.class);
		when(environmentDao.create(any(Environment.class))).thenReturn(
				environment);

		applicationReleaseDao = mock(ApplicationReleaseDao.class);
		productInstanceDao = mock(ProductInstanceDao.class);

		expectedEnvironment = environmentDao.create(environment);

		environmentValidator = new EnvironmentValidatorImpl();
		environmentValidator.setApplicationReleaseDao(applicationReleaseDao);
		environmentValidator.setProductInstanceDao(productInstanceDao);

	}

	@Test
	public void testValidateDeleteOK() throws Exception {
		environmentValidator.validateDelete(expectedEnvironment);
	}

}
