package com.telefonica.euro_iaas.sdc.validation;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.sdc.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseInApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;

public class EnvironmentValidatorImpl implements EnvironmentValidator {

    private ProductInstanceDao productInstanceDao;
    private ApplicationReleaseDao applicationReleaseDao;

    @Override
    public void validateDelete(Environment environment) throws ProductReleaseStillInstalledException,
            ProductReleaseInApplicationReleaseException {

        List<ProductRelease> productReleases = environment.getProductReleases();

        for (int i = 0; i < productReleases.size(); i++) {
            validateProductRelease(productReleases.get(i));
        }
    }

    private void validateProductRelease(ProductRelease productRelease) throws ProductReleaseStillInstalledException,
            ProductReleaseInApplicationReleaseException {

        // validate if the product releases are installed on some VMs
        List<ProductInstance> productInstancesbyProduct = getProductInstancesByProduct(productRelease);
        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();

        if (productInstancesbyProduct.size() > 0) {
            for (ProductInstance product : productInstancesbyProduct) {
                if (product.getStatus().equals(Status.INSTALLED) || product.getStatus().equals(Status.CONFIGURING)
                        || product.getStatus().equals(Status.UPGRADING)
                        || product.getStatus().equals(Status.INSTALLING)) {
                    productInstances.add(product);
                }
            }
        }

        // validate if the products are installed
        if (!productInstances.isEmpty()) {
            throw new ProductReleaseStillInstalledException(productInstances);
        }
        // Check if there are applications that depends on the product
        List<ApplicationRelease> applicationRelease = getApplicationReleaseByProduct(productRelease);

        // validate if the application release depending on product exist
        if (!applicationRelease.isEmpty()) {
            throw new ProductReleaseInApplicationReleaseException(productRelease, applicationRelease);
        }

    }

    private List<ProductInstance> getProductInstancesByProduct(ProductRelease productRelease) {
        ProductInstanceSearchCriteria productCriteria = new ProductInstanceSearchCriteria(null, null, productRelease,
                null);
        return productInstanceDao.findByCriteria(productCriteria);
    }

    private List<ApplicationRelease> getApplicationReleaseByProduct(ProductRelease productRelease) {
        ApplicationReleaseSearchCriteria applicationReleaseCriteria = new ApplicationReleaseSearchCriteria(null,
                productRelease);
        return applicationReleaseDao.findByCriteria(applicationReleaseCriteria);
    }

    /**
     * @param productInstanceDao
     *            the productInstanceDao to set
     */
    public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }

    /**
     * @param applicationReleaseDao
     *            the applicationReleaseDao to set
     */
    public void setApplicationReleaseDao(ApplicationReleaseDao applicationReleaseDao) {
        this.applicationReleaseDao = applicationReleaseDao;
    }

}
