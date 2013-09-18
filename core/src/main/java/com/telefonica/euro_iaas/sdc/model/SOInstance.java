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
 *  SOState represents a SO that is being installed (or is installed).
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
@Entity
public class SOInstance {

    public enum Status {INSTALLING, RUNNING, STOPPING, STOPPED};

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SuppressWarnings("unused")
    @Version
    private Long version;

    @ManyToOne(optional=false)
    private SO so;

    private Date date;

    @Enumerated(EnumType.STRING)
    private Status status;
    
    private String hostname;

    //TODO define more information like host, etc...


    /**
     * <p>Constructor for SOInstance.</p>
     */
    public SOInstance() {
        this.date = new Date();
    }


    /**
     * <p>Constructor for SOInstance.</p>
     *
     * @param so a {@link com.telefonica.euro_iaas.sdc.model.SO} object.
     * @param status a {@link com.telefonica.euro_iaas.sdc.model.SOInstance.Status} object.
     */
    public SOInstance(SO so, Status status) {
        this.so = so;
        this.status = status;
        this.date = new Date();
    }

    /**
     * <p>Getter for the field <code>so</code>.</p>
     *
     * @return the so
     */
    public SO getSo() {
        return so;
    }

    /**
     * <p>Setter for the field <code>so</code>.</p>
     *
     * @param so the so to set
     */
    public void setSo(SO so) {
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
     * <p>Getter for the field <code>hostname</code>.</p>
     *
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * <p>Setter for the field <code>hostname</code>.</p>
     *
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
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

}
