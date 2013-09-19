package com.telefonica.euro_iaas.sdc.bootstrap;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationDao;
import com.telefonica.euro_iaas.sdc.dao.EnvironmentDao;
import com.telefonica.euro_iaas.sdc.dao.NodeCommandDao;
import com.telefonica.euro_iaas.sdc.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.NodeCommand;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Populates data base with synthetic data to emulate the preconditions of SO
 * management, Image deployable management and customer management.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
// TODO delete this class when the preconditions are done.
public class InitDbBootstrap implements ServletContextListener {

    /** {@inheritDoc} */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext ctx = WebApplicationContextUtils
                .getWebApplicationContext(event.getServletContext());
        OSDao soDao = (OSDao) ctx.getBean("osDao");
        ProductDao productDao = (ProductDao) ctx.getBean("productDao");
        ProductReleaseDao productReleaseDao =
            (ProductReleaseDao) ctx.getBean("productReleaseDao");
        EnvironmentDao environmentDao =
                (EnvironmentDao) ctx.getBean("environmentDao");
        
        ApplicationReleaseDao applicationReleaseDao =
            (ApplicationReleaseDao) ctx.getBean("applicationReleaseDao");

        ApplicationDao applicationDao =
            (ApplicationDao) ctx.getBean("applicationDao");
        NodeCommandDao nodeCommandDao = 
        		(NodeCommandDao) ctx.getBean("nodeCommandDao");
        
