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

package com.telefonica.euro_iaas.sdc.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.sdc.dao.ArtifactDao;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;

/**
 * Unit test for ProductInstanceDaoJpaImpl
 * 
 * @author Sergio Arroyo
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class ProductInstanceDaoJpaImplTest {

    @Autowired
    private ProductInstanceDao productInstanceDao;
    @Autowired
    private ArtifactDao artifactDao;
    @Autowired
    private ProductDao productDao;

    @Autowired
    private OSDao osDao;
    @Autowired
    private ProductReleaseDao productReleaseDao;

    /**
     * Test the create and load method
     */
    @Test
    public void testCreate() throws Exception {

        Product product = new Product("myp22", "desc");

        product.addAttribute(new Attribute("clave", "valor"));
        product.addMetadata(new Metadata("metKey", "metValue"));

        productDao.create(product);

        ProductRelease release = new ProductRelease("v1", "releaseNotes1", product, osDao.findAll(), null);

        productReleaseDao.create(release);
        ProductInstance instance = new ProductInstance(release, Status.INSTALLED, new VM("fqn", "ip", "hostname",
                "domain"), "vdc");

        instance = productInstanceDao.create(instance);
        assertEquals(instance, productInstanceDao.load(instance.getId()));
    }

    /**
     * Test the create and load method
     */
    public void testLoad() throws Exception {

        ProductRelease release = productReleaseDao.findAll().get(0);
        ProductInstance instance = new ProductInstance(release, Status.INSTALLED, new VM("fqn", "ip", "hostname",
                "domain"), "vdc");
        instance.setName("name");

        assertEquals(0, productInstanceDao.findAll().size());
        instance = productInstanceDao.create(instance);
        assertEquals(instance, productInstanceDao.load(instance.getId()));
        assertEquals(1, productInstanceDao.findAll().size());
        assertEquals(instance.getArtifacts().size(), 0);
    }

    /**
     * Test the create and load method
     */
    public void testFindByHostnameAndDelete() throws Exception {

        Product product = new Product("p1", "desc");
        product.addAttribute(new Attribute("clave", "valor"));
        product.addMetadata(new Metadata("metKey", "metValue"));
        productDao.create(product);

        ProductRelease release = new ProductRelease("v1", "releaseNotes1", product, osDao.findAll(), null);
        ProductRelease createdRelease = productReleaseDao.create(release);
        VM host = new VM(null, "hostname", "domain");
        ProductInstance pi1 = new ProductInstance(createdRelease, Status.INSTALLED, host, "vdc");
        pi1 = productInstanceDao.create(pi1);

        List<ProductInstance> returned = productInstanceDao.findByHostname("hostname");
        assertEquals(returned.size(), 1);

        for (ProductInstance inst: returned) {
            productInstanceDao.remove(inst);
        }

        returned = productInstanceDao.findByHostname("hostname");
        assertEquals(returned.size(), 1);

    }


    /**
     * Test the create and load method
     */
    public void testLoadWithArtifacts() throws Exception {

        ProductRelease release = productReleaseDao.findAll().get(0);
        Artifact artifact = new Artifact();
        ProductInstance instance = new ProductInstance(release, Status.INSTALLED, new VM("fqn", "ip", "hostname",
                "domain"), "vdc");
        instance.addArtifact(artifact);

        assertEquals(0, productInstanceDao.findAll().size());
        instance = productInstanceDao.create(instance);
        assertEquals(instance, productInstanceDao.load(instance.getId()));
        assertEquals(instance.getArtifacts().size(), 1);
        assertEquals(1, productInstanceDao.findAll().size());
    }

    @Test
    public void testFindByCriteria() throws Exception {

        Product product = new Product("p1", "desc");

        product.addAttribute(new Attribute("clave", "valor"));
        product.addMetadata(new Metadata("metKey", "metValue"));

        productDao.create(product);

        ProductRelease release = new ProductRelease("v1", "releaseNotes1", product, osDao.findAll(), null);
        ProductRelease createdRelease = productReleaseDao.create(release);

        VM host = new VM(null, "hostname", "domain");
        VM host2 = new VM("fqn");

        ProductInstance pi1 = new ProductInstance(release, Status.INSTALLED, host, "vdc");

        ProductInstance pi2 = new ProductInstance(release, Status.UNINSTALLED, host2, "vdc");

        pi1 = productInstanceDao.create(pi1);
        pi2 = productInstanceDao.create(pi2);

        ProductInstanceSearchCriteria criteria = new ProductInstanceSearchCriteria();
        // find all
        List<ProductInstance> instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(2, instances.size());
        // find by Host1
        criteria.setVM(host);
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi1, instances.get(0));

        // find by Host2
        criteria.setVM(host2);
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi2, instances.get(0));

        // find by Status
        criteria.setVM(null);
        criteria.setStatus(Status.INSTALLED);
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi1, instances.get(0));

        criteria.setVm(host);
        criteria.setProductRelease(release);
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi1, instances.get(0));

        criteria.setVm(new VM(null, "hostname", "domain"));
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi1, instances.get(0));

        criteria.setProductName("asd");
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(0, instances.size());
        try {
            productInstanceDao.findUniqueByCriteria(criteria);
            fail("NotUniqueResultException expected");
        } catch (NotUniqueResultException e) {
            // it's ok, exception expected
        }

        criteria.setProductName("p1");
        instances = productInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(pi1, instances.get(0));
        assertEquals(pi1, productInstanceDao.findUniqueByCriteria(criteria));
    }

    @Test
    public void testProductInstanceArtifact() throws Exception {
        VM host = new VM("fqn", "hostname", "domain");
        VM host2 = new VM("fqn");

        Product product = new Product("myp", "desc");

        product.addAttribute(new Attribute("clave", "valor"));
        product.addMetadata(new Metadata("metKey", "metValue"));

        productDao.create(product);

        ProductRelease release = new ProductRelease("v1", "releaseNotes1", product, osDao.findAll(), null);
        ProductRelease createdRelease = productReleaseDao.create(release);

        ProductInstance pi1 = new ProductInstance(release, Status.INSTALLED, host, "vdc");
        pi1.setName("myp");

        ProductInstance pi2 = new ProductInstance(release, Status.UNINSTALLED, host2, "vdc2");

        pi1 = productInstanceDao.create(pi1);
        pi2 = productInstanceDao.create(pi2);
        pi2.setName("other");

        ProductInstance paux = productInstanceDao.findByProductInstanceName("myp");

        assertEquals(pi1, paux);

        Artifact artifact = new Artifact("name", "vdc", pi1, null);
        paux.addArtifact(artifact);
        artifact = artifactDao.create(artifact);

        paux = productInstanceDao.update(paux);

        ProductInstance pde = productInstanceDao.load("myp");
        assertEquals(pde, paux);
        // assertEquals(pde.getArtifacts().get(0).getName(), "name");

        ProductInstanceSearchCriteria criteria = new ProductInstanceSearchCriteria();
        criteria.setProductRelease(release);
        criteria.setVm(host);
        criteria.setProductName("myp");
        criteria.setVdc("vdc");
        // instances = productInstanceDao.findByCriteria(criteria);
        // assertEquals(1, instances.size());
        // assertEquals(pi1, instances.get(0));
        assertEquals(pi1, productInstanceDao.findUniqueByCriteria(criteria));

    }

    /**
     * @param productInstanceDao
     *            the productInstanceDao to set
     */
    public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }

    public void setArtifactDao(ArtifactDao artifactDao) {
        this.artifactDao = artifactDao;
    }

    /**
     * @param osDao
     *            the osDao to set
     */
    public void setOSDao(OSDao osDao) {
        this.osDao = osDao;
    }

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

}
