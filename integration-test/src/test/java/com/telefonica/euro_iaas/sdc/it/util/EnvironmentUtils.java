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

package com.telefonica.euro_iaas.sdc.it.util;

import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.BASE_URL;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.MIME_TYPE;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;

import java.util.List;

import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.EnvironmentService;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

public class EnvironmentUtils {

    private SDCClient client = new SDCClient();
    private EnvironmentService service;

    public Environment load(String environmentName) throws ResourceNotFoundException {
        service = client.getEnvironmentService(getProperty(BASE_URL), getProperty(MIME_TYPE));
        return service.load(environmentName);
    }

    /**
     * Insert the environment
     * 
     * @param environmentName
     *            the name
     * @param environmentDescription
     *            the Description
     * @param productReleaseDtos
     * @return the Environment
     */
    public Environment insert(String environmentName, String environmentDescription,
            List<ProductReleaseDto> productReleaseDtos) {
        service = client.getEnvironmentService(getProperty(BASE_URL), getProperty(MIME_TYPE));

        EnvironmentDto instance = new EnvironmentDto();
        instance.setDescription(environmentDescription);
        instance.setName(environmentName);
        instance.setProducts(productReleaseDtos);

        return service.insert(instance);
    }

    /**
     * Update the environment
     * 
     * @param environmentName
     *            the name
     * @param environmentDescription
     *            the Description
     * @param productReleaseDtos
     * @return the Environment
     */
    public Environment update(String environmentName, String environmentDescription,
            List<ProductReleaseDto> productReleaseDtos) {

        service = client.getEnvironmentService(getProperty(BASE_URL), getProperty(MIME_TYPE));

        EnvironmentDto instance = new EnvironmentDto();
        instance.setDescription(environmentDescription);
        instance.setName(environmentName);
        instance.setProducts(productReleaseDtos);

        return service.update(instance);
    }

    /**
     * Delete the environment
     * 
     * @param environmentName
     *            the environment name
     */
    public void delete(String envName) {
        service = client.getEnvironmentService(getProperty(BASE_URL), getProperty(MIME_TYPE));
        service.delete(envName);
    }
}
