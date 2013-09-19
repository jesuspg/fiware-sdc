package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.sdc.model.Attribute;

/**
 * DTO for application Instance to receive rest request
 * @author Sergio Arroyo
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationInstanceDto {

    private String applicationName;
    private String version;
    private VM vm;
    private List<ReleaseDto> products;
    private List<Attribute> attributes;

    /**
     */
    public ApplicationInstanceDto() {
    }
    /**
     * @param applicationName
     * @param vm
     * @param products
     */
    public ApplicationInstanceDto(String applicationName, String version, VM vm,
            List<ReleaseDto> products) {
        this.version = version;
        this.applicationName = applicationName;
        this.vm = vm;
        this.products = products;
    }
    /**
     * @return the applicationName
     */
    public String getApplicationName() {
        return applicationName;
    }
    /**
     * @param applicationName the applicationName to set
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
    /**
     * @return the vm
     */
    public VM getVm() {
        return vm;
    }
    /**
     * @param vm the vm to set
     */
    public void setVm(VM vm) {
        this.vm = vm;
    }
    /**
     * @return the products
     */
    public List<ReleaseDto> getProducts() {
        return products;
    }
    /**
     * @param products the products to set
     */
    public void setProducts(List<ReleaseDto> products) {
        this.products = products;
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
    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }
    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

}
