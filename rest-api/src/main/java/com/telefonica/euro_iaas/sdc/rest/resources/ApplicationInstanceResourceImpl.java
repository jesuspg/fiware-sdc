package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ApplicationDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.sdc.manager.ApplicationInstanceManagerFactory;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.IpToVM;

/**
 * Default ApplicationInstanceResource implementation.
 *
 * @author Sergio Arroyo
 *
 */
@Path("/application")
@Component
@Scope("request")
public class ApplicationInstanceResourceImpl implements
        ApplicationInstanceResource {

    @InjectParam("applicationInstanceManagerFactory")
    private ApplicationInstanceManagerFactory applicationInstanceManagerFactory;
    @InjectParam("productInstanceManager")
    private ProductInstanceManager productInstanceManager;
    @InjectParam("productDao")
    private ProductDao productDao;
    @InjectParam("applicationDao")
    private ApplicationDao applicationDao;
    @InjectParam("ip2vm")
    private IpToVM ip2vm;


    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance install(String hostname, String domain,
            String ip, List<String> products, String appname) {
        try {
            VM vm;
            if (hostname.isEmpty() || domain.isEmpty()) {
                vm = ip2vm.getVm(ip);
            } else {
                vm = new VM(ip, hostname, domain);
            }

            List<ProductInstance> productList =
                new ArrayList<ProductInstance>();

            // get the product instances
            if (products != null) {
                Product product = null;
                ProductInstanceSearchCriteria criteria =
                    new ProductInstanceSearchCriteria();
                criteria.setStatus(ProductInstance.Status.INSTALLED);
                criteria.setVm(vm);
                for (String app : products) {
                    product = productDao.load(app);
                    criteria.setProduct(product);
                    productList.add(productInstanceManager
                            .loadByCriteria(criteria));
                }
            }
            Application app = applicationDao.load(appname);

            ApplicationInstanceManager manager =
                applicationInstanceManagerFactory.getInstance(app.getType());
            return manager.install(vm, productList,app);

        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        } catch (NotUniqueResultException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall(Long applicationId) {
        try {

            ApplicationInstance app = applicationInstanceManagerFactory
                .getInstance().load(applicationId);
            ApplicationInstanceManager manager =
                applicationInstanceManagerFactory.getInstance(
                        app.getApplication().getType());
            manager.uninstall(app);
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance configure(Long id, Attributes arguments) {
        try {
            ApplicationInstanceManager manager =
                applicationInstanceManagerFactory.getInstance();
            ApplicationInstance application = manager.load(id);
            return manager.configure(application, arguments);
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance load(Long id) throws EntityNotFoundException {

        return applicationInstanceManagerFactory.getInstance().load(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findAll(
            Integer page, Integer pageSize, String orderBy,
            String orderType, Status status) {
        ApplicationInstanceSearchCriteria criteria =
            new ApplicationInstanceSearchCriteria();

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

        return applicationInstanceManagerFactory.getInstance()
                .findByCriteria(criteria);
    }

}
