/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.client.services;

import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Provides a rest api to works with ProductResources based on ProductInstanceService
 * 
 * @author Jesus M. Movilla
 */

public interface ProductResourceService {

    /**
     * Insert the selected Product version (ProductRelease)
     * 
     * @param name
     *            the product name
     * @param Multipart
     *            which includes
     * @param productReleaseDto
     * @param The
     *            cookbook in a tar file
     * @param The
     *            set of installables requires fot the recipe to install/ /uninstall the product
     * @return the product.
     * @throws AlreadyExistsProductReleaseException
     *             if the Product Release exists
     * @throws InvalidProductReleaseException
     *             if the Product Release is invalid due to either OS, Product or Product Release
     * @throws InvalidMultiPartRequestException
     *             when the MUltipart object in the request is null, or its size is different to three or the type sof
     *             the different parts are not ProductReleaseDto, File and File
     */

    ProductRelease insert(MultiPart multiPart);

    /**
     * Delete the ProductRelease in BBDD, the associated Recipe in chef server and the installable files in webdav
     * 
     * @param name
     *            the product name
     * @param version
     *            the concrete version
     * @throws ProductReleaseNotFoundException
     *             if the Product Release does not exists
     * @throws ProductReleaseStillInstalledException
     *             if the Product Release is still installed on some VMs
     */

    void delete(String name, String version);

    /**
     * Update the ProductRelease in BBDD, the associated Recipe in chef server and the installable files in webdav
     * 
     * @param name
     *            the product name
     * @param version
     *            the concrete version
     * @param MultiPart
     *            with three parts (ProductReleaseDto, File cookbook and File installable)
     * @throws ProductReleaseNotFoundException
     *             if the Product Release does not exists
     * @throws ProductReleaseStillInstalledException
     *             if the Product Release is still installed on some VMs
     */

    ProductRelease update(String name, String version, MultiPart multipart);

}
