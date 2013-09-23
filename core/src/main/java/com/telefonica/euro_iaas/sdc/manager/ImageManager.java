package com.telefonica.euro_iaas.sdc.manager;

import java.util.List;

import com.telefonica.euro_iaas.sdc.exception.CanNotDeployException;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.Customer;
import com.telefonica.euro_iaas.sdc.model.SO;
import com.telefonica.euro_iaas.sdc.model.Image;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.SOSearchCriteria;

/**
 * Provides a set of methods to work with Virtual Appliances, giving an faccade
 * for VApp Management UC.
 *
 * @author Sergio Arroyo
 * @version $Id: $
 */
public interface ImageManager {

    /**
     * Find all applications available for a customer.
     *
     * @return the application list
     * @param criteria a {@link com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria} object.
     */
    List<Product> findAllProducts(ProductSearchCriteria criteria);

    /**
     * Find all applications available for a customer.
     *
     * @return the SO list
     * @param criteria a {@link com.telefonica.euro_iaas.sdc.model.searchcriteria.SOSearchCriteria} object.
     */
    List<SO> findAllSSOO(SOSearchCriteria criteria);

    /**
     * Deploy a list of applications in the selected SO.
     *
     * @param so a {@link com.telefonica.euro_iaas.sdc.model.SO} object.
     * @param apps a {@link java.util.List} object.
     * @return the Virtual Appliance.
     * @throws com.telefonica.euro_iaas.sdc.exception.CanNotDeployException if any.
     * @param customer a {@link com.telefonica.euro_iaas.sdc.model.Customer} object.
     */
    Image deploy(Customer customer, SO so, List<Product> apps)
        throws CanNotDeployException;

}
