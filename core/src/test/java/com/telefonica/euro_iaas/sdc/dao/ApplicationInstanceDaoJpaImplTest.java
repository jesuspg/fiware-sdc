package com.telefonica.euro_iaas.sdc.dao;

import java.util.Arrays;
import java.util.List;

import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;

/**
 * Unit test for ApplicationInstanceDao
 *
 * @author Sergio Arroyo
 *
 */
public class ApplicationInstanceDaoJpaImplTest extends AbstractJpaDaoTest {

    private ProductInstanceDao productInstanceDao;
    private ProductReleaseDao productReleaseDao;
    private EnvironmentDao environmentDao;
    private EnvironmentInstanceDao environmentInstanceDao;
    private ProductDao productDao;
    private OSDao osDao;
    private ApplicationDao applicationDao;
    private ApplicationReleaseDao applicationReleaseDao;
    private ApplicationInstanceDao applicationInstanceDao;


    private ApplicationInstance sdc030Instance;
    private ApplicationInstance sdc040Instance;


    public void createElements() throws Exception {
        OS so = new OS("Debian", "95", "Debian 5", "5");
        so = osDao.create(so);
        List<OS> supportedSSOO = Arrays.asList(so);
        
        Product tomcat = new Product("tomcat", "tomcat J2EE container");
        tomcat.addAttribute(new Attribute("port", "8080",
        "The listen port"));
        tomcat.addAttribute(new Attribute("ssl_port", "8443",
        "The ssl listen port"));
        tomcat = productDao.create(tomcat);

        ProductRelease tomcat7 = new ProductRelease(
                "7", "Tomcat server 7", null, tomcat,
                supportedSSOO, null);
        tomcat7 = productReleaseDao.create(tomcat7);
        
        Environment tomcat7Env = new Environment(Arrays.asList(tomcat7));
        tomcat7Env = environmentDao.create(tomcat7Env);
        
        ProductRelease tomcat6 = new ProductRelease(
                "6", "Tomcat server 6", null, tomcat,
                supportedSSOO, null);
        tomcat6 = productReleaseDao.create(tomcat6);
        
        Environment tomcat6Env = new Environment(Arrays.asList(tomcat6));
        tomcat6Env = environmentDao.create(tomcat6Env);
        
        ProductRelease tomcat5 = new ProductRelease(
                "5.5", "Tomcat server 5.5", null, tomcat,
                supportedSSOO, Arrays.asList(tomcat6));
        tomcat5 = productReleaseDao.create(tomcat5);
        
        Environment tomcat5Env = new Environment(Arrays.asList(tomcat5));
        tomcat5Env = environmentDao.create(tomcat5Env);
        
        Environment tomcat567Env = new Environment(Arrays.asList(tomcat5, tomcat6, tomcat7));
        tomcat567Env = environmentDao.create(tomcat567Env);
        
        Application sdc = new Application(
                "sdc", "this application", "war");
        sdc = applicationDao.create(sdc);

        ApplicationRelease sdc030 = new ApplicationRelease(
                "1.0.0", "Add configuration functionallity", null, sdc,
                tomcat567Env, null);
        sdc030 = applicationReleaseDao.create(sdc030);

        ApplicationRelease sdc040 = new ApplicationRelease(
                "1.1.0", "Add update functionallity", null, sdc,
                tomcat567Env, Arrays.asList(sdc030));
        sdc040 = applicationReleaseDao.create(sdc040);

        //create the instances
        ProductInstance tomcat5Instance = new ProductInstance(tomcat6,
                Status.INSTALLED, new VM("fqn3", "ip3", "host3", "domain3"), "vdc");
        tomcat5Instance = productInstanceDao.create(tomcat5Instance);
        ProductInstance tomcat6Instance = new ProductInstance(tomcat6,
                Status.INSTALLED, new VM("fqn", "ip", "host", "domain"), "vdc");
        tomcat6Instance = productInstanceDao.create(tomcat6Instance);
        ProductInstance tomcat7Instance = new ProductInstance(tomcat7,
                Status.INSTALLED, new VM("fqn2", "ip2", "host2", "domain2"), "vdc");
        tomcat7Instance = productInstanceDao.create(tomcat7Instance);
        
       
        
        EnvironmentInstance envInstancet6 = new EnvironmentInstance (
        		tomcat6Env, 
        		Arrays.asList(tomcat6Instance));
        envInstancet6 = environmentInstanceDao.create(envInstancet6);   
        
        EnvironmentInstance envInstancet7 = new EnvironmentInstance (
        		tomcat7Env, 
        		Arrays.asList(tomcat7Instance));
        envInstancet7 = environmentInstanceDao.create(envInstancet7);
        
        sdc030Instance = new ApplicationInstance(sdc030,
        		envInstancet6, Status.INSTALLING,
                tomcat6Instance.getVm(), "vdc");
        sdc030Instance = applicationInstanceDao.create(sdc030Instance);
        
        sdc040Instance = new ApplicationInstance(sdc040,
        		envInstancet7, Status.ERROR,
                tomcat7Instance.getVm(), "vdc");
        sdc040Instance = applicationInstanceDao.create(sdc040Instance);

        Application wordpress = new Application(
                "wordpress", "this application", "php");
        wordpress = applicationDao.create(wordpress);

        ApplicationRelease wp5 = new ApplicationRelease(
                "5", "Add configuration functionallity", null, wordpress,
                tomcat567Env, null);
        wp5 = applicationReleaseDao.create(wp5);
        
        EnvironmentInstance envInstancet567 = new EnvironmentInstance (
        		tomcat567Env, 
        		Arrays.asList(tomcat5Instance,tomcat6Instance,tomcat7Instance));
        envInstancet7 = environmentInstanceDao.create(envInstancet567);
        
        ApplicationInstance wp5instance = new ApplicationInstance(wp5,
        		envInstancet567, Status.UNINSTALLED,
                tomcat7Instance.getVm(), "vdc");
        wp5instance = applicationInstanceDao.create(wp5instance);

    }


