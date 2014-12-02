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

package com.telefonica.euro_iaas.sdc.model;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
import junit.framework.TestCase;

public class ModelTest extends TestCase {

    public static String KEY1 = "key1";
    public static String KEY2 = "key2";
    public static String METADATA1 = "key1";
    public static String METADATA2 = "key2";
    public static String VALUE1 = "value1";
    public static String VALUE2 = "value2";
    public static String VDC = "vdc";
    public static String APPLICATION_NAME = "app";
    public static String APPLICATION_TYPE = "app";

    List<Metadata> metadatas = null;
    java.util.List<Attribute> atts = null;

    /**
     * Test Product Release class
     * 
     * @return
     */

    public void testProductRelease() {
        ProductRelease productRelease = new ProductRelease();
        Metadata metadata = new Metadata(METADATA1, VALUE1);
        Metadata metadata2 = new Metadata();
        metadata2.setKey(METADATA2);
        metadata2.setKey(VALUE2);
        metadatas = new ArrayList<Metadata>();
        metadatas.add(metadata);

        Product product = new Product();
        product.setName("productName");
        product.setDescription("description");
        product.setMetadatas(metadatas);
        productRelease.setProduct(product);
        productRelease.setVersion("1.0");

        Attribute att = new Attribute(KEY1, VALUE1, "description1");
        Attribute att2 = new Attribute(KEY2, VALUE2, "description2");
        atts = new ArrayList<Attribute>();
        atts.add(att);
        atts.add(att2);
        product.setAttributes(atts);

        assertEquals(productRelease.getProduct().getName(), "productName");
        assertEquals(productRelease.getProduct().getDescription(), "description");
        assertEquals(productRelease.getVersion(), "1.0");
        assertSame(att.equals(att2), false);

        for (int i = 0; i < productRelease.getProduct().getAttributes().size(); i++) {
            assertEquals(productRelease.getProduct().getAttributes().get(i).getKey(), "key" + (i + 1));
            assertEquals(productRelease.getProduct().getAttributes().get(i).getValue(), "value" + (i + 1));
            assertEquals(productRelease.getProduct().getAttributes().get(i).getDescription(), "description" + (i + 1));
        }

        for (int i = 0; i < productRelease.getProduct().getMetadatas().size(); i++) {
            assertEquals(productRelease.getProduct().getMetadatas().get(i).getKey(), "key" + (i + 1));
            assertEquals(productRelease.getProduct().getMetadatas().get(i).getValue(), "value" + (i + 1));
        }
    }

    /**
     * /** Test Artifact class
     * 
     * @return
     */
    public void testArtifact() {

        Metadata metadata = new Metadata("metadata1", "value1");
        Metadata metadata2 = new Metadata();
        metadata2.setKey("metadata2");
        metadata2.setKey("value2");
        java.util.List<Metadata> metadatas = new ArrayList<Metadata>();
        metadatas.add(metadata);

        ProductRelease productRelease = new ProductRelease();

        Product product = new Product();
        product.setName("productName");
        product.setDescription("description");
        product.setMetadatas(metadatas);
        productRelease.setProduct(product);
        productRelease.setVersion("1.0");

        Attribute att = new Attribute("key1", "value1", "description1");
        Attribute att2 = new Attribute("key2", "value2", "description2");
        java.util.List<Attribute> atts = new ArrayList<Attribute>();
        atts.add(att);
        atts.add(att2);
        product.setAttributes(atts);

        assertEquals(productRelease.getProduct().getName(), "productName");
        assertEquals(productRelease.getProduct().getDescription(), "description");
        assertEquals(productRelease.getVersion(), "1.0");
        assertSame(att.equals(att2), false);

        for (int i = 0; i < productRelease.getProduct().getAttributes().size(); i++) {
            assertEquals(productRelease.getProduct().getAttributes().get(i).getKey(), "key" + (i + 1));
            assertEquals(productRelease.getProduct().getAttributes().get(i).getValue(), "value" + (i + 1));
            assertEquals(productRelease.getProduct().getAttributes().get(i).getDescription(), "description" + (i + 1));
        }

        for (int i = 0; i < productRelease.getProduct().getMetadatas().size(); i++) {
            assertEquals(productRelease.getProduct().getMetadatas().get(i).getKey(), "metadata" + (i + 1));
            assertEquals(productRelease.getProduct().getMetadatas().get(i).getValue(), "value" + (i + 1));
        }
    }

    /**
     * Test Attributes class. IT is required for the client API
     * 
     * @return
     */
    public void testAttributes() {

        Attribute att = new Attribute("key1", "value1", "description1");

        Attributes atts = new Attributes();
        atts.add(att);
        assertEquals(atts.getAttributes().size(), 1);

    }

    /**
     * Test Metadatas class. IT is required for the client API
     * 
     * @return
     */
    public void testMetadatas() {

        Metadata metadata = new Metadata("key1", "value1", "description1");

        List<Metadata> metadatas = new ArrayList<Metadata>();
        metadatas.add(metadata);
        assertEquals(metadatas.size(), 1);

    }

    public void testGetMetadata() {

        Metadata metadata = new Metadata("key1", "value1", "description1");
        Metadata metadata2 = new Metadata("key2", "value2", "description1");

        Product product = new Product ("name", "description");
        product.addMetadata(metadata);
        product.addMetadata(metadata2);

        Metadata output = product.getMetadata("key1");
        assertNotNull(output);
        assertEquals("value1", output.getValue());
        assertEquals("key1", output.getKey());

    }

    public void testUpdateMetadata() {

        Metadata metadata = new Metadata("key1", "value1", "description1");
        Metadata metadata2 = new Metadata("key1", "value2", "description1");

        Product product = new Product ("name", "description");
        product.addMetadata(metadata);


        Metadata output = product.updateMetadata(metadata2);
        assertNotNull(output);
        assertEquals("value2", output.getValue());
        assertEquals("key1", output.getKey());

    }

    public void testDeleteMetadata() {

        Metadata metadata = new Metadata("key1", "value1", "description1");
        Metadata metadata2 = new Metadata("key2", "value2", "description1");

        Product product = new Product ("name", "description");
        product.addMetadata(metadata);
        product.addMetadata(metadata2);
        assertEquals(product.getMetadatas().size(),2);

        product.deleteMetadata(metadata.getKey());

        assertEquals(product.getMetadatas().size(),1);

    }
}
