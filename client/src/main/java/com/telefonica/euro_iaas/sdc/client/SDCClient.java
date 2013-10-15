/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.client;

import com.sun.jersey.api.client.Client;
import com.telefonica.euro_iaas.sdc.client.services.ChefClientService;
import com.telefonica.euro_iaas.sdc.client.services.ChefNodeService;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceSyncService;
import com.telefonica.euro_iaas.sdc.client.services.ProductReleaseService;
import com.telefonica.euro_iaas.sdc.client.services.ProductService;
import com.telefonica.euro_iaas.sdc.client.services.TaskService;
import com.telefonica.euro_iaas.sdc.client.services.impl.ChefClientServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ChefNodeServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ProductInstanceServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ProductInstanceSyncServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ProductReleaseServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ProductServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.TaskServiceImpl;

public class SDCClient {

    private static Client client = new Client();

    /**
     * Get the service to work with product instances.
     * 
     * @param baseUrl
     *            the base url where the SDC is
     * @param mediaType
     *            the media type (application/xml or application/json)
     * @return the product instance service.
     */
    public ProductInstanceService getProductInstanceService(String baseUrl, String mediaType) {
        return new ProductInstanceServiceImpl(client, baseUrl, mediaType);
    }

    /**
     * Get the service to work with tasks.
     * 
     * @param baseUrl
     *            the base url where the SDC is
     * @param mediaType
     *            the media type (application/xml or application/json)
     * @return the TaskService
     */
    public TaskService getTaskService(String baseUrl, String mediaType) {
        return new TaskServiceImpl(client, baseUrl, mediaType);
    }

    /**
     * Get the service to work with tasks.
     * 
     * @param baseUrl
     *            the base url where the SDC is
     * @param mediaType
     *            the media type (application/xml or application/json)
     * @param maxWaiting
     *            the max time in ms the client will wait for a task
     * @param waitingPeriod
     *            the period of time in ms between retries to get the complete task (this period will increase this way
     *            t = n*waitingPeriod)
     * @return the TaskService
     */
    public TaskService getTaskService(String baseUrl, String mediaType, Long maxWaiting, Long waitingPeriod) {
        return new TaskServiceImpl(client, baseUrl, mediaType, waitingPeriod, maxWaiting);
    }

    /**
     * Get the service to work with product instances in a synchronous way.
     * 
     * @param baseUrl
     *            the base url where the SDC is
     * @param mediaType
     *            the media type (application/xml or application/json)
     * @return the synchronous productInstanceService
     */
    public ProductInstanceSyncService getProductInstanceSyncService(String baseUrl, String mediaType) {
        return new ProductInstanceSyncServiceImpl(getProductInstanceService(baseUrl, mediaType), getTaskService(
                baseUrl, mediaType));
    }

    /**
     * Get the service to work with product instances in a synchronous way.
     * 
     * @param baseUrl
     *            the base url where the SDC is
     * @param mediaType
     *            the media type (application/xml or application/json)
     * @param maxWaiting
     *            the max time in ms the client will wait for a task
     * @param waitingPeriod
     *            the period of time in ms between retries to get the complete task (this period will increase this way
     *            t = n*waitingPeriod)
     * @return the synchronous productInstanceService
     */
    public ProductInstanceSyncService getProductInstanceSyncService(String baseUrl, String mediaType, Long maxWaiting,
            Long waitingPeriod) {
        return new ProductInstanceSyncServiceImpl(getProductInstanceService(baseUrl, mediaType), getTaskService(
                baseUrl, mediaType, maxWaiting, waitingPeriod));
    }

    /**
     * Get the service to work with products in the catalog.
     * 
     * @param baseUrl
     *            the urle where the SDC is
     * @param mediaType
     *            the media type (application/xml or application/json)
     * @return the service.
     */
    public ProductService getProductService(String baseUrl, String mediaType) {
        return new ProductServiceImpl(client, baseUrl, mediaType);
    }
    
    /**
     * Get the service to work with product releases in the catalog.
     * 
     * @param baseUrl
     *            the urle where the SDC is
     * @param mediaType
     *            the media type (application/xml or application/json)
     * @return the service.
     */
    public ProductReleaseService getProductReleaseService(String baseUrl, String mediaType) {
        return new ProductReleaseServiceImpl(client, baseUrl, mediaType);
    }

    /**
     * Get the service to work with ChefNode.
     * 
     * @param baseUrl
     *            the url where the SDC is
     * @param mediaType
     *            the media type (application/xml or application/json)
     * @return the service.
     */
    public ChefNodeService getChefNodeService(String baseUrl, String mediaType) {
        return new ChefNodeServiceImpl(client, baseUrl, mediaType);
    }

    /**
     * Get the service to work with ChefClient.
     * 
     * @param baseUrl
     *            the url where the SDC is
     * @param mediaType
     *            the media type (application/xml or application/json)
     * @return the service.
     */
    public ChefClientService getChefClientService(String baseUrl, String mediaType) {
        return new ChefClientServiceImpl(client, baseUrl, mediaType);
    }
}
