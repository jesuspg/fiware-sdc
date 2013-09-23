package com.telefonica.euro_iaas.sdc.rest.resources;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.SODao;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.SO;

/**
 * This Resource is made for recovering the available SOs and Products
 * @author cristian
 *
 */

//@Path("/system")
//@Component
//@Scope("request")

public class SystemResource implements GetResource {
	@InjectParam("soDao")
	SODao soDao;

	@InjectParam("productDao")
	ProductDao productDao;
	
	@Override
	@Produces(MediaType.APPLICATION_JSON)
	public String getResource(String resourceID) throws Exception {
		StringBuilder sb = new StringBuilder("{");
		
		sb.append("\"solist\" : {");
		for(SO so : soDao.findAll()) {
			System.out.println(so.getName());
			sb.append("\"so\" : \""+so.getName()+"");
			
		}
		sb.append("}, \"productlist\": {");
		for(Product p : productDao.findAll()) {
			System.out.println(p.getName());
		}
		
		
		sb.append("}");
		return sb.toString();
	}

}
