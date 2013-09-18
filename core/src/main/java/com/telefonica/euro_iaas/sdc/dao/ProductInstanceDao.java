package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.commons.dao.BaseDAO;

/**
 * Defines the methods needed to persist AppState objects.
 *
 * @author Sergio Arroyo.
 */
public interface ProductInstanceDao extends BaseDAO<ProductInstance, Long> {
    /**
     * Find the product instances that match with the given criteria.
     * @param criteria the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria);

    /**
     * Find the unique product instances that match with the given criteria.
     * @param criteria the search criteria
     * @return the element that match with the criteria.
     * @throws NotUniqueResultException if the number of elements that match
     *  with the criteria is different to one.
     */
    ProductInstance findUniqueByCriteria(ProductInstanceSearchCriteria criteria)
    throws NotUniqueResultException;

}
