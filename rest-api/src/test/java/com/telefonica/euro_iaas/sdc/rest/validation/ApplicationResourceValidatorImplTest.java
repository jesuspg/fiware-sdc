package com.telefonica.euro_iaas.sdc.rest.validation;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;

public class ApplicationResourceValidatorImplTest extends ValidatorUtils{

    private ApplicationResourceValidatorImpl applicationResourceValidator;
    private ApplicationReleaseDto applicationReleaseDto;

    ReleaseDto releaseDto;

    @Before
    public void setUp() throws Exception {

        applicationResourceValidator = new ApplicationResourceValidatorImpl();

        releaseDto = new ReleaseDto();
        releaseDto.setName("abcd");
        releaseDto.setVersion("0.1.1");

        applicationReleaseDto = new ApplicationReleaseDto();

        applicationReleaseDto.setApplicationName(releaseDto.getName());
        applicationReleaseDto.setApplicationDescription("yumabcd 0.1.1 description");
        applicationReleaseDto.setVersion(releaseDto.getVersion());
        applicationReleaseDto.setReleaseNotes("prueba ReelaseNotes");

        Product product = new Product();
        product.setName("tomcat");
        product.setDescription("tomcat Description");
        ProductRelease productRelease = new ProductRelease();
        productRelease.setProduct(product);
        productRelease.setVersion("0.1.1");
        
        //From ProductRelase to ProductReleaseDto
        List<ProductRelease> supporteProducts =
             Arrays.asList(productRelease);
        
        List<ProductReleaseDto> productReleaseDtos = new ArrayList<ProductReleaseDto>();        
        
        for (int i =0; i<supporteProducts.size(); i++){
    		ProductReleaseDto productReleaseDto = new ProductReleaseDto();
    		productReleaseDto.setProductName(supporteProducts.get(i).getProduct().getName());
    		productReleaseDto.setProductDescription(supporteProducts.get(i).getProduct().getDescription());
    		productReleaseDto.setPrivateAttributes(supporteProducts.get(i).getPrivateAttributes());
    		productReleaseDto.setReleaseNotes(supporteProducts.get(i).getReleaseNotes());
    		productReleaseDto.setSupportedOS(supporteProducts.get(i).getSupportedOOSS());
    		productReleaseDto.setTransitableReleases(supporteProducts.get(i).getTransitableReleases());
    		
    		productReleaseDtos.add(productReleaseDto);
    	}

        applicationReleaseDto.setEnvironmentDto(new EnvironmentDto(productReleaseDtos));

        Attribute privateAttribute = new Attribute("ssl_port",
            "8443", "The ssl listen port");
        Attribute privateAttributeII = new Attribute("port",
            "8080", "The listen port");

        List<Attribute> privateAttributes =
             Arrays.asList(privateAttribute, privateAttributeII);
        applicationReleaseDto.setPrivateAttributes(privateAttributes);


    }

    @Test
    public void testUpdateValidateKO () throws Exception
    {
        /*File recipes = createTempFile ("recipes");
        File installable = createTempFile ("installable");

        byte[] bytesRecipes = getByteFromFile(recipes);
        byte[] bytesInstallable = getByteFromFile (installable);

        deleteFile(recipes);
        deleteFile(installable);
        
        System.out.println("AplictionreleaseDto " + applicationReleaseDto.getApplicationName());
        MultiPart multiPart = new MultiPart().bodyPart(new BodyPart(
        		 applicationReleaseDto,MediaType.APPLICATION_XML_TYPE))
                 .bodyPart(new BodyPart(bytesRecipes,
                         MediaType.APPLICATION_OCTET_STREAM_TYPE))
                 .bodyPart(new BodyPart(bytesInstallable,
                          MediaType.APPLICATION_OCTET_STREAM_TYPE));*/
         
         //applicationResourceValidator.validateUpdate(multiPart);
         //Assert.fail();  

    }
    @Test
    public void testUpdateValidateOK () throws Exception
    {
        File recipes = createTempFile ("recipes");
        File installable = createTempFile ("installable");

        byte[] bytesRecipes = getByteFromFile(recipes);
        byte[] bytesInstallable = getByteFromFile (installable);

        deleteFile(recipes);
        deleteFile(installable);

         MultiPart multiPart = new MultiPart().
             bodyPart(new BodyPart(applicationReleaseDto,
                     MediaType.APPLICATION_JSON_TYPE)).
             bodyPart(new BodyPart(bytesRecipes,
                     MediaType.APPLICATION_OCTET_STREAM_TYPE)).
            bodyPart(new BodyPart(bytesInstallable,
                    MediaType.APPLICATION_OCTET_STREAM_TYPE));

         //applicationResourceValidator.validateUpdate(multiPart);
    }
}
