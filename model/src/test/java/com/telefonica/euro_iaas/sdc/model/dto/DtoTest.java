package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.ArrayList;
import java.util.Map;

import junit.framework.TestCase;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.VM;


public class DtoTest extends TestCase{
	
	public void testProductInstanceDto ()
	{
		ReleaseDto productReleaseDto = new ReleaseDto ("productName",
				"productDescription", "version");
		
		Attribute att = new Attribute ("key1","value1");
		Attribute att2 = new Attribute ();
		att2.setKey("key2");
		att2.setValue("value2");
		
		java.util.List<Attribute> atts = new ArrayList<Attribute>  ();
		atts.add(att);
		atts.add(att2);
		
		VM vm = new VM ();
		vm.setIp("10.33.22.33");
		ProductInstanceDto productInstanceDto = new ProductInstanceDto ();
		productInstanceDto.setVdc("vdc");
		productInstanceDto.setVm(vm);
		productInstanceDto.setProduct(productReleaseDto);
		productInstanceDto.setAttributes(atts);
		
		
		assertEquals(productInstanceDto.getVm().getIp(), "10.33.22.33");
		assertEquals(productInstanceDto.getVdc(), "vdc");
		assertEquals(productInstanceDto.getAttributes().size(), 2);
		
		for (int i= 0; i<productInstanceDto.getAttributes().size(); i++) {
			assertEquals(productInstanceDto.getAttributes().get(i).getKey(), "key"+(i+1));
			assertEquals(productInstanceDto.getAttributes().get(i).getValue(), "value"+(i+1));
		}
	}
	
	public void testReleaseDto ()
	{
		ReleaseDto productReleaseDto = new ReleaseDto ();
		productReleaseDto.setName("name");
		productReleaseDto.setType("type");
		productReleaseDto.setVersion("1.0");

		
		assertEquals(productReleaseDto.getName (), "name");
		assertEquals(productReleaseDto.getType (), "type");
		assertEquals(productReleaseDto.getVersion(), "1.0");
		
	}
	
	public void testProductReleaseDto ()
	{
		Attribute att = new Attribute ("key1","value1");
		java.util.List<Attribute> atts = new ArrayList<Attribute>  ();
		atts.add(att);
		
		VM vm = new VM ();
		vm.setIp("10.33.22.33");
		ProductReleaseDto productReleaseDto = new ProductReleaseDto ("productName",
				"productDescription", "version", "notas", atts, null, null);

		assertEquals(productReleaseDto.getProductName(), "productName");
		assertEquals(productReleaseDto.getProductDescription(), "productDescription");
		assertEquals(productReleaseDto.getVersion(), "version");
		
		productReleaseDto.setProductName("productName2");
		productReleaseDto.setProductDescription("productDescription2");
		productReleaseDto.setVersion("version2");
		

		assertEquals(productReleaseDto.getProductName(), "productName2");
		assertEquals(productReleaseDto.getProductDescription(), "productDescription2");
		assertEquals(productReleaseDto.getVersion(), "version2");
		
	}
	
	public void testArtifactDto ()
	{
		Attribute att = new Attribute ("key1","value1");
		java.util.List<Attribute> atts = new ArrayList<Attribute>  ();
		atts.add(att);
		ArtifactDto artifact = new ArtifactDto ();
		artifact.setAttributes(atts);
		artifact.setName("artefacto");
		
		ArtifactDto artifact2 = new ArtifactDto ("artefacto", atts);
		Attribute att2 = new Attribute ("key2","value2");
		artifact2.addAttribute(att2);
	
		
		for (int i= 0; i<artifact2.getAttributes().size(); i++) {
			assertEquals(artifact.getAttributes().get(i).getKey(), "key"+(i+1));
			assertEquals(artifact.getAttributes().get(i).getValue(), "value"+(i+1));
		}
		assertEquals(artifact.getName(), "artefacto");
		
		Map<String, String> attributes = artifact.getMapAttributes();
		assertEquals(attributes.get("key2"), "value2");
		
	}
	
	public void testApplicationInstanceDto ()
	{
		Attribute att = new Attribute ("key1","value1");
		java.util.List<Attribute> atts = new ArrayList<Attribute>  ();
		atts.add(att);
		VM vm = new VM("ip", "hostname", "domain");
		ApplicationInstanceDto applicationInstance = new ApplicationInstanceDto ();
		applicationInstance.setApplicationName("applicationName");
		applicationInstance.setVersion("1.0");
		applicationInstance.setAttributes(atts);		
		applicationInstance.setVm(vm);


		for (int i= 0; i<applicationInstance.getAttributes().size(); i++) {
			assertEquals(applicationInstance.getAttributes().get(i).getKey(), "key"+(i+1));
			assertEquals(applicationInstance.getAttributes().get(i).getValue(), "value"+(i+1));
		}
		assertEquals(applicationInstance.getApplicationName(), "applicationName");
		assertEquals(applicationInstance.getVersion(), "1.0");
		assertEquals(applicationInstance.getVm().getHostname(), "hostname");
	}
	
	public void testApplicationReleaseDto ()
	{
		Attribute att = new Attribute ("key1","value1");
		java.util.List<Attribute> atts = new ArrayList<Attribute>  ();
		atts.add(att);
		ProductReleaseDto productReleaseDto = new ProductReleaseDto ("productName",
				"productDescription", "version", "notas", atts, null, null);
		java.util.List<ProductReleaseDto> productsRelease = new ArrayList<ProductReleaseDto>  ();
		productsRelease.add(productReleaseDto);
		
		
		
		EnvironmentDto env = new EnvironmentDto ();
		env.setDescription("description");
		env.setName("env name");
		env.setProducts(productsRelease);
		
		ApplicationReleaseDto applicationRelease = new ApplicationReleaseDto ();
		applicationRelease.setApplicationName("applicationName");
		applicationRelease.setApplicationType("type");
		applicationRelease.setApplicationDescription("descrition");
		applicationRelease.setEnvironmentDto(env);
		applicationRelease.setReleaseNotes("notes");
		
		applicationRelease.setVersion("1.0");
		applicationRelease.setPrivateAttributes(atts)	;
		

		for (int i= 0; i<applicationRelease.getPrivateAttributes().size(); i++) {
			assertEquals(applicationRelease.getPrivateAttributes().get(i).getKey(), "key"+(i+1));
			assertEquals(applicationRelease.getPrivateAttributes().get(i).getValue(), "value"+(i+1));
		}
		assertEquals(applicationRelease.getApplicationName(), "applicationName");
		assertEquals(applicationRelease.getVersion(), "1.0");

	}
	
	public void testEnvironmentAndInstanceDto ()
	{
		EnvironmentInstanceDto envInst = new EnvironmentInstanceDto ();
		
	}
	
	
	
	
	

}
