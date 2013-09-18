package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance.Status;
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
    @InjectParam("productDao")
    private ProductDao productDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductInstance> install(String hostname, String domain,
            String ip, List<String> products) {
        try {
            List<Product> productList = new ArrayList<Product>();
            if(products != null) {
                for(String app : products) {
                    productList.add(productDao.load(app));
                }
            }
            return productInstanceManager.install(
                    new VM(ip, hostname, domain), productList);
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