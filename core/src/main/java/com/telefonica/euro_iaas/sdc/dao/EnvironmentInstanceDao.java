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
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.EnvironmentInstanceSearchCriteria;

/**
 * Defines the methods needed to persist Environment objects.
 * 
 * @author Jesus M. Movilla
 */
public interface EnvironmentInstanceDao extends BaseDAO<EnvironmentInstance, Long> {

    /**
     * Get the environment instances that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the retrieved instances.
     */
    List<EnvironmentInstance> findByCriteria(EnvironmentInstanceSearchCriteria criteria);

    /**
     * Find one instance by criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the Instance that match with the given criteria.
     * @throws NotUniqueResultException
     *             if there is no result or more than one
     */
    EnvironmentInstance findUniqueByCriteria(EnvironmentInstanceSearchCriteria criteria)
            throws NotUniqueResultException;

}
