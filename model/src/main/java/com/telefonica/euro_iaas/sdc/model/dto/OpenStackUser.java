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

package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

/**
 * @author dbermejo
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
     * @param username
     *            the username
     * @param password
     *            the password
     * @param authorities
     *            the authorities
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
     * @param token
     *            the token to set
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
     * @param tenantId
     *            the tenantId to set
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
     * @param tenantName
     *            the tenantName to set
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
     * @param domain
     *            the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }
}
