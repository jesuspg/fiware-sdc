/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

/**
 * Represents a host where a OS is running. It is formed by
 * 
 * @author Sergio Arroyo
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VM {
    /** The ip where the host is located. */
    private String ip;
    /** The computer's hostname. */
    private String hostname;
    /** The domain. */
    private String domain;
    /** the fqn ***/
    private String fqn;
    /** the OSType ***/
    private String osType;

    public VM() {
        this.ip = "";
        this.fqn = "";
        this.hostname = "";
        this.domain = "";
        this.osType = "";
    }

    /**
     * @param fqn
     * @param hostname
     * @param domain
     */
    public VM(String fqn, String hostname, String domain) {
        if (fqn == null) {
            this.fqn = "";
        } else {
            this.fqn = fqn;
        }
        if (hostname == null) {
            this.hostname = "";
        } else {
            this.hostname = hostname;
        }
        if (domain == null) {
            this.domain = "";
        } else {
            this.domain = domain;
        }
        this.ip = "";
        this.osType = "";
    }

    /**
     * @param ip
     * @param fqn
     * @param hostname
     * @param domain
     */
    public VM(String fqn, String ip, String hostname, String domain) {
        this.osType = "";
        if (ip == null) {
            this.ip = "";
        } else {
            this.ip = ip;
        }
        if (fqn == null) {
            this.fqn = "";
        } else {
            this.fqn = fqn;
        }
        if (hostname == null) {
            this.hostname = "";
        } else {
            this.hostname = hostname;
        }
        if (domain == null) {
            this.domain = "";
        } else {
            this.domain = domain;
        }
    }

    /**
     * @param hostname
     * @param domain
     */
    /*
     * public VM(String hostname, String domain) { this.ip = ""; this.fqn = ""; if (hostname == null) { this.hostname =
     * ""; } else { this.hostname = hostname; } if (domain == null) { this.domain = ""; } else { this.domain = domain; }
     * }
     */

    /**
     * @param fqn
     * @param ip
     */
    public VM(String fqn, String ip) {
        this.hostname = "";
        this.domain = "";
        this.osType = "";
        if (fqn == null) {
            this.fqn = "";
        } else {
            this.fqn = fqn;
        }
        if (ip == null) {
            this.ip = "";
        } else {
            this.ip = ip;
        }
    }

    /**
     * @param fqn
     */
    public VM(String fqn) {
        if (fqn == null) {
            this.fqn = "";
        } else {
            this.fqn = fqn;
        }
        this.ip = "";
        this.hostname = "";
        this.domain = "";
        this.osType = "";
    }

    /**
     * @param ip
     * @param fqn
     * @param hostname
     * @param domain
     */
    public VM(String fqn, String ip, String hostname, String domain, String osType) {
        if (ip == null) {
            this.ip = "";
        } else {
            this.ip = ip;
        }
        if (fqn == null) {
            this.fqn = "";
        } else {
            this.fqn = fqn;
        }
        if (hostname == null) {
            this.hostname = "";
        } else {
            this.hostname = hostname;
        }
        if (domain == null) {
            this.domain = "";
        } else {
            this.domain = domain;
        }
        if (osType == null) {
            this.osType = "";
        } else {
            this.osType = osType;
        }
    }

    /**
     * Decides if chef can work with this VM or if doesn't.
     * 
     * @return <code>true</code> if Chef can work with the available information for this VM
     */
    public Boolean canWorkWithChef() {
        return !StringUtils.isEmpty(hostname) && !StringUtils.isEmpty(domain);
    }

    /**
     * Decides if SDC Server can communicate with VM or if can't
     * 
     * @return <code>true</code> if SDC can communicate with VM with the available information for this VM
     */
    public Boolean canWorkWithNodes() {
        return !StringUtils.isEmpty(ip);
    }

    /**
     * Get the name that Chef needs to work with.
     * 
     * @return
     */
    public String getChefClientName() {
        if (StringUtils.isEmpty(hostname + domain)) {
            throw new IllegalStateException("ChefClientName can not be empty");
        }

        if (hostname.contains(domain))
            return hostname;
        else
            return hostname + domain;
    }

    public String getExecuteChefConectionUrl() {
        if (!StringUtils.isEmpty(ip)) {
            return ip;
        } else {
            return hostname + domain;
        }
    }

    // // ACCESSORS ////

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname
     *            the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
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

    /**
     * @return the fqn
     */
    public String getFqn() {
        return fqn;
    }

    /**
     * @param fqn
     *            the fqn to set
     */
    public void setFqn(String fqn) {
        this.fqn = fqn;
    }

    /**
     * @return the osType
     */
    public String getOsType() {
        return osType;
    }

    /**
     * @param osType
     *            the osType to set
     */
    public void setOsType(String osType) {
        this.osType = osType;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((domain == null) ? 0 : domain.hashCode());
        result = prime * result + ((fqn == null) ? 0 : fqn.hashCode());
        result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VM other = (VM) obj;
        if (domain == null) {
            if (other.domain != null)
                return false;
        } else if (!domain.equals(other.domain))
            return false;
        if (fqn == null) {
            if (other.fqn != null)
                return false;
        } else if (!fqn.equals(other.fqn))
            return false;
        if (hostname == null) {
            if (other.hostname != null)
                return false;
        } else if (!hostname.equals(other.hostname))
            return false;
        if (ip == null) {
            if (other.ip != null)
                return false;
        } else if (!ip.equals(other.ip))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VM [domain=" + domain + ", hostname=" + hostname + ", ip=" + ip + ", fqn=" + fqn + ", osType=" + osType
                + "]";
    }

}
