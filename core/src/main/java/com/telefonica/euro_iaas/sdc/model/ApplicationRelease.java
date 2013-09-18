package com.telefonica.euro_iaas.sdc.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents the concrete version of an application.
 *
 * @author Sergio Arroyo
 *
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationRelease extends InstallableRelease {

    @ManyToOne(optional=false)
    private Application application;

    @ManyToMany
    private List<ProductRelease> supportedProducts;

    @XmlTransient
    @ManyToMany
    private List<ApplicationRelease> transitableReleases;

    /**
     * Default constructor.
     */
    public ApplicationRelease() {
    }

    /**
     * Constructor of the class.
     * @param version
     * @param releaseNotes
     * @param privateAttributes
     * @param application
     * @param supportedProducts
     * @param applicationReleases
     */
    public ApplicationRelease(String version, String releaseNotes,
            List<Attribute> privateAttributes, Application application,
            List<ProductRelease> supportedProducts,
            List<ApplicationRelease> applicationReleases) {
        super(version, releaseNotes, privateAttributes);
        this.application = application;
        this.supportedProducts = supportedProducts;
        this.transitableReleases = applicationReleases;
    }


    /**
     * Merge the private attributes of a concrete version with the common
     * attributes of the product
     * @return
     */
    public List<Attribute> getAttributes() {
        List<Attribute> attr = new ArrayList<Attribute>();
        attr.addAll(getApplication().getAttributes());
        attr.addAll(getPrivateAttributes());
        return attr;
    }


    ///////// GETTERS AND SETTERS ///////////
    /**
     * @return the application
     */
    public Application getApplication() {
        return application;
    }

    /**
     * @param application the application to set
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     * @return the supportedProducts
     */
    public List<ProductRelease> getSupportedProducts() {
        return supportedProducts;
    }

    /**
     * @param supportedProducts the supportedProducts to set
     */
    public void setSupportedProducts(List<ProductRelease> supportedProducts) {
        this.supportedProducts = supportedProducts;
    }

    /**
     * @return the applicationReleases
     */
    public List<ApplicationRelease> getTransitableReleases() {
        return transitableReleases;
    }

    /**
     * @param transitableReleases the applicationReleases to set
     */
    public void setTransitableReleases(
            List<ApplicationRelease> transitableReleases) {
        this.transitableReleases = transitableReleases;
    }

    /**
     * Add a transitable release.
     * @param transitableRelease the new release.
     */
    public void addTransitableRelease(ApplicationRelease transitableRelease) {
        if (transitableReleases == null) {
            transitableReleases = new ArrayList<ApplicationRelease>();
        }
        transitableReleases.add(transitableRelease);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((application == null) ? 0 : application.hashCode());
        result = prime
                * result
                + ((getVersion() == null) ? 0 : getVersion()
                        .hashCode());
        return result;
    }

    /* (non-Javadoc)
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
        ApplicationRelease other = (ApplicationRelease) obj;
        if (application == null) {
            if (other.application != null)
                return false;
        } else if (!application.equals(other.application))
            return false;
        if (getVersion() == null) {
            if (other.getVersion() != null)
                return false;
        } else if (!getVersion().equals(other.getVersion()))
            return false;
        return true;
    }


}
