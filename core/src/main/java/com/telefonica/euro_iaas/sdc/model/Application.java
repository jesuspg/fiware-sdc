package com.telefonica.euro_iaas.sdc.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Version;

/**
 * Defines the catalog of applications available in the system. An application
 * is a deployable unit that will be run over a product.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
@Entity
public class Application {

    @Id
    private Long id;

    @SuppressWarnings("unused")
    @Version
    private Long v;

    @Column(unique=true, nullable=false, length=256)
    private String name;
    @Column(length=2048)
    private String description;
    @ManyToMany
    private List<Product> supportedProducts;

    /**
     * Default constructor.
     */
    public Application() {}

    /**
     * <p>Constructor for Application.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param supportedProducts a {@link java.util.List} object.
     */
    public Application(String name, String description,
            List<Product> supportedProducts) {
        this.name = name;
        this.description = description;
        this.supportedProducts = supportedProducts;
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
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * <p>Getter for the field <code>supportedProducts</code>.</p>
     *
     * @return the supportedProducts
     */
    public List<Product> getSupportedProducts() {
        return supportedProducts;
    }
    /**
     * <p>Setter for the field <code>supportedProducts</code>.</p>
     *
     * @param supportedProducts the supportedProducts to set
     */
    public void setSupportedProducts(List<Product> supportedProducts) {
        this.supportedProducts = supportedProducts;
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
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Application other = (Application) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
