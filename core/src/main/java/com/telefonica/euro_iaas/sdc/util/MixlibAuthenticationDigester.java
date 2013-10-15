/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
