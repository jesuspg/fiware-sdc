package com.telefonica.euro_iaas.sdc.util;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_URL_TEMPLATE;


import java.net.ConnectException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;




import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.NodeCommandDao;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.NodeCommand;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.NodeCommandSearchCriteria;

public class SDCClientUtilsImpl implements SDCClientUtils {

	public final static String VM_PATH = "/rest/vm";
	public final static String CONFIG_PATH = "/rest/config";
	public final static String EXECUTE_PATH = "/rest/installable";

	private int MAX_TIME = 90000;
	
	private SystemPropertiesProvider propertiesProvider;
	private Client client;
	private NodeCommandDao nodeCommandDao;
	private OSDao osDao;

	/**
	 * {@inheritDoc}
	 */
	
	public VM getVM(String ip, String fqn, String osType) {
		String url = MessageFormat.format(propertiesProvider
				.getProperty(CHEF_CLIENT_URL_TEMPLATE), ip)
				+ VM_PATH;
		WebResource webResource = client.resource(url);
		VM vm = webResource.accept(MediaType.APPLICATION_XML).get(VM.class);
		vm.setFqn(fqn);
		vm.setOsType(osType);

		return vm;
	}

	/**
	 * {@inheritDoc}
	 */

	public void execute(VM vm) throws NodeExecutionException {
		try {
			String url = MessageFormat.format(propertiesProvider
					.getProperty(CHEF_CLIENT_URL_TEMPLATE), vm
					.getExecuteChefConectionUrl())
					+ EXECUTE_PATH;
			WebResource webResource = client.resource(url);
			webResource.accept(MediaType.APPLICATION_XML).post();
		} catch (UniformInterfaceException e) {
			throw new NodeExecutionException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */

	public List<Attribute> configure(VM vm, List<Attribute> configuration) {
		String url = MessageFormat.format(propertiesProvider
				.getProperty(CHEF_CLIENT_URL_TEMPLATE), vm
				.getExecuteChefConectionUrl())
				+ CONFIG_PATH;
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON_TYPE,
						MediaType.APPLICATION_XML_TYPE).type(
						MediaType.APPLICATION_XML_TYPE).put(
						ClientResponse.class, configuration);
		Attributes outputAttributes = response.getEntity(Attributes.class);
		return outputAttributes;
	}

	/**
	 * {@inheritDoc}
	 */

	public Attribute configureProperty(VM vm, Attribute attribute) {
		String url = MessageFormat.format(propertiesProvider
				.getProperty(CHEF_CLIENT_URL_TEMPLATE), vm
				.getExecuteChefConectionUrl())
				+ CONFIG_PATH;
		WebResource webResource = client.resource(url);
		/*
		 * return webResource.accept(MediaType.APPLICATION_XML)
		 * .type(MediaType.APPLICATION_XML).entity(configuration)
		 * .put(Attributes.class);
		 */
		ClientResponse response = webResource.accept(MediaType.APPLICATION_XML)
				.type(MediaType.APPLICATION_XML).put(ClientResponse.class,
						attribute);
		Attribute outputAttribute = response.getEntity(Attribute.class);
		return outputAttribute;

		/*
		 * ClientConfig config = new DefaultClientConfig(); Client client2 =
		 * Client.create(config); String url = MessageFormat.format(
		 * propertiesProvider.getProperty(CHEF_CLIENT_URL_TEMPLATE),
		 * vm.getExecuteChefConectionUrl()) + CONFIG_PATH; WebResource service =
		 * client2.resource(url); ClientResponse response =
		 * service.accept(MediaType.APPLICATION_XML) .put(ClientResponse.class,
		 * attribute); Attribute outputAttribute =
		 * response.getEntity(Attribute.class); return outputAttribute;
		 */
		// Return code should be 201 == created resource

		// Builder builder =
		// webResource.accept(MediaType.APPLICATION_XML).type(MediaType.APPLICATION_XML);
		// create Attributes object because Jersey can not write List<Attribute>
		// builder.entity(attribute);
		// Attribute outputAttribute = builder.put(Attribute.class);
		// return outputAttribute;
	}

	/**
	 * {@inheritDoc}
	 */

	public void setNodeCommands(VM vm)
			 {
		OS os = null;
		try {
			os = osDao.load("76");
		} catch (EntityNotFoundException e) {
		  //  Logger.warn (" The Operatng Sytem has not been provided");
		//	throw new InvalidInstallProductRequestException(
			//		" The Operating Sytem has not been provided");
			
		}

		List<NodeCommand> nodeCommands = nodeCommandDao
				.findByCriteria(new NodeCommandSearchCriteria(os));

		ArrayList<Attribute> configuration = new ArrayList<Attribute>();
		ArrayList<Attribute> outputAttributes = new ArrayList<Attribute>();

		for (int i = 0; i < nodeCommands.size(); i++) {
			configuration.add(new Attribute(nodeCommands.get(i).getKey(),
					nodeCommands.get(i).getValue()));
		}

		// List <Attribute> attributes = configure (vm, configuration);
		for (int i = 0; i < configuration.size(); i++) {
			outputAttributes.add(configureProperty(vm, configuration.get(i)));
		}

	}
	
	public void checkIfSdcNodeIsReady (String ip) throws NodeExecutionException {
		String url = MessageFormat.format(propertiesProvider
				.getProperty(CHEF_CLIENT_URL_TEMPLATE), ip)
				+ VM_PATH;
		
		WebResource webResource = client.resource(url);
		String response = "response";
		int time = 10000;
		while (!(response.contains("ip"))){
				try {
					Thread.sleep(time);
					System.out.println("Checking node : " + ip + " time:" + time);
					if (time > MAX_TIME){
						String errorMesg = "SdcNode " + ip + " is not ready";
						throw new NodeExecutionException (errorMesg);
					}				
					try{
						response = webResource.accept(MediaType.APPLICATION_XML).get(String.class);
					}catch(Exception e){
						System.out.println("The sdc client is not ready yet. time: " + time);
					}
					time += time;
				} catch (InterruptedException e) {
					String errorMsg = e.getMessage();
					throw new NodeExecutionException (errorMsg, e);
				} 	
				
		}
		
	}

	/**
	 * @param nodeCommandDao
	 *            the nodeCommandDao to set
	 */
	public void setNodeCommandDao(NodeCommandDao nodeCommandDao) {
		this.nodeCommandDao = nodeCommandDao;
	}

	/**
	 * @param osDao
	 *            the osDao to set
	 */
	public void setOsDao(OSDao osDao) {
		this.osDao = osDao;
	}

	/**
	 * @param propertiesProvider
	 *            the propertiesProvider to set
	 */
	public void setPropertiesProvider(
			SystemPropertiesProvider propertiesProvider) {
		this.propertiesProvider = propertiesProvider;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}

}