    public void testFindByProduct() throws Exception{
        createElements();
        ApplicationInstanceSearchCriteria criteria =
                new ApplicationInstanceSearchCriteria();
        criteria.setProduct(sdc040Instance.getEnvironmentInstance()
        		.getProductInstances().iterator().next());
        List<ApplicationInstance> instances =
                applicationInstanceDao.findByCriteria(criteria);
        assertEquals(2, instances.size());
        assertEquals(sdc040Instance, instances.iterator().next());
    }

    public void testFindByVM() throws Exception{
        createElements();
        ApplicationInstanceSearchCriteria criteria =
                new ApplicationInstanceSearchCriteria();
        criteria.setVm(new VM("fqn","ip", "host", "domain"));
        List<ApplicationInstance> instances = applicationInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(sdc030Instance, instances.iterator().next());
    }

    public void testFindByStatus() throws Exception{
        createElements();
        ApplicationInstanceSearchCriteria criteria =
                new ApplicationInstanceSearchCriteria();
        criteria.setStatus(Arrays.asList(Status.ERROR));
        List<ApplicationInstance> instances = applicationInstanceDao.findByCriteria(criteria);
        assertEquals(1, instances.size());
        assertEquals(sdc040Instance, instances.iterator().next());

        criteria.setStatus(Arrays.asList(Status.INSTALLING));
        instances = applicationInstanceDao.findByCriteria(criteria);

        assertEquals(1, instances.size());
        assertEquals(sdc030Instance, instances.iterator().next());

        criteria.setStatus(Arrays.asList(Status.INSTALLING, Status.UNINSTALLED));
        instances = applicationInstanceDao.findByCriteria(criteria);

        assertEquals(2, instances.size());
        assertEquals(sdc030Instance, instances.iterator().next());

        criteria.setStatus(Arrays.asList(Status.ERROR, Status.INSTALLING));
        instances = applicationInstanceDao.findByCriteria(criteria);

        assertEquals(2, instances.size());

        criteria.setStatus(Arrays.asList(Status.CONFIGURING, Status.INSTALLED));
        instances = applicationInstanceDao.findByCriteria(criteria);

        assertEquals(0, instances.size());
    }

    public void testFindByApplicationName() throws Exception{
        createElements();
        ApplicationInstanceSearchCriteria criteria =
                new ApplicationInstanceSearchCriteria();
        criteria.setApplicationName("sdc");
        List<ApplicationInstance> instances =
                applicationInstanceDao.findByCriteria(criteria);
        assertEquals(2, instances.size());
    }


    /**
     * @param productInstanceDao the productInstanceDao to set
     */
    public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }


    /**
     * @param productReleaseDao the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }
    
    /**
     * @param environmentDao the environmentDao to set
     */
    public void setEnvironmentDao(EnvironmentDao environmentDao) {
        this.environmentDao = environmentDao;
    }

    /**
     * @param environmentInstanceDao the environmentDao to set
     */
    public void setEnvironmentInstanceDao(EnvironmentInstanceDao environmentInstanceDao) {
        this.environmentInstanceDao = environmentInstanceDao;
    }
    
    /**
     * @param productDao the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }


    /**
     * @param osDao the osDao to set
     */
    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
    }


    /**
     * @param applicationDao the applicationDao to set
     */
    public void setApplicationDao(ApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }


    /**
     * @param applicationReleaseDao the applicationReleaseDao to set
     */
    public void setApplicationReleaseDao(ApplicationReleaseDao applicationReleaseDao) {
        this.applicationReleaseDao = applicationReleaseDao;
    }


    /**
     * @param applicationInstanceDao the applicationInstanceDao to set
     */
    public void setApplicationInstanceDao(
            ApplicationInstanceDao applicationInstanceDao) {
        this.applicationInstanceDao = applicationInstanceDao;
    }

}
