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

package com.telefonica.euro_iaas.sdc.util;

import java.util.List;

import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Provides some methods to add the SDC-Client behavior.
 * 
 * @author Sergio Arroyo
 */
public interface SDCClientUtils {

    /**
     * Retrieve the VM where the node is.
     * 
     * @param ip
     *            the ip
     * @param fqn
     *            the fqn
     * @param osType
     *            the OSType
     * @return the VM containing the hostname and domain.
     */
    VM getVM(String ip, String fqn, String osType);

    /**
     * Make the target node call Chef to execute the queued tasks.
     * 
     * @param vm
     *            the node
     */
    void execute(VM vm) throws NodeExecutionException;

    /**
     * Add or update some configuration parameters to node.
     * 
     * @param vm
     *            the node
     * @param configuration
     *            the configuration properties
     * @return the actual values in client.
     */
    List<Attribute> configure(VM vm, List<Attribute> configuration);

    /**
     * Add or update some configuration parameters to node.
     * 
     * @param vm
     *            the node
     * @param attribute
     *            the property to be modfied
     * @return the modified property.
     */
    Attribute configureProperty(VM vm, Attribute attribute);

    /**
     * set the NodeCommands in a particular node.
     * 
     * @param vm
     *            the node
     */
    void setNodeCommands(VM vm) throws InvalidInstallProductRequestException;

    /**
     * Check if the node is ready to install software.
     * 
     * @param ip
     *            IP where node is
     * @throws NodeExecutionException
     */
    void checkIfSdcNodeIsReady(String ip) throws NodeExecutionException;
}
