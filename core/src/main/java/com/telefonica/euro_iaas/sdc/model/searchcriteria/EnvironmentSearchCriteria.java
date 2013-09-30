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

package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Provides some criteria to search Environment entities.
 * 
 * @author Jesus M. Movilla
 */
public class EnvironmentSearchCriteria extends AbstractSearchCriteria {

    /**
     * The productRelease
     */
    private ProductRelease productRelease;

    /**
     * Default constructor
     */
    public EnvironmentSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param productRelease
     */
    public EnvironmentSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType,
            ProductRelease productRelease) {
        super(page, pageSize, orderBy, orderType);
        this.productRelease = productRelease;
    }

    /**
     * @param productRelease
     */
    public EnvironmentSearchCriteria(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }

    /**
     * @return the productRelease
     */
    public ProductRelease getProductRelease() {
        return productRelease;
    }

    /**
     * @param productRelease
     *            the productRelease to set
     */
    public void setProductRelease(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }

    @Override
    public String toString() {
        return "ProductInstanceSearchCriteria [productRelease=" + productRelease + "]";

    }

}
