package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.FSMViolationException;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.NotTransitableException;
import com.telefonica.euro_iaas.sdc.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.installator.Installator;
import com.telefonica.euro_iaas.sdc.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.validation.ProductInstanceValidator;
import com.xmlsolutions.annotation.UseCase;

public class ProductInstanceManagerImpl implements ProductInstanceManager {

    private ProductInstanceDao productInstanceDao;

    private ProductDao productDao;
    private ProductInstanceValidator validator;

    private Installator chefInstallator;
    private Installator puppetInstallator;

    private String INSTALATOR_CHEF = "chef";

    protected String INSTALL = "install";
    protected String UNINSTALL = "uninstall";
    protected String CONFIGURE = "configure";
    protected String DEPLOY_ARTIFACT = "deployArtifact";
    protected String UNDEPLOY_ARTIFACT = "undeployArtifact";

    @Override
    public ProductInstance install(VM vm, String vdc, ProductRelease productRelease, List<Attribute> attributes)
            throws NodeExecutionException, AlreadyInstalledException, InvalidInstallProductRequestException,
            EntityNotFoundException {

        // if (INSTALATOR_CHEF.equals(product.getMapMetadata().get("installator"))) {
        //
        // }else{
        //
        // }

        // Check that there is not another product installed
        ProductInstance instance = null;
        try {

            instance = productInstanceDao.load(vm.getFqn() + "_" + productRelease.getProduct().getName() + "_"
                    + productRelease.getVersion());

            System.out.println("intance:" + instance.getStatus());

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

            Product product = productDao.load(productRelease.getProduct().getName());

            if (INSTALATOR_CHEF.equals(product.getMapMetadata().get("installator"))) {
                chefInstallator.validateInstalatorData(vm);
                chefInstallator.callService(instance, vm, attributes, INSTALL);
            } else {
                puppetInstallator.validateInstalatorData(vm);
                puppetInstallator.callService(vm, vdc, productRelease, INSTALL);
            }

            instance.setStatus(Status.INSTALLED);
            return productInstanceDao.update(instance);

        } catch (InstallatorException sce) {
            restoreInstance(previousStatus, instance);
            throw new SdcRuntimeException(sce);
        } catch (RuntimeException e) {
            // by default restore the previous state when a runtime is thrown
            restoreInstance(previousStatus, instance);
            throw new SdcRuntimeException(e);
        }

    }

