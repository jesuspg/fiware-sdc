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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.telefonica.fiware.commons.openstack.auth.OpenStackAccess;
import com.telefonica.fiware.commons.openstack.auth.OpenStackKeystone;
import com.telefonica.fiware.commons.openstack.auth.exception.OpenStackException;
import com.telefonica.fiware.commons.util.RegionCache;
import com.telefonica.fiware.commons.util.TokenCache;

/**
 * Tests for OpenStackRegionImpl
 */
public class OpenStackRegionImplTest {

    /**
     * Should return the url when url in cache.
     */
    @Test
    public void shouldGetPuppetDBEndPointWithUrlInCache() throws OpenStackException {
        // given
        OpenStackRegion openStackRegion = new OpenStackRegionImpl();
        RegionCache regionCache = new RegionCache();
        regionCache.putUrl("Spain", "puppetmaster", "http://puppetmaster:8080");
        TokenCache tokenCache = new TokenCache();
        OpenStackAccess openStackAccess = mock(OpenStackAccess.class);
        OpenStackKeystone openStackKeystone = mock(OpenStackKeystone.class);
        when(openStackAccess.getOpenStackKeystone()).thenReturn(openStackKeystone);
        List<String> names = new ArrayList<String>();
        names.add("Spain");
        names.add("Trento");
        when(openStackKeystone.parseRegionNames(any(JSONObject.class), anyString())).thenReturn(names);
        tokenCache.putAdmin(openStackAccess);

        // when
        String url = openStackRegion.getPuppetDBEndPoint();

        // then
        assertNotNull(url);
        assertEquals("http://puppetmaster:8080", url);
    }

    /**
     * Should return the url with http and port.
     */
    @Test
    public void shouldGetValidPuppetDBEndPointWithHttpAndPort() throws OpenStackException {
        // given
        OpenStackRegion openStackRegion = new OpenStackRegionImpl();
        RegionCache regionCache = new RegionCache();
        regionCache.putUrl("Spain", "puppetmaster", "puppetmaster.com");
        TokenCache tokenCache = new TokenCache();
        OpenStackAccess openStackAccess = mock(OpenStackAccess.class);
        OpenStackKeystone openStackKeystone = mock(OpenStackKeystone.class);
        when(openStackAccess.getOpenStackKeystone()).thenReturn(openStackKeystone);
        List<String> names = new ArrayList<String>();
        names.add("Spain");
        names.add("Trento");
        when(openStackKeystone.parseRegionNames(any(JSONObject.class), anyString())).thenReturn(names);
        tokenCache.putAdmin(openStackAccess);

        // when
        String url = openStackRegion.getPuppetDBEndPoint();

        // then
        assertNotNull(url);
        assertEquals("http://puppetmaster.com:8080", url);
    }

    /**
     * Should throw exception with error in parser
     */
    @Test(expected = OpenStackException.class)
    public void shouldThrowExceptionInGetPuppetDBEndPointWithErrorInParser() throws OpenStackException {
        // given
        OpenStackRegion openStackRegion = new OpenStackRegionImpl();
        TokenCache tokenCache = new TokenCache();
        OpenStackAccess openStackAccess = mock(OpenStackAccess.class);
        OpenStackKeystone openStackKeystone = mock(OpenStackKeystone.class);
        when(openStackAccess.getOpenStackKeystone()).thenReturn(openStackKeystone);
        List<String> names = new ArrayList<String>();
        names.add("Spain");
        names.add("Trento");
        when(openStackKeystone.parseRegionNames(any(JSONObject.class), anyString())).thenReturn(names);
        tokenCache.putAdmin(openStackAccess);

        // when
        openStackRegion.getPuppetDBEndPoint();

        // then
    }
}
