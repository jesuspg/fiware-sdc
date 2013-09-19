package com.telefonica.euro_iaas.sdc.it.util;

import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.BASE_URL;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.MIME_TYPE;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;

import java.util.List;

import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.client.services.EnvironmentInstanceService;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;

public class EnvironmentInstanceUtils {

	private SDCClient client = new SDCClient();
	private EnvironmentInstanceService service;

	public EnvironmentInstance load(Long environmentInstanceID)
			throws ResourceNotFoundException {
		service = client.getEnvironmentInstanceService(getProperty(BASE_URL),
				getProperty(MIME_TYPE));
		return service.load(environmentInstanceID);
	}

	/**
	 * Insert the environmentInstance
	 * 
	 * @param environmentDto
	 *            the name
	 * @param productInstanceDtos
	 * @return the EnvironmentInstance
	 */
	public EnvironmentInstance insert(EnvironmentDto environmentDto,
			List<ProductInstanceDto> productInstanceDtos) {
		service = client.getEnvironmentInstanceService(getProperty(BASE_URL),
				getProperty(MIME_TYPE));

		EnvironmentInstanceDto instance = new EnvironmentInstanceDto();
		instance.setEnvironment(environmentDto);
		instance.setProducts(productInstanceDtos);

		return service.insert(instance);
	}

	/**
	 * Update the environmentInstance
	 * 
	 * @param environmentDto
	 *            the environmentDto
	 * @param productInstanceDtos
	 * @return the EnvironmentInstance
	 */
	public EnvironmentInstance update(EnvironmentDto environmentDto,
			List<ProductInstanceDto> productInstanceDtos) {

		service = client.getEnvironmentInstanceService(getProperty(BASE_URL),
				getProperty(MIME_TYPE));

		EnvironmentInstanceDto instance = new EnvironmentInstanceDto();
		instance.setEnvironment(environmentDto);
		instance.setProducts(productInstanceDtos);

		return service.update(instance);
	}

	/**
	 * Delete the environmentInstance
	 * 
	 * @param environmentId
	 *            the id
	 */
	public void delete(Long Id) {
		service = client.getEnvironmentInstanceService(getProperty(BASE_URL),
				getProperty(MIME_TYPE));
		service.delete(Id);
	}
}
