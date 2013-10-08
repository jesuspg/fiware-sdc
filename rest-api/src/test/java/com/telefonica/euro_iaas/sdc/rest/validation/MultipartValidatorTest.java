/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.rest.validation;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

/**
 * @author Jesus M. Movilla
 */
public class MultipartValidatorTest extends ValidatorUtils {

    MultipartValidator multipartValidator;
    ProductReleaseDto productReleaseDto;

    @Before
    public void setUp() throws Exception {
        multipartValidator = new MultipartValidator();
        productReleaseDto = new ProductReleaseDto();

        productReleaseDto.setProductName("yum");
        productReleaseDto.setProductDescription("yum 0.1.1 description");
        productReleaseDto.setVersion("0.1.1");
        productReleaseDto.setReleaseNotes("prueba ReelaseNotes");

        OS os = new OS("Debian", "95", "Debian def 5.2", "5.2");
        List<OS> supportedOS = Arrays.asList(os);
        productReleaseDto.setSupportedOS(supportedOS);

        Attribute privateAttribute = new Attribute("ssl_port", "8443", "The ssl listen port");
        Attribute privateAttributeII = new Attribute("port", "8080", "The listen port");

        /*List<Attribute> privateAttributes = Arrays.asList(privateAttribute, privateAttributeII);
        productReleaseDto.setPrivateAttributes(privateAttributes);*/

        System.out.println("Fin setUp() ");
    }

    @Test
    public void testValidateMultipartisNull() throws Exception {
        MultiPart multiPart = null;

        try {
            multipartValidator.validateMultipart(multiPart, String.class);
            Assert.fail();
        } catch (InvalidMultiPartRequestException e) {
            // Expected Exception
        }

    }

    @Test
    public void testValidateMultipartSize() throws Exception {
        MultiPart multiPart = new MultiPart().bodyPart(new BodyPart("OK", MediaType.APPLICATION_JSON_TYPE));

        try {
            multipartValidator.validateMultipart(multiPart, String.class);
            Assert.fail();
        } catch (InvalidMultiPartRequestException e) {
            // Expected Exception
        }
    }

    @Test
    public void testValidateMultipartFirstEntityPart() throws Exception {
        String st = new String("Prueba");

        MultiPart multiPart = new MultiPart().bodyPart(new BodyPart(st, MediaType.APPLICATION_JSON_TYPE))
                .bodyPart(new BodyPart(productReleaseDto, MediaType.APPLICATION_JSON_TYPE))
                .bodyPart(new BodyPart(productReleaseDto, MediaType.APPLICATION_JSON_TYPE));

        System.out.println("Clase of type " + productReleaseDto.getClass() + " " + productReleaseDto.getProductName());
        try {
            multipartValidator.validateMultipart(multiPart, productReleaseDto.getClass());
            Assert.fail();
        } catch (InvalidMultiPartRequestException e) {
            // Expected Exception
        }
    }

    @Test
    public void testValidateMultipartSecondEntityPart() throws Exception {

        // Construct a MultiPart with three body parts
        MultiPart multiPart = new MultiPart().bodyPart(new BodyPart(productReleaseDto, MediaType.APPLICATION_XML_TYPE))
                .bodyPart(new BodyPart("OK", MediaType.APPLICATION_JSON_TYPE))
                .bodyPart(new BodyPart(productReleaseDto, MediaType.APPLICATION_JSON_TYPE));

        try {
            multipartValidator.validateMultipart(multiPart, productReleaseDto.getClass());
            Assert.fail();
        } catch (InvalidMultiPartRequestException e) {
            // Expected Exception
        }
    }

    @Test
    public void testValidateMultipartThirdEntityPart() throws Exception {

        File recipes = createTempFile("recipes");
        byte[] bytesRecipes = getByteFromFile(recipes);
        recipes.deleteOnExit();

        MultiPart multiPart = new MultiPart().bodyPart(new BodyPart(productReleaseDto, MediaType.APPLICATION_XML_TYPE))
                .bodyPart(new BodyPart(bytesRecipes, MediaType.APPLICATION_JSON_TYPE))
                .bodyPart(new BodyPart(productReleaseDto, MediaType.APPLICATION_JSON_TYPE));

        try {
            multipartValidator.validateMultipart(multiPart, productReleaseDto.getClass());
            Assert.fail();
        } catch (InvalidMultiPartRequestException e) {
            // Expected Exception
        }
    }

    @Test
    public void testMultiPartValidatorOK() throws Exception {
        File recipes = createTempFile("recipes");
        File installable = createTempFile("installable");

        byte[] bytesRecipes = getByteFromFile(recipes);
        byte[] bytesInstallable = getByteFromFile(installable);

        deleteFile(recipes);
        deleteFile(installable);

        MultiPart multiPart = new MultiPart()
                .bodyPart(new BodyPart(productReleaseDto, MediaType.APPLICATION_JSON_TYPE))
                .bodyPart(new BodyPart(bytesRecipes, MediaType.APPLICATION_OCTET_STREAM_TYPE))
                .bodyPart(new BodyPart(bytesInstallable, MediaType.APPLICATION_OCTET_STREAM_TYPE));

        // multipartValidator.validateMultipart(multiPart,
        // productReleaseDto.getClass());

    }
}
