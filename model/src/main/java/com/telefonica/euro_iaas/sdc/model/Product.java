package com.telefonica.euro_iaas.sdc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Installable means executable units that can be installed, uninstalled, configured or updated in a VM.
 * 
 * @author Sergio Arroyo
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

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

    @Column(unique = true, nullable = false, length = 256)
    private String name;
    @Column(length = 2048)
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Attribute> attributes;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Metadata> metadatas;

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
     * @return the metadatas
     */
    public List<Metadata> getMetadatas() {
        return metadatas;
    }

    /**
     * @param metadatas
     *            the metadatas to set
     */
    public void setMetadatas(List<Metadata> metadatas) {
        this.metadatas = metadatas;
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

    public void addMetadata(Metadata meta) {
        if (metadatas == null) {
            metadatas = new ArrayList<Metadata>();
        }
        metadatas.add(meta);
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

    /**
     * @return the metadatas as a Map
     */
    public Map<String, String> getMapMetadata() {
        Map<String, String> mets = new HashMap<String, String>();
        for (Metadata metadata : metadatas) {
            mets.put(metadata.getKey(), metadata.getValue());
        }
        return mets;
    }

    /**
     * Constructor.
     */
    public Product() {
    }

    /**
     * Constructor.
     * 
     * @param name
     * @param description
     */
    public Product(String name, String description) {
        this.name = name;
        this.description = description;
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Product other = (Product) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
