/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.ArrayList;
import java.util.Map;

import com.telefonica.euro_iaas.sdc.model.Attribute;
import junit.framework.TestCase;
import org.junit.Before;

public class DtoTest extends TestCase {

    public static String KEY1 = "key1";
    public static String KEY2 = "key2";
    public static String VALUE1 = "value1";
    public static String VALUE2 = "value2";
    public static String VDC = "vdc";

    java.util.List<Attribute> atts = null;

    ProductInstanceDto productInstanceDto = null;

    @Before
    public void setUp() {
        Attribute att = new Attribute(KEY1, VALUE1);
        Attribute att2 = new Attribute();
        att2.setKey(KEY2);
        att2.setValue(VALUE2);

        atts = new ArrayList<Attribute>();
        atts.add(att);
        atts.add(att2);
    }

    /**
     * Test Product Instance Dto class
     * 
     * @return
     */

    public void testProductInstanceDto() {
        ReleaseDto productReleaseDto = new ReleaseDto("productName", "productDescription", "version");

        VM vm = new VM();
        vm.setIp("10.33.22.33");
        productInstanceDto = new ProductInstanceDto();
        productInstanceDto.setVdc(VDC);
        productInstanceDto.setVm(vm);
        productInstanceDto.setProduct(productReleaseDto);
        productInstanceDto.setAttributes(atts);

        assertEquals(productInstanceDto.getVm().getIp(), "10.33.22.33");
        assertEquals(productInstanceDto.getVdc(), VDC);
        assertEquals(productInstanceDto.getAttributes().size(), 2);

        for (int i = 0; i < productInstanceDto.getAttributes().size(); i++) {
            assertEquals(productInstanceDto.getAttributes().get(i).getKey(), "key" + (i + 1));
            assertEquals(productInstanceDto.getAttributes().get(i).getValue(), "value" + (i + 1));
        }
    }

    /**
     * Test Release Dto class
     * 
     * @return
     */

    public void testReleaseDto() {
        ReleaseDto productReleaseDto = new ReleaseDto();
        productReleaseDto.setName("name");
        productReleaseDto.setType("type");
        productReleaseDto.setVersion("1.0");

        assertEquals(productReleaseDto.getName(), "name");
        assertEquals(productReleaseDto.getType(), "type");
        assertEquals(productReleaseDto.getVersion(), "1.0");

    }

    /**
     * Test Product Release Dto class
     * 
     * @return
     */
    public void testProductReleaseDto() {

        VM vm = new VM();
        vm.setIp("10.33.22.33");
        ProductReleaseDto productReleaseDto = new ProductReleaseDto("productName", "productDescription", "version",
                "notas", atts, null, null, null);

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

    /**
     * Test Artifact Dto class
     * 
     * @return
     */
    public void testArtifactDto() {

        ArtifactDto artifact2 = new ArtifactDto("artefacto", atts);
        Attribute att2 = new Attribute(KEY1, VALUE1);
        artifact2.addAttribute(att2);

        assertEquals(artifact2.getAttributes().get(0).getKey(), KEY1);
        assertEquals(artifact2.getAttributes().get(0).getValue(), VALUE1);

        assertEquals(artifact2.getName(), "artefacto");

        Map<String, String> attributes = artifact2.getMapAttributes();
        assertEquals(attributes.get(KEY1), VALUE1);
    }
}
