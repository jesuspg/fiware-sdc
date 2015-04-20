/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.rest.validation;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.junit.Before;
import org.junit.Test;

import com.telefonica.fiware.commons.dao.AlreadyExistsEntityException;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class ProductResourceValidatorImplTest extends ValidatorUtils {

    private ProductResourceValidatorImpl productResourceValidator;
    private ProductReleaseDto productReleaseDto;
    private Product product;
    private ProductManager productManager;
    private ProductReleaseManager productReleaseManager;
    private GeneralResourceValidatorImpl generalValidator;

    ReleaseDto releaseDto;

    @Before
    public void setUp() throws Exception {

        product = new Product();
        productResourceValidator = new ProductResourceValidatorImpl();
        productManager = mock(ProductManager.class);
        generalValidator = new GeneralResourceValidatorImpl();
        productReleaseManager = mock(ProductReleaseManager.class);
        productResourceValidator.setGeneralValidator(generalValidator);
        productResourceValidator.setProductManager(productManager);
        productResourceValidator.setProductReleaseManager(productReleaseManager);

        when(productManager.exist(any(String.class))).thenReturn(false);

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

        /*
         * List<Attribute> privateAttributes = Arrays.asList(privateAttribute,
         * privateAttributeII);
         * productReleaseDto.setPrivateAttributes(privateAttributes);
         */
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
         * try{ productResourceValidator. validateUpdate(releaseDto, multiPart);
         * Assert.fail(); }catch (InvalidProductReleaseUpdateRequestException
         * e){ //Expected Exception }
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

    @Test(expected = InvalidEntityException.class)
    public void testValidateNameWhenIsNull() throws Exception {
        String name = null;
        productResourceValidator.validateInsert(product);
    }

    @Test(expected = InvalidEntityException.class)
    public void testValidateNameWhenIsEmpty() throws Exception {
        String name = "";
        product.setName(name);
        productResourceValidator.validateInsert(product);
    }

    @Test(expected = InvalidEntityException.class)
    public void testValidateMetadataOPenPortError() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("open_ports", "22 80 ee");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    @Test
    public void testValidateMetadataRange() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("open_ports", "8000-8999");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    @Test
    public void testValidateMetadataRangeUdp() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("open_ports_udp", "8000-8999");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    @Test(expected = InvalidEntityException.class)
    public void testValidateMetadataRangeUdpError() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("open_ports_udp", "8000-100000000");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    @Test(expected = InvalidEntityException.class)
    public void testValidateMetadataRangeUdpError2() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("open_ports_udp", "ab-100000000");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    @Test(expected = InvalidEntityException.class)
    public void testValidateMetadataRangeUdpError3() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("open_ports_udp", "-23-1000");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    /**
     * It test the validation of udp ports where there data
     * is correct.
     * @throws Exception
     */
    @Test
    public void testValidateMetadataRangeUdpLimit() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("open_ports_udp", "0");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    /**
     * It test the validation of udp ports where there data
     * is correct.
     * @throws Exception
     */
    @Test
    public void testValidateMetadataRangeUdpLimit2() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("open_ports_udp", "65535");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    @Test
    public void testValidateMetadataOPenPortOK() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("open_ports", "22 80 111");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    @Test
    public void testValidateMetadataInstallatorOK() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("installator", "chef");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    @Test
    public void testValidateMetadataInstallatorPuppetOK() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("installator", "puppet");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    @Test(expected = InvalidEntityException.class)
    public void testValidateMetadataInstallatorError() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("installator", "pep");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    @Test(expected = InvalidEntityException.class)
    public void testValidateMetadataDependenceError() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("dependencies", "noexistproduct");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    @Test
    public void testValidatePublic() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("public", "yes");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    @Test(expected = InvalidEntityException.class)
    public void testValidatePublicError() throws Exception {
        String name = "t";
        product.setName(name);
        Metadata meta = new Metadata("public", "novalid");
        product.addMetadata(meta);
        productResourceValidator.validateInsert(product);
    }

    @Test(expected = InvalidEntityException.class)
    public void testValidateNameWhenIsLOngerThan256Characters() throws Exception {
        String name = "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
                + "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
                + "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
                + "12345678901234567";
        product.setName(name);
        productResourceValidator.validateInsert(product);
    }

    @Test
    public void testValidateProductReleasenOK() throws Exception {
        ProductRelease productRelease = new ProductRelease();
        product.setName("1");
        productRelease.setProduct(product);
        productRelease.setVersion("1.0");
        productResourceValidator.validateInsert(product);
    }

    @Test(expected = InvalidEntityException.class)
    public void testValidateVesrion128Characters() throws Exception {
        String version = "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
                + "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
                + "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
                + "12345678901234567";
        ProductReleaseDto productRelease = new ProductReleaseDto();
        productRelease.setProductName("1");
        productRelease.setVersion(version);
        when(productReleaseManager.load(any(Product.class), any(String.class))).thenThrow(
                new EntityNotFoundException(ProductRelease.class, "", productRelease));
        when(productManager.exist(any(String.class))).thenReturn(true);
        productResourceValidator.validateInsert("name", productRelease);
    }

    @Test
    public void testValidateProductRelase() throws Exception {

        product.setName("1");
        ProductReleaseDto productRelease = new ProductReleaseDto();

        productRelease.setProductName(product.getName());
        productRelease.setVersion("1.0");

        when(productManager.exist(any(String.class))).thenReturn(true);
        when(productReleaseManager.load(any(Product.class), any(String.class))).thenThrow(
                new EntityNotFoundException(ProductRelease.class, "", productRelease));
        productResourceValidator.validateInsert("name", productRelease);
    }

    @Test
    public void testValidateAttributes() throws InvalidEntityException, AlreadyExistsEntityException,
            InvalidProductException {
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES)).thenReturn(
                "Plain|IP|IPALL");

        productResourceValidator.setSystemPropertiesProvider(systemPropertiesProvider);

        Attribute attribute = new Attribute("ssl_port", "8443", "The ssl listen port", "Plain");

        product.setName("1");

        product.addAttribute(attribute);

        productResourceValidator.validateInsert(product);

        verify(systemPropertiesProvider, times(1)).getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES);
    }

    @Test(expected = InvalidProductException.class)
    public void testValidateAttributesBadType() throws InvalidEntityException, AlreadyExistsEntityException,
            InvalidProductException {
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES)).thenReturn(
                "Plain|IP|IPALL");

        productResourceValidator.setSystemPropertiesProvider(systemPropertiesProvider);

        Attribute attribute = new Attribute("ssl_port", "8443", "The ssl listen port", "XXXXX");

        product.setName("1");

        product.addAttribute(attribute);

        productResourceValidator.validateInsert(product);

        verify(systemPropertiesProvider, times(1)).getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES);
    }

    @Test
    public void testValidateAttributesSetDefaultValue() throws InvalidEntityException, AlreadyExistsEntityException,
            InvalidProductException {
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES)).thenReturn(
                "Plain|IP|IPALL");

        productResourceValidator.setSystemPropertiesProvider(systemPropertiesProvider);

        Attribute attribute = new Attribute("ssl_port", "8443", "The ssl listen port");

        product.setName("1");

        product.addAttribute(attribute);

        productResourceValidator.validateInsert(product);

        verify(systemPropertiesProvider, times(1)).getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES);
    }

    @Test(expected = InvalidProductException.class)
    public void testValidateAttributesBadType3() throws InvalidEntityException, AlreadyExistsEntityException,
            InvalidProductException {
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES)).thenReturn(
                "Plain|IP|IPALL");

        productResourceValidator.setSystemPropertiesProvider(systemPropertiesProvider);

        Attribute attribute = new Attribute("ssl_port", "8443", "The ssl listen port", "|");

        product.setName("1");

        product.addAttribute(attribute);

        productResourceValidator.validateInsert(product);

        verify(systemPropertiesProvider, times(1)).getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES);
    }
    
    @Test(expected = InvalidProductException.class)
    public void testValidateAttributesBadType4() throws InvalidEntityException, AlreadyExistsEntityException,
            InvalidProductException {
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES)).thenReturn(
                "Plain|IP|IPALL");

        productResourceValidator.setSystemPropertiesProvider(systemPropertiesProvider);

        Attribute attribute = new Attribute("ssl_port", "8443", "The ssl listen port", "Pl");

        product.setName("1");

        product.addAttribute(attribute);

        productResourceValidator.validateInsert(product);

        verify(systemPropertiesProvider, times(1)).getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES);
    }
    
    @Test(expected = InvalidProductException.class)
    public void testValidateAttributesBadType5() throws InvalidEntityException, AlreadyExistsEntityException,
            InvalidProductException {
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES)).thenReturn(
                "Plain|IP|IPALL");

        productResourceValidator.setSystemPropertiesProvider(systemPropertiesProvider);

        Attribute attribute = new Attribute("ssl_port", "8443", "The ssl listen port", "");

        product.setName("1");

        product.addAttribute(attribute);

        productResourceValidator.validateInsert(product);

        verify(systemPropertiesProvider, times(1)).getProperty(SystemPropertiesProvider.AVAILABLE_ATTRIBUTE_TYPES);
    }
}
