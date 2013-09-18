package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;

/**
 * default ProductResource implementation
 * @author Sergio Arroyo
 *
 */
@Path("/catalog/product")
@Component
@Scope("request")
public class ProductResourceImpl implements ProductResource {

    @InjectParam("productManager")
    private ProductManager productManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findAll(Integer page, Integer pageSize,
            String orderBy, String orderType) {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
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
        return productManager.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product load(String name) throws EntityNotFoundException {
        return productManager.load(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attribute> loadAttributes(String name)
            throws EntityNotFoundException {
        return productManager.load(name).getAttributes();
    }

}
