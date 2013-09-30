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
