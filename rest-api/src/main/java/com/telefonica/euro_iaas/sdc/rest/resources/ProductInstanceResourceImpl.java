package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;

/**
 * Default ProductInstanceResource implementation.
 *
 * @author Sergio Arroyo
 *
 */
@Path("/product")
@Component
@Scope("request")
public class ProductInstanceResourceImpl implements ProductInstanceResource {

    @InjectParam("productInstanceManager")
    private ProductInstanceManager productInstanceManager;
    @InjectParam("productManager")
    private ProductManager productManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance install(ProductInstanceDto product) {
        try {
            Product p = productManager.load(product.getProduct().getProduct());
            ProductRelease loadedProduct = productManager.load(
                    p, product.getProduct().getVersion());
            return productInstanceManager.install(
                    product.getVm(), loadedProduct);
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall(Long productId) {
        try {
            ProductInstance productInstance = productInstanceManager.load(productId);
            productInstanceManager.uninstall(productInstance);
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    
    /*@Override
    public ProductInstance upgrade(Long id, ReleaseDto releaseDto) {
        ProductInstance productInstance;
        try {
        	productInstance = productInstanceManager.load(id);
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(
                    "There is no productInstance with id " + id, e);
        }
        
        Product product;
        ProductRelease productRelease;
        ProductInstance newProductInstance;
        try {
        	product = productManager.load(releaseDto.getProduct());
        	productRelease = 
        		productManager.load(product, releaseDto.getVersion());
        	newProductInstance = 
        		productInstanceManager.upgrade(productInstance, productRelease);
        } catch (EntityNotFoundException e) {
        	throw new SdcRuntimeException(
                    "There is no productInstance with id " + id, e);
        } catch (NotTransitableException nte)
        {
        	throw new SdcRuntimeException(
                    "This upgrade requested is NOT allowed for " +
                    "productInstance with id " + id, nte);
        }
        return newProductInstance;
    }*/
    
    /**
     * {@inheritDoc}
     */
    
    @Override
    public ProductInstance upgrade(Long id, String newVersion) {
        ProductInstance productInstance;
        try {
        	productInstance = productInstanceManager.load(id);
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(
                    "There is no productInstance with id " + id, e);
        }
        
        ProductRelease productRelease;
        try {
        	productRelease = 
        		productManager.load(productInstance.getProduct().getProduct(), 
        				newVersion);
        	productInstance = 
        		productInstanceManager.upgrade(productInstance, productRelease);
        } catch (EntityNotFoundException e) {
        	throw new SdcRuntimeException(
                    "There is no productInstance with id " + id, e);
        } catch (NotTransitableException nte)
        {
        	throw new SdcRuntimeException(
                    "This upgrade requested is NOT allowed for " +
                    "productInstance with id " + id, nte);
        }
        return productInstance;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance configure(Long id, Attributes arguments) {
        ProductInstance product;
        try {
            product = productInstanceManager.load(id);
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(
                    "There is no productInstance with id " + id, e);
        }
        productInstanceManager.configure(product, arguments);
        return product;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductInstance> findAll(String hostname, String domain,
            String ip, Integer page, Integer pageSize, String orderBy,
            String orderType, Status status) {
        ProductInstanceSearchCriteria criteria =
            new ProductInstanceSearchCriteria();

        //the parameters are nullable
        Boolean validHost = !StringUtils.isEmpty(ip) ||
            (!StringUtils.isEmpty(domain) && !StringUtils.isEmpty(hostname));
        if (validHost) {
            VM host = new VM(
                    ip != null ? ip : "",
                    hostname != null ? hostname : "",
                    domain != null ? domain : "");
            criteria.setVM(host);
        }
        criteria.setStatus(status);
        if (page != null && pageSize != null) {
            criteria.setPage(page);
            criteria.setPageSize(pageSize);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            criteria.setOrderBy(orderBy);
        }
        if (!StringUtils.isEmpty(orderType)) {
            criteria.setOrderBy(orderType);
        }

        return productInstanceManager.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance load(Long id) throws EntityNotFoundException {
        return productInstanceManager.load(id);
    }

}
