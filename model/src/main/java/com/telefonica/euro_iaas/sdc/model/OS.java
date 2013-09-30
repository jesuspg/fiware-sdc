package com.telefonica.euro_iaas.sdc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * Represents an available SO in the system. This entity does not provides any information about a concrete
 * installation.
 * 
 * @author Sergio Arroyo
 * @version $Id: $
 */
@Entity
public class OS {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @SuppressWarnings("unused")
    @Version
    private Long v;

    @Column(unique = true, nullable = false, length = 3)
    private String osType;
    // @Column(unique=true, nullable=false, length=256)
    @Column(length = 128)
    private String name;
    @Column(length = 2048)
    private String description;
    @Column(length = 128)
    private String version;

    /**
     * Default constructor
     */
    public OS() {
    }

    /**
     * <p>
     * Constructor for SO.
     * </p>
     * 
     * @param OSType
     *            a {@link java.lang.String} object.
     * @param name
     *            a {@link java.lang.String} object.
     * @param description
     *            a {@link java.lang.String} object.
     * @param version
     *            a {@link java.lang.String} object.
     */
    public OS(String osType, String name, String description, String version) {
        this.osType = osType;
        this.name = name;
        this.description = description;
        this.version = version;
    }

    /**
     * <p>
     * Getter for the field <code>name</code>.
     * </p>
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Setter for the field <code>name</code>.
     * </p>
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>
     * Getter for the field <code>OSType</code>.
     * </p>
     * 
     * @return the OSType
     */
    public String getOsType() {
        return osType;
    }

    /**
     * <p>
     * Setter for the field <code>OsType</code>.
     * </p>
     * 
     * @param name
     *            the OsType to set
     */
    public void setOsType(String osType) {
        this.osType = osType;
    }

    /**
     * <p>
     * Getter for the field <code>description</code>.
     * </p>
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>
     * Setter for the field <code>description</code>.
     * </p>
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>
     * Getter for the field <code>version</code>.
     * </p>
     * 
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * <p>
     * Setter for the field <code>version</code>.
     * </p>
     * 
     * @param version
     *            the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * <p>
     * Getter for the field <code>id</code>.
     * </p>
     * 
     * @return the id
     */
    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((osType == null) ? 0 : osType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OS other = (OS) obj;
        if (osType == null) {
            if (other.osType != null)
                return false;
        } else if (!osType.equals(other.osType))
            return false;
        return true;
    }

}
