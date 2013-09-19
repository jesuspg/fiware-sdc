package com.telefonica.euro_iaas.sdc.rest.resources;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
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
@Path("/product")
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
    public Task install(ProductInstanceDto product, String callback) {
        try {
            Product p = productManager.load(product.getProduct().getName());
            ProductRelease loadedProduct = productManager.load(p, product
                    .getProduct().getVersion());
            List<Attribute> attributes = product.getAttributes();
            if (attributes == null) {
                attributes = new ArrayList<Attribute>();
            }

            Task task = createTask(MessageFormat.format(
                    "Install product {0} in  VM {1}{2}",
                    product.getProduct().getName(),
                    product.getVm().getHostname(), product.getVm().getDomain()));
            productInstanceAsyncManager.install(product.getVm(), loadedProduct,
                    attributes, task, callback);
            return task;
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task uninstall(String host, String domain, String name,
            String callback) {
        try {
            ProductInstance product = load(host, domain, name);
            Task task = createTask(MessageFormat.format(
                    "Uninstall product {0} in  VM {1}{2}", name, host, domain));
            productInstanceAsyncManager.uninstall(product, task, callback);
            return task;
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task upgrade(String host, String domain, String name,
            String version, String callback) {
        try {
            ProductInstance product = load(host, domain, name);
            //work around to fix problem with lazy init when validator looks
            //for transitable releases.
            product.getProduct().getTransitableReleases().size();
            ProductRelease newRelease = productManager.load(product
                    .getProduct().getProduct(), version);
            Task task = createTask(MessageFormat.format(
                    "Upgrade product {0} in  VM {1}{2} from version {3} to {4}",
                    name, host, domain, product.getProduct().getVersion(), version));
            productInstanceAsyncManager.upgrade(product, newRelease, task, callback);
            return task;
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task configure(String host, String domain, String name,
            String callback, Attributes arguments) {
        try {
            ProductInstance product = load(host, domain, name);
            Task task = createTask(MessageFormat.format(
                    "Configure product {0} in  VM {1}{2}", name, host, domain));
            productInstanceAsyncManager.configure(product, arguments, task,
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
    public ProductInstance load(String host, String domain, String name)
            throws EntityNotFoundException {
        ProductInstanceSearchCriteria criteria = new ProductInstanceSearchCriteria();
        criteria.setProductName(name);
        criteria.setVm(new VM(host, domain));
        try {
            return productInstanceAsyncManager.loadByCriteria(criteria);
        } catch (NotUniqueResultException e) {
            throw new EntityNotFoundException(ProductInstance.class, "id",
                    criteria);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductInstance> findAll(String hostname, String domain,
            String ip, Integer page, Integer pageSize, String orderBy,
            String orderType, Status status) {
        ProductInstanceSearchCriteria criteria = new ProductInstanceSearchCriteria();

        // the parameters are nullable
        Boolean validHost = !StringUtils.isEmpty(ip)
                || (!StringUtils.isEmpty(domain) && !StringUtils
                        .isEmpty(hostname));
        if (validHost) {
            VM host = new VM(ip != null ? ip : "", hostname != null ? hostname
                    : "", domain != null ? domain : "");
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

        return productInstanceAsyncManager.findByCriteria(criteria);
    }


    private Task createTask(String description) {
        Task task = new Task(TaskStates.RUNNING);
        task.setDescription(description);
        return taskManager.createTask(task);
    }
}
