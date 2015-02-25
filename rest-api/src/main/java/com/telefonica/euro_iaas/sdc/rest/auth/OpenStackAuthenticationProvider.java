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

package com.telefonica.euro_iaas.sdc.rest.auth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;

import org.openstack.docs.identity.api.v2.AuthenticateResponse;
import org.openstack.docs.identity.api.v2.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.sdc.rest.util.TokenCache;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * The Class OpenStackAuthenticationProvider.
 */
public class OpenStackAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    /**
     * The system properties provider.
     */
    private SystemPropertiesProvider systemPropertiesProvider;
    /**
     * The Constant SYSTEM_FIWARE.
     */
    public static final String SYSTEM_FIWARE = "FIWARE";
    /**
     * The Constant SYSTEM_FASTTRACK.
     */
    public static final String SYSTEM_FASTTRACK = "FASTTRACK";
    /**
     * The Constant CODE_200. HTTP 200 ok
     */
    public static final int CODE_200 = 200;
    /**
     * The Constant CODE_203. HTTP 203 ok
     */
    public static final int CODE_203 = 203;
    /**
     * The Constant CODE_401.
     */
    public static final int CODE_401 = 401;
    /**
     * The log.
     */
    private static Logger log = LoggerFactory.getLogger(OpenStackAuthenticationProvider.class);

    /**
     * Thread to recover a valid X-Auth-Token.
     */
    private OpenStackAuthenticationToken oSAuthToken;

    /**
     * Cache for tokens.
     */
    private TokenCache tokenCache;

    /**
     * Jersey client used to validates token to OpenStack.
     */
    private Client client;

    /**
     * Default constructor.
     */
    public OpenStackAuthenticationProvider() {

        client = ClientBuilder.newClient();
        oSAuthToken = null;
        tokenCache = new TokenCache();
    }

    /*
     * (non-Javadoc) @seeorg.springframework.security.authentication.dao. AbstractUserDetailsAuthenticationProvider
     * #additionalAuthenticationChecks( org.springframework.security.core.userdetails.UserDetails, org.springframework
     * .security.authentication.UsernamePasswordAuthenticationToken)
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication) {
    }

    /**
     * Authentication fast track.
     * 
     * @param username
     *            the username
     * @param tenantId
     *            the tenantId
     * @return the open stack user
     */
    private PaasManagerUser authenticationFastTrack(String username, String tenantId) {
        return null;

    }

    /**
     * Authentication fiware.
     * 
     * @param token
     *            the token
     * @param tenantId
     *            the tenantId
     * @return the open stack user
     */

    public PaasManagerUser authenticationFiware(String token, String tenantId) {

        String keystoneURL = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_URL);

        String adminUser = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_USER);

        String adminPass = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_PASS);

        String adminTenant = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_TENANT);

        Client client1 = ClientBuilder.newClient();
        configureOpenStackAuthenticationToken(keystoneURL, adminUser, adminPass, adminTenant, client1);

        String[] adminCredentials = tokenCache.getAdmin();

        if (adminCredentials == null) {
            adminCredentials = oSAuthToken.getCredentials();
            tokenCache.putAdmin(adminCredentials[0], adminCredentials[1]);
        }

        log.debug("Keystone URL : " + keystoneURL);
        log.debug("adminToken : " + adminCredentials[0]);

        AuthenticateResponse authenticateResponse = tokenCache.getAuthenticateResponse(token, tenantId);

        try {
            if (authenticateResponse == null) {

                WebTarget webResource = client.target(keystoneURL);
                WebTarget tokens = webResource.path("tokens").path(token);
                Invocation.Builder builder = tokens.request();
                Response response = builder.accept(MediaType.APPLICATION_XML)
                        .header("X-Auth-Token", adminCredentials[0]).get();
                int status = response.getStatus();

                if ((status == CODE_200) || (status == CODE_203)) {
                    // Validate user's token
                    authenticateResponse = response.readEntity(AuthenticateResponse.class);
                    PaasManagerUser userValidated = createPaasManagerUser(token, tenantId, authenticateResponse);
                    log.info("generated new token for tenantId:" + tenantId + ": " + token);
                    tokenCache.put(token + "-" + tenantId, authenticateResponse);

                    return userValidated;
                } else {
                    log.warn("response status:" + status);

                    if (status == CODE_401) {
                        throw new BadCredentialsException("Invalid token");
                    }

                    throw new AuthenticationServiceException("Invalid token");
                }
            } else {
                return createPaasManagerUser(token, tenantId, authenticateResponse);
            }
        } catch (Exception e) {
            log.warn("Exception in authentication: " + e);
            throw new AuthenticationServiceException("Unknown problem", e);
        }
    }

    /**
     * Creates paasManagerUser.
     * 
     * @param token
     * @param tenantId
     * @param authenticateResponse
     * @return
     */
    private PaasManagerUser createPaasManagerUser(String token, String tenantId,
            AuthenticateResponse authenticateResponse) {

        if (!tenantId.equals(authenticateResponse.getToken().getTenant().getId())) {
            throw new AuthenticationServiceException("Token " + authenticateResponse.getToken().getTenant().getId()
                    + " not valid for the tenantId provided:" + tenantId);
        }

        Set<GrantedAuthority> authsSet = new HashSet<GrantedAuthority>();

        if (authenticateResponse.getUser().getRoles() != null) {
            for (Role role : authenticateResponse.getUser().getRoles().getRole()) {
                authsSet.add(new GrantedAuthorityImpl(role.getName()));
            }
        }

        PaasManagerUser user = new PaasManagerUser(authenticateResponse.getUser().getOtherAttributes()
                .get(new QName("username")), token, authsSet);

        user.setTenantId(tenantId);
        user.setTenantName(authenticateResponse.getToken().getTenant().getName());
        user.setToken(token);
        return user;
    }

    private void configureOpenStackAuthenticationToken(String keystoneURL, String adminUser, String adminPass,
            String adminTenant, Client client) {
        ArrayList<Object> params = new ArrayList();

        params.add(keystoneURL + "tokens");
        params.add(adminTenant);
        params.add(adminUser);
        params.add(adminPass);
        params.add(client);

        if (oSAuthToken == null) {
            oSAuthToken = new OpenStackAuthenticationToken(params);
        } else {
            oSAuthToken.initialize(params);
        }
    }

    /**
     * Gets the system properties provider.
     * 
     * @return the systemPropertiesProvider
     */
    public final SystemPropertiesProvider getSystemPropertiesProvider() {
        return systemPropertiesProvider;
    }

    /*
     * (non-Javadoc) @seeorg.springframework.security.authentication.dao. AbstractUserDetailsAuthenticationProvider
     * #retrieveUser(java.lang.String, org .springframework.security.authentication.UsernamePasswordAuthenticationToken
     * )
     */
    @Override
    protected final UserDetails retrieveUser(final String username,
            final UsernamePasswordAuthenticationToken authentication) {
        String system = systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM);

        PaasManagerUser user = null;

        if (null != authentication.getCredentials()) {
            String tenantId = authentication.getCredentials().toString();

            if (SYSTEM_FIWARE.equals(system)) {
                user = authenticationFiware(username, tenantId);
            } else if (SYSTEM_FASTTRACK.equals(system)) {
                user = authenticationFastTrack(username, tenantId);
            }
        } else {
            String str = "Missing tenantId header";
            log.info(str);
            throw new BadCredentialsException(str);
        }

        return user;
    }

    /**
     * Sets the system properties provider.
     * 
     * @param pSystemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider pSystemPropertiesProvider) {
        this.systemPropertiesProvider = pSystemPropertiesProvider;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Getter the oSAuthToken.
     */
    public OpenStackAuthenticationToken getoSAuthToken() {
        return oSAuthToken;
    }

    /**
     * Setter the oSAuthToken.
     */
    public void setoSAuthToken(OpenStackAuthenticationToken oSAuthToken) {
        this.oSAuthToken = oSAuthToken;
    }

}