        try {
            soDao.load("95");
            soDao.load("94");
        } catch (EntityNotFoundException e) {
            try {
                OS so1 = new OS("95", "Debian", "Debian 5", "5");
                OS so2 = new OS("94", "Ubuntu", "Ubuntu 10.04", "10.04");
                so1 = soDao.create(so1);
                so2 = soDao.create(so2);
                List<OS> supportedSSOO1 = Arrays.asList(so1);
                List<OS> supportedSSOO2 = Arrays.asList(so2);
                List<OS> supportedSSOO12 = Arrays.asList(so1,so2);
                
                NodeCommand nodeCommandso11 = new NodeCommand(so1, "chefclient_command", "chef-client");
                nodeCommandso11 =nodeCommandDao.create(nodeCommandso11);
                NodeCommand nodeCommandso12 = new NodeCommand(so1, "hostname_command", "hostname");
                nodeCommandso12 = nodeCommandDao.create(nodeCommandso12);
                NodeCommand nodeCommandso13 = new NodeCommand(so1, "domain_command", "dnsdomainname");
                nodeCommandso13 = nodeCommandDao.create(nodeCommandso13);
                NodeCommand nodeCommandso14 = new NodeCommand(so1, "ipaddress_command", "hostname -I");
                nodeCommandso14 = nodeCommandDao.create(nodeCommandso14);
                NodeCommand nodeCommandso21 = new NodeCommand(so2, "chefclient_command", "chef-client");
                nodeCommandso21 = nodeCommandDao.create(nodeCommandso21);
                NodeCommand nodeCommandso22 = new NodeCommand(so2, "hostname_command", "hostname");
                nodeCommandso22 = nodeCommandDao.create(nodeCommandso22);
                NodeCommand nodeCommandso23 = new NodeCommand(so2, "domain_command", "dnsdomainname");
                nodeCommandso23 = nodeCommandDao.create(nodeCommandso23);
                NodeCommand nodeCommandso24 = new NodeCommand(so2, "ipaddress_command", "hostname -I");
                nodeCommandso24 = nodeCommandDao.create(nodeCommandso24);
                
                //Tomcat Product Releases
                Product tomcat = new Product("tomcat", "tomcat J2EE container");
                tomcat.addAttribute(new Attribute("port", "8080",
                "The listen port"));
                tomcat.addAttribute(new Attribute("ssl_port", "8443",
                "The ssl listen port"));
                tomcat = productDao.create(tomcat);

                ProductRelease tomcat7 = new ProductRelease(
                        "7", "Tomcat server 7", null, tomcat,
                        supportedSSOO2, null);
                tomcat7 = productReleaseDao.create(tomcat7);
                
                ProductRelease tomcat6 = new ProductRelease(
                        "6", "Tomcat server 6", null, tomcat,
                        supportedSSOO12, null);
                tomcat6 = productReleaseDao.create(tomcat6);
                
       
                ProductRelease tomcat5 = new ProductRelease(
                        "5.5", "Tomcat server 5.5", null, tomcat,
                        supportedSSOO1, Arrays.asList(tomcat6));
                tomcat5 = productReleaseDao.create(tomcat5);
               
                tomcat5.setTransitableReleases(Arrays.asList(tomcat6));
                tomcat5 = productReleaseDao.update(tomcat5);
                tomcat6.setTransitableReleases(Arrays.asList(tomcat5, tomcat7));
                tomcat6 = productReleaseDao.update(tomcat6);
                tomcat7.setTransitableReleases(Arrays.asList(tomcat6));
                tomcat7 = productReleaseDao.update(tomcat7);
                
                //Postgresql Product Releases
                Product postgresql = new Product("postgresql",
                        "db manager");
                postgresql.addAttribute(new Attribute("username", "postgres",
                        "The administrator usename"));
                postgresql.addAttribute(new Attribute("password", "postgres",
                        "The administrator password"));
                postgresql = productDao.create(postgresql);

                ProductRelease postgres84 = new ProductRelease(
                        "8.4", "blah blah blah", null, postgresql,
                        supportedSSOO12, null);
                postgres84 = productReleaseDao.create(postgres84);
                
  
                ProductRelease postgres83 = new ProductRelease(
                        "8.3", "blah blah blah", null, postgresql,
                        supportedSSOO1, Arrays.asList(postgres84));
                postgres83 = productReleaseDao.create(postgres83);
                
             
                //Wiki Product Releases
                Product mediawiki = new Product("mediawiki", "MediaWiki Product");
                mediawiki.addAttribute(new Attribute("wikiname", "Wiki to be shown",
                "The name of the wiki"));
                mediawiki.addAttribute(new Attribute("path", "/demo",
                "The url context to be displayed"));
                // mediawiki.addAttribute(new Attribute("logogif", "",
                //       "The url context to be displayed"));
                mediawiki = productDao.create(mediawiki);
                
                ProductRelease mediawiki1 = new ProductRelease(
                        "1.17.0", "Mediawiki 1.17.0", null, mediawiki,
                        supportedSSOO12, null);
                mediawiki1 = productReleaseDao.create(mediawiki1);
                

                //Enviroments
                Environment t5Env = new Environment(Arrays.asList(tomcat5));
                t5Env = environmentDao.create(t5Env);
                Environment t6Env = new Environment(Arrays.asList(tomcat6));
                t6Env = environmentDao.create(t6Env);
                Environment t7Env = new Environment(Arrays.asList(tomcat7));
                t7Env = environmentDao.create(t7Env);
                Environment p84Env = new Environment(Arrays.asList(postgres84));
                p84Env = environmentDao.create(p84Env);
                Environment p83Env = new Environment(Arrays.asList(postgres83));  
                p83Env = environmentDao.create(p83Env);
                Environment mw1Env = new Environment(Arrays.asList(mediawiki1));  
                mw1Env = environmentDao.create(mw1Env);
                
                Environment t7_p84Env = new Environment(Arrays.asList(tomcat7,postgres84));
                t7_p84Env = environmentDao.create(t7_p84Env);
                Environment t7_p83Env = new Environment(Arrays.asList(tomcat7,postgres83));
                t7_p83Env = environmentDao.create(t7_p83Env);
                Environment t6_p84Env = new Environment(Arrays.asList(tomcat6,postgres84));
                t6_p84Env = environmentDao.create(t6_p84Env);
                Environment t6_p83Env = new Environment(Arrays.asList(tomcat6,postgres83));
                t6_p83Env = environmentDao.create(t6_p83Env);
                Environment t5_p84Env = new Environment(Arrays.asList(tomcat5,postgres84));
                t5_p84Env = environmentDao.create(t5_p84Env);
                Environment t5_p83Env = new Environment(Arrays.asList(tomcat5,postgres83));
                t5_p83Env = environmentDao.create(t5_p83Env);
                
                
                //sdc Application
                Application sdc = new Application(
                        "sdc", "this application", "war");

               // sdc.addAttribute(new Attribute("tomcat_home", "/opt/apache-tomcat",
               // "the url where CATALINA is installed"));
                sdc.addAttribute(new Attribute("application_context", "sdc",
                        "the context where the application will be deployed"));

                sdc.addAttribute(new Attribute("sdc_home_scripts",
                        "/opt/sdc/scripts",
                        "the location where the scripts will be stored"));
                sdc.addAttribute(new Attribute("sdc_war_name",
                        "sdc-server-rest-api-0.1.2-SNAPSHOT.war",
                        "the war file"));

                sdc.addAttribute(new Attribute("driver_class_name",
                        "org.postgresql.Driver", "The db driver to determine the" +
                                "concrete DB manager"));
                sdc.addAttribute(new Attribute("postgresql_url",
                        "jdbc:postgresql://localhost:5432/sdc",
                        "The URL wher the database is."));
                sdc.addAttribute(new Attribute("postgresql_db_user", "postgres",
                "The administrator usename"));
                sdc.addAttribute(new Attribute("postgresql_db_password",
                        "postgres", "The administrator password"));
                sdc = applicationDao.create(sdc);

                ApplicationRelease sdc030 = new ApplicationRelease(
                        "1.0.0", "Add configuration functionallity", null, sdc,
                        t6_p84Env, null);
                sdc030 = applicationReleaseDao.create(sdc030);

               /* ApplicationRelease sdc040 = new ApplicationRelease(
                        "1.1.0", "Add update functionallity", null, sdc,
                        Arrays.asList(postgres84, tomcat5),
                        Arrays.asList(sdc030));
                sdc040 = applicationReleaseDao.create(sdc040);*/

                //sdc030.addTransitableRelease(sdc040);
                sdc030 = applicationReleaseDao.update(sdc030);


            } catch (AlreadyExistsEntityException e1) {
                throw new RuntimeException(e1);
            } catch (InvalidEntityException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

}
