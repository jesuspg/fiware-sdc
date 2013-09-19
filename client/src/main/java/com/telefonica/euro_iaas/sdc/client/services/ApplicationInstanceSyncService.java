package com.telefonica.euro_iaas.sdc.client.services;

import java.util.List;

import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;

/**
 * Provides the operations to work with application instances in a synchronous
 * mode
 *
 * @author Sergio Arroyo
 *
 */
public interface ApplicationInstanceSyncService extends
        BaseInstallableSyncService<ApplicationInstance> {

    /**
     * Install a list of application in a given host running on the selected
     * products.
     *
     * @param vdc
     *            the vdc where the application will be installed.
     * @param application
     *            the application to install containing the VM, the appName and
     *            the products where the application is going to be installed
     * @return the task referencing the installed application.
     * @throws IncompatibleProductsException
     *             if the selected products are not compatible with the given
     *             application
     * @throws AlreadyInstalledException
     *             if the application is running on the system
     * @throws NotInstalledProductsException
     *             if the needed products to install the application are not
     *             installed
     * @throws MaxTimeWaitingExceedException
     *             if the operation spend more time that is allowed
     * @throws InvalidExecutionException
     *             if the return of the task is not success
     */
    ApplicationInstance install(String vdc, ApplicationInstanceDto application)
            throws MaxTimeWaitingExceedException, InvalidExecutionException;

    /**
     * Retrieve all ApplicationInstance that match with a given criteria.
     *
     * @param hostname
     *            the host name where the product is installed (<i>nullable</i>)
     * @param domain
     *            the domain where the machine is (<i>nullable</i>)
     * @param ip
     *            the ip of the host (<i>nullable</i>) *
     * @param hostname
     *            the host name where the product is installed (<i>nullable</i>)
     * @param domain
     *            the domain where the machine is (<i>nullable</i>)
     * @param ip
     *            the ip of the host (<i>nullable</i>)
     * @param fqn
     *            the fqn of the host (<i>nullable</i>)
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query
     *            (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by
     *            default <i>nullable</i>)
     * @param status
     *            the status the product (<i>nullable</i>)
     * @param vdc
     *            the vdc where the application is installed
     * @param applicationName
     *            the applicationName
     * @return the retrieved application instances.
     */
    List<ApplicationInstance> findAll(String hostname, String domain,
            String ip, String fqn, Integer page, Integer pageSize, String orderBy,
            String orderType, List<Status> status, String vdc,
            String applicationName);

}
