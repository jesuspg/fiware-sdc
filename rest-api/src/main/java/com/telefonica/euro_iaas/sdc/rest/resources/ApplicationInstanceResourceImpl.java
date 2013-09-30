/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.rest.resources;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Path;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.EnvironmentNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationManager;
import com.telefonica.euro_iaas.sdc.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.sdc.manager.EnvironmentManager;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.manager.async.ApplicationInstanceAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.TaskError;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.IpToVM;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Default ApplicationInstanceResource implementation.
 * 
 * @author Sergio Arroyo
 */
@Path("/vdc/{vdc}/application")
@Component
@Scope("request")
public class ApplicationInstanceResourceImpl implements ApplicationInstanceResource {

    @InjectParam("applicationInstanceAsyncManager")
    private ApplicationInstanceAsyncManager applicationInstanceAsyncManager;
    @InjectParam("productInstanceManager")
    private ProductInstanceManager productInstanceManager;
    @InjectParam("environmentInstanceManager")
    private EnvironmentInstanceManager environmentInstanceManager;
    @InjectParam("environmentManager")
    private EnvironmentManager environmentManager;
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
        String environment_name = "";
        try {
            VM vm = application.getVm();
            if (!vm.canWorkWithChef()) {
                vm = ip2vm.getVm(vm.getIp(), vm.getFqn(), vm.getOsType());
            }

            Task task = createTask(
                    MessageFormat.format("Install application {0} in  VM {1}{2}", application.getApplicationName(),
                            vm.getHostname(), vm.getDomain()), vdc);

            List<ProductInstance> productList = new ArrayList<ProductInstance>();
            // get the product instances
            List<ProductReleaseDto> products = application.getEnvironmentInstanceDto().getEnvironment().getProducts();

            if (products != null) {
                List<ProductRelease> notFoundProducts = new ArrayList<ProductRelease>();
                ProductInstanceSearchCriteria criteria = new ProductInstanceSearchCriteria();
                criteria.setVm(vm);
                for (ProductReleaseDto relDto : products) {
                    Product p = productManager.load(relDto.getProductName());
                    ProductRelease product = productManager.load(p, relDto.getVersion());
                    criteria.setProductRelease(product);
                    try {
                        ProductInstance pInstance = productInstanceManager.loadByCriteria(criteria);
                        productList.add(pInstance);
                        environment_name = environment_name + pInstance.getProductRelease().getProduct().getName()
                                + pInstance.getProductRelease().getVersion() + "_";

                    } catch (NotUniqueResultException e) {
                        notFoundProducts.add(product);
                    } catch (EntityNotFoundException e) {
                        notFoundProducts.add(product);
                    }
                }
                if (!notFoundProducts.isEmpty()) {
                    task.setStatus(TaskStates.ERROR);
                    task.setEndTime(new Date());
                    TaskError error = new TaskError(MessageFormat.format("Unable to install the"
                            + "application due the products listed bellow" + " are not installed: {0}",
                            getProductUninstalled(notFoundProducts)));
                    error.setVenodrSpecificErrorCode("NotInstalledProductsException");

                    task.setError(error);
                    return taskManager.updateTask(task);
                }
            }
            Application app = applicationManager.load(application.getApplicationName());
            ApplicationRelease release = applicationManager.load(app, application.getVersion());

            List<Attribute> attributes = application.getAttributes();
            if (attributes == null) {
                attributes = new ArrayList<Attribute>();
            }

            // TODO sarroyo: workaround to fix problem with lazy init.
            // release.getSupportedProducts().size();

            // get EnvironmentInstance
            EnvironmentInstanceDto environmentInstanceDto = application.getEnvironmentInstanceDto();

            Environment environment = environmentManager.load(environment_name);

            EnvironmentInstance environmentInstance = environmentInstanceManager.load(new EnvironmentInstance(
                    environment, productList).getId());

            applicationInstanceAsyncManager.install(vm, vdc, environmentInstance, release, attributes, task, callback);

            return task;
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        } catch (EnvironmentNotFoundException e2) {
            throw new SdcRuntimeException(e2);
        } catch (EnvironmentInstanceNotFoundException e3) {
            throw new SdcRuntimeException(e3);
        }
    }

    private String getProductUninstalled(List<ProductRelease> products) {
        String list = "";
        for (ProductRelease release : products) {
            if (!list.endsWith("")) {
                list = list.concat(", ");
            }
            list = list.concat(release.getProduct().getName() + "-" + release.getVersion());
        }
        return list;

    }

    @Override
    public Task uninstall(String vdc, String name, String callback) {
        ApplicationInstance app = load(name);
        VM vm = app.getEnvironmentInstance().getProductInstances().iterator().next().getVm();
        Task task = createTask(
                MessageFormat.format("Uninstall application {0} in  VM {1}{2}", app.getApplication().getApplication()
                        .getName(), vm.getHostname(), vm.getDomain()), vdc);
        applicationInstanceAsyncManager.uninstall(app, task, callback);
        return task;
    }

    /**
     * {@inheritDoc}
     */
    /*
     * @Override public ApplicationInstance load(Long id) { try { return applicationInstanceAsyncManager.load(id); }
     * catch (EntityNotFoundException e) { throw new WebApplicationException(e, 404); } }
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationInstance load(String name) {
        /*
         * try { return applicationInstanceAsyncManager.load(name); } catch (EntityNotFoundException e) { throw new
         * WebApplicationException(e, 404); }
         */
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationInstance> findAll(String hostname, String domain, String ip, String fqn, Integer page,
            Integer pageSize, String orderBy, String orderType, List<Status> status, String vdc, String applicationName) {
        ApplicationInstanceSearchCriteria criteria = new ApplicationInstanceSearchCriteria();
        Boolean validHost = (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(fqn))
                || (!StringUtils.isEmpty(domain) && !StringUtils.isEmpty(hostname));
        if (validHost) {
            VM host = new VM(fqn != null ? fqn : "", ip != null ? ip : "", hostname != null ? hostname : "",
                    domain != null ? domain : "");
            criteria.setVm(host);

            /*
             * if (productName != null && productVersion != null){ ProductInstance productInstance = new
             * ProductInstance( new ProductRelease ( productVersion, null, null, new Product( productName, null), null,
             * null), null, host, null); criteria.setProduct(productInstance); }
             */
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

    @Override
    public Task configure(String vdc, String name, String callback, Attributes arguments) {
        ApplicationInstance application = load(name);
        VM vm = application.getEnvironmentInstance().getProductInstances().iterator().next().getVm();
        Task task = createTask(
                MessageFormat.format("Configure application {0} in  VM {1}{2}", application.getApplication()
                        .getApplication().getName(), vm.getHostname(), vm.getDomain()), vdc);
        applicationInstanceAsyncManager.configure(application, arguments, task, callback);
        return task;
    }

    @Override
    public Task upgrade(String vdc, String name, String version, String callback) {
        /*
         * try { ApplicationInstance application = load(name); VM vm =
         * application.getEnvironmentInstance().getProductInstances() .iterator().next().getVm(); ApplicationRelease
         * release = applicationManager.load( application.getApplication().getApplication(), version); Task task =
         * createTask(MessageFormat.format("Upgrade application" + " {0} in  VM {1}{2} from version {3} to {4}",
         * application.getApplication().getApplication().getName(), vm.getHostname(), vm.getDomain(),
         * application.getApplication() .getVersion(), version),vdc); //TODO sarroyo: workarround to fix lazy init
         * problem: application.getApplication().getTransitableReleases().size(); application
         * .getApplication().getEnvironment().getProductReleases().size();
         * release.getEnvironment().getProductReleases().size(); applicationInstanceAsyncManager.upgrade(application,
         * release, task, callback); return task; } catch (EntityNotFoundException e) { throw new
         * SdcRuntimeException(e); }
         */
        return null;
    }
}
