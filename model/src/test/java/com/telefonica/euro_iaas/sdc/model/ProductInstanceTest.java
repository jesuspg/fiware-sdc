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
import java.util.Arrays;

import com.telefonica.euro_iaas.sdc.model.dto.VM;
import junit.framework.TestCase;

public class ProductInstanceTest extends TestCase {
    ProductRelease pr1 = null;
    VM vm = null;

    /**
     * Test Product Release class
     * 
     * @return
     */
    public void testProductRelease() {
        Product product = new Product("Product::server", "description");
        OS os = new OS("os1", "1", "os1 description", "v1");
        ProductRelease productRelease = new ProductRelease("version", "releaseNotes", product, Arrays.asList(os),
                null);

        assertEquals(productRelease.getProduct().getName(), "Product::server");
        assertEquals(productRelease.getVersion(), "version");

        vm = new VM("ip", "hostname", "domain");
        Product p1 = new Product("p1", "description");
        Product p2 = new Product("p2", "description");
        pr1 = new ProductRelease("version1", "releaseNotes1", p1, null, null);
        ProductRelease pr2 = new ProductRelease("version2", "releaseNotes2", p2, null, null);

        java.util.List<ProductInstance> products = Arrays.asList(new ProductInstance(pr1,
                ProductInstance.Status.INSTALLED, vm, "vdc"), new ProductInstance(pr2,
                ProductInstance.Status.INSTALLED, vm, "vdc"));

    }

    /**
     * Test Product Instance class
     * 
     * @return
     */
    public void testProductInstance() {
        Attribute att = new Attribute("key1", "value1", "description1");
        java.util.List<Attribute> atts = new ArrayList<Attribute>();
        atts.add(att);
        Artifact artifact = new Artifact("artifact", atts);

        Artifact artifact2 = new Artifact();
        artifact2.setName("artifact1");
        artifact2.addAttribute(att);

        ProductInstance productInstance = new ProductInstance();
        productInstance.addArtifact(artifact);
        productInstance.addArtifact(artifact2);
        productInstance.setName("productInstance");
        productInstance.setProductRelease(pr1);
        productInstance.setVdc("vdc");
        assertEquals(productInstance.getArtifacts().size(), 2);
        
        for (Artifact art: productInstance.getArtifacts()) {
            assertNotNull(art.getName());
        }
        

    }

}
