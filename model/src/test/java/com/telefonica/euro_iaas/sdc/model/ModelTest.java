package com.telefonica.euro_iaas.sdc.model;

import java.util.ArrayList;

import junit.framework.TestCase;

import com.telefonica.euro_iaas.sdc.model.dto.VM;


public class ModelTest extends TestCase{
	
	public void testProductRelease ()
	{
		ProductRelease productRelease = new ProductRelease (); 
		Attribute metadata = new Attribute ("metadata1","value1");
		Attribute metadata2 = new Attribute ();
		metadata2.setKey("metadata2");
		metadata2.setKey("value2");
		java.util.List<Attribute> metadatas = new ArrayList<Attribute>  ();
		metadatas.add(metadata);
	
		Product product = new Product ();
		product.setName("productName");
		product.setDescription("description");
		product.setMetadata(metadatas);
		productRelease.setProduct(product);
		productRelease.setVersion("1.0");
		Attribute att = new Attribute ("key1","value1");
		java.util.List<Attribute> atts = new ArrayList<Attribute>  ();
		atts.add(att);
		product.setAttributes(atts);
		


		assertEquals(productRelease.getProduct().getName(), "productName");
		assertEquals(productRelease.getProduct().getDescription(), "description");
		assertEquals(productRelease.getVersion(), "1.0");
		
		for (int i= 0; i<productRelease.getProduct().getAttributes().size(); i++) {
			assertEquals(productRelease.getProduct().getAttributes().get(i).getKey(), "key"+(i+1));
			assertEquals(productRelease.getProduct().getAttributes().get(i).getValue(), "value"+(i+1));
		}
		
		for (int i= 0; i<productRelease.getProduct().getMetadata().size(); i++) {
			assertEquals(productRelease.getProduct().getMetadata().get(i).getKey(), "metadata"+(i+1));
			assertEquals(productRelease.getProduct().getMetadata().get(i).getValue(), "value"+(i+1));
		}
	}
	
	
	
	

}
