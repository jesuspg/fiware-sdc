package com.telefonica.euro_iaas.sdc.model;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

import com.telefonica.euro_iaas.sdc.model.Application;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;


public class ProductInstanceTest extends TestCase{
	
	public void testProductInstance ()
	{
		Product product = new Product("Product::server", "description");
		OS os =  new OS("os1", "1", "os1 description", "v1");
		ProductRelease productRelease = new ProductRelease("version", "releaseNotes", null,
				product, Arrays.asList(os), null);

		assertEquals(productRelease.getProduct().getName(), "Product::server");
		assertEquals(productRelease.getVersion(), "version");
		
		VM vm = new VM("ip", "hostname", "domain");
		Product p1 = new Product("p1", "description");
		Product p2 = new Product("p2", "description");
		ProductRelease pr1 = new ProductRelease("version1", "releaseNotes1",
				null, p1, null, null);
		ProductRelease pr2 = new ProductRelease("version2", "releaseNotes2",
				null, p2, null, null);

		Environment env1 = new Environment(Arrays.asList(pr1, pr2));

		java.util.List<ProductInstance> products = Arrays.asList(new ProductInstance(pr1,
				ProductInstance.Status.INSTALLED, vm, "vdc"),
				new ProductInstance(pr2, ProductInstance.Status.INSTALLED, vm,
						"vdc"));
		Application application = new Application("app", "desc", "war");
		ApplicationRelease appRelease = new ApplicationRelease("version", "releaseNotes", null,
				application, env1, null);

		EnvironmentInstance envInstance = new EnvironmentInstance(env1, products);
		ApplicationInstance applicationInstance = new ApplicationInstance(appRelease, envInstance,
				Status.INSTALLED, vm, "vdc");
		assertEquals(applicationInstance.getApplication().getApplication().getName(), "app");
	}
	
	

}
