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

import java.util.Date;
import java.util.Map;

/**
 * Provides the mechanism to create headers to authenticate against Chef server.
 * 
 * @author Sergio Arroyo
 */
public interface MixlibAuthenticationDigester {

    /**
     * Get the necessary headers to send an authenticated request to Chef server.
     * 
     * @param method
     *            the HTTP method (will be capitalized))
     * @param path
     *            the path
     * @param body
     *            the payload
     * @param timestamp
     *            the date (to prevent repetitive attacks)
     * @param id
     *            the client id of Chef Server
     * @param pkUrl
     *            the url where the private key is.
     * @return the headers.
     */
    public Map<String, String> digest(String method, String path, String body, Date timestamp, String id, String pkUrl);
}
