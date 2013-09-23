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
import com.telefonica.euro_iaas.sdc.dao.CustomerDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.Customer;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.OS;

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
        ApplicationDao applicationDao =
            (ApplicationDao) ctx.getBean("applicationDao");
        CustomerDao customerDao = (CustomerDao) ctx.getBean("customerDao");
        try {
            soDao.load("debian5");
        } catch (EntityNotFoundException e) {
            try {
                Customer customer = new Customer("demo");

                customerDao.create(customer);
                OS so = new OS("debian5", "Debian 5", "5");
                so = soDao.create(so);
                List<OS> supportedSSOO = Arrays.asList(so);
                Product tomcat = new Product("tomcat", "6", supportedSSOO);
                tomcat = productDao.create(tomcat);
                Product apache = new Product("apache2", "2", supportedSSOO);
                apache = productDao.create(apache);
                Product mysql = new Product("mysql", "5.11", supportedSSOO);
                mysql = productDao.create(mysql);
                Product mysql_server = new Product("mysql::server", "5.11 ", supportedSSOO);
                mysql_server = productDao.create(mysql_server);
                Product postgresql = new Product("postgresql", "8.3 or 8.4", supportedSSOO);
                postgresql = productDao.create(postgresql);
                Product postgresql_server = new Product("postgresql::server", "8.3 or 8.4", supportedSSOO);
                postgresql_server = productDao.create(postgresql_server);

                Application sdc = new Application("sdc", "this application",
                        Arrays.asList(postgresql_server, mysql_server, tomcat),
                "war");
                sdc = applicationDao.create(sdc);
                //
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
