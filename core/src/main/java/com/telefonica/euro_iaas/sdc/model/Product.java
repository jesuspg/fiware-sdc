package com.telefonica.euro_iaas.sdc.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Version;
/**
 * Represents an Product (like application server, data base manager, etc.)
 * available in the system.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SuppressWarnings("unused")
    @Version
    private Long v;

    @Column(unique=true, nullable=false, length=256)
    private String name;
    @Column(length=128)
    private String version;
    @ManyToMany(fetch=FetchType.EAGER)
    private List<SO> suportedSSOO;

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
    public Product(String name, String version, List<SO> suportedSSOO) {
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
    public List<SO> getSuportedSSOO() {
        return suportedSSOO;
    }
    /**
     * <p>Setter for the field <code>suportedSSOO</code>.</p>
     *
     * @param suportedSSOO the suportedSSOO to set
     */
    public void setSuportedSSOO(List<SO> suportedSSOO) {
        this.suportedSSOO = suportedSSOO;
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
