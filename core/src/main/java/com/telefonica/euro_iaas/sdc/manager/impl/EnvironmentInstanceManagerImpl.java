package com.telefonica.euro_iaas.sdc.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.dao.EnvironmentDao;
import com.telefonica.euro_iaas.sdc.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.sdc.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsEnvironmentInstanceException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationInstanceStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.EnvironmentNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.InvalidEnvironmentInstanceException;
import com.telefonica.euro_iaas.sdc.exception.ProductInstanceNotFoundException;
import com.telefonica.euro_iaas.sdc.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.EnvironmentInstanceSearchCriteria;
import com.telefonica.euro_iaas.sdc.validation.EnvironmentInstanceValidator;

public class EnvironmentInstanceManagerImpl implements
		EnvironmentInstanceManager {

	private EnvironmentInstanceValidator validator;
	private ProductInstanceDao productInstanceDao;
	private EnvironmentDao environmentDao;
	private EnvironmentInstanceDao environmentInstanceDao;
	private static Logger LOGGER = Logger
			.getLogger("EnvironmentManagerInstanceImpl");

	@Override
	public EnvironmentInstance load(Long id)
			throws EnvironmentInstanceNotFoundException {
		try {
			return environmentInstanceDao.load(id);
		} catch (EntityNotFoundException e) {
			String environmentInstanceNotFoundException = " The Environment Instance"
					+ id + " DOES NOT EXIST ";
			LOGGER.log(Level.SEVERE, environmentInstanceNotFoundException);
			throw new EnvironmentInstanceNotFoundException(
					environmentInstanceNotFoundException);
		}
	}

	@Override
	public List<EnvironmentInstance> findAll() {
		return environmentInstanceDao.findAll();
	}

	@Override
	public List<EnvironmentInstance> findByCriteria(
			EnvironmentInstanceSearchCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnvironmentInstance insert(EnvironmentInstance environmentInstance)
			throws EnvironmentNotFoundException,
			ProductInstanceNotFoundException,
			InvalidEnvironmentInstanceException,
			AlreadyExistsEnvironmentInstanceException {

		validator.validateInsert(environmentInstance);

		return insertEnvironmentInstanceBBDD(environmentInstance);
	}

	@Override
	public void delete(Long Id) throws EnvironmentInstanceNotFoundException,
			ApplicationInstanceStillInstalledException {

		deleteEnvironmentInstanceBBDD(Id);
	}

	@Override
	public EnvironmentInstance update(EnvironmentInstance environmentInstance)
			throws EnvironmentInstanceNotFoundException,
			EnvironmentNotFoundException, ProductInstanceNotFoundException,
			InvalidEnvironmentInstanceException {

		validator.validateUpdate(environmentInstance);
		return updateEnvironmentInstanceBBDD(environmentInstance);
	}

	private EnvironmentInstance insertEnvironmentInstanceBBDD(
			EnvironmentInstance environmentInstance)
			throws EnvironmentNotFoundException,
			ProductInstanceNotFoundException,
			InvalidEnvironmentInstanceException,
			AlreadyExistsEnvironmentInstanceException {

		ProductInstance productInstance;
		Environment environment = environmentInstance.getEnvironment();
		Environment environmentEnv;
		List<ProductInstance> productInstances = environmentInstance
				.getProductInstances();
		List<ProductInstance> productInstancesEnvironment = new ArrayList<ProductInstance>();
		EnvironmentInstance envInstance;

		// Environment
		try {
			environmentEnv = environmentDao.load(environment.getName());
		} catch (EntityNotFoundException e) {
			String environmentNotFoundException = "The Environment "
					+ environment.getName() + " NOT FOUND ";
			LOGGER.log(Level.SEVERE, environmentNotFoundException);
			throw new EnvironmentNotFoundException(environmentNotFoundException);

		}

		// ProductInstances
		for (int i = 0; i < productInstances.size(); i++) {
			ProductInstance pInstance = productInstances.get(i);
			try {
				productInstance = productInstanceDao.load(pInstance.getId());
			} catch (EntityNotFoundException e) {
				String productInstanceNotFoundException = " The Product Instance"
						+ pInstance.getProductRelease().getProduct().getName()
						+ "with id " + pInstance.getId() + "  DOES NOT EXIST ";
				LOGGER.log(Level.SEVERE, productInstanceNotFoundException);
				throw new ProductInstanceNotFoundException(
						productInstanceNotFoundException);
			}
			productInstancesEnvironment.add(productInstance);
		}

		// EnvironmentInstance
		EnvironmentInstance environmentInstanceIn = new EnvironmentInstance(
				environmentEnv, productInstancesEnvironment);
		try {
			envInstance = environmentInstanceDao.load(environmentInstance
					.getId());
		} catch (EntityNotFoundException e) {
			try {
				envInstance = environmentInstanceDao
						.create(environmentInstanceIn);
			} catch (InvalidEntityException e1) {
				String invalidEnvironmentInstanceException = " The Environment "
						+ environmentInstanceIn.getEnvironment().getName()
						+ " with ID = "
						+ environmentInstanceIn.getId()
						+ " is INVALID ";
				LOGGER.log(Level.SEVERE, invalidEnvironmentInstanceException);
				throw new InvalidEnvironmentInstanceException(
						invalidEnvironmentInstanceException);

			} catch (AlreadyExistsEntityException e1) {
				String alreadyExistsEnvironmentInstanceException = " The Environment "
						+ environmentInstanceIn.getEnvironment().getName()
						+ "with ID "
						+ environmentInstanceIn.getId()
						+ " ALREADY EXISTS ";
				LOGGER.log(Level.SEVERE,
						alreadyExistsEnvironmentInstanceException);
				throw new AlreadyExistsEnvironmentInstanceException(
						alreadyExistsEnvironmentInstanceException);

			}

		}
		return envInstance;
	}

	private EnvironmentInstance updateEnvironmentInstanceBBDD(
			EnvironmentInstance environmentInstance)
			throws EnvironmentInstanceNotFoundException,
			ProductInstanceNotFoundException, EnvironmentNotFoundException {

		ProductInstance productInstance;
		Environment environment;
		List<ProductInstance> productInstances = environmentInstance
				.getProductInstances();
		List<ProductInstance> existedProductInstances = new ArrayList<ProductInstance>();

		LOGGER.log(Level.INFO, "Environment Instance Before Loading "
				+ environmentInstance.getEnvironment().getName() + " with ID "
				+ environmentInstance.getId());
		// Environment
		try {
			environment = environmentDao.load(environmentInstance
					.getEnvironment().getName());
		} catch (EntityNotFoundException e) {
			String environmentNotFoundException = "The Environment "
					+ environmentInstance.getEnvironment().getName()
					+ " NOT FOUND ";
			LOGGER.log(Level.SEVERE, environmentNotFoundException);
			throw new EnvironmentNotFoundException(environmentNotFoundException);
		}

		// ProductInstances
		for (int i = 0; i < productInstances.size(); i++) {
			ProductInstance pInstance = productInstances.get(i);
			try {
				productInstance = productInstanceDao.load(pInstance.getId());
			} catch (EntityNotFoundException e) {
				String productInstanceNotFoundException = " The Product Instance"
						+ pInstance.getProductRelease().getProduct().getName()
						+ "with id " + pInstance.getId() + "  DOES NOT EXIST ";
				LOGGER.log(Level.SEVERE, productInstanceNotFoundException);
				throw new ProductInstanceNotFoundException(
						productInstanceNotFoundException);
			}
			existedProductInstances.add(productInstance);
		}

		environmentInstance.setProductInstances(existedProductInstances);
		environmentInstance.setEnvironment(environment);

		// Update Environment Instance
		try {
			environmentInstance = environmentInstanceDao
					.update(environmentInstance);
		} catch (InvalidEntityException e) {
			String InvalidEnvironmentInstanceNotFoundException = " The Environment Instance"
					+ environmentInstance.getEnvironment().getName()
					+ " with ID "
					+ environmentInstance.getId()
					+ " IS NOT VALID ";
			LOGGER.log(Level.SEVERE,
					InvalidEnvironmentInstanceNotFoundException);
			throw new EnvironmentInstanceNotFoundException(
					InvalidEnvironmentInstanceNotFoundException);

		}
		return environmentInstance;
	}

	private void deleteEnvironmentInstanceBBDD(Long Id)
			throws EnvironmentInstanceNotFoundException,
			ApplicationInstanceStillInstalledException {

		EnvironmentInstance env;
		try {
			env = environmentInstanceDao.load(Id);
			validator.validateDelete(env);
		} catch (EntityNotFoundException e) {
			String environmentIntanceNotFoundException = " The Environment Instance"
					+ Id + " DOES NOT EXIST ";
			LOGGER.log(Level.SEVERE, environmentIntanceNotFoundException);
			throw new EnvironmentInstanceNotFoundException(
					environmentIntanceNotFoundException);
		}
		environmentInstanceDao.remove(env);
	}

	/**
	 * @param productInstanceDao
	 *            the productInstanceDao to set
	 */
	public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
		this.productInstanceDao = productInstanceDao;
	}

	/**
	 * @param environmentDao
	 *            the environmentDao to set
	 */
	public void setEnvironmentDao(EnvironmentDao environmentDao) {
		this.environmentDao = environmentDao;
	}

	/**
	 * @param environmentInstanceDao
	 *            the environmentInstanceDao to set
	 */
	public void setEnvironmentInstanceDao(
			EnvironmentInstanceDao environmentInstanceDao) {
		this.environmentInstanceDao = environmentInstanceDao;
	}

	/**
	 * @param validator
	 *            the validator to set
	 */
	public void setValidator(EnvironmentInstanceValidator validator) {
		this.validator = validator;
	}
}
