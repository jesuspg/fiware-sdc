/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.sdc.model.Attribute;

/**
 * DTO to receive the complete information when a product release is going to be installed.
 * 
 * @author Sergio Arroyo
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductInstanceDto {

    // private ProductReleaseDto product;
    private ReleaseDto product;
    private VM vm;
    private List<Attribute> attributes;
    private String vdc;

    /**
     */
    public ProductInstanceDto() {
    }

    /**
     * @param product
     * @param vm
     */
    public ProductInstanceDto(ReleaseDto product, VM vm) {
        this.product = product;
        this.vm = vm;
    }

    /**
     * @return the product
     */
    public ReleaseDto getProduct() {
        return product;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProduct(ReleaseDto product) {
        this.product = product;
    }

    /**
     * @return the vm
     */
    public VM getVm() {
        return vm;
    }

    /**
     * @param vm
     *            the vm to set
     */
    public void setVm(VM vm) {
        this.vm = vm;
    }

    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param vdc
     *            the vdc to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }
}
