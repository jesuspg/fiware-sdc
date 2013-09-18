package com.telefonica.euro_iaas.sdc.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

/**
 * Represents a host where a OS is running. It is formed by
 *
 * @author Sergio Arroyo
 *
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

    public VM() {
        this.ip = "";
        this.hostname = "";
        this.domain = "";
    }

    /**
     * @param hostname
     * @param domain
     */
    public VM(String ip, String hostname, String domain) {
        if (ip == null) {
            this.ip = "";
        } else {
            this.ip = ip;
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
    public VM(String hostname, String domain) {
        this.ip = "";
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
     * @param ip
     */
    public VM(String ip) {
        if (ip == null) {
            this.ip = "";
        } else {
            this.ip = ip;
        }
        this.hostname = "";
        this.domain = "";
    }

    /**
     * Decides if chaf can work with this VM or if doesn't.
     *
     * @return <code>true</code> if Chef can work with the available information
     *         for this VM
     */
    public Boolean canWorkWithChef() {
        return !StringUtils.isEmpty(hostname) && !StringUtils.isEmpty(domain);
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

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((domain == null) ? 0 : domain.hashCode());
        result = prime * result
                + ((hostname == null) ? 0 : hostname.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
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
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VM [domain=" + domain + ", hostname=" + hostname + ", ip=" + ip
                + "]";
    }

}
