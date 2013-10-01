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

package com.telefonica.euro_iaas.sdc.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

/**
 * A product release is a concrete version of a given product.
 * 
 * @author Sergio Arroyo
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class InstallableRelease {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlTransient
    private Long id;

    // commented out due to problems in updateProductRelease in Catalog
    // Management
    /*
     * @SuppressWarnings("unused")
     * @Version
     * @XmlTransient private Long v;
     */

    @Column(length = 128)
    private String version;

    @Column(length = 2048)
    private String releaseNotes;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Attribute> privateAttributes;

    /**
     * Default constructor.
     */
    public InstallableRelease() {
    }

    /**
     * Constructor of the class
     * 
     * @param version
     *            the version
     * @param releaseNotes
     *            the releases notes
     * @param privateAttributes
     *            the attributes available for this concrete release.
     */
    public InstallableRelease(String version, String releaseNotes, List<Attribute> privateAttributes) {
        super();
        this.version = version;
        this.releaseNotes = releaseNotes;
        this.privateAttributes = privateAttributes;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the releaseNotes
     */
    public String getReleaseNotes() {
        return releaseNotes;
    }

    /**
     * @param releaseNotes
     *            the releaseNotes to set
     */
    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    /**
     * @return the privateAttributes
     */
    public List<Attribute> getPrivateAttributes() {
        return privateAttributes;
    }

    /**
     * @param privateAttributes
     *            the privateAttributes to set
     */
    public void setPrivateAttributes(List<Attribute> privateAttributes) {
        this.privateAttributes = privateAttributes;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

}
