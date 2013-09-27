package com.telefonica.euro_iaas.sdc.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;

public class ModelTest extends TestCase {

	public static String KEY1 = "key1";
	public static String KEY2 = "key2";
	public static String METADATA1 = "key1";
	public static String METADATA2 = "key2";
	public static String VALUE1 = "value1";
	public static String VALUE2 = "value2";
	public static String VDC = "vdc";
	public static String APPLICATION_NAME = "app";
	public static String APPLICATION_TYPE = "app";

	List<Attribute> metadatas = null;
	java.util.List<Attribute> atts = null;

	/**
	 * Test Product Release class
	 * 
	 * @return
	 */

	public void testProductRelease() {
		ProductRelease productRelease = new ProductRelease();
		Attribute metadata = new Attribute(METADATA1, VALUE1);
		Attribute metadata2 = new Attribute();
		metadata2.setKey(METADATA2);
		metadata2.setKey(VALUE2);
		metadatas = new ArrayList<Attribute>();
		metadatas.add(metadata);

		Product product = new Product();
		product.setName("productName");
		product.setDescription("description");
		product.setMetadata(metadatas);
		productRelease.setProduct(product);
		productRelease.setVersion("1.0");

		Attribute att = new Attribute(KEY1, VALUE1, "description1");
		Attribute att2 = new Attribute(KEY2, VALUE2, "description2");
		atts = new ArrayList<Attribute>();
		atts.add(att);
		atts.add(att2);
		product.setAttributes(atts);

		assertEquals(productRelease.getProduct().getName(), "productName");
		assertEquals(productRelease.getProduct().getDescription(),
				"description");
		assertEquals(productRelease.getVersion(), "1.0");
		assertSame(att.equals(att2), false);

		for (int i = 0; i < productRelease.getProduct().getAttributes().size(); i++) {
			assertEquals(productRelease.getProduct().getAttributes().get(i)
					.getKey(), "key" + (i + 1));
			assertEquals(productRelease.getProduct().getAttributes().get(i)
					.getValue(), "value" + (i + 1));
			assertEquals(productRelease.getProduct().getAttributes().get(i)
					.getDescription(), "description" + (i + 1));
		}

		for (int i = 0; i < productRelease.getProduct().getMetadata().size(); i++) {
			assertEquals(productRelease.getProduct().getMetadata().get(i)
					.getKey(), "key" + (i + 1));
			assertEquals(productRelease.getProduct().getMetadata().get(i)
					.getValue(), "value" + (i + 1));
		}
	}

	public void testApplicationRelease() {
		ApplicationRelease applicationRelease = new ApplicationRelease();

		Application application = new Application();

		application.setDescription("description");
		application.setMetadata(metadatas);
		application.setName(APPLICATION_NAME);
		application.setType(APPLICATION_TYPE);
		application.setAttributes(atts);

		applicationRelease.setApplication(application);
		applicationRelease.setVersion("1.0");

		assertEquals(applicationRelease.getApplication().getName(),
				APPLICATION_NAME);

	}

	/**
	 * /** Test Artifact class
	 * 
	 * @return
	 */
	public void testArtifact() {

		Attribute metadata = new Attribute("metadata1", "value1");
		Attribute metadata2 = new Attribute();
		metadata2.setKey("metadata2");
		metadata2.setKey("value2");
		java.util.List<Attribute> metadatas = new ArrayList<Attribute>();
		metadatas.add(metadata);

		ProductRelease productRelease = new ProductRelease();

		Product product = new Product();
		product.setName("productName");
		product.setDescription("description");
		product.setMetadata(metadatas);
		productRelease.setProduct(product);
		productRelease.setVersion("1.0");

		Attribute att = new Attribute("key1", "value1", "description1");
		Attribute att2 = new Attribute("key2", "value2", "description2");
		java.util.List<Attribute> atts = new ArrayList<Attribute>();
		atts.add(att);
		atts.add(att2);
		product.setAttributes(atts);

		assertEquals(productRelease.getProduct().getName(), "productName");
		assertEquals(productRelease.getProduct().getDescription(),
				"description");
		assertEquals(productRelease.getVersion(), "1.0");
		assertSame(att.equals(att2), false);

		for (int i = 0; i < productRelease.getProduct().getAttributes().size(); i++) {
			assertEquals(productRelease.getProduct().getAttributes().get(i)
					.getKey(), "key" + (i + 1));
			assertEquals(productRelease.getProduct().getAttributes().get(i)
					.getValue(), "value" + (i + 1));
			assertEquals(productRelease.getProduct().getAttributes().get(i)
					.getDescription(), "description" + (i + 1));
		}

		for (int i = 0; i < productRelease.getProduct().getMetadata().size(); i++) {
			assertEquals(productRelease.getProduct().getMetadata().get(i)
					.getKey(), "metadata" + (i + 1));
			assertEquals(productRelease.getProduct().getMetadata().get(i)
					.getValue(), "value" + (i + 1));
		}
	}

	/**
	 * Test Attributes class. IT is required for the client API
	 * 
	 * @return
	 */
	public void testAttributes() {

		Attribute att = new Attribute("key1", "value1", "description1");

		Attributes atts = new Attributes();
		atts.add(att);
		assertEquals(atts.getAttributes().size(), 1);

	}

}
