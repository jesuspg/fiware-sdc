package com.telefonica.euro_iaas.sdc.rest.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.MultiPart;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ApplicationReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.exception.EnvironmentNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.InvalidApplicationReleaseUpdateRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidMultiPartRequestException;
import com.telefonica.euro_iaas.sdc.exception.InvalidProductReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ApplicationManager;
import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ApplicationSearchCriteria;
import com.telefonica.euro_iaas.sdc.rest.validation.ApplicationResourceValidator;

/**
 * default ApplicationResource implementation
 * 
 * @author Sergio Arroyo
 * 
 */
@Path("/catalog/application")
@Component
@Scope("request")
public class ApplicationResourceImpl implements ApplicationResource {

	@InjectParam("applicationManager")
	private ApplicationManager applicationManager;

	private ApplicationResourceValidator validator;
	private static Logger LOGGER = Logger.getLogger("ApplicationResourceImpl");

	/**
	 * {@inheritDoc}
	 * 
	 * @throws InvalidApplicationReleaseUpdateRequestException
	 * @throws InvalidMultiPartRequestException
	 * @throws InvalidProductReleaseException
	 */
	@Override
	public ApplicationRelease insert(MultiPart multiPart)
			throws AlreadyExistsApplicationReleaseException,
			InvalidApplicationReleaseException,
			ProductReleaseNotFoundException, InvalidMultiPartRequestException,
			InvalidProductReleaseException, EnvironmentNotFoundException {

		validator.validateInsert(multiPart);

		File cookbook = null;
		File installable = null;

		// First part contains a ApplicationReleaseDto object
		ApplicationReleaseDto applicationReleaseDto = (ApplicationReleaseDto) multiPart
				.getBodyParts().get(0).getEntityAs(ApplicationReleaseDto.class);
		LOGGER.log(Level.INFO, " Insert ApplicationRelease "
				+ applicationReleaseDto.getApplicationName() + " version "
				+ applicationReleaseDto.getVersion());

		Application application = new Application(applicationReleaseDto
				.getApplicationName(), applicationReleaseDto
				.getApplicationDescription(), applicationReleaseDto
				.getApplicationType());

		for (int i = 0; applicationReleaseDto.getPrivateAttributes().size() < i; i++)
			application.addAttribute(applicationReleaseDto
					.getPrivateAttributes().get(i));

		// From EnvironmentDto to Environment
		EnvironmentDto environmentDto = applicationReleaseDto
				.getEnvironmentDto();
		List<ProductReleaseDto> productReleaseDtos = environmentDto
				.getProducts();
		List<ProductRelease> productReleases = new ArrayList<ProductRelease>();

		for (int i = 0; i < productReleaseDtos.size(); i++) {
			ProductRelease productRelease = new ProductRelease();
			productRelease.setProduct(new Product(productReleaseDtos.get(i)
					.getProductName(), productReleaseDtos.get(i)
					.getProductDescription()));
			productRelease.setPrivateAttributes(productReleaseDtos.get(i)
					.getPrivateAttributes());
			productRelease.setReleaseNotes(productReleaseDtos.get(i)
					.getReleaseNotes());
			productRelease.setSupportedOOSS(productReleaseDtos.get(i)
					.getSupportedOS());
			productRelease.setTransitableReleases(productReleaseDtos.get(i)
					.getTransitableReleases());

			productReleases.add(productRelease);
		}

		ApplicationRelease applicationRelease = new ApplicationRelease(
				applicationReleaseDto.getVersion(), applicationReleaseDto
						.getReleaseNotes(), applicationReleaseDto
						.getPrivateAttributes(), application, new Environment(
						productReleases), applicationReleaseDto
						.getTransitableReleases());

		try {
			cookbook = File.createTempFile("cookbook-"
					+ applicationReleaseDto.getApplicationName() + "-"
					+ applicationReleaseDto.getVersion() + ".tar", ".tmp");

			installable = File.createTempFile("installable-"
					+ applicationReleaseDto.getApplicationName() + "-"
					+ applicationReleaseDto.getVersion() + ".tar", ".tmp");

			cookbook = getFileFromBodyPartEntity((BodyPartEntity) multiPart
					.getBodyParts().get(1).getEntity(), cookbook);
			installable = getFileFromBodyPartEntity((BodyPartEntity) multiPart
					.getBodyParts().get(2).getEntity(), installable);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return applicationManager.insert(applicationRelease, cookbook,
				installable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Application> findAll(Integer page, Integer pageSize,
			String orderBy, String orderType) {
		ApplicationSearchCriteria criteria = new ApplicationSearchCriteria();
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
		return applicationManager.findByCriteria(criteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Application load(String name) throws EntityNotFoundException {
		return applicationManager.load(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Attribute> loadAttributes(String name)
			throws EntityNotFoundException {
		return applicationManager.load(name).getAttributes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ApplicationRelease> findAll(String name, Integer page,
			Integer pageSize, String orderBy, String orderType) {
		ApplicationReleaseSearchCriteria criteria = new ApplicationReleaseSearchCriteria();

		if (!StringUtils.isEmpty(name)) {
			try {
				Application application = applicationManager.load(name);
				criteria.setApplication(application);
			} catch (EntityNotFoundException e) {
				throw new SdcRuntimeException("Can not find the application "
						+ name, e);
			}
		}

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
		return applicationManager.findReleasesByCriteria(criteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicationRelease load(String name, String version)
			throws EntityNotFoundException {
		Application application = applicationManager.load(name);
		return applicationManager.load(application, version);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws
	 */
	@Override
	public void delete(String name, String version)
			throws ApplicationReleaseNotFoundException,
			ApplicationReleaseStillInstalledException {

		LOGGER.log(Level.INFO, "Delete ApplicationRelease. ApplicationName : "
				+ name + " ApplicationVersion : " + version);

		Application application;
		try {
			application = applicationManager.load(name);
		} catch (EntityNotFoundException e) {
			throw new ApplicationReleaseNotFoundException(e);
		}

		ApplicationRelease applicationRelease;
		try {
			applicationRelease = applicationManager.load(application, version);
		} catch (EntityNotFoundException e) {
			throw new ApplicationReleaseNotFoundException(e);
		}

		applicationManager.delete(applicationRelease);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicationRelease update(MultiPart multiPart)
			throws ApplicationReleaseNotFoundException,
			InvalidApplicationReleaseException,
			ProductReleaseNotFoundException,
			InvalidApplicationReleaseUpdateRequestException,
			InvalidMultiPartRequestException, InvalidProductReleaseException,
			EnvironmentNotFoundException {

		ApplicationReleaseDto applicationReleaseDto = (ApplicationReleaseDto) multiPart
				.getBodyParts().get(0).getEntityAs(ApplicationReleaseDto.class);

		LOGGER.log(Level.INFO, " Update ApplicationRelease "
				+ applicationReleaseDto.getApplicationName() + " version "
				+ applicationReleaseDto.getVersion());

		Application application = new Application();
		ApplicationRelease applicationRelease = new ApplicationRelease();

		application.setName(applicationReleaseDto.getApplicationName());

		if (applicationReleaseDto.getApplicationDescription() != null)
			application.setDescription(applicationReleaseDto
					.getApplicationDescription());

		if (applicationReleaseDto.getApplicationType() != null)
			application.setType(applicationReleaseDto.getApplicationType());

		for (int i = 0; applicationReleaseDto.getPrivateAttributes().size() < 1; i++)
			application.addAttribute(applicationReleaseDto
					.getPrivateAttributes().get(i));

		applicationRelease.setApplication(application);

		if (applicationReleaseDto.getVersion() != null)
			applicationRelease.setVersion(applicationReleaseDto.getVersion());

		// ReleaseNotes
		if (applicationReleaseDto.getReleaseNotes() != null)
			applicationRelease.setReleaseNotes(applicationReleaseDto
					.getReleaseNotes());

		// PrivateAttributes
		if (applicationReleaseDto.getPrivateAttributes() != null)
			applicationRelease.setPrivateAttributes(applicationReleaseDto
					.getPrivateAttributes());

		// Environment
		if (applicationReleaseDto.getEnvironmentDto() != null) {

			EnvironmentDto environmentDto = applicationReleaseDto
					.getEnvironmentDto();
			List<ProductReleaseDto> productReleaseDtos = environmentDto
					.getProducts();
			List<ProductRelease> productReleases = new ArrayList<ProductRelease>();

			for (int i = 0; i < productReleaseDtos.size(); i++) {
				ProductRelease productRelease = new ProductRelease();
				productRelease.setProduct(new Product(productReleaseDtos.get(i)
						.getProductName(), productReleaseDtos.get(i)
						.getProductDescription()));
				productRelease.setPrivateAttributes(productReleaseDtos.get(i)
						.getPrivateAttributes());
				productRelease.setReleaseNotes(productReleaseDtos.get(i)
						.getReleaseNotes());
				productRelease.setSupportedOOSS(productReleaseDtos.get(i)
						.getSupportedOS());
				productRelease.setTransitableReleases(productReleaseDtos.get(i)
						.getTransitableReleases());

				productReleases.add(productRelease);
			}

			applicationRelease.setEnvironment(new Environment(productReleases));

		}
		// TransitableRelease
		if (applicationReleaseDto.getTransitableReleases() != null)
			applicationRelease.setTransitableReleases(applicationReleaseDto
					.getTransitableReleases());

		validator.validateUpdate(multiPart);

		File cookbook = null;
		File installable = null;

		try {
			cookbook = File.createTempFile("cookbook-"
					+ applicationReleaseDto.getApplicationName() + "-"
					+ applicationReleaseDto.getVersion() + ".tar", ".tmp");

			cookbook = getFileFromBodyPartEntity((BodyPartEntity) multiPart
					.getBodyParts().get(1).getEntity(), cookbook);
		} catch (IOException e) {
			throw new SdcRuntimeException(e);
		}

		try {
			installable = File.createTempFile("installable-"
					+ applicationReleaseDto.getApplicationName() + "-"
					+ applicationReleaseDto.getVersion() + ".tar", ".tmp");
			installable = getFileFromBodyPartEntity((BodyPartEntity) multiPart
					.getBodyParts().get(2).getEntity(), installable);
		} catch (IOException e) {
			throw new SdcRuntimeException(e);
		}

		return applicationManager.update(applicationRelease, cookbook,
				installable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Attribute> loadAttributes(String name, String version)
			throws EntityNotFoundException {
		return load(name, version).getAttributes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ApplicationRelease> findTransitable(String name, String version)
			throws EntityNotFoundException {
		return load(name, version).getTransitableReleases();
	}

	private File getFileFromBodyPartEntity(BodyPartEntity bpe, File file) {
		try {
			InputStream input = bpe.getInputStream();

			OutputStream out = new FileOutputStream(file);

			byte[] buf = new byte[1024];
			int len;
			while ((len = input.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			input.close();

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "An error was produced : " + e.toString());
			throw new SdcRuntimeException();
		}
		return file;
	}

	/**
	 * @param validator
	 *            the validator to set
	 */
	public void setValidator(ApplicationResourceValidator validator) {
		this.validator = validator;
	}

	@Override
	public List<ApplicationRelease> findAllReleases(Integer page,
			Integer pageSize, String orderBy, String orderType) {
		ApplicationReleaseSearchCriteria criteria = new ApplicationReleaseSearchCriteria();
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
		return applicationManager.findReleasesByCriteria(criteria);
	}
}
