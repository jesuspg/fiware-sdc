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

package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyInstalledException;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
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
import com.telefonica.euro_iaas.sdc.validation.ProductInstanceValidator;
import com.xmlsolutions.annotation.Requirement;
import com.xmlsolutions.annotation.UseCase;

/**
 * Implements ProductManager using Chef to do that.
 * 
 * @author Sergio Arroyo
 */
@UseCase(traceTo = "UC_001", status = "implemented")
@Requirement(traceTo = "BR001", status = "implemented")

public class ProductInstanceManagerChefImpl extends
		BaseInstallableInstanceManager implements ProductInstanceManager {

	private ProductInstanceDao productInstanceDao;

	private ProductDao productDao;
	private IpToVM ip2vm;
	private ProductInstanceValidator validator;

	/**
	 * {@inheritDoc}
	 */
	@UseCase(traceTo = "UC_001.1", status = "implemented")
	@Override
	public ProductInstance install(VM vm, String vdc, ProductRelease productRelease,
			List<Attribute> attributes) throws NodeExecutionException,
			AlreadyInstalledException, InvalidInstallProductRequestException {
		
		if (!vm.canWorkWithChef()) {
			sdcClientUtils.checkIfSdcNodeIsReady(vm.getIp());
			sdcClientUtils.setNodeCommands(vm);
			
			vm = ip2vm.getVm(vm.getIp(), vm.getFqn(), vm.getOsType());
			// Configure the node with the corresponding node commands
		}
		
		// Check that there is not another product installed
		ProductInstance instance = null;
		try{
		
		   instance = productInstanceDao.load(vm.getFqn() + "_" + productRelease.getProduct().getName()
					+ "_" + productRelease.getVersion());
		   if (instance.getStatus().equals(Status.INSTALLED)){
			 throw new AlreadyInstalledException (instance);   
		   }
		   else if (!(instance.getStatus().equals(Status.UNINSTALLED))&&!(instance.getStatus().equals(Status.ERROR)))
			   throw new InvalidInstallProductRequestException ("Product " + productRelease.getProduct().getName() + " " + 
					   productRelease.getVersion() + " cannot be installed in the VM " + vm.getFqn() + " strage status:  "+ instance.getStatus());   
		}
		catch (EntityNotFoundException e){
			try{
				instance = createProductInstance (productRelease,
					 vm,  vdc, attributes);
			}catch (Exception e2){
				throw new InvalidInstallProductRequestException ("Product " + productRelease.getProduct().getName() + " " + 
					   productRelease.getVersion() + " cannot be installed in the VM " + vm.getFqn() 
					   + " error in creating the isntance:  "+ e2.getMessage());   
			}
			
		} 
	
		
		Status previousStatus = null;
	
		try {
			// we need the hostname + domain so if we haven't that information,
			// shall to get it.
			/*if (!vm.canWorkWithChef()) {
				sdcClientUtils.checkIfSdcNodeIsReady(vm.getIp());
				sdcClientUtils.setNodeCommands(vm);
				
				vm = ip2vm.getVm(vm.getIp(), vm.getFqn(), vm.getOsType());
				// Configure the node with the corresponding node commands
			}*/
			// makes the validations
		//	instance = getProductToInstall(product, vm, vdc, attributes);
			previousStatus = instance.getStatus();
			// now we have the productInstance so can validate the operation
			validator.validateInstall(instance);
			
			instance.setStatus(Status.INSTALLING);
			instance.setVm(vm);
			// Id for the ProductInstance
			instance = productInstanceDao.update(instance);


			String installRecipe = recipeNamingGenerator
					.getInstallRecipe(instance);
			callChef(productRelease.getProduct().getName(), installRecipe, vm,
					attributes);
			instance.setStatus(Status.INSTALLED);
			return productInstanceDao.update(instance);

		} catch (CanNotCallChefException sce) {
			restoreInstance(previousStatus, instance);
			throw new SdcRuntimeException(sce);
		} catch (InvalidEntityException e) {
			throw new SdcRuntimeException(e);
		
		} catch (RuntimeException e) {
			// by default restore the previous state when a runtime is thrown
			restoreInstance(previousStatus, instance);
			throw new SdcRuntimeException(e);
		} catch (NodeExecutionException e) {
			restoreInstance(Status.ERROR, instance);
			throw e;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws FSMViolationException
	 * @throws ApplicationInstalledException
	 */
	@UseCase(traceTo = "UC_001.2", status = "implemented")
	@Override
	public void uninstall(ProductInstance productInstance)
			throws NodeExecutionException,
			FSMViolationException {
		Status previousStatus = productInstance.getStatus();
		try {
			validator.validateUninstall(productInstance);
			productInstance.setStatus(Status.UNINSTALLING);
			productInstance = productInstanceDao.update(productInstance);

			// at least has one
			String uninstallRecipe = recipeNamingGenerator
					.getUninstallRecipe(productInstance);
			callChef(uninstallRecipe, productInstance.getVm());

			productInstance.setStatus(Status.UNINSTALLED);
			productInstanceDao.update(productInstance);
		} catch (CanNotCallChefException e) {
			restoreInstance(previousStatus, productInstance);
			throw new SdcRuntimeException(e);
		} catch (InvalidEntityException e) {
			throw new SdcRuntimeException(e);
		} catch (RuntimeException e) {
			// by default restore the previous state when a runtime is thrown
			restoreInstance(previousStatus, productInstance);
			throw new SdcRuntimeException(e);
		} catch (NodeExecutionException e) {
			restoreInstance(Status.ERROR, productInstance);
			throw e;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProductInstance configure(ProductInstance productInstance,
			List<Attribute> configuration) throws NodeExecutionException,
			FSMViolationException {
		System.out.println ("Configuring product instance " + productInstance.getName() + " " + configuration);
		Status previousStatus = productInstance.getStatus();
		try {
			validator.validateConfigure(productInstance);
			productInstance.setStatus(Status.CONFIGURING);
			productInstance = productInstanceDao.update(productInstance);

			System.out.println ("Get VM");
			VM vm = productInstance.getVm();

		//	String backupRecipe = recipeNamingGenerator
			//		.getBackupRecipe(productInstance);
			//callChef(backupRecipe, vm);

	/*		String uninstallRecipe = recipeNamingGenerator
					.getUninstallRecipe(productInstance);
			callChef(uninstallRecipe, vm);*/
			System.out.println ("Load product " + productInstance
					.getProductRelease().getProduct().getName());
			Product product = productDao.load(productInstance
					.getProductRelease().getProduct().getName());

			if (configuration != null) {
			  product.setAttributes(configuration);
			}
			System.out.println ("Update product " + productInstance
					.getProductRelease().getProduct().getName());
			productDao.update(product);

			ProductRelease productRelease = productInstance.getProductRelease();
			productRelease.setProduct(product);
			
			String recipe = recipeNamingGenerator.getConfigureRecipe(productInstance);
			System.out.println ("recipe " + recipe);
	        callChef(
			productInstance.getProductRelease().getProduct().getName(),
			recipe, productInstance.getVm(), configuration);
	

		/*	String recipe = recipeNamingGenerator
					.getInstallRecipe(productInstance);
			callChef(
					productInstance.getProductRelease().getProduct().getName(),
					recipe, productInstance.getVm(), configuration);

			String restoreRecipe = recipeNamingGenerator
					.getRestoreRecipe(productInstance);
			callChef(restoreRecipe, vm);*/

			productInstance.setProductRelease(productRelease);
			productInstance.setStatus(Status.INSTALLED);
			return productInstanceDao.update(productInstance);

		} catch (CanNotCallChefException e) {
			restoreInstance(previousStatus, productInstance);
			throw new SdcRuntimeException(e);
		} catch (RuntimeException e) { // by runtime restore the previous state
			// restore the status
			restoreInstance(previousStatus, productInstance);
			throw new SdcRuntimeException(e);
		} catch (NodeExecutionException e) {
			restoreInstance(Status.ERROR, productInstance);
			throw e;
		} catch (InvalidEntityException e) {
			throw new SdcRuntimeException(e);
		} catch (EntityNotFoundException e) {
			throw new SdcRuntimeException(e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@UseCase(traceTo = "UC_001.4", status = "implemented")
	@Override
	public ProductInstance upgrade(ProductInstance productInstance,
			ProductRelease productRelease) throws NotTransitableException,
			NodeExecutionException, FSMViolationException {
		Status previousStatus = productInstance.getStatus();
		try {
			validator.validateUpdate(productInstance, productRelease);
			// update the status
			productInstance.setStatus(Status.UPGRADING);
			productInstance = productInstanceDao.update(productInstance);

			VM vm = productInstance.getVm();

			String backupRecipe = recipeNamingGenerator
					.getBackupRecipe(productInstance);
			callChef(backupRecipe, vm);

			String uninstallRecipe = recipeNamingGenerator
					.getUninstallRecipe(productInstance);
			callChef(uninstallRecipe, vm);

			productInstance.setProductRelease(productRelease);

			String installRecipe = recipeNamingGenerator
					.getInstallRecipe(productInstance);
			callChef(installRecipe, vm);

			String restoreRecipe = recipeNamingGenerator
					.getRestoreRecipe(productInstance);
			callChef(restoreRecipe, vm);

			productInstance.setStatus(Status.INSTALLED);
			return productInstanceDao.update(productInstance);

		} catch (CanNotCallChefException sce) {
			restoreInstance(previousStatus, productInstance);
			throw new SdcRuntimeException(sce);
		} catch (InvalidEntityException e) {
			// don't restore the status because this exception is storing the
			// product in database so it will fail anyway
			throw new SdcRuntimeException(e);
		} catch (RuntimeException e) { // by runtime restore the previous state
			// restore the status
			restoreInstance(previousStatus, productInstance);
			throw new SdcRuntimeException(e);
		} catch (NodeExecutionException e) {
			restoreInstance(Status.ERROR, productInstance);
			throw e;
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
	public ProductInstance load(String vdc, Long id)
			throws EntityNotFoundException {
		ProductInstance instance = productInstanceDao.load(id);
		if (!instance.getVdc().equals(vdc)) {
			throw new EntityNotFoundException(ProductInstance.class, "vdc", vdc);
		}
		return instance;
	}

	@Override
	public ProductInstance load(String vdc, String name)
			throws EntityNotFoundException {
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
	public List<ProductInstance> findByCriteria(
			ProductInstanceSearchCriteria criteria) {
		return productInstanceDao.findByCriteria(criteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProductInstance loadByCriteria(ProductInstanceSearchCriteria criteria)
			throws EntityNotFoundException, NotUniqueResultException {
		List<ProductInstance> products = productInstanceDao
				.findByCriteria(criteria);
		if (products.size() == 0) {
			throw new EntityNotFoundException(ProductInstance.class,
					"searchCriteria", criteria.toString());
		} else if (products.size() > 1) {
			throw new NotUniqueResultException();
		}
		return products.get(0);
	}

	@Override
	public ProductInstance update(ProductInstance productInstance) {
		try {
			return productInstanceDao.update(productInstance);
		} catch (InvalidEntityException e) {
			throw new SdcRuntimeException(e);
		}
	}

	// //////// PRIVATE METHODS ///////////
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
	private ProductInstance restoreInstance(Status previousStatus,
			ProductInstance instance) {
		instance.setStatus(previousStatus);
		return update(instance);
	}

	/**
	 * Creates or find the product instance in installation operation.
	 * 
	 * @param product
	 * @param vm
	 * @return
	 */
	private ProductInstance getProductToInstall(ProductRelease productRelease,
			VM vm, String vdc, List<Attribute> attributes) {
		ProductInstance instance;
		try {
			ProductInstanceSearchCriteria criteria = new ProductInstanceSearchCriteria();
			criteria.setVm(vm);
			criteria.setProductName(productRelease.getProduct().getName());
			instance = productInstanceDao.findUniqueByCriteria(criteria);

			Product product;
			try {
				product = productDao
						.load(productRelease.getProduct().getName());
			} catch (EntityNotFoundException e) {
				product = new Product(productRelease.getProduct().getName(),
						productRelease.getProduct().getDescription());
			}
			product.setAttributes(attributes);

			productRelease.setProduct(product);
			instance.setProductRelease(productRelease);
			instance.setName(vm.getFqn() + "_"
					+ productRelease.getProduct().getName() + "_"
					+ productRelease.getVersion());

		} catch (NotUniqueResultException e) {
			instance = new ProductInstance(productRelease, Status.UNINSTALLED,
					vm, vdc);
		}
		return instance;
	}
	
	public ProductInstance createProductInstance (ProductRelease productRelease,
			VM vm, String vdc, List<Attribute> attributes) 
					throws InvalidEntityException, AlreadyExistsEntityException {
		
		ProductInstance instance = new ProductInstance();
		
		Product product = null;
		try {
			product = productDao
					.load(productRelease.getProduct().getName());
		} catch (EntityNotFoundException e) {
			product = new Product(productRelease.getProduct().getName(),
					productRelease.getProduct().getDescription());
		}
		product.setAttributes(attributes);

		productRelease.setProduct(product);
		
		instance.setProductRelease(productRelease);
		instance.setVm(vm);
		instance.setVdc(vdc);
		instance.setStatus(Status.UNINSTALLED);
		instance.setName(vm.getFqn() + "_" + productRelease.getProduct().getName()
				+ "_" + productRelease.getVersion());
		

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

}
