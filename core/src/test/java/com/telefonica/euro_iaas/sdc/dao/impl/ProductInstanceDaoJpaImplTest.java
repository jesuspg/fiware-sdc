/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
        assertEquals(paux.getArtifacts().get(0).getName(), "name");

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
