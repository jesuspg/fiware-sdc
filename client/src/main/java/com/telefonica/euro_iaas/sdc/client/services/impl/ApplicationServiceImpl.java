package com.telefonica.euro_iaas.sdc.client.services.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;



import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;

import com.telefonica.euro_iaas.sdc.client.ClientConstants;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.model.ApplicationReleases;
import com.telefonica.euro_iaas.sdc.client.services.ApplicationService;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ApplicationReleaseDto;

public class ApplicationServiceImpl extends AbstractBaseService implements
		ApplicationService {

	public ApplicationServiceImpl(Client client, String baseUrl,
			String mediaType) {
		setBaseHost(baseUrl);
		setType(mediaType);
		setClient(client);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicationRelease add(ApplicationReleaseDto releaseDto,
			InputStream cookbook, InputStream files) {
		try {
			MultiPart payload = new MultiPart().bodyPart(
					new BodyPart(releaseDto, MediaType.valueOf(getType())))
					.bodyPart(
							new BodyPart(IOUtils.toByteArray(cookbook),
									MediaType.APPLICATION_OCTET_STREAM_TYPE))
					.bodyPart(
							new BodyPart(IOUtils.toByteArray(files),
									MediaType.APPLICATION_OCTET_STREAM_TYPE));

			String url = getBaseHost() + ClientConstants.BASE_APPLICATION_PATH;
			WebResource wr = getClient().resource(url);
			return wr.accept(getType()).type("multipart/mixed").entity(payload)
					.post(ApplicationRelease.class);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicationRelease update(ApplicationReleaseDto releaseDto,
			InputStream cookbook, InputStream files) {
		try {

			MultiPart payload = new MultiPart().bodyPart(
					new BodyPart(releaseDto, MediaType.valueOf(getType())))
					.bodyPart(
							new BodyPart(IOUtils.toByteArray(cookbook),
									MediaType.APPLICATION_OCTET_STREAM_TYPE))
					.bodyPart(
							new BodyPart(IOUtils.toByteArray(files),
									MediaType.APPLICATION_OCTET_STREAM_TYPE));

			String url = getBaseHost()
					+ MessageFormat.format(
							ClientConstants.APPLICATION_RELEASE_PATH,
							releaseDto.getApplicationName(), releaseDto
									.getVersion());
			WebResource wr = getClient().resource(url);
			return wr.accept(getType()).type("multipart/mixed").entity(payload)
					.put(ApplicationRelease.class);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(String appname, String version) {

		String url = getBaseHost()
				+ MessageFormat.format(
						ClientConstants.APPLICATION_RELEASE_PATH, appname,
						version);
		;
		WebResource wr = getClient().resource(url);

		wr.accept(getType()).type(getType()).delete(ClientResponse.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicationRelease load(String application, String version)
			throws ResourceNotFoundException {
		String url = getBaseHost()
				+ MessageFormat.format(
						ClientConstants.APPLICATION_RELEASE_PATH, application,
						version);
		WebResource wr = getClient().resource(url);
		try {
			return wr.accept(getType()).get(ApplicationRelease.class);
		} catch (Exception e) {
			throw new ResourceNotFoundException(ApplicationRelease.class, url);
		}
	}

	@Override
	public List<ApplicationRelease> findAll(Integer page, Integer pageSize,
			String orderBy, String orderType, String applicationName) {
		String url;
		if (StringUtils.isEmpty(applicationName)) {
			url = getBaseHost() + ClientConstants.ALL_APPLICATION_RELEASE_PATH;
		} else {
			url = getBaseHost()
					+ MessageFormat.format(
							ClientConstants.BASE_APPLICATION_RELEASE_PATH,
							applicationName);
		}
		WebResource wr = getClient().resource(url);
		MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();
		searchParams = addParam(searchParams, "page", page);
		searchParams = addParam(searchParams, "pageSize", pageSize);
		searchParams = addParam(searchParams, "orderBy", orderBy);
		searchParams = addParam(searchParams, "orderType", orderType);

		return wr.queryParams(searchParams).accept(getType()).get(
				ApplicationReleases.class);
	}
}
