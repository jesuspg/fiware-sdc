package com.telefonica.euro_iaas.sdc.dao;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

public class ProductReleaseImplTest extends AbstractJpaDaoTest {

	private ProductReleaseDao productReleaseDao;

	private ProductRelease productRelease;
	private Product product;

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

		/*
		 * productReleaseDao = mock(ProductReleaseDao.class);
		 * when(productReleaseDao.create(any(ProductRelease.class))).thenReturn(
		 * expectedProductRelease);
		 * when(productReleaseDao.update(any(ProductRelease.class))).thenReturn(
		 * expectedProductRelease);
		 */
	}

	@Test
	public void createProductRelease() {

		ProductRelease createdRelease;
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

		ProductRelease loadedRelease;
		try {
			loadedRelease = productReleaseDao.load(product, "v1");
			Assert.assertEquals(productRelease, loadedRelease);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param productReleaseDao
	 *            the productReleaseDao to set
	 */
	public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
		this.productReleaseDao = productReleaseDao;
	}

}
