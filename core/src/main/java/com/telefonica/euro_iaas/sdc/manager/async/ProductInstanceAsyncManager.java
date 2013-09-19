package com.telefonica.euro_iaas.sdc.manager.async;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;

/**
 * Defines the interface to work with async requests.
 *
 * @author Sergio Arroyo
 *
 */
public interface ProductInstanceAsyncManager {

    /**
     * Install a list of products in a given vm.
     * @param vm the vm where  instance will be running in
     * @param product the product to install
     * @param attributes the configuration
     * @param task the task which contains the information about the async execution
     * @param callback if not empty, contains the url where the result of the
     * execution will be sent
     */
    void install(VM vm, ProductRelease product,
            List<Attribute> attributes, Task task, String callback);

    /**
     * Configure an installed product
     * @param productInstance the installed product to configure
     * @param configuration the configuration parameters.
     * @param task the task which contains the information about the async execution
     * @param callback if not empty, contains the url where the result of the
     * @return the configured product.
     */
    void configure(ProductInstance productInstance,
            List<Attribute> configuration, Task task, String callback);

    /**
     * Upgrade a ProductInstance
     * @param productInstance the installed product to upgrade
     * @param productRelease the productRelease to upgrade to.
     * @param task the task which contains the information about the async execution
     * @param callback if not empty, contains the url where the result of the
     * @return the productInstance upgraded.
     */
    void upgrade(ProductInstance productInstance,
            ProductRelease productRelease, Task task, String callback);

    /**
     * Uninstall a previously installed product.
     * @param productInstance the candidate to uninstall
     * @param task the task which contains the information about the async execution
     * @param callback if not empty, contains the url where the result of the
     */
    void uninstall(ProductInstance productInstance, Task task, String callback);

    /**
     * Find the ProductInstance using the given id.
     * @param id the productInstance identifier
     * @return the productInstance
     * @throws EntityNotFoundException if the product instance does not exists
     */
    ProductInstance load(Long id) throws EntityNotFoundException;


    /**
     * Find the ProductInstance that match with the given criteria.
     * @param criteria the search criteria
     * @return the productInstance
     * @throws EntityNotFoundException if the product instance does not exists
     * @throws NotUniqueResultException if there are more than a product that
     * match with the given criteria
     */
    ProductInstance loadByCriteria(ProductInstanceSearchCriteria criteria)
        throws EntityNotFoundException, NotUniqueResultException;

    /**
     * Find the product instances that match with the given criteria.
     * @param criteria the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria);

}
