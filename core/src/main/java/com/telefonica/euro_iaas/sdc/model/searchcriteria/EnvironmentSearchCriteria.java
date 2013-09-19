package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Provides some criteria to search Environment entities.
 * 
 * @author Jesus M. Movilla
 * 
 */
public class EnvironmentSearchCriteria extends AbstractSearchCriteria {

	/**
	 * The productRelease
	 */
	private ProductRelease productRelease;

	/**
	 * Default constructor
	 */
	public EnvironmentSearchCriteria() {
	}

	/**
	 * @param page
	 * @param pagesize
	 * @param orderBy
	 * @param orderType
	 * @param productRelease
	 */
	public EnvironmentSearchCriteria(Integer page, Integer pageSize,
			String orderBy, String orderType, ProductRelease productRelease) {
		super(page, pageSize, orderBy, orderType);
		this.productRelease = productRelease;
	}

	/**
	 * @param productRelease
	 */
	public EnvironmentSearchCriteria(ProductRelease productRelease) {
		this.productRelease = productRelease;
	}

	/**
	 * @return the productRelease
	 */
	public ProductRelease getProductRelease() {
		return productRelease;
	}

	/**
	 * @param productRelease
	 *            the productRelease to set
	 */
	public void setProductRelease(ProductRelease productRelease) {
		this.productRelease = productRelease;
	}

	@Override
	public String toString() {
		return "ProductInstanceSearchCriteria [productRelease="
				+ productRelease + "]";

	}

}
