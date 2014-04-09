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

import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Provides the way to retrieve (or deduce) the host an domain given an IP address.
 * 
 * @author Sergio Arroyo
 */
public interface IpToVM {
    /**
     * Retrieve some information about the virtual machine located in a concrete IP address.
     * 
     * @param ip
     *            the ip of the associated VM.
     * @param fqn
     *            the fqn of the associated VM.
     * @param osType
     *            the osType of the associated VM.
     * @return the VM with the whole information available (ip, host and domain)
     */
    VM getVm(String ip, String fqn, String osType);
}
