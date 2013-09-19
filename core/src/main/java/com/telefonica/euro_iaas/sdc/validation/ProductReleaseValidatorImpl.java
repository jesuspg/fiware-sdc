package com.telefonica.euro_iaas.sdc.validation;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;

public class ProductReleaseValidatorImpl implements ProductReleaseValidator{
	
	private ProductInstanceDao productInstanceDao;
	
	public void validateDelete(ProductRelease productRelease)
      throws ProductReleaseStillInstalledException{
		//validate if the product release are installed on some VMs
		List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
		for (ProductInstance product : getProductInstancesByProduct (productRelease)) {
            if (product.getStatus().equals(Status.INSTALLED)
                    || product.getStatus().equals(Status.CONFIGURING)
                    || product.getStatus().equals(Status.UPGRADING)
                    || product.getStatus().equals(Status.INSTALLING)) {
            	productInstances.add(product);
            }
        }
		//validate if the products are installed
        if (!productInstances.isEmpty()) {
            throw new ProductReleaseStillInstalledException(productInstances);
        }
	  }
	
	private List<ProductInstance> getProductInstancesByProduct(
            ProductRelease productRelease) {
        ProductInstanceSearchCriteria productCriteria =
                new ProductInstanceSearchCriteria(null, 
                		null, productRelease);
        return productInstanceDao.findByCriteria(productCriteria);
    }
	
	/**
     * @param productInstanceDao the productInstanceDao to set
     */
    public void setProductInstanceDao(
            ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }
}
