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

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_ID;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_PASS;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_DATE_FORMAT;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_TIME_ZONE;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;

/**
 * Default MixlibAuthenticationDigester implementation.
 * 
 * @author Sergio Arroyo
 */
public class MixlibAuthenticationDigesterImpl implements MixlibAuthenticationDigester {

    private Signer signer;
    private SystemPropertiesProvider propertiesProvider;

    public final static String AUTH_HEADER_PREFIX = "X-Ops-Authorization-";
    public final static String CONTENT_HASH_HEADER = "X-Ops-Content-Hash";
    public final static String TIMESTAMP_HEADER = "X-Ops-Timestamp";
    public final static String USER_HEADER = "X-Ops-Userid";
    //public final static String CHEFSERVER_VERSION_HEADER = "X-chef-version";
    //public final static String CHEFSERVER_VERSION_VALUE = "0.10.8";
    public final static String SIGN_VERSION_HEADER = "X-Ops-Sign";
    public final static String SIGN_VERSION_VALUE = "version=1.0";
    
    private final static String BODY_TEMPLATE = "Method:{0}\nHashed Path:{1}\nX-Ops-Content-Hash:{2}\n"
            + "X-Ops-Timestamp:{3}\nX-Ops-UserId:{4}";

    /**
     * {@inheritDoc}
     */
    public Map<String, String> digest(String method, String path, String body, Date timestamp, String id, String pkUrl) {
        try {
            DateFormat df = new SimpleDateFormat(propertiesProvider.getProperty(CHEF_DATE_FORMAT));
            df.setTimeZone(TimeZone.getTimeZone(propertiesProvider.getProperty(CHEF_TIME_ZONE)));

            Map<String, String> headers = new HashMap<String, String>();
            headers.put(CONTENT_HASH_HEADER, getHash(body));
            headers.put(TIMESTAMP_HEADER, df.format(timestamp));
            headers.put(USER_HEADER, id);
            headers.put(SIGN_VERSION_HEADER, SIGN_VERSION_VALUE);
            //headers.put(CHEFSERVER_VERSION_HEADER, CHEFSERVER_VERSION_VALUE);
            //headers.put("Accept", "application/json");
            
            String digest = MessageFormat.format(BODY_TEMPLATE, method.toUpperCase(), getHash(path), getHash(body),
                    df.format(timestamp), id);
            digest = signer.sign(digest, new File(pkUrl));
            Integer length;
            Integer iteration = 1;
            while (!digest.isEmpty()) {
                length = digest.length() >= 60 ? 60 : digest.length();
                headers.put(AUTH_HEADER_PREFIX + iteration, digest.substring(0, length));
                digest = digest.substring(length);
                iteration = iteration + 1;
            }
            return headers;
        } catch (Exception e) {
            throw new SdcRuntimeException();
        }
    }

    private String getHash(String body) {

        String b64 = Base64.encodeBase64String(DigestUtils.sha(body.getBytes()));
        return b64;
    }

    /**
     * @param signer
     *            the signer to set
     */
    public void setSigner(Signer signer) {
        this.signer = signer;
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

}
