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

import java.io.InputStream;
import java.util.List;

import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ProductService;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;


import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.BASE_URL;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.MIME_TYPE;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;

public class ProductUtils {

    private SDCClient client = new SDCClient();
    private ProductService service;

    public ProductRelease load(String productName, String version) throws ResourceNotFoundException {
        service = client.getProductService(getProperty(BASE_URL), getProperty(MIME_TYPE));
        return service.load(productName, version);
    }

    /**
     * Insert the product
     * 
     * @param productName
     *            the product name
     * @param version
     *            the version
     * @param transitableReleases
     * @param ip
     *            the ip where the product will be installed
     * @param vdc
     *            the vdc where the node is
     * @return the installed product
     * @throws MaxTimeWaitingExceedException
     *             if the installation takes more time than expected
     * @throws InvalidExecutionException
     *             if the product can not be installed
     */
    public ProductRelease add(String productName, String version, String description, String releaseNotes,
            List<Attribute> attributes, List<OS> supportedOS, List<ProductRelease> transitableReleases) {
        service = client.getProductService(getProperty(BASE_URL), getProperty(MIME_TYPE));

        ProductReleaseDto instance = new ProductReleaseDto(productName, description, version, releaseNotes, attributes,
                supportedOS, transitableReleases);
        InputStream cookbook = this.getClass().getResourceAsStream("/files/" + productName + version + "-cookbook.tar");
        InputStream binaries = this.getClass().getResourceAsStream("/files/" + productName + version + "-bin.tar");

        return service.add(instance, cookbook, binaries);
    }

    /**
     * Update the product
     * 
     * @param productName
     *            the product name
     * @param version
     *            the version
     * @param transitableReleases
     * @param ip
     *            the ip where the product will be installed
     * @param vdc
     *            the vdc where the node is
     * @return the installed product
     * @throws MaxTimeWaitingExceedException
     *             if the installation takes more time than expected
     * @throws InvalidExecutionException
     *             if the product can not be installed
     */
    public ProductRelease update(String productName, String version, String description, String releaseNotes,
            List<Attribute> attributes, List<OS> supportedOS, List<ProductRelease> transitableReleases) {
        service = client.getProductService(getProperty(BASE_URL), getProperty(MIME_TYPE));

        ProductReleaseDto instance = new ProductReleaseDto(productName, description, version, releaseNotes, attributes,
                supportedOS, transitableReleases);
        InputStream cookbook = this.getClass().getResourceAsStream("/files/" + productName + version + "-cookbook.tar");
        InputStream binaries = this.getClass().getResourceAsStream("/files/" + productName + version + "-bin.tar");

        return service.update(instance, cookbook, binaries);
    }

    /**
     * Delete the product
     * 
     * @param productName
     *            the product name
     * @param version
     *            the version
     * @throws MaxTimeWaitingExceedException
     *             if the installation takes more time than expected
     * @throws InvalidExecutionException
     *             if the product can not be installed
     */
    public void delete(String productName, String version) {
        service = client.getProductService(getProperty(BASE_URL), getProperty(MIME_TYPE));

        service.delete(productName, version);
    }
}
