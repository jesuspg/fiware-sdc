package com.telefonica.euro_iaas.sdc.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO to receive rest request with the product releases objects.
 *
 * @author Sergio Arroyo
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReleaseDto {

    private String name;
    private String version;
    private String type;

    /**
     * Default constructor
     */
    public ReleaseDto() {
    }
   
    /**
     * Constructor of the class
     * @param name
     * @param version
     * @param type
     */
    public ReleaseDto(String name, String version, String type) {
        this.name = name;
        this.version = version;
        this.type = type;
    }
    
    /**
     * @return the type
     */
    public String getType() {
		return type;
	}
    /**
     * @param type the type to set
     */
	public void setType(String type) {
		this.type = type;
	}

	/**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }
    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }


}
