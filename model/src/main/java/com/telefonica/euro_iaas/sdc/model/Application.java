package com.telefonica.euro_iaas.sdc.model;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Defines the catalog of applications available in the system. An application is a deployable unit that will be run
 * over a product.
 * 
 * @author Sergio Arroyo
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Application extends Product {

    private String type;

    /**
     * Default constructor.
     */
    public Application() {
    }

    /**
     * <p>
     * Constructor for Application.
     * </p>
     * 
     * @param name
     *            a {@link java.lang.String} object.
     * @param description
     *            a {@link java.lang.String} object.
     * @param supportedProducts
     *            a {@link java.util.List} object.
     */
    public Application(String name, String description, String type) {
        super(name, description);
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
