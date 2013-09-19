package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;


/**
 * Search criteria for products entities.
 * 
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class ProductSearchCriteria extends AbstractSearchCriteria {

	/**
	 * @param page
	 * @param pagesize
	 * @param orderBy
	 * @param orderType
	 */
	public ProductSearchCriteria(Integer page, Integer pageSize,
			String orderBy, String orderType) {
		super(page, pageSize, orderBy, orderType);
	}

	/**
	 * @param orderBy
	 * @param orderType
	 */
	public ProductSearchCriteria(String orderBy, String orderType) {
		super(orderBy, orderType);
	}

	/**
	 * @param page
	 * @param pagesize
	 */
	public ProductSearchCriteria(Integer page, Integer pageSize) {
		super(page, pageSize);
	}

	/**
     */
	public ProductSearchCriteria() {
		super();
	}
}
