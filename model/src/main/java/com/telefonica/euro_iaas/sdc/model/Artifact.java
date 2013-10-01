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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents an artifact to be installed on a ProductRelease.
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Artifact {

    public static final String VDC_FIELD = "vdc";

    public static final String PRODUCT_FIELD = "productInstance";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @XmlTransient
    private Long id;

    @Column(unique = true, nullable = false, length = 256)
    private String name;

    @Column(nullable = false, length = 256)
    private String vdc;

    // @ManyToOne(optional=false)
    // private ProductInstance productInstance;

    // @ManyToOne
    // @JoinColumn(name = "productinstance_id")
    // private ProductInstance productInstance;

    // @ManyToOne(optional=false)
    // @JoinColumn(name="productinstance_id")
    // @JoinColumn(name = "id")
    // @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "productinstance_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = ProductInstance.class, optional = false, fetch = FetchType.LAZY)
    @XmlTransient
    private ProductInstance productInstance;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Attribute> attributes;

    /**
     * Default Constructor
     */
    public Artifact() {

    }

    /**
     * @param name
     * @param path
     * @param artifactType
     * @param productRelease
     */
    public Artifact(String name, List<Attribute> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    /**
     * @param name
     * @param path
     * @param artifactType
     * @param productRelease
     */
    public Artifact(String name, String vdc, ProductInstance productInstance, List<Attribute> attributes) {
        this.name = name;
        this.vdc = vdc;
        this.productInstance = productInstance;
        this.attributes = attributes;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    /**
     * @return the attributes
     */

    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the attributes
     */

    public ProductInstance getProductInstance() {
        return this.productInstance;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setProductInstance(ProductInstance productInstance) {
        this.productInstance = productInstance;
    }

    /**
     * Add a new attribute.
     * 
     * @param attribute
     *            the attribute
     */
    public void addAttribute(Attribute attribute) {
        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        attributes.add(attribute);
    }

    /**
     * @return the attributes as a Map
     */
    public Map<String, String> getMapAttributes() {
        Map<String, String> atts = new HashMap<String, String>();
        for (Attribute att : attributes) {
            atts.put(att.getKey(), att.getValue());
        }
        return atts;
    }

}
