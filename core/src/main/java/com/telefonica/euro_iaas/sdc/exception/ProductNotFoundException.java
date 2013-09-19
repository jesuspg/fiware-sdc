package com.telefonica.euro_iaas.sdc.exception;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Exception thrown when trying to delete a ProductRelease that does not exist.
 * 
 * @author Jesus M. Movilla
 */
@SuppressWarnings("serial")
public class ProductNotFoundException extends Exception {

	private Product product;

	public ProductNotFoundException() {
		super();
	}

	public ProductNotFoundException(List<ProductRelease> productReleases) {
		this.product = product;
	}

	public ProductNotFoundException(String msg) {
		super(msg);
	}

	public ProductNotFoundException(Throwable e) {
		super(e);
	}

	public ProductNotFoundException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param productRelease
	 *            the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}
}
