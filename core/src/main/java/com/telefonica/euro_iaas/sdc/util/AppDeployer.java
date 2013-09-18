package com.telefonica.euro_iaas.sdc.util;

import java.util.List;

import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.Customer;
import com.telefonica.euro_iaas.sdc.model.SOInstance;
import com.telefonica.euro_iaas.sdc.model.Image;

/**
 * <p>AppDeployer interface.</p>
 *
 * @author ju
 * @version $Id: $
 */
public interface AppDeployer {

    /**
     * <p>install</p>
     *
     * @param customer a {@link com.telefonica.euro_iaas.sdc.model.Customer} object.
     * @param so a {@link com.telefonica.euro_iaas.sdc.model.SOInstance} object.
     * @param apps a {@link java.util.List} object.
     * @return a {@link com.telefonica.euro_iaas.sdc.model.Image} object.
     */
    Image install(Customer customer, SOInstance so, List<Product> apps);

    /**
     * <p>uninstall</p>
     *
     * @param customer a {@link com.telefonica.euro_iaas.sdc.model.Customer} object.
     * @param so a {@link com.telefonica.euro_iaas.sdc.model.SOInstance} object.
     * @param app a {@link com.telefonica.euro_iaas.sdc.model.Product} object.
     * @return a {@link com.telefonica.euro_iaas.sdc.model.Image} object.
     */
    Image uninstall(Customer customer, SOInstance so, Product app);

}
