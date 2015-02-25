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

package com.telefonica.euro_iaas.sdc.rest.util;

import java.io.InputStream;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import org.openstack.docs.identity.api.v2.AuthenticateResponse;

/**
 * Create and manage the cache to the response of OpenStack for request the admin token. Based on ehcache open source.
 */
public class TokenCache {

    public static final String CACHE_NAME = "token";

    /**
     * The maximum amount of time between accesses before an element expires.
     */
    private static final long TIME_TO_IDLE = 1200L;
    /**
     * The maximum time between creation time and when an element expires.
     */
    private static final long TIME_TO_LIVE = 1200L;

    /**
     * cache.
     */
    private Cache cache;

    /**
     * Default constructor. Creates the cache.
     */
    public TokenCache() {

        CacheManager singletonManager;
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("/ehcache.xml");
            singletonManager = CacheManager.newInstance(inputStream);
        } catch (Exception e) {
            singletonManager = CacheManager.create();
            singletonManager.addCache(CACHE_NAME);
            cache.getCacheConfiguration();
            cache = singletonManager.getCache(CACHE_NAME);
            CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
            cacheConfiguration.setTimeToIdleSeconds(TIME_TO_IDLE);
            cacheConfiguration.setTimeToLiveSeconds(TIME_TO_LIVE);

        }
        cache = singletonManager.getCache(CACHE_NAME);

    }

    /**
     * Put new a new AuthenticateResponse value using token like key, in cache.
     * 
     * @param key
     * @param authenticateResponse
     */
    public void put(String key, AuthenticateResponse authenticateResponse) {
        cache.put(new Element(key, authenticateResponse));
    }

    /**
     * Put new admin token in cache.
     * 
     * @param token
     * @param tenantId
     */
    public void putAdmin(String token, String tenantId) {
        cache.put(new Element("admin", new String[] { token, tenantId }));
    }

    /**
     * Return admin credentials.
     * 
     * @return
     */
    public String[] getAdmin() {
        if (cache.isKeyInCache("admin")) {
            return (String[]) cache.get("admin").getObjectValue();
        } else {
            return null;
        }
    }

    /**
     * Get from cache the response of OpenStack by key. The key is token string.
     * 
     * @param token
     * @param tenantId
     * @return
     */
    public AuthenticateResponse getAuthenticateResponse(String token, String tenantId) {
        String key = token + "-" + tenantId;

        if (cache.isKeyInCache(key) && (cache.get(key) != null)) {

            return (AuthenticateResponse) cache.get(key).getObjectValue();
        } else {
            return null;
        }
    }
}
