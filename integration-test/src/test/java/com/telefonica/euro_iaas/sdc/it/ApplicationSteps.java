package com.telefonica.euro_iaas.sdc.it;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.it.util.ApplicationUtils;
import com.telefonica.euro_iaas.sdc.it.util.ProductUtils;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ApplicationRelease;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

import cuke4duke.Table;
import cuke4duke.annotation.I18n.EN.Given;
import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;
import cuke4duke.annotation.Pending;

public class ApplicationSteps {

    private List<ProductRelease> supportedProducts;
    private List<Attribute> attributes;
    private List<ApplicationRelease> transitableReleases;
    
    private ApplicationRelease existedApplication;
    private ApplicationRelease addedApplication;
    private ApplicationRelease updatedApplication;
    
    //ADD Application
    @Given("^application default attributes:$")
    public void getAttributes(Table table) {
        attributes = new ArrayList<Attribute>();
        for (List<String> row: table.rows()) {
            attributes.add(new Attribute(row.get(0), row.get(1), row.get(2)));
        }
    }

    @Given("^the application transitable releases:$")
    public void getTransitableReleases(Table table) throws Exception {
        transitableReleases = new ArrayList<ApplicationRelease>();
        ApplicationUtils manager = new ApplicationUtils();
        for (List<String> row: table.rows()) {
            transitableReleases.add(manager.load(row.get(0), row.get(1)));
        }
    }
    
    @Given("^the application supported product release$")
    public void getProductReleases(Table table) {
    	supportedProducts = new ArrayList<ProductRelease>();
        for (List<String> row: table.rows()) {
        	ProductRelease productRelease = new ProductRelease();
        	
        	productRelease.setProduct(new Product(row.get(0), "description"));
        	productRelease.setVersion(row.get(1));
        	supportedProducts.add(productRelease);
        }
    }
    
    @When("^I add application \"([^\"]*)\" \"([^\"]*)\" of type \"([^\"]*)\" \\(\"([^\"]*)\"\\)$")
    public void addApplicationToCatalog (String applicationName, 
    	String version, String type, String description){
    	ApplicationUtils manager = new ApplicationUtils();
    	addedApplication = manager.add(applicationName, version, type, description,
                "", attributes, supportedProducts, transitableReleases);
    }
    
    @Then("^I get application \"([^\"]*)\" \"([^\"]*)\" in the catalog")
    public void assertAddedApplication(String applicationName, String version) 
    	throws ResourceNotFoundException {
    	ApplicationUtils manager = new ApplicationUtils();
    	existedApplication = manager.load(applicationName, version); 
    	Assert.assertNotNull(existedApplication);
    }
    
    //UPDATE APPLICATION
    @When("^I update application \"([^\"]*)\" \"([^\"]*)\" of type \"([^\"]*)\" \\(\"([^\"]*)\"\\)$")
    public void updateApplicationToCatalog (String applicationName, 
    	String version, String type, String description){
    	
    	ApplicationUtils manager = new ApplicationUtils();
    	updatedApplication = manager.update(applicationName, version, type, description,
                "", attributes, supportedProducts, transitableReleases);
    }
    
    @Then("^I get updated application \"([^\"]*)\" \"([^\"]*)\" in the catalog$")
    public void assertUpdatedApplication(String applicationName, String version) {
    	
    }
    //DELETE APPLICATION
    @Given("^a application \"([^\"]*)\" \"([^\"]*)\" present in the Catalog")
    public void getApplicationFromCatalog(String applicationName, String version)  
    	throws Exception {
    	System.out.println ("application: " +  applicationName + 
    			"version: " + version);
    	ApplicationUtils manager = new ApplicationUtils();
    	existedApplication = manager.load(applicationName, version);  
    	System.out.println ("existed application: " 
    	+ existedApplication.getApplication().getName() +
    			"version: " + existedApplication.getVersion());
    }
    
    @When("^I delete application \"([^\"]*)\" \"([^\"]*)\"$")
    public void deleteApplicationFromCatalog(String applicationName, String version)
    throws Exception {
    	ApplicationUtils manager = new ApplicationUtils();
        manager.delete(applicationName, version);
    }

    @Then("^there is not application \"([^\"]*)\" \"([^\"]*)\" in the catalog$")
    public void assertDeletedProduct(String applicationName, String version) 
    		throws ResourceNotFoundException {
    	//ProductUtils manager = new ProductUtils(); 
    	//Assert.assertNull(manager.load(productName, version));
    }
    
}
