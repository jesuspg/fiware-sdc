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

package com.telefonica.euro_iaas.sdc.client.services;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;

public interface ProductInstanceService extends BaseInstallableService<ProductInstance> {

    /**
     * Install a product in a given host.
     * 
     * @param product
     *            the concrete release of a product to install. It also contains information about the VM where the
     *            product is going to be installed
     * @param callback
     *            if not empty, contains the url where the result of the async operation will be sent
     * @return the installed product.
     */
    Task install(String vdc, ProductInstanceDto product, String callback);

    Task installArtifact(String vdc, String productInstanceId, Artifact artefact, String callback);

    Task uninstallArtifact(String vdc, String productInstanceId, Artifact artefact, String callback);

    /**
     * Retrieve all ProductInstance created in the system.
     * 
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
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @param status
     *            the status the product (<i>nullable</i>)
     * @param vdc
     *            defines the vdc where the products are installed (<i>not nullable</i>).
     * @return the product instances that match with the criteria.
     */
    List<ProductInstance> findAll(String hostname, String domain, String ip, String fqn, Integer page,
            Integer pageSize, String orderBy, String orderType, Status status, String vdc, String productName);

}
