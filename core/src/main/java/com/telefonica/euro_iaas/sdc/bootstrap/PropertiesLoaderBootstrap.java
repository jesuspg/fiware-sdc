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

package com.telefonica.euro_iaas.sdc.bootstrap;

import java.util.Properties;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.telefonica.euro_iaas.commons.properties.PropertiesProvider;
import com.telefonica.euro_iaas.commons.properties.impl.PropertiesProviderFactoryImpl;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;

/**
 * Class in charge to persist the system properties in data base on bootstrap time.
 * 
 * @author Sergio Arroyo
 */
public class PropertiesLoaderBootstrap implements ServletContextListener {

    private static final Logger LOGGER = Logger.getAnonymousLogger();
    private static final String NAMESPACE = "/SystemConfiguration.properties";

    /**
     * {@inheritDoc}
     */

    public void contextDestroyed(ServletContextEvent arg0) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */

    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        EntityManagerFactory emf = (EntityManagerFactory) ctx.getBean("entityManagerFactory");

        PropertiesProvider propertiesProvider = new PropertiesProviderFactoryImpl().createPropertiesProvider(emf);
        Properties properties = propertiesProvider.load(NAMESPACE);
        try {
            LOGGER.info("store namespace: " + NAMESPACE);
            propertiesProvider.store(properties, NAMESPACE);
        } catch (Exception e) {
            throw new SdcRuntimeException(e);
        }
    }

}
