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

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;

public class ProductResourceValidatorImplTest extends ValidatorUtils {

    private ProductResourceValidatorImpl productResourceValidator;
    private ProductReleaseDto productReleaseDto;
    private Product product;
    private GeneralResourceValidatorImpl generalValidator;
    
    ReleaseDto releaseDto;

    @Before
    public void setUp() throws Exception {

        product = new Product();
        productResourceValidator = new ProductResourceValidatorImpl();
        generalValidator = new GeneralResourceValidatorImpl();
        
        productResourceValidator.setGeneralValidator(generalValidator);
        
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

        /*List<Attribute> privateAttributes = Arrays.asList(privateAttribute, privateAttributeII);
        productReleaseDto.setPrivateAttributes(privateAttributes);*/
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
    
    @Test(expected=InvalidEntityException.class)
    public void testValidateNameWhenIsNull() throws Exception {
        String name=null;
        productResourceValidator.validateInsert(product);
    }
    
    @Test(expected=InvalidEntityException.class)
    public void testValidateNameWhenIsEmpty() throws Exception {
        String name="";
        product.setName(name);
        productResourceValidator.validateInsert(product);
    }
    
    @Test(expected=InvalidEntityException.class)
    public void testValidateNameWhenIsLOngerThan256Characters() throws Exception {
        String name=
             "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
             "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
             "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
             "12345678901234567";
        product.setName(name);
        productResourceValidator.validateInsert(product);
    }
}
