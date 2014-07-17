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

package com.telefonica.euro_iaas.sdc.client;

import com.sun.jersey.api.client.Client;
import com.telefonica.euro_iaas.sdc.client.services.ChefClientService;
import com.telefonica.euro_iaas.sdc.client.services.ChefNodeService;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceSyncService;
import com.telefonica.euro_iaas.sdc.client.services.ProductReleaseService;
import com.telefonica.euro_iaas.sdc.client.services.ProductService;
import com.telefonica.euro_iaas.sdc.client.services.SdcClientConfig;
import com.telefonica.euro_iaas.sdc.client.services.TaskService;
import com.telefonica.euro_iaas.sdc.client.services.impl.ChefClientServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ChefNodeServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ProductInstanceServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ProductInstanceSyncServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ProductReleaseServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ProductServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.SdcClientConfigImp;
import com.telefonica.euro_iaas.sdc.client.services.impl.TaskServiceImpl;

public class SDCClient {

    private static SdcClientConfigImp client = new SdcClientConfigImp();

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
