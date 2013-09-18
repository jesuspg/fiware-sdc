package com.telefonica.euro_iaas.sdc.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ChefExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;

/**
 * Defines the operations the system shall be able to do with Products
 *
 * @author Sergio Arroyo
 *
 */
public interface ProductInstanceManager {

    /**
     * Install a list of products in a given vm.
     * @param vm the vm where  instance will be running in
     * @param product the product to install
     * @param attributes the configuration
     *
     * @return the of installed product.
     */
    ProductInstance install(VM vm, ProductRelease product,
            List<Attribute> attributes)
        throws ChefExecutionException ;

    /**
     * Configure an installed product
     * @param productInstance the installed product to configure
     * @param configuration the configuration parameters.
     * @return the configured product.
     */
    ProductInstance configure(ProductInstance productInstance,
            List<Attribute> configuration)
        throws ChefExecutionException ;

    /**
     * Upgrade a ProductInstance
     * @param productInstance the installed product to upgrade
     * @param configuration the configuration parameters.
     * @return the configured product.
     */
    ProductInstance upgrade(ProductInstance productInstance,
            ProductRelease productRelease) throws NotTransitableException,
            ChefExecutionException ;

    /**
     * Uninstall a previously installed product.
     * @param productInstance the candidate to uninstall
     */
    void uninstall(ProductInstance productInstance)
        throws ChefExecutionException ;

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
     * Retrieve all ProductInstance created in the system.
     * @return the existent product instances.
     */
    List<ProductInstance> findAll();

    /**
     * Find the product instances that match with the given criteria.
     * @param criteria the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria);

}
