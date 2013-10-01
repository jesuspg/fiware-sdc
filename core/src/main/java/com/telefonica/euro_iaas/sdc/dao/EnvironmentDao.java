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

package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.EnvironmentSearchCriteria;

/**
 * Defines the methods needed to persist Environment objects.
 * 
 * @author Jesus M. Movilla
 */
public interface EnvironmentDao extends BaseDAO<Environment, String> {
    /**
     * Find the environments that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */

    List<Environment> findByCriteria(EnvironmentSearchCriteria criteria);

    /**
     * Find the environment that match with the given criteria.
     * 
     * @param name
     *            the Environment name
     * @return the element that match with the criteria.
     * @throws EntityNotFoundException
     *             if there is no product with the given version
     */
    Environment load(String name) throws EntityNotFoundException;

}
