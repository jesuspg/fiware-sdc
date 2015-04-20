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

package com.telefonica.euro_iaas.sdc.keystoneutils;

import java.util.List;

import com.telefonica.fiware.commons.openstack.auth.exception.OpenStackException;

/**
 * Utilities for manage OpenStack regions.
 */
public interface OpenStackRegion {

    /**
     * How to get an endpoint by region.
     * 
     * @param name
     *            e.g. nova, quantum, glance, etc...
     * @param regionName
     * @return the http url with de endpoint.
     * @throws OpenStackException
     */
    String getEndPointByNameAndRegionName(String name, String regionName) throws OpenStackException;

    /**
     * Get a list with the name of all regions.
     * 
     * @return
     */
    List<String> getRegionNames() throws OpenStackException;

    /**
     * @return
     * @throws OpenStackException
     */
    String getDefaultRegion() throws OpenStackException;

    String getChefServerEndPoint() throws OpenStackException;

    String getPuppetWrapperEndPoint() throws OpenStackException;

    String getWebdavPoint() throws OpenStackException;

    String getNovaEndPoint(String regionName) throws OpenStackException;

    String getPuppetDBEndPoint() throws OpenStackException;
}
