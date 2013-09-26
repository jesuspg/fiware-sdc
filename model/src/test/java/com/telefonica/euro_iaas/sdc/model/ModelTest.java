package com.telefonica.euro_iaas.sdc.model;

import java.util.ArrayList;

import junit.framework.TestCase;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

public class ModelTest extends TestCase {

	/**
	 * Test Product Release class
	 * 
	 * @return
	 */

	public void testProductRelease() {
		ProductRelease productRelease = new ProductRelease();
		Attribute metadata = new Attribute("metadata1", "value1");
		Attribute metadata2 = new Attribute();
		metadata2.setKey("metadata2");
		metadata2.setKey("value2");
		java.util.List<Attribute> metadatas = new ArrayList<Attribute>();
		metadatas.add(metadata);

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
	 * Test Artifact class
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

}
