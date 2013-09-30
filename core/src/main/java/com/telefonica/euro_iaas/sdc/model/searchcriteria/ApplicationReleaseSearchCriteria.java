package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

public class ApplicationReleaseSearchCriteria extends AbstractSearchCriteria {

    private Application application;
    private ProductRelease productRelease;

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
     * @param productRelease
     */
    public ApplicationReleaseSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType,
            Application application, ProductRelease productRelease) {
        super(page, pageSize, orderBy, orderType);
        this.application = application;
        this.productRelease = productRelease;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param vm
     * @param application
     * @param productRelease
     */
    public ApplicationReleaseSearchCriteria(String orderBy, String orderType, Application application,
            ProductRelease productRelease) {
        super(orderBy, orderType);
        this.application = application;
        this.productRelease = productRelease;
    }

    /**
     * @param page
     * @param pagesize
     * @param vm
     * @param application
     * @param productRelease
     */
    public ApplicationReleaseSearchCriteria(Integer page, Integer pageSize, Application application,
            ProductRelease productRelease) {
        super(page, pageSize);
        this.application = application;
        this.productRelease = productRelease;
    }

    /**
     * @param application
     * @param productRelease
     */
    public ApplicationReleaseSearchCriteria(Application application, ProductRelease productRelease) {
        this.application = application;
        this.productRelease = productRelease;
    }

    /**
     * @return the application
     */
    public Application getApplication() {
        return application;
    }

    /**
     * @param Application
     *            the application to set
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     * @return the productRelease
     */
    public ProductRelease getProductRelease() {
        return productRelease;
    }

    /**
     * @param ProductRelease
     *            the productRelease to set
     */
    public void setProductRelease(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }

}
