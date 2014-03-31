/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.client.services;

import java.io.InputStream;
import java.util.List;

import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

/**
 * @author jesus.movilla
 */
public interface ProductReleaseService {

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
    ProductRelease add(ProductReleaseDto releaseDto, InputStream cookbook, InputStream files, String token, String tenant);

    /**
     * @param releaseDto
     * @return the created product release
     */
    ProductRelease add(ProductReleaseDto releaseDto,String token, String tenant);

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
    ProductRelease update(ProductReleaseDto releaseDto, InputStream cookbook, InputStream files, String token, String tenant);

    /**
     * Delete the product release from the catalogue
     * 
     * @param pname
     *            product name
     * @param version
     *            version product
     * @return void.
     */
    void delete(String pname, String version, String token, String tenant);

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
    ProductRelease load(String product, String version,String token, String tenant) throws ResourceNotFoundException;

    /**
     * Retrieve all ProductReleases available in SDC catalog.
     * 
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @param productName
     *            the different releases for the given product (<i>not nullable</i>).
     * @param osType
     *            the different supported operating system (<i>not nullable</i>).
     * @return the product instances that match with the criteria.
     */
    List<ProductRelease> findAll(Integer page, Integer pageSize, String orderBy, String orderType, String productName,
            String osType, String token, String tenant);
}
