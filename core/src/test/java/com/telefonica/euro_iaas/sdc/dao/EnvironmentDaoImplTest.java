package com.telefonica.euro_iaas.sdc.dao;

import java.util.Arrays;
import java.util.List;


import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.EnvironmentSearchCriteria;

/**
 * Unit test for EnvironmentDao
 * 
 * @author Jesus M. Movilla
 * 
 */
public class EnvironmentDaoImplTest extends AbstractJpaDaoTest {

	private ProductReleaseDao productReleaseDao;
	private EnvironmentDao environmentDao;

	private ProductRelease createdRelease;

	private Environment environment;
	private Environment createdEnvironment;
	private Environment loadedEnvironment;
	private List<Environment> foundEnvironments;

	@Before
	public void prepare() throws Exception {
		Product product = new Product();
		product.setName("yum");
		product.setDescription("yum description");

		ProductRelease productRelease = new ProductRelease();
		productRelease.setProduct(product);
		productRelease.setVersion("0.1.1");
		productRelease.setReleaseNotes("prueba ReelaseNotes");

		OS os = new OS("Debian", "95", "Debian def 5.2", "5.2");
		List<OS> supportedOOSS = Arrays.asList(os);
		productRelease.setSupportedOOSS(supportedOOSS);

		Attribute privateAttribute = new Attribute("ssl_port", "8443",
				"The ssl listen port");
		Attribute privateAttributeII = new Attribute("port", "8080",
				"The listen port");

		List<Attribute> privateAttributes = Arrays.asList(privateAttribute,
				privateAttributeII);
		productRelease.setPrivateAttributes(privateAttributes);

		try {
			createdRelease = productReleaseDao.create(productRelease);
			Assert.assertEquals(productRelease, createdRelease);
		} catch (InvalidEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyExistsEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void createEnvironement() {

		try {
			createdEnvironment = environmentDao.create(environment);
			Assert.assertEquals(createdEnvironment, environment);
		} catch (InvalidEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyExistsEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Environment loadedEnvironment;
		try {
			loadedEnvironment = environmentDao.load(createdEnvironment
					.getName());
			Assert.assertEquals(createdEnvironment, loadedEnvironment);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void loadEnvironement() {
		try {
			loadedEnvironment = environmentDao.load(createdEnvironment
					.getName());
			Assert.assertEquals(createdEnvironment, loadedEnvironment);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void foundEnvironement() {
		EnvironmentSearchCriteria criteria = new EnvironmentSearchCriteria();

		foundEnvironments = environmentDao.findByCriteria(criteria);
		Assert.assertEquals(1, foundEnvironments.size());
		Assert.assertEquals(createdEnvironment, foundEnvironments.get(0));
	}

	/**
	 * @param productReleaseDao
	 *            the productReleaseDao to set
	 */
	public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
		this.productReleaseDao = productReleaseDao;
	}

	/**
	 * @param environmentDao
	 *            the environmentDao to set
	 */
	public void setEnvironmentDao(EnvironmentDao environmentDao) {
		this.environmentDao = environmentDao;
	}
}
