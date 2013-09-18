package com.telefonica.euro_iaas.sdc.rest.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.manager.ProductManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;

/**
 * default ProductResource implementation
 *
 * @author Sergio Arroyo
 *
 */
@Path("/catalog/product")
@Component
@Scope("request")
public class ProductResourceImpl implements ProductResource {

    @InjectParam("productManager")
    private ProductManager productManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductRelease insert(MultiPart multiPart)
    	throws AlreadyExistsEntityException, InvalidEntityException{
    	File cookbook = null;
    	File installable = null;
    	
    	// First part contains a Project object
    	ProductReleaseDto productReleaseDto = 
    		multiPart.getBodyParts().get(0).getEntityAs(ProductReleaseDto.class);
        
    	System.out.println("INSERT ProductRelease. ProductName : " 
    			+ productReleaseDto.getProductName()
    			+ " ProductVersion : " + productReleaseDto.getVersion());
    	
    	Product product = new Product (
				productReleaseDto.getProductName(),
				productReleaseDto.getProductDescription());
    	
    	for (int i=0; productReleaseDto.getPrivateAttributes().size() < 1; i++)
    		product.addAttribute(productReleaseDto.getPrivateAttributes().get(i));
		
		ProductRelease productRelease = new ProductRelease(
                productReleaseDto.getVersion(), 
                productReleaseDto.getReleaseNotes(),
                productReleaseDto.getPrivateAttributes(),
                product,
                productReleaseDto.getSupportedOS(),
                productReleaseDto.getTransitableReleases() );
		
		try{
			cookbook = File.createTempFile("cookbook-" + 
					productReleaseDto.getProductName() + "-" +
					productReleaseDto.getVersion() + ".tar", ".tmp");  
			
	    	installable = File.createTempFile("installable-" + 
					productReleaseDto.getProductName() + "-" +
					productReleaseDto.getVersion() + ".tar", ".tmp");
	    	
	        cookbook = 
	        	getFileFromBodyPartEntity ((BodyPartEntity) multiPart.getBodyParts().get(1).getEntity(), cookbook);
	        installable = 
	        	getFileFromBodyPartEntity ((BodyPartEntity) multiPart.getBodyParts().get(2).getEntity() ,installable);
	       
		} catch (IOException e)
		{
			System.out.println("Se produjo el error : "+e.toString());
		}
		
		
        return productManager.insert(productRelease, cookbook, 
				installable);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findAll(Integer page, Integer pageSize,
            String orderBy, String orderType) {
        ProductSearchCriteria criteria = new ProductSearchCriteria();

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
        return productManager.findByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product load(String name) throws EntityNotFoundException {
        return productManager.load(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attribute> loadAttributes(String name)
            throws EntityNotFoundException {
        return productManager.load(name).getAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductRelease> findAll(String name, Integer page,
            Integer pageSize, String orderBy, String orderType) {
        ProductReleaseSearchCriteria criteria = new ProductReleaseSearchCriteria();

        if (!StringUtils.isEmpty(name)) {
            try {
                Product product = productManager.load(name);
                criteria.setProduct(product);
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
        return productManager.findReleasesByCriteria(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductRelease load(String name, String version)
            throws EntityNotFoundException {
        Product product = productManager.load(name);
        return productManager.load(product, version);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String name, String version) {
    	 
    	System.out.println("Delete ProductRelease. ProductName : " 
    			+ name
    			+ " ProductVersion : " + version);
    	
    	Product product = new Product();
    	product.setName(name);
    	
    	ProductRelease productRelease = new ProductRelease();
		productRelease.setProduct(product);
		productRelease.setVersion(version);
		
		productManager.delete(productRelease);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attribute> loadAttributes(String name, String version)
            throws EntityNotFoundException {
        return load(name, version).getAttributes();
    }

    @Override
    public List<ProductRelease> findTransitable(String name, String version)
            throws EntityNotFoundException {
        return load(name, version).getTransitableReleases();
    }
    
    private File getFileFromBodyPartEntity(BodyPartEntity bpe, File file )
    {
        try {
        	InputStream input = bpe.getInputStream();
        	
        	OutputStream out=new FileOutputStream(file);
          
        	byte[] buf =new byte[1024];
        	int len;
        	while((len=input.read(buf))>0){
        		out.write(buf,0,len);
        	}
        	out.close();
        	input.close();
                  
        }catch(IOException e){
        	System.out.println("An error was produced : "+e.toString());
        }
        return file;
        
    }

}
