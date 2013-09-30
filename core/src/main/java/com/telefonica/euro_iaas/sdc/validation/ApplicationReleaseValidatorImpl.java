package com.telefonica.euro_iaas.sdc.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;

public class ApplicationReleaseValidatorImpl implements ApplicationReleaseValidator {

    private ProductReleaseDao productReleaseDao;
    private ProductDao productDao;
    private ApplicationInstanceDao applicationInstanceDao;

    public void validateInsert(ApplicationRelease applicationRelease) throws ProductReleaseNotFoundException {
        // validate if the product release are in the system
        Product product;
        List<ProductRelease> inexistentProductReleases = new ArrayList<ProductRelease>();

        List<ProductRelease> productReleases = applicationRelease.getEnvironment().getProductReleases();

        for (ProductRelease productRelease : productReleases) {
            try {
                product = productDao.load(productRelease.getProduct().getName());
            } catch (EntityNotFoundException e1) {
                String productNotFounMessageLog = "Product " + productRelease.getProduct().getName()
                        + " is not in the System";
                throw new ProductReleaseNotFoundException(productNotFounMessageLog, e1);
            }

            try {
                productRelease = productReleaseDao.load(product, productRelease.getVersion());
            } catch (EntityNotFoundException e) {
                inexistentProductReleases.add(productRelease);
            }
        }

        // validate if there are ProductReleases uninstalled
        if (!inexistentProductReleases.isEmpty()) {
            throw new ProductReleaseNotFoundException(inexistentProductReleases);
        }
    }

    public void validateDelete(ApplicationRelease applicationRelease) throws ApplicationReleaseStillInstalledException {
        // validate if the application release are installed on some VMs
        List<ApplicationInstance> existentApplicationInstances = new ArrayList<ApplicationInstance>();
        List<ApplicationInstance> applicationInstances = getApplicationInstancesByApplication(applicationRelease);

        for (ApplicationInstance application : applicationInstances) {
            if (application.getStatus().equals(Status.INSTALLED) || application.getStatus().equals(Status.CONFIGURING)
                    || application.getStatus().equals(Status.UPGRADING)
                    || application.getStatus().equals(Status.INSTALLING)) {
                existentApplicationInstances.add(application);
            }
        }

        // validate if the products are installed
        if (!existentApplicationInstances.isEmpty()) {
            throw new ApplicationReleaseStillInstalledException(applicationInstances);
        }
    }

    // ***** PRIVATE METHODS *******************//
    private List<ApplicationInstance> getApplicationInstancesByApplication(ApplicationRelease applicationRelease) {

        ApplicationInstanceSearchCriteria applicationCriteria = new ApplicationInstanceSearchCriteria(
                Arrays.asList(Status.INSTALLED), null, null, applicationRelease.getApplication().getName(), null);

        return applicationInstanceDao.findByCriteria(applicationCriteria);
    }

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

    /**
     * @param productDao
     *            the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * @param applicationInstanceDao
     *            the applicationInstanceDao to set
     */
    public void setApplicationInstanceDao(ApplicationInstanceDao applicationInstanceDao) {
        this.applicationInstanceDao = applicationInstanceDao;
    }
}
