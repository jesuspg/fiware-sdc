package com.telefonica.euro_iaas.sdc.rest.validation;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import org.junit.Before;
import org.junit.Test;

public class ProductResourceValidatorImplTest extends ValidatorUtils {

    private ProductResourceValidatorImpl productResourceValidator;
    private ProductReleaseDto productReleaseDto;

    ReleaseDto releaseDto;

    @Before
    public void setUp() throws Exception {

        productResourceValidator = new ProductResourceValidatorImpl();

        releaseDto = new ReleaseDto();
        releaseDto.setName("abcd");
        releaseDto.setVersion("0.1.1");

        productReleaseDto = new ProductReleaseDto();

        productReleaseDto.setProductName(releaseDto.getName());
        productReleaseDto.setVersion(releaseDto.getVersion());
        productReleaseDto.setReleaseNotes("prueba ReelaseNotes");

        OS so = new OS("Debian", "95", "5.5", "Description");
        List<OS> supportedOS = Arrays.asList(so);
        productReleaseDto.setSupportedOS(supportedOS);

        Attribute privateAttribute = new Attribute("ssl_port", "8443", "The ssl listen port");
        Attribute privateAttributeII = new Attribute("port", "8080", "The listen port");

        List<Attribute> privateAttributes = Arrays.asList(privateAttribute, privateAttributeII);
        productReleaseDto.setPrivateAttributes(privateAttributes);
    }

    @Test
    public void testUpdateValidateKO() throws Exception {
        File recipes = createTempFile("recipes");
        File installable = createTempFile("installable");

        releaseDto.setName("sdc");
        releaseDto.setVersion("0.1");

        byte[] bytesRecipes = getByteFromFile(recipes);
        byte[] bytesInstallable = getByteFromFile(installable);

        deleteFile(recipes);
        deleteFile(installable);

        MultiPart multiPart = new MultiPart().bodyPart(new BodyPart(productReleaseDto, MediaType.APPLICATION_XML_TYPE))
                .bodyPart(new BodyPart(bytesRecipes, MediaType.APPLICATION_OCTET_STREAM_TYPE))
                .bodyPart(new BodyPart(bytesInstallable, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        /*
         * try{ productResourceValidator. validateUpdate(releaseDto, multiPart); Assert.fail(); }catch
         * (InvalidProductReleaseUpdateRequestException e){ //Expected Exception }
         */
    }

    @Test
    public void testUpdateValidateOK() throws Exception {
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

        // productResourceValidator.validateUpdate(releaseDto,multiPart);
    }
}
