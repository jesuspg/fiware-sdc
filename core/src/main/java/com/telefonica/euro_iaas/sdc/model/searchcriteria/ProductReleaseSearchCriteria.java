/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.Product;

/**
 * Provides some criteria to search ProductInstance entities.
 * 
 * @author Sergio Arroyo
 */
public class ProductReleaseSearchCriteria extends AbstractSearchCriteria {

    /**
     * The product.
     */
    private Product product;

    /**
     * The osType.
     */
    private String osType;

   
    /**
     * Default constructor
     */
    public ProductReleaseSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param vm
     * @param product
     */
    public ProductReleaseSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType,
            Product product) {
        super(page, pageSize, orderBy, orderType);
        this.product = product;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param product
     */
    public ProductReleaseSearchCriteria(String orderBy, String orderType, Product product) {
        super(orderBy, orderType);
        this.product = product;
    }

    /**
     * @param page
     * @param pagesize
     * @param vm
     * @param product
     */
    public ProductReleaseSearchCriteria(Integer page, Integer pageSize, Product product) {
        super(page, pageSize);
        this.product = product;
    }

    /**
     * @param vm
     */
    public ProductReleaseSearchCriteria(Product product) {
        this.product = product;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return the osType
     */
    public String getOSType() {
        return osType;
    }

    /**
     * @param osType
     *            the osTypeto set
     */
    public void setOSType(String osType) {
        this.osType = osType;
    }
}
