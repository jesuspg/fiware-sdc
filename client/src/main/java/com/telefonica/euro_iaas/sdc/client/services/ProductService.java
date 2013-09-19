package com.telefonica.euro_iaas.sdc.client.services;

import java.io.InputStream;
import java.util.List;

import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

/**
 * Provides the methods which encapsulate SDC's product management's related
 * calls.
 * 
 * @author Sergio Arroyo
 * 
 */
public interface ProductService {

	/**
	 * Add to the catalog the product release.
	 * 
	 * @param releaseDto
	 *            the release dto
	 * @param cookbook
	 *            the file containing the cookbook
	 * @param files
	 *            the file containing the binaries.
	 * @return the created product.
	 */
	ProductRelease add(ProductReleaseDto releaseDto, InputStream cookbook,
			InputStream files);

	/**
	 * Update to the catalog the product release.
	 * 
	 * @param releaseDto
	 *            the release dto
	 * @param cookbook
	 *            the file containing the cookbook
	 * @param files
	 *            the file containing the binaries.
	 * @return the created product.
	 */
	ProductRelease update(ProductReleaseDto releaseDto, InputStream cookbook,
			InputStream files);

	/**
	 * Delete the product release from the catalogue
	 * 
	 * @param pname
	 *            product name
	 * @param version
	 *            version product
	 * @return void.
	 */
	void delete(String pname, String version);

	/**
	 * Retrieve the product release from the SDC catalog
	 * 
	 * @param product
	 *            the product
	 * @param version
	 *            the version
	 * @return the product release
	 * @throws ResourceNotFoundException
	 *             if the selected product does not exists.
	 */
	ProductRelease load(String product, String version)
			throws ResourceNotFoundException;

	/**
	 * Retrieve all ProductReleases available in SDC catalog.
	 * 
	 * @param page
	 *            for pagination is 0 based number(<i>nullable</i>)
	 * @param pageSize
	 *            for pagination, the number of items retrieved in a query
	 *            (<i>nullable</i>)
	 * @param orderBy
	 *            the file to order the search (id by default <i>nullable</i>)
	 * @param orderType
	 *            defines if the order is ascending or descending (asc by
	 *            default <i>nullable</i>)
	 * @param productName
	 *            the different releases for the given product (<i>not
	 *            nullable</i>).
	 * @param osType
	 *            the different supported operating system (<i>not
	 *            nullable</i>).
	 * @return the product instances that match with the criteria.
	 */
	List<ProductRelease> findAll(Integer page, Integer pageSize,
			String orderBy, String orderType, String productName, String osType);
}
