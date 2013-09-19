package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.IncompatibleProductsException;
import com.telefonica.euro_iaas.sdc.exception.NotInstalledProductsException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.sdc.manager.ApplicationManager;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
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

    @InjectParam("applicationInstanceManager")
    private ApplicationInstanceManager applicationInstanceManager;
    @InjectParam("productInstanceManager")
    private ProductInstanceManager productInstanceManager;
    @InjectParam("productManager")
    private ProductManager productManager;
    @InjectParam("applicationManager")
    private ApplicationManager applicationManager;
    @InjectParam("ip2vm")
    private IpToVM ip2vm;


    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance install(ApplicationInstanceDto application)
    throws NodeExecutionException, IncompatibleProductsException,
    AlreadyInstalledException, NotInstalledProductsException {
        try {
            VM vm = application.getVm();
            if (!vm.canWorkWithChef()) {
                vm = ip2vm.getVm(vm.getIp());
            }

            List<ProductInstance> productList =
                new ArrayList<ProductInstance>();
            // get the product instances
            List<ReleaseDto> products = application.getProducts();
            if (products != null) {
                List<ProductRelease> notFoundProducts =
                        new ArrayList<ProductRelease>();
                ProductInstanceSearchCriteria criteria =
                    new ProductInstanceSearchCriteria();
                criteria.setVm(vm);
                for (ReleaseDto relDto : products) {
                    Product p = productManager.load(relDto.getName());
                    ProductRelease product = productManager.load(p, relDto.getVersion());
                    criteria.setProduct(product);
                    try {
                        productList.add(productInstanceManager
                                .loadByCriteria(criteria));
                    } catch (NotUniqueResultException e) {
                        notFoundProducts.add(product);
                    } catch (EntityNotFoundException e) {
                        notFoundProducts.add(product);
                    }
                }
                if (!notFoundProducts.isEmpty()) {
                    throw new NotInstalledProductsException(notFoundProducts);
                }
            }
            Application app = applicationManager.load(
                    application.getApplicationName());
            ApplicationRelease release = applicationManager.load(
                    app, application.getVersion());

            List<Attribute> attributes = application.getAttributes();
            if (attributes == null) {
                attributes = new ArrayList<Attribute>();
            }
            return applicationInstanceManager.install(
                    vm, productList, release, attributes);

        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall(Long applicationId) throws NodeExecutionException, FSMViolationException {
        try {

            ApplicationInstance app =
                applicationInstanceManager.load(applicationId);
            applicationInstanceManager.uninstall(app);
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance configure(Long id, Attributes arguments)
    throws NodeExecutionException, FSMViolationException{
        try {
            ApplicationInstance application = applicationInstanceManager.load(id);
            return applicationInstanceManager.configure(application, arguments);
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance upgrade(Long id, String version)
            throws NodeExecutionException, IncompatibleProductsException,
            FSMViolationException {
        try {
            ApplicationInstance instance =
                applicationInstanceManager.load(id);
            ApplicationRelease release = applicationManager.load(
                    instance.getApplication().getApplication(), version);
            return applicationInstanceManager.upgrade(instance, release);
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        } catch (NotTransitableException e) {
            throw new SdcRuntimeException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance load(Long id) throws EntityNotFoundException {
        return applicationInstanceManager.load(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findAll(
            Integer page, Integer pageSize, String orderBy,
            String orderType, List<Status> status) {
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

        return applicationInstanceManager.findByCriteria(criteria);
    }

}