    @Override
    public void uninstall(ProductInstance productInstance) throws NodeExecutionException, FSMViolationException,
            EntityNotFoundException {
        Status previousStatus = productInstance.getStatus();
        try {
            validator.validateUninstall(productInstance);
            productInstance.setStatus(Status.UNINSTALLING);
            productInstance = productInstanceDao.update(productInstance);

            Product product = productDao.load(productInstance.getProductRelease().getProduct().getName());

            if (INSTALATOR_CHEF.equals(product.getMapMetadata().get("installator"))) {
                // canviar aqui callChef(uninstallRecipe,
                // productInstance.getVm());

                chefInstallator.callService(productInstance, UNINSTALL);
            } else {
                puppetInstallator.callService(productInstance.getVm(), productInstance.getVdc(),
                        productInstance.getProductRelease(), UNINSTALL);
            }

            productInstance.setStatus(Status.UNINSTALLED);
            productInstanceDao.update(productInstance);
        } catch (InstallatorException e) {
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (RuntimeException e) {
            // by default restore the previous state when a runtime is thrown
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @throws InstallatorException
     */
    @Override
    public ProductInstance configure(ProductInstance productInstance, List<Attribute> configuration)
            throws NodeExecutionException, FSMViolationException, InstallatorException {
        System.out.println("Configuring product instance " + productInstance.getName() + " " + configuration);
        Status previousStatus = productInstance.getStatus();
        try {
            validator.validateConfigure(productInstance);
            productInstance.setStatus(Status.CONFIGURING);
            productInstance = productInstanceDao.update(productInstance);

            System.out.println("Get VM");
            VM vm = productInstance.getVm();

            System.out.println("Load product " + productInstance.getProductRelease().getProduct().getName());
            Product product = productDao.load(productInstance.getProductRelease().getProduct().getName());

            if (configuration != null) {
                for (int j = 0; j < configuration.size(); j++) {
                    Attribute attribute = configuration.get(j);
                    product.addAttribute(attribute);
                }
            }
            System.out.println("Update product " + productInstance.getProductRelease().getProduct().getName());
            productDao.update(product);

            ProductRelease productRelease = productInstance.getProductRelease();
            productRelease.setProduct(product);

            if (INSTALATOR_CHEF.equals(product.getMapMetadata().get("installator"))) {
                chefInstallator.callService(productInstance, productInstance.getVm(), configuration, CONFIGURE);
            } else {
                throw new InstallatorException("Product not configurable in Puppet");
            }

            /*
             * String recipe = recipeNamingGenerator .getInstallRecipe(productInstance); callChef(
             * productInstance.getProductRelease().getProduct().getName(), recipe, productInstance.getVm(),
             * configuration); String restoreRecipe = recipeNamingGenerator .getRestoreRecipe(productInstance);
             * callChef(restoreRecipe, vm);
             */

            productInstance.setProductRelease(productRelease);
            productInstance.setStatus(Status.INSTALLED);
            return productInstanceDao.update(productInstance);

        } catch (InstallatorException e) {
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (RuntimeException e) { // by runtime restore the previous state
            // restore the status
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        } catch (NodeExecutionException e) {
            restoreInstance(Status.ERROR, productInstance);
            throw e;
        } catch (EntityNotFoundException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * Go to previous state when a runtime exception is thrown in any method which can change the status of the product
     * instance.
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

    @Override
    public ProductInstance update(ProductInstance productInstance) {
        return productInstanceDao.update(productInstance);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws EntityNotFoundException
     */
    @UseCase(traceTo = "UC_001.4", status = "implemented")
    @Override
    public ProductInstance upgrade(ProductInstance productInstance, ProductRelease productRelease)
            throws NotTransitableException, NodeExecutionException, FSMViolationException, InstallatorException,
            EntityNotFoundException {
        Status previousStatus = productInstance.getStatus();
        try {
            validator.validateUpdate(productInstance, productRelease);
            // update the status
            productInstance.setStatus(Status.UPGRADING);
            productInstance = productInstanceDao.update(productInstance);
            productInstance.setProductRelease(productRelease);

            VM vm = productInstance.getVm();
            Product product = productDao.load(productInstance.getName());

            if (INSTALATOR_CHEF.equals(product.getMapMetadata().get("installator"))) {
                chefInstallator.upgrade(productInstance, vm);
            } else {
                throw new InstallatorException("Product not upgradeable in Puppet");
            }

            productInstance.setStatus(Status.INSTALLED);
            return productInstanceDao.update(productInstance);

        } catch (RuntimeException e) { // by runtime restore the previous state
            // restore the status
            restoreInstance(previousStatus, productInstance);
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductInstance> findAll() {
        return productInstanceDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance load(String vdc, Long id) throws EntityNotFoundException {
        ProductInstance instance = productInstanceDao.load(id);
        if (!instance.getVdc().equals(vdc)) {
            throw new EntityNotFoundException(ProductInstance.class, "vdc", vdc);
        }
        return instance;
    }

    @Override
    public ProductInstance load(String vdc, String name) throws EntityNotFoundException {
        ProductInstance instance = productInstanceDao.load(name);
        if (!instance.getVdc().equals(vdc)) {
            throw new EntityNotFoundException(ProductInstance.class, "vdc", vdc);
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria) {
        return productInstanceDao.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductInstance loadByCriteria(ProductInstanceSearchCriteria criteria) throws EntityNotFoundException,
            NotUniqueResultException {
        List<ProductInstance> products = productInstanceDao.findByCriteria(criteria);
        if (products.size() == 0) {
            throw new EntityNotFoundException(ProductInstance.class, "searchCriteria", criteria.toString());
        } else if (products.size() > 1) {
            throw new NotUniqueResultException();
        }
        return products.get(0);
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
     * @param validator
     *            the validator to set
     */
    public void setValidator(ProductInstanceValidator validator) {
        this.validator = validator;
    }

    public void setChefInstallator(Installator chefInstallator) {
        this.chefInstallator = chefInstallator;
    }

    public void setPuppetInstallator(Installator puppetInstallator) {
        this.puppetInstallator = puppetInstallator;
    }

}
