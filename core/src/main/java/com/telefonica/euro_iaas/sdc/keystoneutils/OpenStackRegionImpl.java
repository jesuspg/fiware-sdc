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

import javax.ws.rs.client.Client;

import org.apache.http.conn.HttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.fiware.commons.openstack.auth.OpenStackAccess;
import com.telefonica.fiware.commons.openstack.auth.OpenStackAuthenticationToken;
import com.telefonica.fiware.commons.openstack.auth.exception.OpenStackException;
import com.telefonica.fiware.commons.util.PoolHttpClient;
import com.telefonica.fiware.commons.util.RegionCache;
import com.telefonica.fiware.commons.util.TokenCache;

/**
 * This class implements OpenStackRegion interface.<br>
 * {@inheritDoc}
 */
public class OpenStackRegionImpl implements OpenStackRegion {

    private Client client;

    private RegionCache regionCache;

    private TokenCache tokenCache;

    private OpenStackAuthenticationToken openStackAuthenticationToken;

    private HttpClientConnectionManager httpConnectionManager;

    /**
     * the properties configuration.
     */
    private SystemPropertiesProvider systemPropertiesProvider;

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(OpenStackRegionImpl.class);

    /**
     * Default constructor. Configure caches
     */
    public OpenStackRegionImpl() {

        client = PoolHttpClient.getInstance(httpConnectionManager).getClient();
        regionCache = new RegionCache();

        tokenCache = new TokenCache();
    }

    @Override
    public String getEndPointByNameAndRegionName(String type, String regionName) throws OpenStackException {

        String url = regionCache.getUrl(regionName, type);

        if (url != null) {
            log.debug("Get url for sdc in region " + url);
            return url;
        } else {
            OpenStackAccess openStackAccess = this.getTokenAdmin();

            String result = openStackAccess.getOpenStackKeystone().parseEndpoint(openStackAccess.getAccessJSON(), type,
                    regionName);
            if (result == null) {
                throw new OpenStackException("region not found");
            }
            regionCache.putUrl(regionName, type, result);

            return result;
        }
    }

    public OpenStackAccess getTokenAdmin() throws OpenStackException {

        OpenStackAccess openStackAccess;

        openStackAccess = tokenCache.getAdmin();

        if (openStackAccess == null) {

            if (openStackAuthenticationToken == null) {
                String url = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_URL);

                String user = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_USER);

                String pass = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_PASS);

                String tenant = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_TENANT);

                openStackAuthenticationToken = new OpenStackAuthenticationToken(url, user, pass, tenant);
            }
            openStackAccess = openStackAuthenticationToken.getAdminCredentials(client);
            tokenCache.putAdmin(openStackAccess);
        }
        return openStackAccess;

    }

    @Override
    public String getPuppetWrapperEndPoint() throws OpenStackException {
        String defaulRegion = this.getDefaultRegion();
        log.info("Get url for puppet for default region " + defaulRegion);
        String url;
        try {
            url = getEndPointByNameAndRegionName("puppetwrapper", defaulRegion);
        } catch (OpenStackException e) {
            String msn = "It is not possible to obtain the Puppet Wrapper endpoint";
            log.info(msn);
            throw new OpenStackException(msn);

        }
        return url;
    }

    @Override
    public String getPuppetDBEndPoint() throws OpenStackException {
        String defaultRegion = this.getDefaultRegion();
        log.info("Get url for puppetdb for default region " + defaultRegion);
        String url;
        try {
            url = getEndPointByNameAndRegionName("puppetmaster", defaultRegion);
        } catch (OpenStackException e) {
            String msn = "It is not possible to obtain the Puppet DB endpoint " + e;
            log.info(msn);
            throw new OpenStackException(msn);

        }
        if (url.startsWith("http://")) {
            return url;
        }
        return "http://" + url + ":8080";
    }

    public String getNovaEndPoint(String regionName) throws OpenStackException {

        String url = getEndPointByNameAndRegionName("compute", regionName);
        log.debug("getNovaEndPoint " + regionName + " " + url);

        Integer index = url.lastIndexOf("/");
        url = url.substring(0, index + 1);

        return url;

    }

    public String getWebdavPoint() throws OpenStackException {
        String defaulRegion = this.getDefaultRegion();
        log.info("Get url for webdav for default region " + defaulRegion);
        String url;
        try {
            url = getEndPointByNameAndRegionName("webdav", defaulRegion);
        } catch (OpenStackException e) {
            String msn = "It is not possible to obtain the Puppet Master endpoint";
            log.info(msn);
            throw new OpenStackException(msn);

        }
        return url;
    }

    @Override
    public String getDefaultRegion() throws OpenStackException {
        log.debug("Get default region");

        List<String> regions;
        try {
            regions = getRegionNames();
            log.debug("regions " + regions + " " + regions.size());
        } catch (OpenStackException e) {
            String msn = "It is not possible to obtain the SDC endpoint";
            log.error(msn);
            throw new OpenStackException(msn);
        }

        return regions.get(0);
    }

    /**
     * Get a list with the name of all regions for token.
     */
    public List<String> getRegionNames() throws OpenStackException {

        OpenStackAccess openStackAccess = this.getTokenAdmin();
        return openStackAccess.getOpenStackKeystone().parseRegionNames(openStackAccess.getAccessJSON(), "nova");

    }

    public SystemPropertiesProvider getSystemPropertiesProvider() {
        return systemPropertiesProvider;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getFederatedQuantumEndPoint() throws OpenStackException {
        String url = getEndPointByNameAndRegionName("federatednetwork", getDefaultRegion());
        return url;
    }

    public HttpClientConnectionManager getHttpConnectionManager() {
        return httpConnectionManager;
    }

    public void setHttpConnectionManager(HttpClientConnectionManager httpConnectionManager) {
        this.httpConnectionManager = httpConnectionManager;
    }

    public RegionCache getRegionCache() {
        return regionCache;
    }

    public String getChefServerEndPoint() throws OpenStackException {

        String defaulRegion = this.getDefaultRegion();
        log.info("Get url for chef-server for default region " + defaulRegion);
        String url;

        try {
            url = getEndPointByNameAndRegionName("chef-server", defaulRegion);
        } catch (OpenStackException e) {
            String msn = "It is not possible to obtain the Puppet Master endpoint";
            log.info(msn);
            throw new OpenStackException(msn);

        }
        return url;
    }
}
