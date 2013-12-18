package com.telefonica.euro_iaas.sdc.manager.impl;

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallPuppetException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.util.IpToVM;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.validation.ProductInstanceValidator;

public class ProductInstanceManagerPuppetImpl /*extends BaseInstallableInstanceManager*/ implements ProductInstanceManager {
    

    private HttpClient client;

    private ProductInstanceDao productInstanceDao;

    private ProductDao productDao;
    private IpToVM ip2vm;
    private ProductInstanceValidator validator;
    private SystemPropertiesProvider propertiesProvider;

    @Override
    public ProductInstance install(VM vm, String vdc, ProductRelease productRelease, List<Attribute> attributes)
            throws NodeExecutionException, AlreadyInstalledException, InvalidInstallProductRequestException {

        if (!vm.canWorkWithInstallatorServer()) {
            String message = "The VM does not include the node hostname required to Install " + "software";
            throw new InvalidInstallProductRequestException(message);
        }

//        isNodeRegistered(vm.getHostname());

        // Check that there is not another product installed
        ProductInstance instance = null;
        try {

            instance = productInstanceDao.load(vm.getFqn() + "_" + productRelease.getProduct().getName() + "_"
                    + productRelease.getVersion());
            if (instance.getStatus().equals(Status.INSTALLED)) {
                throw new AlreadyInstalledException(instance);
            } else if (!(instance.getStatus().equals(Status.UNINSTALLED))
                    && !(instance.getStatus().equals(Status.ERROR)))
                throw new InvalidInstallProductRequestException("Product " + productRelease.getProduct().getName()
                        + " " + productRelease.getVersion() + " cannot be installed in the VM " + vm.getFqn()
                        + " strage status:  " + instance.getStatus());
        } catch (EntityNotFoundException e) {
            try {
                instance = createProductInstance(productRelease, vm, vdc, attributes);
            } catch (Exception e2) {
                throw new InvalidInstallProductRequestException("Product " + productRelease.getProduct().getName()
                        + " " + productRelease.getVersion() + " cannot be installed in the VM " + vm.getFqn()
                        + " error in creating the isntance:  " + e2.getMessage());
            }

        }

        Status previousStatus = null;

        try {
            // makes the validations
            // instance = getProductToInstall(product, vm, vdc, attributes);
            previousStatus = instance.getStatus();
            // now we have the productInstance so can validate the operation
            validator.validateInstall(instance);

            instance.setStatus(Status.INSTALLING);
            instance.setVm(vm);
            // Id for the ProductInstance
            instance = productInstanceDao.update(instance);

            // call puppet

            callPuppetService(vm, vdc, productRelease, attributes);

            instance.setStatus(Status.INSTALLED);
            return productInstanceDao.update(instance);

        } catch (CanNotCallPuppetException sce) {
            restoreInstance(previousStatus, instance);
            throw new SdcRuntimeException(sce);
        } catch (InvalidEntityException e) {
            throw new SdcRuntimeException(e);

        } catch (RuntimeException e) {
            // by default restore the previous state when a runtime is thrown
            restoreInstance(previousStatus, instance);
            throw new SdcRuntimeException(e);
        }

    }

    @Override
    public ProductInstance configure(ProductInstance productInstance, List<Attribute> configuration)
            throws NodeExecutionException, FSMViolationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProductInstance upgrade(ProductInstance productInstance, ProductRelease productRelease)
            throws NotTransitableException, NodeExecutionException, FSMViolationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void uninstall(ProductInstance productInstance) throws NodeExecutionException, FSMViolationException {
        // TODO Auto-generated method stub

    }

    @Override
    public ProductInstance load(String vdc, Long id) throws EntityNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProductInstance load(String vdc, String name) throws EntityNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProductInstance loadByCriteria(ProductInstanceSearchCriteria criteria) throws EntityNotFoundException,
            NotUniqueResultException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProductInstance> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProductInstance update(ProductInstance productInstance) {
        // TODO Auto-generated method stub
        return null;
    }

    private void callPuppetService(VM vm, String vdc, ProductRelease product, List<Attribute> attributes)
            throws CanNotCallPuppetException {

        
        HttpPost postInstall = new HttpPost(propertiesProvider.getProperty(SystemPropertiesProvider.PUPPET_MASTER_URL) + "install/"+vdc+"/"
              + vm.getHostname() + "/" + product.getProduct().getName() + "/" + product.getVersion());
        
        HttpResponse response;
        

        try {
            response = client.execute(postInstall);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);

            if (statusCode != 200) {
                throw new CanNotCallPuppetException(format("[install] response code was: {0}", statusCode));
            }

            // generate files in puppet master
            HttpPost postGenerate = new HttpPost(propertiesProvider.getProperty(SystemPropertiesProvider.PUPPET_MASTER_URL)+ "generate/"
                  + vm.getHostname());
            
            response = client.execute(postGenerate);
            statusCode = response.getStatusLine().getStatusCode();
            entity = response.getEntity();
            EntityUtils.consume(entity);

            if (statusCode != 200) {
                throw new CanNotCallPuppetException(format("[install] generete files response code was: {0}",
                        statusCode));
            }
        } catch (IOException e) {
            throw new CanNotCallPuppetException(e);
        } catch (IllegalStateException e1) {
            throw new CanNotCallPuppetException(e1);
        }

    }

    private ProductInstance createProductInstance(ProductRelease productRelease, VM vm, String vdc,
            List<Attribute> attributes) throws InvalidEntityException, AlreadyExistsEntityException {

        ProductInstance instance = new ProductInstance();

        Product product = null;
        try {
            product = productDao.load(productRelease.getProduct().getName());
        } catch (EntityNotFoundException e) {
            product = new Product(productRelease.getProduct().getName(), productRelease.getProduct().getDescription());
        }
        product.setAttributes(attributes);

        productRelease.setProduct(product);

        instance.setProductRelease(productRelease);
        instance.setVm(vm);
        instance.setVdc(vdc);
        instance.setStatus(Status.UNINSTALLED);
        instance.setName(vm.getFqn() + "_" + productRelease.getProduct().getName() + "_" + productRelease.getVersion());

        instance = productInstanceDao.create(instance);
        return instance;

    }

    /**
     * Go to previous state when a runtime exception is thrown in any method
     * which can change the status of the product instance.
     * 
     * @param previousStatus
     *            the previous status
     * @param instance
     *            the product instance
     * @return the instance.
     */
    private ProductInstance restoreInstance(Status previousStatus, ProductInstance instance) {
        instance.setStatus(previousStatus);
        return update(instance);
    }

    // //////////// I.O.C /////////////
    /**
     * @param productInstanceDao
     *            the productInstanceDao to set
     */
    public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }

    /**
     * @param productDao
     *            the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * @param ip2vm
     *            the ip2vm to set
     */
    public void setIp2vm(IpToVM ip2vm) {
        this.ip2vm = ip2vm;
    }

    /**
     * @param validator
     *            the validator to set
     */
    public void setValidator(ProductInstanceValidator validator) {
        this.validator = validator;
    }

    /**
     * @param client
     *            the client to set
     */
    public void setClient(HttpClient client) {
        this.client = client;
    }
    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

}
