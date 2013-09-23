package com.telefonica.euro_iaas.sdc.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
@Entity
/**
 * <p>Image class.</p>
 *
 * @author ju
 * @version $Id: $
 */
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SuppressWarnings("unused")
    @Version
    private Long version;

    private String description;
    
    private String url;

    @OneToOne(optional=false)
    private SOInstance so;
    @OneToMany(cascade=CascadeType.ALL)
    private List<ProductInstance> apps;
    @ManyToOne
    private Customer customer;

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
     * <p>Getter for the field <code>so</code>.</p>
     *
     * @return the so
     */
    public SOInstance getSo() {
        return so;
    }

    /**
     * <p>Setter for the field <code>so</code>.</p>
     *
     * @param so the so to set
     */
    public void setSo(SOInstance so) {
        this.so = so;
    }

    /**
     * <p>Getter for the field <code>apps</code>.</p>
     *
     * @return the apps
     */
    public List<ProductInstance> getApps() {
        return apps;
    }

    /**
     * <p>Setter for the field <code>apps</code>.</p>
     *
     * @param apps the apps to set
     */
    public void setApps(List<ProductInstance> apps) {
        this.apps = apps;
    }

    /**
     * <p>Getter for the field <code>customer</code>.</p>
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * <p>Setter for the field <code>customer</code>.</p>
     *
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }
    
    /**
     * <p>Getter for the field <code>url</code>.</p>
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * <p>Setter for the field <code>url</code>.</p>
     *
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
    //TODO define equals criteria.

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Image other = (Image) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


}
