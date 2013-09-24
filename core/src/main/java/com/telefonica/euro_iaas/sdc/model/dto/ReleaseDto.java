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

    private String product;
    private String version;

    /**
     * Default constructor
     */
    public ReleaseDto() {
    }

    /**
     * Constructor of the class
     * @param product
     * @param version
     */
    public ReleaseDto(String product, String version) {
        this.product = product;
        this.version = version;
    }

    /**
     * @return the product
     */
    public String getProduct() {
        return product;
    }
    /**
     * @param product the product to set
     */
    public void setProduct(String product) {
        this.product = product;
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
