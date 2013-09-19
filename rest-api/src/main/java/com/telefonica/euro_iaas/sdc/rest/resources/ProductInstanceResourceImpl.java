package com.telefonica.euro_iaas.sdc.rest.resources;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.manager.async.ProductInstanceAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
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
@Path("/vdc/{vdc}/product")
@Component
@Scope("request")
public class ProductInstanceResourceImpl implements ProductInstanceResource {

    @InjectParam("productInstanceAsyncManager")
    private ProductInstanceAsyncManager productInstanceAsyncManager;
    @InjectParam("productManager")
    private ProductManager productManager;
    @InjectParam("taskManager")
    private TaskManager taskManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Task install(String vdc, ProductInstanceDto product, String callback) {
        try {
            Product p = productManager.load(product.getProduct().getProductName());
            ProductRelease loadedProduct = productManager.load(p, product
                    .getProduct().getVersion());
            List<Attribute> attributes = product.getAttributes();
            if (attributes == null) {
                attributes = new ArrayList<Attribute>();
            }

            Task task = createTask(MessageFormat.format(
                    "Install product {0} in  VM {1}{2}", product.getProduct()
                            .getProductName(), product.getVm().getHostname(), product
                            .getVm().getDomain()), vdc);
            productInstanceAsyncManager.install(product.getVm(), vdc,
                    loadedProduct, attributes, task, callback);
            return task;
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task uninstall(String vdc, Long id, String callback) {

        ProductInstance product = load(vdc, id);
        Task task = createTask(MessageFormat.format(
                "Uninstall product {0} in  VM {1}{2}", product.getProduct()
                        .getProduct().getName(), product.getVm().getHostname(),
                product.getVm().getDomain()), vdc);
        productInstanceAsyncManager.uninstall(product, task, callback);
        return task;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task upgrade(String vdc, Long id, String version, String callback) {
        try {
            ProductInstance product = load(vdc, id);
            // work around to fix problem with lazy init when validator looks
            // for transitable releases.
            product.getProduct().getTransitableReleases().size();
            ProductRelease newRelease = productManager.load(product
                    .getProduct().getProduct(), version);
            Task task = createTask(MessageFormat.format(
                    "Upgrade product {0} in  VM {1}{2} from version {3} to {4}",
                            product.getProduct().getProduct().getName(),
                            product.getVm().getHostname(), product.getVm()
                                    .getDomain(), product.getProduct()
                                    .getVersion(), version), vdc);
            productInstanceAsyncManager.upgrade(product, newRelease, task,
                    callback);
            return task;
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task configure(String vdc, Long id, String callback, Attributes arguments) {
        ProductInstance product = load(vdc, id);
        Task task = createTask(MessageFormat.format(
                "Uninstall product {0} in  VM {1}{2}", product.getProduct()
                        .getProduct().getName(), product.getVm().getHostname(),
                product.getVm().getDomain()), vdc);
        productInstanceAsyncManager.configure(product, arguments, task,
                callback);
        return task;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance load(String vdc, Long id) {
        try {
            return productInstanceAsyncManager.load(vdc, id);
        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductInstance> findAll(String hostname, String domain,
            String ip, String fqn, Integer page, Integer pageSize, String orderBy,
            String orderType, Status status, String vdc, String product) {
        ProductInstanceSearchCriteria criteria =
                new ProductInstanceSearchCriteria();
        criteria.setVdc(vdc);
        // the parameters are nullable
        Boolean validHost = (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(fqn))
                || (!StringUtils.isEmpty(domain) && !StringUtils
                        .isEmpty(hostname));
        if (validHost) {
            VM host = new VM(fqn != null ? fqn : "", ip != null ? ip : "", 
            		hostname != null ? hostname: "", 
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
        if (!StringUtils.isEmpty(product)) {
            criteria.setProductName(product);
        }

        return productInstanceAsyncManager.findByCriteria(criteria);
    }

    private Task createTask(String description, String vdc) {
        Task task = new Task(TaskStates.RUNNING);
        task.setDescription(description);
        task.setVdc(vdc);
        return taskManager.createTask(task);
    }
}
