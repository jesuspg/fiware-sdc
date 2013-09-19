package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.sdc.exception.AlreadyExistsEnvironmentException;
import com.telefonica.euro_iaas.sdc.exception.EnvironmentNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.InvalidEnvironmentException;
import com.telefonica.euro_iaas.sdc.exception.ProductNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseInApplicationReleaseException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.ProductReleaseStillInstalledException;
import com.telefonica.euro_iaas.sdc.manager.EnvironmentManager;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.EnvironmentSearchCriteria;

/**
 * default Environment implementation
 * @author Jesus M. Movilla
 *
 */
@Path("/catalog/environment")
@Component
@Scope("request")
public class EnvironmentResourceImpl implements EnvironmentResource {
	
	@InjectParam("environmentManager")
    private EnvironmentManager environmentManager;
	
    /**
     * {@inheritDoc}
     */
    @Override
	public Environment insert(EnvironmentDto environmentDto)
			throws AlreadyExistsEnvironmentException, InvalidEnvironmentException, 
			ProductNotFoundException, ProductReleaseNotFoundException {
		 List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
		 List<ProductReleaseDto> productReleaseDtos = environmentDto.getProducts();
		 
		 for (int i=0; i < productReleaseDtos.size(); i++) {
			 ProductReleaseDto productReleaseDto = productReleaseDtos.get(i);
			 Product product = new Product (
				 productReleaseDto.getProductName(),
				 productReleaseDto.getProductDescription());

			 for (int j=0; productReleaseDto.getPrivateAttributes().size() < 1; j++)
				 product.addAttribute(productReleaseDto.getPrivateAttributes().get(j));

			 ProductRelease productRelease = new ProductRelease(
				productReleaseDto.getVersion(),
	            productReleaseDto.getReleaseNotes(),
	            productReleaseDto.getPrivateAttributes(),
	            product,
	            productReleaseDto.getSupportedOS(),
	            productReleaseDto.getTransitableReleases() );
			 
			 productReleases.add(productRelease);
		 }
				 
		 Environment environment = new Environment(productReleases);
		  
		 return environmentManager.insert(environment);
	 }
	 
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Environment> findAll(Integer page, Integer pageSize, 
		String orderBy, String orderType) {
		 
		EnvironmentSearchCriteria criteria = new EnvironmentSearchCriteria();
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
	    return environmentManager.findByCriteria(criteria);
	}
	 
    /**
     * {@inheritDoc}
     */
    @Override
    public Environment load(String name) throws EnvironmentNotFoundException {
		 return environmentManager.load(name);
	 }
	 
	 /*public List<Environment> findAll(String envName,
	            Integer page, Integer pageSize, String orderBy, String orderType){
		 return Arrays.asList(new EnvironmentResource(), new EnvironmentResource());
	 }
	 */
    /**
     * {@inheritDoc}
     * @throws EnvironmentNotFoundException
     * @throws ProductReleaseInApplicationReleaseException 
     * @throws ProductReleaseStillInstalledException 
     */
    @Override
    public void delete (String envName) throws 
    EnvironmentNotFoundException, ProductReleaseStillInstalledException,
    ProductReleaseInApplicationReleaseException{
		 
    	environmentManager.delete(envName);
	 }
	 
    /**
     * {@inheritDoc}
     * @throws ProductNotFoundException 
     * @throws ProductReleaseNotFoundException 
     */
    @Override
    public Environment update(EnvironmentDto environmentDto)
		        throws EnvironmentNotFoundException,
		        InvalidEnvironmentException, ProductReleaseNotFoundException, ProductNotFoundException {
    	
    	List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
		List<ProductReleaseDto> productReleaseDtosToBeUpdated = 
				environmentDto.getProducts();
		 
		 for (int i=0; i < productReleaseDtosToBeUpdated.size(); i++) {
			 ProductReleaseDto productReleaseDto = productReleaseDtosToBeUpdated.get(i);
			 Product product = new Product (
				 productReleaseDto.getProductName(),
				 productReleaseDto.getProductDescription());

			 for (int j=0; productReleaseDto.getPrivateAttributes().size() < 1; j++)
				 product.addAttribute(productReleaseDto.getPrivateAttributes().get(j));

			 ProductRelease productRelease = new ProductRelease(
				productReleaseDto.getVersion(),
	            productReleaseDto.getReleaseNotes(),
	            productReleaseDto.getPrivateAttributes(),
	            product,
	            productReleaseDto.getSupportedOS(),
	            productReleaseDto.getTransitableReleases() );
			 
			 productReleases.add(productRelease);
		 }
				 
		 Environment environment = new Environment(productReleases);
		  
		 return environmentManager.update(environment);

	 }
}
