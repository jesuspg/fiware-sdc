package com.telefonica.euro_iaas.sdc.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.rest.resources.ProductResourceImpl;
import com.telefonica.euro_iaas.sdc.rest.validation.ProductResourceValidator;

public class ProductResourceImplTest {
	public static String PRODUCT_NAME = "Product::server";
	public static String PRODUCT_VERSION = "Product::version";
	ProductResourceImpl productResource = null;

	@Before
	public void setUp() throws Exception {
		productResource = new ProductResourceImpl();
		ProductManager productManager = mock(ProductManager.class);
		ProductResourceValidator productResourceValidator = mock(ProductResourceValidator.class);
		productResource.setValidator(productResourceValidator);
		productResource.setProductManager(productManager);
		Product product = new Product(PRODUCT_NAME, "description");
		OS os = new OS("os1", "1", "os1 description", "v1");
		ProductRelease productRelease = new ProductRelease(PRODUCT_VERSION,
				"releaseNotes", null, product, Arrays.asList(os), null);

		when(productManager.insert(any(ProductRelease.class))).thenReturn(
				productRelease);

	}

	@Test
	public void testInsert() throws Exception {
		ProductReleaseDto productReleaseDto = new ProductReleaseDto();
		productReleaseDto.setProductName(PRODUCT_NAME);
		productReleaseDto.setProductDescription("yum 0.1.1 description");
		productReleaseDto.setVersion(PRODUCT_VERSION);
		productReleaseDto.setReleaseNotes("prueba ReelaseNotes");

		ProductRelease productRelease = productResource
				.insert(productReleaseDto);
		assertEquals(productRelease.getProduct().getName(), PRODUCT_NAME);
		assertEquals(productRelease.getVersion(), PRODUCT_VERSION);

	}

	public void testUpdate() throws Exception {

		// Client c = Client.create();
		// WebResource service = c.resource(BASE_URI);
		//
		// ProductReleaseDto productReleaseDto = new ProductReleaseDto();
		// productReleaseDto.setProductName("yum");
		// productReleaseDto.setProductDescription("yum 0.1.1 description update");
		// productReleaseDto.setVersion("0.1.1");
		// productReleaseDto.setReleaseNotes("yum update ReelaseNotes");
		//
		// OS os = new OS("Debian def 5.2", "Debian def 5.2","5.2");
		// List<OS> supportedOS = Arrays.asList(os);
		// productReleaseDto.setSupportedOS(supportedOS);
		//
		//
		// Attribute privateAttribute = new Attribute("ssl_port",
		// "8443", "The ssl listen port");
		// Attribute privateAttributeII = new Attribute("port",
		// "8080", "The listen port");
		//
		// List<Attribute> privateAttributes =
		// Arrays.asList(privateAttribute, privateAttributeII);
		// productReleaseDto.setPrivateAttributes(privateAttributes);
		//
		// //Second and third Multipart
		// File recipes =
		// new File ("E:\\TID\\desarrollo\\doc\\files\\yum-0.1.1.tar");
		// File installable =
		// //new File
		// ("E:\\TID\\desarrollo\\doc\\files\\install_chef_client_package.sh");
		// new File
		// ("E:\\TID\\desarrollo\\doc\\files\\installable-yum-0.1.1.tar");
		//
		// byte[] bytesRecipes = getByteFromFile(recipes);
		// byte[] bytesInstallable = getByteFromFile (installable);
		//
		// // Construct a MultiPart with three body parts
		// MultiPart multiPart = new MultiPart().
		// bodyPart(new BodyPart(productReleaseDto,
		// MediaType.APPLICATION_XML_TYPE)).
		// bodyPart(new BodyPart(bytesRecipes,
		// MediaType.APPLICATION_OCTET_STREAM_TYPE)).
		// bodyPart(new BodyPart(bytesInstallable,
		// MediaType.APPLICATION_OCTET_STREAM_TYPE));
		//
		// // POST the request
		// ClientResponse response = service.path("/").
		// type("multipart/mixed").post(ClientResponse.class, multiPart);
		// System.out.println("Response Status : " +
		// response.getEntity(String.class));
	}

	@Test
	public void testDelete() throws Exception {
		/*
		 * Client c = Client.create(); WebResource service = c.resource(BASE_URI
		 * + "/yum/release/0.1.1");
		 * 
		 * System.out.println("Before invoking service");
		 * 
		 * // Delete the resource service.path("/").
		 * type(MediaType.APPLICATION_XML).delete();
		 */}

	private byte[] getByteFromFile(File file) throws FileNotFoundException,
			IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}
}
