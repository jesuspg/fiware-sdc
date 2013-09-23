package com.telefonica.euro_iaas.sdc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.telefonica.euro_iaas.sdc.model.dto.VM;
/**
 *  SOState represents a SO that is being installed (or is installed).
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OSInstance {

    public enum Status {INSTALLING, RUNNING, STOPPING, STOPPED};

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SuppressWarnings("unused")
    @Version
    @XmlTransient
    private Long version;

    @ManyToOne(optional=false)
    private OS so;

    private Date date;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Embedded
    private VM vm;

    @Column(length=1024)
    private String imageFileLocation;

    //TODO define more information like host, etc...


    /**
     * <p>Constructor for SOInstance.</p>
     */
    public OSInstance() {
        this.date = new Date();
    }


    /**
     * <p>Constructor for SOInstance.</p>
     *
     * @param so a {@link com.telefonica.euro_iaas.sdc.model.OS} object.
     * @param status a {@link com.telefonica.euro_iaas.sdc.model.OSInstance.Status} object.
     */
    public OSInstance(OS so, Status status, VM vm) {
        this.so = so;
        this.status = status;
        this.vm = vm;
        this.date = new Date();
    }

    /**
     * <p>Getter for the field <code>so</code>.</p>
     *
     * @return the so
     */
    public OS getSo() {
        return so;
    }

    /**
     * <p>Setter for the field <code>so</code>.</p>
     *
     * @param so the so to set
     */
    public void setSo(OS so) {
        this.so = so;
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
     * @return the vm
     */
    public VM getVM() {
        return vm;
    }


    /**
     * @param vm the host to set
     */
    public void setVM(VM vm) {
        this.vm = vm;
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


    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }


    /**
     * @return the imageFileLocation
     */
    public String getImageFileLocation() {
        return imageFileLocation;
    }


    /**
     * @param imageFileLocation the imageFileLocation to set
     */
    public void setImageFileLocation(String imageFileLocation) {
        this.imageFileLocation = imageFileLocation;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
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
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OSInstance other = (OSInstance) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


}
