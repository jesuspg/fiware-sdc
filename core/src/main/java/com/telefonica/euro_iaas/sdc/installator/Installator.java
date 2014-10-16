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

package com.telefonica.euro_iaas.sdc.installator;

import java.util.List;

import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * @author alberts
 *
 */
public interface Installator {

    
    /**
     * Execute the service according to parameters
     * @param vm
     * @param vdc
     * @param productRelease
     * @param action
     * @param token
     * @throws InstallatorException
     * @throws NodeExecutionException
     */
    void callService(VM vm, String vdc, ProductRelease productRelease, String action, String token) throws InstallatorException, NodeExecutionException;
    
    /**
     * Execute the service according to parameters
     * @param productInstance
     * @param vm
     * @param attributes
     * @param action
     * @param token
     * @throws InstallatorException
     * @throws NodeExecutionException
     */
    void callService(ProductInstance productInstance, VM vm, List<Attribute> attributes, String action, String token)
            throws InstallatorException, NodeExecutionException;

    /**
     * Execute the service according to parameters
     * @param productInstance
     * @param vm
     * @param token
     * @throws InstallatorException
     */
    void upgrade(ProductInstance productInstance, VM vm, String token) throws InstallatorException;

    /**
     * Execute the service according to parameters
     * @param productInstance
     * @param action
     * @param token
     * @throws InstallatorException
     * @throws NodeExecutionException
     */
    void callService(ProductInstance productInstance, String action, String token) throws InstallatorException, NodeExecutionException;
    
    /**
     * Data Validation
     * @param vm
     * @param token
     * @throws InvalidInstallProductRequestException
     * @throws NodeExecutionException
     */
    void validateInstalatorData(VM vm, String token) throws InvalidInstallProductRequestException, NodeExecutionException;
}
