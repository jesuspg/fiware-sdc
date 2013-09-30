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

/**
 * Search criteria for application entities.
 * 
 * @author Sergio Arroyo
 * @version $Id: $
 */
public class ApplicationSearchCriteria extends AbstractSearchCriteria {

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     */
    public ApplicationSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType) {
        super(page, pageSize, orderBy, orderType);
    }

    /**
     * @param orderBy
     * @param orderType
     */
    public ApplicationSearchCriteria(String orderBy, String orderType) {
        super(orderBy, orderType);
    }

    /**
     * @param page
     * @param pagesize
     */
    public ApplicationSearchCriteria(Integer page, Integer pageSize) {
        super(page, pageSize);
    }

    /**
     */
    public ApplicationSearchCriteria() {
        super();
    }

}
