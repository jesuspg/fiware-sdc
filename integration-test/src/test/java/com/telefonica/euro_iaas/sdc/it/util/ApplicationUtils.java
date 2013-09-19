package com.telefonica.euro_iaas.sdc.it.util;

import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.BASE_URL;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.MIME_TYPE;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;

import java.io.InputStream;
import java.util.List;

import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.ApplicationService;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentDto;

public class ApplicationUtils {

	private SDCClient client = new SDCClient();
	private ApplicationService service;

	public ApplicationRelease load(String applicationName, String version)
			throws ResourceNotFoundException {
		service = client.getApplicationService(getProperty(BASE_URL),
				getProperty(MIME_TYPE));
		return service.load(applicationName, version);
	}

	/**
	 * Insert the application
	 * 
	 * @param applicationName
	 *            the application name
	 * @param version
	 *            the version
	 * @param transitableReleases
	 * @param ip
	 *            the ip where the application will be installed
	 * @param vdc
	 *            the vdc where the node is
	 * @return the inserted application
	 * @throws MaxTimeWaitingExceedException
	 *             if the installation takes more time than expected
	 * @throws InvalidExecutionException
	 *             if the product can not be installed
	 */
	public ApplicationRelease add(String applicationName, String version,
			String type, String description, String releaseNotes,
			List<Attribute> attributes, EnvironmentDto environment,
			List<ApplicationRelease> transitableReleases) {
		service = client.getApplicationService(getProperty(BASE_URL),
				getProperty(MIME_TYPE));

		ApplicationReleaseDto instance = new ApplicationReleaseDto(
				applicationName, description, type, version, releaseNotes,
				attributes, environment, transitableReleases);
		InputStream cookbook = this.getClass().getResourceAsStream(
				"/files/" + applicationName + version + "-cookbook.tar");
		InputStream binaries = this.getClass().getResourceAsStream(
				"/files/" + applicationName + version + "-bin.tar");

		return service.add(instance, cookbook, binaries);
	}

	/**
	 * Update the application
	 * 
	 * @param applicationName
	 *            the application name
	 * @param version
	 *            the version
	 * @param type
	 *            , the application type
	 * @param description
	 *            the application description
	 * @param transitableReleases
	 * @param supportedProducts
	 *            ProductRelease over whch application is installed
	 * @return the updated application
	 * @throws MaxTimeWaitingExceedException
	 *             if the installation takes more time than expected
	 * @throws InvalidExecutionException
	 *             if the product can not be installed
	 */
	public ApplicationRelease update(String applicationName, String version,
			String description, String type, String releaseNotes,
			List<Attribute> attributes, EnvironmentDto environment,
			List<ApplicationRelease> transitableReleases) {
		service = client.getApplicationService(getProperty(BASE_URL),
				getProperty(MIME_TYPE));

		ApplicationReleaseDto instance = new ApplicationReleaseDto(
				applicationName, description, type, version, releaseNotes,
				attributes, environment, transitableReleases);

		InputStream cookbook = this.getClass().getResourceAsStream(
				"/files/" + applicationName + version + "-cookbook.tar");
		InputStream binaries = this.getClass().getResourceAsStream(
				"/files/" + applicationName + version + "-bin.tar");

		return service.update(instance, cookbook, binaries);
	}

	/**
	 * Delete the application
	 * 
	 * @param applicationName
	 *            the application name
	 * @param version
	 *            the version
	 * @throws MaxTimeWaitingExceedException
	 *             if the installation takes more time than expected
	 * @throws InvalidExecutionException
	 *             if the product can not be installed
	 */
	public void delete(String applicationName, String version) {
		service = client.getApplicationService(getProperty(BASE_URL),
				getProperty(MIME_TYPE));

		service.delete(applicationName, version);
	}
}
