/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;

/**
 * Search criteria for products entities.
 * 
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class ProductSearchCriteria extends AbstractSearchCriteria {

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     */
    public ProductSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType) {
        super(page, pageSize, orderBy, orderType);
    }

    /**
     * @param orderBy
     * @param orderType
     */
    public ProductSearchCriteria(String orderBy, String orderType) {
        super(orderBy, orderType);
    }

    /**
     * @param page
     * @param pagesize
     */
    public ProductSearchCriteria(Integer page, Integer pageSize) {
        super(page, pageSize);
    }

    /**
     */
    public ProductSearchCriteria() {
        super();
    }
}
