package com.telefonica.euro_iaas.sdc.rest.resources;

import javax.ws.rs.Path;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * default Environment implementation
 * 
 * @author Jesus M. Movilla
 * 
 */
@Path("/catalog/environmentInstance")
@Component
@Scope("request")
public class EnvironmentInstanceResourceImpl implements
		EnvironmentInstanceResource {

	/*
	 * @InjectParam("environmentInstanceManager") private
	 * EnvironmentInstanceManager environmentInstanceManager;
	 * 
	 * @Override public EnvironmentInstance insert( EnvironmentInstanceDto
	 * environmentInstanceDto) throws AlreadyExistsEnvironmentInstanceException,
	 * InvalidEnvironmentInstanceException, EnvironmentNotFoundException,
	 * ProductInstanceNotFoundException {
	 * 
	 * List<ProductInstance> productInstances = new
	 * ArrayList<ProductInstance>(); List<ProductRelease> productReleases = new
	 * ArrayList<ProductRelease>(); List<ProductInstanceDto> productInstanceDtos
	 * = environmentInstanceDto.getProducts();
	 * 
	 * for (int i=0; i < productInstanceDtos.size(); i++) { ProductInstanceDto
	 * productInstanceDto = productInstanceDtos.get(i); Product product = new
	 * Product ();
	 * product.setName(productInstanceDto.getProduct().getProductName());
	 * 
	 * if (productInstanceDto.getProduct().getProductDescription()!= null)
	 * product.setDescription(
	 * productInstanceDto.getProduct().getProductDescription());
	 * 
	 * if (productInstanceDto.getProduct().getPrivateAttributes()!= null)
	 * product.setAttributes(
	 * productInstanceDto.getProduct().getPrivateAttributes());
	 * 
	 * ProductRelease productRelease = new ProductRelease(
	 * productInstanceDto.getProduct().getVersion(), null, //ReleaseNotes
	 * null,//Attributes product, null, //SupportedSO, null);// Transitable
	 * Releases
	 * 
	 * if (productInstanceDto.getProduct().getReleaseNotes()!= null)
	 * productRelease.setReleaseNotes(
	 * productInstanceDto.getProduct().getReleaseNotes());
	 * 
	 * if (productInstanceDto.getProduct().getPrivateAttributes() != null)
	 * productRelease.setPrivateAttributes(
	 * productInstanceDto.getProduct().getPrivateAttributes());
	 * 
	 * if (productInstanceDto.getProduct().getSupportedOS() != null)
	 * productRelease.setSupportedOOSS(
	 * productInstanceDto.getProduct().getSupportedOS());
	 * 
	 * if (productInstanceDto.getProduct().getTransitableReleases() != null)
	 * productRelease.setTransitableReleases(
	 * productInstanceDto.getProduct().getTransitableReleases());
	 * 
	 * 
	 * ProductInstance productInstance = new ProductInstance();
	 * 
	 * productInstance.setProductRelease(productRelease);
	 * productInstance.setStatus(Status.INSTALLED);
	 * productInstance.setVm(productInstanceDto.getVm());
	 * productInstance.setVdc(productInstanceDto.getVdc());
	 * 
	 * productReleases.add(productRelease);
	 * productInstances.add(productInstance); }
	 * 
	 * Environment environment = new Environment(productReleases);
	 * 
	 * EnvironmentInstance environmentInstance = new EnvironmentInstance(
	 * environment,productInstances);
	 * 
	 * return environmentInstanceManager.insert(environmentInstance); }
	 * 
	 * @Override public List<EnvironmentInstance> findAll(Integer page, Integer
	 * pageSize, String orderBy, String orderType) {
	 * 
	 * EnvironmentInstanceSearchCriteria criteria = new
	 * EnvironmentInstanceSearchCriteria(); if (page != null && pageSize !=
	 * null) { criteria.setPage(page); criteria.setPageSize(pageSize); } if
	 * (!StringUtils.isEmpty(orderBy)) { criteria.setOrderBy(orderBy); } if
	 * (!StringUtils.isEmpty(orderType)) { criteria.setOrderBy(orderType); }
	 * return environmentInstanceManager.findByCriteria(criteria); }
	 * 
	 * @Override public EnvironmentInstance load(Long Id) throws
	 * EnvironmentInstanceNotFoundException { return
	 * environmentInstanceManager.load(Id); }
	 * 
	 * @Override public void delete(Long Id) throws
	 * EnvironmentInstanceNotFoundException,
	 * ApplicationInstanceStillInstalledException {
	 * 
	 * environmentInstanceManager.delete(Id); }
	 * 
	 * @Override public EnvironmentInstance update( EnvironmentInstanceDto
	 * environmentInstanceDto) throws EnvironmentInstanceNotFoundException,
	 * InvalidEnvironmentInstanceException, EnvironmentNotFoundException,
	 * ProductInstanceNotFoundException {
	 * 
	 * List<ProductInstance> productInstances = new
	 * ArrayList<ProductInstance>(); List<ProductRelease> productReleases = new
	 * ArrayList<ProductRelease>(); List<ProductInstanceDto> productInstanceDtos
	 * = environmentInstanceDto.getProducts();
	 * 
	 * for (int i=0; i < productInstanceDtos.size(); i++) { ProductInstanceDto
	 * productInstanceDto = productInstanceDtos.get(i); Product product = new
	 * Product ();
	 * product.setName(productInstanceDto.getProduct().getProductName());
	 * 
	 * if (productInstanceDto.getProduct().getProductDescription()!= null)
	 * product.setDescription(
	 * productInstanceDto.getProduct().getProductDescription());
	 * 
	 * if (productInstanceDto.getProduct().getPrivateAttributes()!= null)
	 * product.setAttributes(
	 * productInstanceDto.getProduct().getPrivateAttributes());
	 * 
	 * ProductRelease productRelease = new ProductRelease(
	 * productInstanceDto.getProduct().getVersion(), null, //ReleaseNotes
	 * null,//Attributes product, null, //SupportedSO, null);// Transitable
	 * Releases
	 * 
	 * if (productInstanceDto.getProduct().getReleaseNotes()!= null)
	 * productRelease.setReleaseNotes(
	 * productInstanceDto.getProduct().getReleaseNotes());
	 * 
	 * if (productInstanceDto.getProduct().getPrivateAttributes() != null)
	 * productRelease.setPrivateAttributes(
	 * productInstanceDto.getProduct().getPrivateAttributes());
	 * 
	 * if (productInstanceDto.getProduct().getSupportedOS() != null)
	 * productRelease.setSupportedOOSS(
	 * productInstanceDto.getProduct().getSupportedOS());
	 * 
	 * if (productInstanceDto.getProduct().getTransitableReleases() != null)
	 * productRelease.setTransitableReleases(
	 * productInstanceDto.getProduct().getTransitableReleases());
	 * 
	 * 
	 * ProductInstance productInstance = new ProductInstance();
	 * 
	 * productInstance.setProductRelease(productRelease);
	 * productInstance.setStatus(Status.INSTALLED);
	 * productInstance.setVm(productInstanceDto.getVm());
	 * productInstance.setVdc(productInstanceDto.getVdc());
	 * 
	 * productReleases.add(productRelease);
	 * productInstances.add(productInstance); }
	 * 
	 * Environment environment = new Environment(productReleases);
	 * 
	 * EnvironmentInstance environmentInstance = new EnvironmentInstance(
	 * environment,productInstances);
	 * 
	 * return environmentInstanceManager.update(environmentInstance); }
	 */

}
