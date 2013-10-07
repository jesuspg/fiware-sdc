/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.bootstrap;

import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.telefonica.euro_iaas.commons.properties.PropertiesProvider;
import com.telefonica.euro_iaas.commons.properties.PropertiesProviderMBean;
import com.telefonica.euro_iaas.commons.properties.impl.PropertiesProviderFactoryImpl;
import com.telefonica.euro_iaas.commons.properties.mbeans.MBeanUtils;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;

/**
 * Load System Properties file and stores it in BD (not modify already stored properties).
 * 
 * @author Sergio Arroyo
 */
public class PropertiesMBeanRegisterBootstrap implements ServletContextListener {
    private static final Logger LOGGER = Logger.getAnonymousLogger();

    /**
     * Unregister the mbean.
     */

    public void contextDestroyed(ServletContextEvent event) {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        PropertiesProvider propertiesUtil = new PropertiesProviderFactoryImpl()
                .createPropertiesProvider((EntityManagerFactory) ctx.getBean("entityManagerFactory"));
        try {
            for (String namespace : propertiesUtil.getNamespaces()) {
                LOGGER.info("Unregistering mbean " + namespace);
                MBeanUtils.unregister(event.getServletContext().getContextPath() + ":service=SystemConfiguration-"
                        + namespace);
            }
        } catch (Exception e) {
            throw new SdcRuntimeException(e);
        }

    }

    /**
     * Stores every system properties in BD and show them by JMX
     */

    public void contextInitialized(ServletContextEvent event) {

        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());

        PropertiesProvider propertiesUtil = new PropertiesProviderFactoryImpl()
                .createPropertiesProvider((EntityManagerFactory) ctx.getBean("entityManagerFactory"));
        try {
            for (String namespace : propertiesUtil.getNamespaces()) {
                PropertiesProviderMBean mbean = new PropertiesProviderMBean(namespace, propertiesUtil);
                LOGGER.info("Registering mbean " + namespace);
                MBeanUtils.register(mbean, event.getServletContext().getContextPath() + ":service=SystemConfiguration-"
                        + namespace);
            }
        } catch (Exception e) {
            throw new SdcRuntimeException(e);
        }
    }
}
