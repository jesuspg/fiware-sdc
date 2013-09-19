package com.telefonica.euro_iaas.sdc.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;

/**
 * Defines the methods needed to persist ProductRelease objects.
 * 
 * @author Sergio Arroyo.
 */
public interface ProductReleaseDao extends BaseDAO<ProductRelease, Long> {
	/**
	 * Find the product releases that match with the given criteria.
	 * 
	 * @param criteria
	 *            the search criteria
	 * @return the list of elements that match with the criteria.
	 */
	List<ProductRelease> findByCriteria(ProductReleaseSearchCriteria criteria);

	/**
	 * Find the product release that match with the given criteria.
	 * 
	 * @param product
	 *            the product
	 * @param version
	 *            the release version
	 * @return the element that match with the criteria.
	 * @throws EntityNotFoundException
	 *             if there is no product with the given version
	 */
	ProductRelease load(Product product, String version)
			throws EntityNotFoundException;

}
