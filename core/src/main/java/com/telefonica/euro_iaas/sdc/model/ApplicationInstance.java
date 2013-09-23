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
 * Defines a concrete application running over a concrete product instance.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
@Entity
public class ApplicationInstance {
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
    private Application application;
    @ManyToOne(optional=false)
    private ProductInstance productInstance;

    private Date date;

    /**
     * Default constructor.
     */
    public ApplicationInstance() {
        this.date = new Date();
    }
    /**
     * <p>Constructor for ApplicationInstance.</p>
     *
     * @param application a {@link com.telefonica.euro_iaas.sdc.model.Application} object.
     * @param productInstance a {@link com.telefonica.euro_iaas.sdc.model.ProductInstance} object.
     * @param status a {@link com.telefonica.euro_iaas.sdc.model.ApplicationInstance.Status} object.
     */
    public ApplicationInstance(Application application,
            ProductInstance productInstance, Status status) {
        this();
        this.application = application;
        this.productInstance = productInstance;
        this.status = status;
    }
    /**
     * <p>Constructor for ApplicationInstance.</p>
     *
     * @param id a {@link java.lang.Long} object.
     */
    public ApplicationInstance(Long id) {
        this();
        this.id = id;
    }


    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * <p>Getter for the field <code>application</code>.</p>
     *
     * @return the application
     */
    public Application getApplication() {
        return application;
    }

    /**
     * <p>Setter for the field <code>application</code>.</p>
     *
     * @param application the application to set
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     * <p>Getter for the field <code>productInstance</code>.</p>
     *
     * @return the productInstance
     */
    public ProductInstance getProductInstance() {
        return productInstance;
    }

    /**
     * <p>Setter for the field <code>productInstance</code>.</p>
     *
     * @param productInstance the productInstance to set
     */
    public void setProductInstance(ProductInstance productInstance) {
        this.productInstance = productInstance;
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
        ApplicationInstance other = (ApplicationInstance) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


}
