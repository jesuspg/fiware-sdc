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

package com.telefonica.euro_iaas.sdc.rest.resources;

import javax.ws.rs.Path;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * default Environment implementation
 * 
 * @author Jesus M. Movilla
 */
@Path("/catalog/environment")
@Component
@Scope("request")
public class EnvironmentResourceImpl implements EnvironmentResource {

    /*
     * @InjectParam("environmentManager") private EnvironmentManager environmentManager;
     * @Override public Environment insert(EnvironmentDto environmentDto) throws AlreadyExistsEnvironmentException,
     * InvalidEnvironmentException, ProductNotFoundException, ProductReleaseNotFoundException { List<ProductRelease>
     * productReleases = new ArrayList<ProductRelease>(); List<ProductReleaseDto> productReleaseDtos =
     * environmentDto.getProducts(); for (int i=0; i < productReleaseDtos.size(); i++) { ProductReleaseDto
     * productReleaseDto = productReleaseDtos.get(i); Product product = new Product (
     * productReleaseDto.getProductName(), productReleaseDto.getProductDescription()); for (int j=0;
     * productReleaseDto.getPrivateAttributes().size() < 1; j++)
     * product.addAttribute(productReleaseDto.getPrivateAttributes().get(j)); ProductRelease productRelease = new
     * ProductRelease( productReleaseDto.getVersion(), productReleaseDto.getReleaseNotes(),
     * productReleaseDto.getPrivateAttributes(), product, productReleaseDto.getSupportedOS(),
     * productReleaseDto.getTransitableReleases() ); productReleases.add(productRelease); } Environment environment =
     * new Environment(productReleases); return environmentManager.insert(environment); }
     * @Override public List<Environment> findAll(Integer page, Integer pageSize, String orderBy, String orderType) {
     * EnvironmentSearchCriteria criteria = new EnvironmentSearchCriteria(); if (page != null && pageSize != null) {
     * criteria.setPage(page); criteria.setPageSize(pageSize); } if (!StringUtils.isEmpty(orderBy)) {
     * criteria.setOrderBy(orderBy); } if (!StringUtils.isEmpty(orderType)) { criteria.setOrderBy(orderType); } return
     * environmentManager.findByCriteria(criteria); }
     * @Override public Environment load(String name) throws EnvironmentNotFoundException { return
     * environmentManager.load(name); }
     */

    /**
     * {@inheritDoc}
     * 
     * @throws EnvironmentNotFoundException
     * @throws ProductReleaseInApplicationReleaseException
     * @throws ProductReleaseStillInstalledException
     */
    /*
     * @Override public void delete (String envName) throws EnvironmentNotFoundException,
     * ProductReleaseStillInstalledException, ProductReleaseInApplicationReleaseException{
     * environmentManager.delete(envName); }
     */

    /**
     * {@inheritDoc}
     * 
     * @throws ProductNotFoundException
     * @throws ProductReleaseNotFoundException
     */
    /*
     * @Override public Environment update(EnvironmentDto environmentDto) throws EnvironmentNotFoundException,
     * InvalidEnvironmentException, ProductReleaseNotFoundException, ProductNotFoundException { List<ProductRelease>
     * productReleases = new ArrayList<ProductRelease>(); List<ProductReleaseDto> productReleaseDtosToBeUpdated =
     * environmentDto.getProducts(); for (int i=0; i < productReleaseDtosToBeUpdated.size(); i++) { ProductReleaseDto
     * productReleaseDto = productReleaseDtosToBeUpdated.get(i); Product product = new Product (
     * productReleaseDto.getProductName(), productReleaseDto.getProductDescription()); for (int j=0;
     * productReleaseDto.getPrivateAttributes().size() < 1; j++)
     * product.addAttribute(productReleaseDto.getPrivateAttributes().get(j)); ProductRelease productRelease = new
     * ProductRelease( productReleaseDto.getVersion(), productReleaseDto.getReleaseNotes(),
     * productReleaseDto.getPrivateAttributes(), product, productReleaseDto.getSupportedOS(),
     * productReleaseDto.getTransitableReleases() ); productReleases.add(productRelease); } Environment environment =
     * new Environment(productReleases); return environmentManager.update(environment); }
     */
}
