package com.telefonica.euro_iaas.sdc.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
/**
 *  Defines a product that is installed in the system.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
@Entity
public class ProductInstance {
    /**
     * Defines the value of the different status the Application could be.
     */
    public enum Status {INSTALLING, INSTALLED, ERROR, UNINSTALLING, UNINSTALLED};

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SuppressWarnings("unused")
    @Version
    private Long version;

    @ManyToOne(optional=false)
    private Product product;

    private Date date;

    @Enumerated(EnumType.STRING)
    private Status status;


    /**
     * <p>Constructor for ProductInstance.</p>
     */
    public ProductInstance() {
        this.date = new Date();
    }

    /**
     * <p>Constructor for ProductInstance.</p>
     *
     * @param application a {@link com.telefonica.euro_iaas.sdc.model.Product} object.
     * @param status a {@link com.telefonica.euro_iaas.sdc.model.ProductInstance.Status} object.
     */
    public ProductInstance(Product product, Status status) {
        this.product = product;
        this.status = status;
        this.date = new Date();
    }

    /**
     * <p>Getter for the field <code>product</code>.</p>
     *
     * @return the application
     */
    public Product getProduct() {
        return product;
    }

    /**
     * <p>Setter for the field <code>product</code>.</p>
     *
     * @param prodcut the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * <p>Getter for the field <code>date</code>.</p>
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * <p>Setter for the field <code>date</code>.</p>
     *
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * <p>Setter for the field <code>status</code>.</p>
     *
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    //TODO (Sergio Arroyo) define equals criteria.


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
        ProductInstance other = (ProductInstance) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


}
