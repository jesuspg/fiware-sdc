package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.Application;

public class ApplicationReleaseSearchCriteria extends AbstractSearchCriteria {

    private Application application;

    /**
     * Default constructor
     */
    public ApplicationReleaseSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param vm
     * @param application
     */
    public ApplicationReleaseSearchCriteria(Integer page, Integer pageSize,
            String orderBy, String orderType, Application application) {
        super(page, pageSize, orderBy, orderType);
        this.application = application;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param vm
     * @param application
     */
    public ApplicationReleaseSearchCriteria(String orderBy, String orderType,
            Application application) {
        super(orderBy, orderType);
        this.application = application;
    }

    /**
     * @param page
     * @param pagesize
     * @param vm
     * @param application
     */
    public ApplicationReleaseSearchCriteria(Integer page, Integer pageSize,
            Application application) {
        super(page, pageSize);
        this.application = application;
    }

    /**
     * @param vm
     */
    public ApplicationReleaseSearchCriteria(Application application) {
        this.application = application;
    }

    /**
     * @return the application
     */
    public Application getApplication() {
        return application;
    }

    /**
     * @param Application the application to set
     */
    public void setApplication(Application application) {
        this.application = application;
    }

}
