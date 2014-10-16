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
 * A product release is a concrete version of a given product.
 * 
 * @author Sergio Arroyo
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductRelease extends InstallableRelease {

    @ManyToOne(optional = false)
    private Product product;

    @ManyToMany
    private List<OS> supportedOOSS;

    @XmlTransient
    @ManyToMany
    private List<ProductRelease> transitableReleases;

    /**
     * Default constructor
     */
    public ProductRelease() {
    }

    /**
     * Constructor of the class.
     * 
     * @param version
     * @param releaseNotes
     * @param privateAttributes
     * @param product
     * @param supportedOOSS
     * @param transitableReleases
     */
    // public ProductRelease(String version, String releaseNotes,
    // List<Attribute> privateAttributes, Product product,
    // List<OS> supportedOOSS, List<ProductRelease> transitableReleases) {
    // super(version, releaseNotes, privateAttributes);
    public ProductRelease(String version, String releaseNotes, Product product, List<OS> supportedOOSS,
            List<ProductRelease> transitableReleases) {
        super(version, releaseNotes);
        this.product = product;
        this.supportedOOSS = supportedOOSS;
        this.transitableReleases = transitableReleases;
    }

    /**
     * Merge the private attributes of a concrete version with the common
     * attributes of the product
     * 
     * @return
     */
    /*
     * public List<Attribute> getAttributes() { List<Attribute> attr = new
     * ArrayList<Attribute>(); if (getProduct().getAttributes() != null) {
     * attr.addAll(getProduct().getAttributes()); } if (getPrivateAttributes()
     * != null) { attr.addAll(getPrivateAttributes()); } return attr; }
     */

    // /////// GETTERS AND SETTERS /////////
    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return the suportedOOSS
     */
    public List<OS> getSupportedOOSS() {
        return supportedOOSS;
    }

    /**
     * @param suportedOOSS
     *            the suportedOOSS to set
     */
    public void setSupportedOOSS(List<OS> supportedOOSS) {
        this.supportedOOSS = supportedOOSS;
    }

    /**
     * @return the transitableReleases
     */
    public List<ProductRelease> getTransitableReleases() {
        return transitableReleases;
    }

    /**
     * @param transitableReleases
     *            the transitableReleases to set
     */
    public void setTransitableReleases(List<ProductRelease> transitableReleases) {
        this.transitableReleases = transitableReleases;
    }

    /**
     * Add a transitable release.
     * 
     * @param transitableRelease
     *            the new release.
     */
    public void addTransitableRelease(ProductRelease transitableRelease) {
        if (transitableReleases == null) {
            transitableReleases = new ArrayList<ProductRelease>();
        }
        transitableReleases.add(transitableRelease);
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
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
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
        ProductRelease other = (ProductRelease) obj;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (!product.equals(other.product))
            return false;
        if (getVersion() == null) {
            if (other.getVersion() != null)
                return false;
        } else if (!getVersion().equals(other.getVersion()))
            return false;
        return true;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value
     * format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[ProductRelease]");
        sb.append("[product = ").append(this.product).append("]");
        sb.append("[supportedOOSS = ").append(this.supportedOOSS).append("]");
        sb.append("[transitableReleases = ").append(this.transitableReleases).append("]");
        sb.append("]");
        return sb.toString();
    }

}
