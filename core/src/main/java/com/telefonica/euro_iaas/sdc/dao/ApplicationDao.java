package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationSearchCriteria;
/**
 * Defines the methods needed to persist Application objects.
 *
 * @author Sergio Arroyo.
 */
public interface ApplicationDao extends BaseDAO<Application, String> {
    /**
     * Find by criteria
     * @param criteria the search criteria (containing pagination info, and some
     * fields criteria).
     * @return the elements that match with the search criteria
     */
    List<Application> findByCriteria(ApplicationSearchCriteria criteria);

}
