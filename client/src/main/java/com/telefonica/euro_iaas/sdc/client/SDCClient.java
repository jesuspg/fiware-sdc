package com.telefonica.euro_iaas.sdc.client;

import com.sun.jersey.api.client.Client;
import com.telefonica.euro_iaas.sdc.client.services.ApplicationInstanceService;
import com.telefonica.euro_iaas.sdc.client.services.ApplicationInstanceSyncService;
import com.telefonica.euro_iaas.sdc.client.services.ApplicationService;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceSyncService;
import com.telefonica.euro_iaas.sdc.client.services.ProductService;
import com.telefonica.euro_iaas.sdc.client.services.TaskService;
import com.telefonica.euro_iaas.sdc.client.services.impl.ApplicationInstanceServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ApplicationInstanceSyncServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ApplicationServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ProductInstanceServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ProductInstanceSyncServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.ProductServiceImpl;
import com.telefonica.euro_iaas.sdc.client.services.impl.TaskServiceImpl;

public class SDCClient {

    private static Client client = new Client();

    /**
     * Get the service to work with product instances.
     * @param baseUrl the base url where the SDC is
     * @param mediaType the media type (application/xml or application/json)
     * @return the product instance service.
     */
    public ProductInstanceService getProductInstanceService(
            String baseUrl, String mediaType) {
        return new ProductInstanceServiceImpl(client, baseUrl, mediaType);
    }

    /**
     * Get the service to work with application instances.
     * @param baseUrl the base url where the SDC is
     * @param mediaType the media type (application/xml or application/json)
     * @return the application instance service.
     */
    public ApplicationInstanceService getApplicationInstanceService(
            String baseUrl, String mediaType) {
        return new ApplicationInstanceServiceImpl(client, baseUrl, mediaType);
    }

    /**
     * Get the service to work with tasks
     * @param baseUrl the base url where the SDC is
     * @param mediaType the media type (application/xml or application/json)
     * @return the TaskService
     */
    public TaskService getTaskService(String baseUrl, String mediaType) {
        return new TaskServiceImpl(client, baseUrl, mediaType);
    }

    /**
     * Get the service to work with tasks
     * @param baseUrl the base url where the SDC is
     * @param mediaType the media type (application/xml or application/json)
     * @param maxWaiting the max time in ms the client will wait for a task
     * @param waitingPeriod the period of time in ms between retries to get the
     * complete task (this period will increase  this way t = n*waitingPeriod)
     * @return the TaskService
     */
    public TaskService getTaskService(String baseUrl, String mediaType,
            Long maxWaiting, Long waitingPeriod) {
        return new TaskServiceImpl(client, baseUrl, mediaType,
                waitingPeriod, maxWaiting);
    }

    /**
     * Get the service to work with product instances in a synchronous way
     * @param baseUrl the base url where the SDC is
     * @param mediaType the media type (application/xml or application/json)
     * @return the synchronous productInstanceService
     */
    public ProductInstanceSyncService getProductInstanceSyncService(
            String baseUrl, String mediaType) {
        return new ProductInstanceSyncServiceImpl(
                getProductInstanceService(baseUrl, mediaType),
                getTaskService(baseUrl, mediaType));
    }

    /**
     * Get the service to work with product instances in a synchronous way
     * @param baseUrl the base url where the SDC is
     * @param mediaType the media type (application/xml or application/json)
     * @param maxWaiting the max time in ms the client will wait for a task
     * @param waitingPeriod the period of time in ms between retries to get the
     * complete task (this period will increase  this way t = n*waitingPeriod)
     * @return the synchronous productInstanceService
     */
    public ProductInstanceSyncService getProductInstanceSyncService(
            String baseUrl, String mediaType, Long maxWaiting, Long waitingPeriod) {
        return new ProductInstanceSyncServiceImpl(
                getProductInstanceService(baseUrl, mediaType),
                getTaskService(baseUrl, mediaType, maxWaiting, waitingPeriod));
    }


    /**
     * Get the service to work with application instances in a synchronous way
     * @param baseUrl the base url where the SDC is
     * @param mediaType the media type (application/xml or application/json)
     * @return the synchronous applicationInstanceService
     */
    public ApplicationInstanceSyncService getApplicationInstanceSyncService(
            String baseUrl, String mediaType) {
        return new ApplicationInstanceSyncServiceImpl(
                getApplicationInstanceService(baseUrl, mediaType),
                getTaskService(baseUrl, mediaType));

    }

    /**
     * Get the service to work with application instances in a synchronous way
     * @param baseUrl the base url where the SDC is
     * @param mediaType the media type (application/xml or application/json)
     * @param maxWaiting the max time in ms the client will wait for a task
     * @param waitingPeriod the period of time in ms between retries to get the
     * complete task (this period will increase  this way t = n*waitingPeriod)
     * @return the synchronous applicationInstanceService
     */
    public ApplicationInstanceSyncService getApplicationInstanceSyncService(
            String baseUrl, String mediaType, Long maxWaiting, Long waitingPeriod) {
        return new ApplicationInstanceSyncServiceImpl(
                getApplicationInstanceService(baseUrl, mediaType),
                getTaskService(baseUrl, mediaType, maxWaiting, waitingPeriod));
    }


    /**
     * Get the service to work with products and product releases in the catalog.
     * @param baseUrl the urle where the SDC is
     * @param mediaType the media type (application/xml or application/json)
     * @return the service.
     */
    public ProductService getProductService(String baseUrl, String mediaType){
        return new ProductServiceImpl(client, baseUrl, mediaType);
    }
    
    /**
     * Get the service to work with applications and application releases in the catalog.
     * @param baseUrl the url where the SDC is
     * @param mediaType the media type (application/xml or application/json)
     * @return the service.
     */
    public ApplicationService getApplicationService(String baseUrl, String mediaType){
        return new ApplicationServiceImpl(client, baseUrl, mediaType);
    }
}
