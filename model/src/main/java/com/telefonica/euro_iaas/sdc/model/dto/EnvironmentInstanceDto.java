/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO to receive the complete information when a product release is going to be installed.
 * 
 * @author Jesus M. Movilla
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnvironmentInstanceDto {

    private EnvironmentDto environment;
    private List<ProductInstanceDto> products;

    /**
     */
    public EnvironmentInstanceDto() {
    }

    /**
     * @param environment
     * @param vm
     */
    public EnvironmentInstanceDto(EnvironmentDto environment, List<ProductInstanceDto> products) {
        this.environment = environment;
        this.products = products;
    }

    /**
     * @return the environment
     */
    public EnvironmentDto getEnvironment() {
        return environment;
    }

    /**
     * @param environment
     *            the environment to set
     */
    public void setEnvironment(EnvironmentDto environment) {
        this.environment = environment;
    }

    /**
     * @return the products
     */
    public List<ProductInstanceDto> getProducts() {
        if (products == null) {
            products = new ArrayList<ProductInstanceDto>();
        }
        return products;
    }

    /**
     * @param products
     *            the products to set
     */
    public void setProducts(List<ProductInstanceDto> products) {
        this.products = products;
    }

}
