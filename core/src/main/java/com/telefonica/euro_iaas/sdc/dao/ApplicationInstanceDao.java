package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;
/**
 * Defines the methods needed to persist Application objects.
 *
 * @author Sergio Arroyo.
 */
public interface ApplicationInstanceDao
    extends BaseDAO<ApplicationInstance, Long> {

    /**
     * Get the application instances that match with the given criteria.
     * @param criteria the search criteria
     * @return the retrieved instances.
     */
    List<ApplicationInstance> findByCriteria(
            ApplicationInstanceSearchCriteria criteria);

    /**
     * Find one instance by criteria.
     * @param criteria the search criteria
     * @return the Instance that match with the given criteria.
     * @throws NotUniqueResultException if there is no result or more than one
     */
    ApplicationInstance findUniqueByCriteria(
            ApplicationInstanceSearchCriteria criteria)
                    throws NotUniqueResultException;

}
