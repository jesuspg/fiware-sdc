package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;

/**
 * Provides some criteria to search ProductInstance entities.
 *
 * @author Sergio Arroyo
 *
 */
public class ApplicationInstanceSearchCriteria extends AbstractSearchCriteria {

    /**
     * The status of the application (<i>this criteria return a list
     * of entities<i>).
     */
    private Status status;

    /**
     * Default constructor
     */
    public ApplicationInstanceSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param vm
     */
    public ApplicationInstanceSearchCriteria(Integer page, Integer pageSize,
            String orderBy, String orderType, Status status) {
        super(page, pageSize, orderBy, orderType);
        this.status = status;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param vm
     */
    public ApplicationInstanceSearchCriteria(String orderBy, String orderType,
           Status status) {
        super(orderBy, orderType);
        this.status = status;
    }

    /**
     * @param page
     * @param pagesize
     * @param vm
     */
    public ApplicationInstanceSearchCriteria(Integer page, Integer pageSize,
            Status status) {
        super(page, pageSize);
        this.status = status;
    }

    /**
     * @param vm
     */
    public ApplicationInstanceSearchCriteria(Status status) {
        this.status = status;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

}
