package com.telefonica.euro_iaas.sdc.it.util;

import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.BASE_URL;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.MIME_TYPE;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;

import java.util.List;

import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceSyncService;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Provides some utility methods to work with Products
 * @author Sergio Arroyo
 *
 */
public class ProductInstanceUtils {

    private SDCClient client = new SDCClient();
    private ProductInstanceSyncService service;

    /**
     * Install the product
     * @param productName the product name
     * @param version the version
     * @param ip the ip where the product will be installed
     * @param vdc the vdc where the node is
     * @return the installed product
     * @throws MaxTimeWaitingExceedException if the installation takes more time
     * than expected
     * @throws InvalidExecutionException if the product can not be installed
     */
    public ProductInstance install(
            String productName, String version, String ip, String vdc)
            throws MaxTimeWaitingExceedException, InvalidExecutionException {
        service =
                client.getProductInstanceSyncService(getProperty(BASE_URL),
                        getProperty(MIME_TYPE));
        ProductInstanceDto instance = new ProductInstanceDto(
                new ReleaseDto(productName, version, null),
                new VM(ip));
        return service.install(vdc, instance);
    }

    /**
     * Configure the selected product
     * @param vdc
     * @param id
     * @param attributes
     * @return the configured product
     * @throws MaxTimeWaitingExceedException if the installation takes more time
     * than expected
     * @throws InvalidExecutionException if the product can not be configured
     */
    public ProductInstance configure(String vdc, Long id,
            List<Attribute> attributes)
            throws MaxTimeWaitingExceedException, InvalidExecutionException {
        service =
                client.getProductInstanceSyncService(getProperty(BASE_URL),
                        getProperty(MIME_TYPE));
        return service.configure(vdc, id, attributes);
    }

    /**
     * Upgrade the selected product from actual version to "newVersion".
     * @param vdc
     * @param id
     * @param newVersion
     * @return the upgraded product
     * @throws MaxTimeWaitingExceedException if the installation takes more time
     * than expected
     * @throws InvalidExecutionException if the product can not be configured
     */
    public ProductInstance upgrade(String vdc, Long id, String newVersion)
            throws MaxTimeWaitingExceedException, InvalidExecutionException {
        service =
                client.getProductInstanceSyncService(getProperty(BASE_URL),
                        getProperty(MIME_TYPE));
        return service.upgrade(vdc, id, newVersion);
    }

    /**
     * Uninstall the selected product.
     * @param vdc
     * @param id
     * @return the uninstalled product
     * @throws MaxTimeWaitingExceedException if the installation takes more time
     * than expected
     * @throws InvalidExecutionException if the product can not be configured
     */
    public ProductInstance uninstall(String vdc, Long id)
            throws MaxTimeWaitingExceedException, InvalidExecutionException {
        service =
                client.getProductInstanceSyncService(getProperty(BASE_URL),
                        getProperty(MIME_TYPE));
        return service.uninstall(vdc, id);
    }

    /**
     * Load the selected product.
     * @param vdc
     * @param id
     * @return the loaded product
     * @throws ResourceNotFoundException
     */
    public ProductInstance load(String vdc, Long id)
            throws ResourceNotFoundException {
        service =
                client.getProductInstanceSyncService(getProperty(BASE_URL),
                        getProperty(MIME_TYPE));
        return service.load(vdc, id);
    }

    /**
     * Install the product if not previously installed
     * @param productName
     * @param version
     * @param ip
     * @param vdc
     * @return
     * @throws MaxTimeWaitingExceedException if the installation takes more time
     * than expected
     * @throws InvalidExecutionException if the product can not be configured
     */
    public ProductInstance installIfNotInstalled(
            String productName, String version, String ip, String vdc)
            throws MaxTimeWaitingExceedException, InvalidExecutionException {
        ProductInstance instance = null;
        service =
                client.getProductInstanceSyncService(getProperty(BASE_URL),
                        getProperty(MIME_TYPE));
        List<ProductInstance> instances = service.findAll(null, null, ip,
                null, null, null, null, null, vdc, productName);
        if (!instances.isEmpty()) {
            instance = instances.iterator().next();
            instance = uninstallIfNeed(instance, version);
        } else {
            instance = install(productName, version, ip, vdc);
        }

        if (!isInstalled(instance)) {
            instance = install(productName, version, ip, vdc);
        }
        return instance;
    }

    /**
     * Uninstall the product if the version is not the correct one.
     * @param instance
     * @param version
     * @return
     * @throws MaxTimeWaitingExceedException
     * @throws InvalidExecutionException
     */
    private ProductInstance uninstallIfNeed(
            ProductInstance instance, String version)
            throws MaxTimeWaitingExceedException, InvalidExecutionException {
        if (!instance.getProduct().getVersion().equals(version)
                && (isInstalled(instance))) {
            instance = uninstall(instance.getVdc(), instance.getId());
        }
        return instance;
    }

    /**
     * Decides if a product instance is installed
     * @param instance
     * @return
     */
    private boolean isInstalled(ProductInstance instance) {
        return (!instance.getStatus().equals(Status.ERROR)
                && !instance.getStatus().equals(Status.UNINSTALLED));
    }

}
