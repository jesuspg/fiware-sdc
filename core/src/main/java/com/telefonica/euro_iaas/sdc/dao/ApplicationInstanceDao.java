package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;
import com.telefonica.euro_iaas.commons.dao.BaseDAO;
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
}
