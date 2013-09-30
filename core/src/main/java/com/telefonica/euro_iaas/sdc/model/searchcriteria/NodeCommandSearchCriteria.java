package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.OS;

/**
 * Provides some criteria to search NodeCommand entities.
 * 
 * @author Jesus M. Movilla
 */
public class NodeCommandSearchCriteria extends AbstractSearchCriteria {

    /**
     * The os.
     */
    private OS os;

    /**
     * Default constructor
     */
    public NodeCommandSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param os
     */
    public NodeCommandSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType, OS os) {
        super(page, pageSize, orderBy, orderType);
        this.os = os;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param os
     */
    public NodeCommandSearchCriteria(String orderBy, String orderType, OS os) {
        super(orderBy, orderType);
        this.os = os;
    }

    /**
     * @param page
     * @param pagesize
     * @param os
     */
    public NodeCommandSearchCriteria(Integer page, Integer pageSize, OS os) {
        super(page, pageSize);
        this.os = os;
    }

    /**
     * @param os
     */
    public NodeCommandSearchCriteria(OS os) {
        this.os = os;
    }

    /**
     * @return the os
     */
    public OS getOS() {
        return os;
    }

    /**
     * @param os
     *            the os to set
     */
    public void setOS(OS os) {
        this.os = os;
    }
}
