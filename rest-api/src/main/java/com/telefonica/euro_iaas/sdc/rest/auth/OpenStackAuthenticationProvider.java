/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.rest.auth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.openstack.docs.identity.api.v2.AuthenticateResponse;
import org.openstack.docs.identity.api.v2.Role;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.sdc.model.dto.PaasManagerUser;
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
     * The Constant CODE_401.
     */
    public static final int CODE_401 = 401;
    /**
     * The Constant CODE_403.
     */
    public static final int CODE_403 = 403;
    /**
     * The Constant CODE_404.
     */
    public static final int CODE_404 = 404;
    /**
     * The max number of reintent.
     */
    public static final int MAX_REINTENT = 5;
    /**
     * The log.
     */
    private static Logger log = Logger.getLogger(OpenStackAuthenticationProvider.class);
    /**
     * Thread to recover a valid X-Auth-Token each 24 hour
     */
    OpenStackAuthenticationToken oSAuthToken = null;

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
    @SuppressWarnings("deprecation")
    private PaasManagerUser authenticationFiware(String token, String tenantId) {

        String keystoneURL = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_URL);

        String adminUser = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_USER);

        String adminPass = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_PASS);

        String adminTenant = systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_TENANT);

        String thresholdString = systemPropertiesProvider
                .getProperty(SystemPropertiesProvider.VALIDATION_TIME_THRESHOLD);

        DefaultHttpClient httpClient = new DefaultHttpClient();

        if (oSAuthToken == null) {
            ArrayList<Object> params = new ArrayList();

            Long threshold = Long.parseLong(thresholdString);

            params.add(keystoneURL);
            params.add(adminTenant);
            params.add(adminUser);
            params.add(adminPass);
            params.add(httpClient);
            params.add(threshold);

            oSAuthToken = new OpenStackAuthenticationToken(params);
        }

        String[] credential = oSAuthToken.getCredentials();

        log.debug("Keystone URL : " + keystoneURL);
        log.debug("adminToken : " + credential[0]);

        Client client = Client.create();
        WebResource webResource = client.resource(keystoneURL);
        try {

            // Validate user's token
            AuthenticateResponse responseAuth = webResource.path("tokens").path(token)
                    .header("Accept", "application/xml").header("X-Auth-Token", credential[0])
                    .get(AuthenticateResponse.class);

            if (!tenantId.equals(responseAuth.getToken().getTenant().getId())) {
                throw new AuthenticationServiceException("Token " + responseAuth.getToken().getTenant().getId()
                        + " not valid for the tenantId provided:" + tenantId);
            }

            Set<GrantedAuthority> authsSet = new HashSet<GrantedAuthority>();

            if (responseAuth.getUser().getRoles() != null) {
                for (Role role : responseAuth.getUser().getRoles().getRole()) {
                    authsSet.add(new GrantedAuthorityImpl(role.getName()));
                }
            }

            PaasManagerUser user = new PaasManagerUser(responseAuth.getUser().getOtherAttributes()
                    .get(new QName("username")), token, authsSet);

            user.setTenantId(tenantId);
            user.setTenantName(responseAuth.getToken().getTenant().getName());
            user.setToken(token);
            return user;

        } catch (UniformInterfaceException e) {
            // this.OSAuthToken.close();
            // this.OSAuthToken = null;

            log.error("response status:" + e.getResponse().getStatus());

            if ((e.getResponse().getStatus() == CODE_401) || (e.getResponse().getStatus() == CODE_403)
                    || (e.getResponse().getStatus() == CODE_404)) {
                throw new BadCredentialsException("Token not valid", e);
            }

            throw new AuthenticationServiceException("Token not valid", e);
        } catch (Exception e) {
            // this.OSAuthToken.close();
            // this.OSAuthToken = null;

            throw new AuthenticationServiceException("unknown problem", e);
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

        String tenantId = authentication.getCredentials().toString();

        if (SYSTEM_FIWARE.equals(system)) {
            user = authenticationFiware(username, tenantId);
        } else if (SYSTEM_FASTTRACK.equals(system)) {
            user = authenticationFastTrack(username, tenantId);
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

}
