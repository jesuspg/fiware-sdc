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

import java.util.List;

/**
 * DTO to receive the complete information when a product release is going to be installed.
 * 
 * @author Jesus M. Movilla
 */

public class EnvironmentDto {

    private List<ProductReleaseDto> products;
    private String name;
    private String description;

    /**
     */
    public EnvironmentDto() {
    }

    /**
     * @param name
     * @param products
     */
    public EnvironmentDto(String name, List<ProductReleaseDto> products) {
        this.name = name;
        this.products = products;
    }

    /**
     * @param products
     */
    public EnvironmentDto(List<ProductReleaseDto> products) {
        this.name = getNameFromProductReleaseList(products);
        this.products = products;
    }

    public List<ProductReleaseDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductReleaseDto> products) {
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String getNameFromProductReleaseList(List<ProductReleaseDto> productReleases) {
        String name = "";
        for (ProductReleaseDto productRelease : productReleases) {
            name = name + productRelease.getProductName() + "-" + productRelease.getVersion() + "_";
        }
        return name;
    }
}
