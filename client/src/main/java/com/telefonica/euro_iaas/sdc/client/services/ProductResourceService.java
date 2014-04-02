/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
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
