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
    public ApplicationSearchCriteria(Integer page, Integer pageSize,
            String orderBy, String orderType) {
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

