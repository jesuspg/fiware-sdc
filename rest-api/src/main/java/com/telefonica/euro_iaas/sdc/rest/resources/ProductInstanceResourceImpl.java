package com.telefonica.euro_iaas.sdc.rest.resources;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

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
import com.telefonica.euro_iaas.sdc.rest.validation.ProductInstanceResourceValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Default ProductInstanceResource implementation.
 * 
 * @author Sergio Arroyodd
 */
@Path("/vdc/{vdc}/productInstance")
@Component
@Scope("request")
public class ProductInstanceResourceImpl implements ProductInstanceResource {

    @InjectParam("productInstanceAsyncManager")
    private ProductInstanceAsyncManager productInstanceAsyncManager;
    @InjectParam("productManager")
    private ProductManager productManager;
    @InjectParam("taskManager")
    private TaskManager taskManager;

    private ProductInstanceResourceValidator validator;

    /**
     * {@inheritDoc}
     */
    public Task install(String vdc, ProductInstanceDto product, String callback) {

        // validator.validateInsert(product);
        try {
            Product p = productManager.load(product.getProduct().getName());
            ProductRelease loadedProduct = productManager.load(p, product.getProduct().getVersion());
            List<Attribute> attributes = product.getAttributes();
            if (attributes == null) {
                attributes = new ArrayList<Attribute>();
            }

            Task task = createTask(MessageFormat.format("Install product {0} in  VM {1}{2}", product.getProduct()
                    .getName(), product.getVm().getHostname(), product.getVm().getDomain()), vdc);

            productInstanceAsyncManager.install(product.getVm(), vdc, loadedProduct, attributes, task, callback);
            return task;
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */

    public Task uninstall(String vdc, String name, String callback) {

        ProductInstance product = load(vdc, name);
        Task task = uninstall(product, vdc, callback);
        return task;
    }

    private Task uninstall(ProductInstance productInstance, String vdc, String callback) {
        Task task = createTask(MessageFormat.format("Uninstall product {0} in  VM {1}{2}", productInstance
                .getProductRelease().getProduct().getName(), productInstance.getVm().getHostname(), productInstance
                .getVm().getDomain()), vdc);
        productInstanceAsyncManager.uninstall(productInstance, task, callback);
        return task;
    }

    /**
     * {@inheritDoc}
     */

    public Task upgrade(String vdc, String name, String version, String callback) {
        ProductInstance productInstance = load(vdc, name);
        Task task = upgrade(vdc, productInstance, version, callback);
        return task;

    }

    private Task upgrade(String vdc, ProductInstance productInstance, String version, String callback) {
        try {

            // work around to fix problem with lazy init when validator looks
            // for transitable releases.
            productInstance.getProductRelease().getTransitableReleases().size();
            ProductRelease newRelease = productManager.load(productInstance.getProductRelease().getProduct(), version);
            Task task = createTask(MessageFormat.format("Upgrade product {0} in  VM {1}{2} from version {3} to {4}",
                    productInstance.getProductRelease().getProduct().getName(), productInstance.getVm().getHostname(),
                    productInstance.getVm().getDomain(), productInstance.getProductRelease().getVersion(), version),
                    vdc);
            productInstanceAsyncManager.upgrade(productInstance, newRelease, task, callback);
            return task;
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */

    public Task configure(String vdc, String name, String callback, Attributes arguments) {
        ProductInstance productInstance = load(vdc, name);
        Task task = configure(vdc, productInstance, arguments, callback);
        return task;
    }

    public Task configure(String vdc, ProductInstance productInstance, Attributes arguments, String callback) {

        Task task = createTask(MessageFormat.format("Uninstall product {0} in  VM {1}{2}", productInstance
                .getProductRelease().getProduct().getName(), productInstance.getVm().getHostname(), productInstance
                .getVm().getDomain()), vdc);
        productInstanceAsyncManager.configure(productInstance, arguments, task, callback);
        return task;
    }

    /**
     * {@inheritDoc}
     */

    public ProductInstance load(String vdc, String name) {
        try {
            ProductInstance pro = productInstanceAsyncManager.load(vdc, name);
            return pro;
        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        } catch (Exception e) {
            throw new WebApplicationException(e, 500);
        }
    }

    /**
     * {@inheritDoc}
     */

    public List<ProductInstance> findAll(String hostname, String domain, String ip, String fqn, Integer page,
            Integer pageSize, String orderBy, String orderType, Status status, String vdc, String product) {
        ProductInstanceSearchCriteria criteria = new ProductInstanceSearchCriteria();
        criteria.setVdc(vdc);
        // the parameters are nullable
        Boolean validHost = (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(fqn))
                || (!StringUtils.isEmpty(domain) && !StringUtils.isEmpty(hostname));
        if (validHost) {
            VM host = new VM(fqn != null ? fqn : "", ip != null ? ip : "", hostname != null ? hostname : "",
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

    /**
     * @param validator
     *            the validator to set
     */
    public void setValidator(ProductInstanceResourceValidator validator) {
        this.validator = validator;
    }

    /**
     * @param validator
     *            the validator to set
     */
    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setProductInstanceAsyncManager(ProductInstanceAsyncManager productInstanceAsyncManager) {
        this.productInstanceAsyncManager = productInstanceAsyncManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

}
