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

package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.Properties;
import javax.ws.rs.Path;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Default SystemConfigurationResource implementation
 * 
 * @author Sergio Arroyo
 */
@Path("/crm/configuration/properties")
@Component
@Scope("request")
public class SystemConfigurationResourceImpl implements SystemConfigurationResource {

    @InjectParam("systemPropertiesProvider")
    SystemPropertiesProvider propertiesProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public Attributes findAttributes() {
        Properties configuration = propertiesProvider.loadProperties();
        Attributes attributes = new Attributes();
        for (Object key : configuration.keySet()) {
            String strKey = (String) key;
            attributes.add(new Attribute(strKey, configuration.getProperty(strKey)));
        }
        return attributes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAttributes(Attributes attributes) {
        Properties configuration = new Properties();
        for (Attribute attribute : attributes) {
            configuration.put(attribute.getKey(), attribute.getValue());
        }
        propertiesProvider.setProperties(configuration);
    }

}
