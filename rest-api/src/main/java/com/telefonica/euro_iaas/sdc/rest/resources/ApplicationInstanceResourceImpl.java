package com.telefonica.euro_iaas.sdc.rest.resources;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationManager;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.manager.async.ApplicationInstanceAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.TaskError;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
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
@Path("/vdc/{vdc}/application")
@Component
@Scope("request")
public class ApplicationInstanceResourceImpl implements
        ApplicationInstanceResource {

    @InjectParam("applicationInstanceAsyncManager")
    private ApplicationInstanceAsyncManager applicationInstanceAsyncManager;
    @InjectParam("productInstanceManager")
    private ProductInstanceManager productInstanceManager;
    @InjectParam("productManager")
    private ProductManager productManager;
    @InjectParam("applicationManager")
    private ApplicationManager applicationManager;
    @InjectParam("ip2vm")
    private IpToVM ip2vm;
    @InjectParam("taskManager")
    private TaskManager taskManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Task install(String vdc, ApplicationInstanceDto application, String callback) {
        try {
            VM vm = application.getVm();
            if (!vm.canWorkWithChef()) {
                vm = ip2vm.getVm(vm.getIp(), vm.getFqn());
            }

            Task task = createTask(MessageFormat.format(
                    "Install application {0} in  VM {1}{2}",
                    application.getApplicationName(),
                    vm.getHostname(), vm.getDomain()), vdc);

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
                    ProductRelease product = productManager.load(
                            p, relDto.getVersion());
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
                    task.setStatus(TaskStates.ERROR);
                    task.setEndTime(new Date());
                    TaskError error = new TaskError(MessageFormat.format(
                            "Unable to install the"
                            + "application due the products listed bellow"
                            + " are not installed: {0}",
                            getProductUninstalled(notFoundProducts)));
                    error.setVenodrSpecificErrorCode(
                            "NotInstalledProductsException");

                    task.setError(error);
                    return taskManager.updateTask(task);
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

            //TODO sarroyo: workaround to fix problem with lazy init.
            release.getSupportedProducts().size();

            applicationInstanceAsyncManager.install(
                    vm, vdc, productList, release, attributes, task, callback);
            return task;
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    private String getProductUninstalled(List<ProductRelease> products) {
        String list = "";
        for (ProductRelease release : products) {
            if (!list.endsWith("")) {
                list = list.concat(", ");
            }
            list = list.concat(release.getProduct().getName() + "-"
                    + release.getVersion());
        }
        return list;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Task uninstall(String vdc, Long id, String callback) {
        ApplicationInstance app = load(id);
        VM vm = app.getProducts().iterator().next().getVm();
        Task task = createTask(MessageFormat.format(
                "Uninstall application {0} in  VM {1}{2}",
                app.getApplication().getApplication().getName(),
                vm.getHostname(), vm.getDomain()), vdc);
        applicationInstanceAsyncManager.uninstall(app, task, callback);
        return task;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task configure(String vdc, Long id, String callback, Attributes arguments) {
        ApplicationInstance application = load(id);
        VM vm = application.getProducts().iterator().next().getVm();
        Task task = createTask(MessageFormat.format(
                "Configure application {0} in  VM {1}{2}",
                application.getApplication().getApplication().getName(),
                vm.getHostname(), vm.getDomain()), vdc);
        applicationInstanceAsyncManager.configure(application, arguments,
                task, callback);
        return task;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task upgrade(String vdc, Long id, String version, String callback) {
        try {
            ApplicationInstance application = load(id);
            VM vm = application.getProducts().iterator().next().getVm();
            ApplicationRelease release = applicationManager.load(
                    application.getApplication().getApplication(), version);
            Task task = createTask(MessageFormat.format("Upgrade application"
                    + " {0} in  VM {1}{2} from version {3} to {4}",
                    application.getApplication().getApplication().getName(),
                    vm.getHostname(), vm.getDomain(),
                    application.getApplication()
                    .getVersion(), version),vdc);

            //TODO sarroyo: workarround to fix lazy init problem:
            application.getApplication().getTransitableReleases().size();
            application.getApplication().getSupportedProducts().size();
            release.getSupportedProducts().size();

            applicationInstanceAsyncManager.upgrade(application, release,
                    task, callback);
            return task;
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance load(Long id) {
        try {
            return applicationInstanceAsyncManager.load(id);
        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findAll(String hostname, String domain,
            String ip, String fqn, Integer page, Integer pageSize, String orderBy,
            String orderType, List<Status> status, String vdc,
            String applicationName) {
        ApplicationInstanceSearchCriteria criteria =
            new ApplicationInstanceSearchCriteria();
        Boolean validHost = (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(fqn))
                || (!StringUtils.isEmpty(domain) && !StringUtils
                        .isEmpty(hostname));
        if (validHost) {
        	VM host = new VM(fqn != null ? fqn : "", ip != null ? ip : "", 
            		hostname != null ? hostname: "", 
                    domain != null ? domain : "");
            criteria.setVm(host);
            
            /*if (productName != null && productVersion != null){
            	ProductInstance productInstance = new ProductInstance(
            			new ProductRelease (
            					productVersion, 
            					null,
            					null,
            					new Product(
            							productName,
            							null),
            					null, 
            		            null), 
            		    null, 
            		    host,
            			null);
            	criteria.setProduct(productInstance);
            }*/
        }
        
        criteria.setVdc(vdc);
        criteria.setStatus(status);
        criteria.setApplicationName(applicationName);
        
          
        
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

        return applicationInstanceAsyncManager.findByCriteria(criteria);
    }


    private Task createTask(String description, String vdc) {
        Task task = new Task(TaskStates.RUNNING);
        task.setDescription(description);
        task.setVdc(vdc);
        return taskManager.createTask(task);
    }
}
