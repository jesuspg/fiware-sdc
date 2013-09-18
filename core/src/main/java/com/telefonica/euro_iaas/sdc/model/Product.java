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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 * Represents an Product (like application server, data base manager, etc.)
 * available in the system.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlTransient
    private Long id;

    @SuppressWarnings("unused")
    @Version
    @XmlTransient
    private Long v;

    @Column(unique=true, nullable=false, length=256)
    private String name;
    @Column(length=128)
    private String version;
    @ManyToMany(fetch=FetchType.EAGER)
    private List<OS> suportedSSOO;
    @OneToMany(cascade=CascadeType.ALL)
    private List<Attribute> attributes;

    /**
     * <p>Constructor for Product.</p>
     */
    public Product() {
    }

    /**
     * <p>Constructor for Product.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param version a {@link java.lang.String} object.
     * @param suportedSSOO a {@link java.util.List} object.
     */
    public Product(String name, String version, List<OS> suportedSSOO) {
        this.name = name;
        this.version = version;
        this.suportedSSOO = suportedSSOO;
    }
    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * <p>Getter for the field <code>version</code>.</p>
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }
    /**
     * <p>Setter for the field <code>version</code>.</p>
     *
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }
    /**
     * <p>Getter for the field <code>suportedSSOO</code>.</p>
     *
     * @return the suportedSSOO
     */
    public List<OS> getSuportedSSOO() {
        return suportedSSOO;
    }
    /**
     * <p>Setter for the field <code>suportedSSOO</code>.</p>
     *
     * @param suportedSSOO the suportedSSOO to set
     */
    public void setSuportedSSOO(List<OS> suportedSSOO) {
        this.suportedSSOO = suportedSSOO;
    }

    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
    /**
     * Add a new attribute.
     *
     * @param attribute the attribute
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

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    /** {@inheritDoc} */
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
