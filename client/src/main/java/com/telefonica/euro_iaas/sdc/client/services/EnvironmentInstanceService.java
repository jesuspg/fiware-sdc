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

import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentInstanceDto;

public interface EnvironmentInstanceService {

    /**
     * Add the selected EnvironmentInstance in to the SDC's catalog. If the Environment already exists, then add the new
     * Release.
     * 
     * @param EnvironmentInstanceDto
     *            <ol>
     *            <li>The EnvironmentInstanceDto: contains the information about the environment instance</li>
     *            </ol>
     */

    EnvironmentInstance insert(EnvironmentInstanceDto environmentInstanceDto);

    /**
     * Retrieve all EnvironmentInstances available created in the system.
     * 
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @return the EnvironmentInstances.
     */

    List<EnvironmentInstance> findAll(Integer page, Integer pageSize, String orderBy, String orderType);

    /**
     * Retrieve the selected Environment.
     * 
     * @param name
     *            the environment name
     * @return the environment.
     */

    EnvironmentInstance load(Long Id);

    /**
     * Delete the EnvironmentInstance in BBDD,
     * 
     * @param name
     *            the env name
     */

    void delete(Long Id);

    /**
     * Update the EnvironmentInstance in BBDD,
     * 
     * @param EnvironmentInstanceDto
     *            the EnvironmentInstance
     */

    EnvironmentInstance update(EnvironmentInstanceDto environmentInstanceDto);
}
