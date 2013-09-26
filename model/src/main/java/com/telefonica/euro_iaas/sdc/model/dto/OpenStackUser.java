/**
 *   (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 *
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */
package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

/**
 * @author dbermejo
 *
 */
public class OpenStackUser extends User {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    /**
     * The tenantId.
     */
    private String tenantId = "";
    /**
     * The tenantName.
     */
    private String tenantName = "";
    /**
     * The token.
     */
    private String token = "";
    /**
     * The Domain.
     */
    private String domain = "FIWARE";

    /**
     * Instantiates a new open stack user.
     *
     * @param username the username
     * @param password the password
     * @param authorities the authorities
     */
    public OpenStackUser(final String username, final String password,
            final Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.token = password;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * @param tenantId the tenantId to set
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return the tenantName
     */
    public String getTenantName() {
        return tenantName;
    }

    /**
     * @param tenantName the tenantName to set
     */
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }
}
