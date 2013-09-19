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
import com.telefonica.euro_iaas.sdc.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
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
        ApplicationReleaseDao applicationReleaseDao =
            (ApplicationReleaseDao) ctx.getBean("applicationReleaseDao");

        ApplicationDao applicationDao =
            (ApplicationDao) ctx.getBean("applicationDao");
        try {
            soDao.load("debian5");
        } catch (EntityNotFoundException e) {
            try {
                OS so = new OS("debian5", "Debian 5", "5");
                so = soDao.create(so);
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

                ProductRelease tomcat6 = new ProductRelease(
                        "6", "Tomcat server 6", null, tomcat,
                        supportedSSOO, null);
                tomcat6 = productReleaseDao.create(tomcat6);

                ProductRelease tomcat5 = new ProductRelease(
                        "5.5", "Tomcat server 5.5", null, tomcat,
                        supportedSSOO, Arrays.asList(tomcat6));
                tomcat5 = productReleaseDao.create(tomcat5);

                tomcat5.setTransitableReleases(Arrays.asList(tomcat6));
                tomcat6.setTransitableReleases(Arrays.asList(tomcat5, tomcat7));
                tomcat6 = productReleaseDao.update(tomcat6);

                tomcat7.setTransitableReleases(Arrays.asList(tomcat6));
                tomcat7 = productReleaseDao.update(tomcat7);

                Product postgresql = new Product("postgresql",
                        "db manager");
                postgresql.addAttribute(new Attribute("username", "postgres",
                        "The administrator usename"));
                postgresql.addAttribute(new Attribute("password", "postgres",
                        "The administrator password"));

                postgresql = productDao.create(postgresql);

                ProductRelease postgres84 = new ProductRelease(
                        "8.4", "blah blah blah", null, postgresql,
                        supportedSSOO, null);
                postgres84 = productReleaseDao.create(postgres84);

                ProductRelease postgres83 = new ProductRelease(
                        "8.3", "blah blah blah", null, postgresql,
                        supportedSSOO, Arrays.asList(postgres84));
                postgres83 = productReleaseDao.create(postgres83);

                Application sdc = new Application(
                        "sdc", "this application", "war");

                sdc.addAttribute(new Attribute("tomcat_home", "/opt/apache-tomcat-7.0.12",
                "the url where CATALINA is installed"));
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
                        Arrays.asList(postgres84,tomcat6), null);
                sdc030 = applicationReleaseDao.create(sdc030);

                ApplicationRelease sdc040 = new ApplicationRelease(
                        "1.1.0", "Add update functionallity", null, sdc,
                        Arrays.asList(postgres84, tomcat5),
                        Arrays.asList(sdc030));
                sdc040 = applicationReleaseDao.create(sdc040);

                sdc030.addTransitableRelease(sdc040);
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
