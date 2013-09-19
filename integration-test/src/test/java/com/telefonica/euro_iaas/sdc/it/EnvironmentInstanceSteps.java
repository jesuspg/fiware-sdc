package com.telefonica.euro_iaas.sdc.it;

import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VDC;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VM;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.telefonica.euro_iaas.sdc.it.util.ApplicationInstanceUtils;
import com.telefonica.euro_iaas.sdc.it.util.EnvironmentInstanceUtils;
import com.telefonica.euro_iaas.sdc.it.util.EnvironmentUtils;
import com.telefonica.euro_iaas.sdc.it.util.ProductInstanceUtils;
import com.telefonica.euro_iaas.sdc.it.util.ProductUtils;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.EnvironmentInstance;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;

import cuke4duke.Table;
import cuke4duke.annotation.Pending;
import cuke4duke.annotation.I18n.EN.Given;
import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;

/**
 * Contains the necessary steps to install an application using the SDC.
 *
 * @author Jesus M. Movilla
 *
 */
public class EnvironmentInstanceSteps {

    private EnvironmentInstance createdEnvironment;
    private List<OS> supportedOS;
    private List<Attribute> attributes;
    private List<ProductRelease> transitableReleases;
    private ProductRelease addedProduct;
    private List<ProductReleaseDto> productReleaseDtos;
    
    private ProductInstance createdProduct;
    private List<ProductInstance> productInstances;
    private List<ProductInstanceDto> productInstanceDtos;
    private ProductInstanceDto productInstanceDto;
    
    private Environment environmentInserted;
    private EnvironmentDto environmentDto;
    
    private EnvironmentInstance environmentInstanceInserted;

    
  //ADD PRODUCT
    @Given("^OS:$")
    public void getOOSS(Table table) {
        supportedOS = new ArrayList<OS>();
        for (List<String> row: table.rows()) {
            supportedOS.add(new OS(row.get(0), row.get(1), row.get(2), "description"));
        }
    }

    @Given("^default attributes:$")
    public void getAttributes(Table table) {
        attributes = new ArrayList<Attribute>();
        for (List<String> row: table.rows()) {
            attributes.add(new Attribute(row.get(0), row.get(1), row.get(2)));
        }
    }

    @Given("^the transitable releases:$")
    public void getTransitableReleases(Table table) throws Exception {
        transitableReleases = new ArrayList<ProductRelease>();
        ProductUtils manager = new ProductUtils();
        for (List<String> row: table.rows()) {
            transitableReleases.add(manager.load(row.get(0), row.get(1)));
        }
    }
    
    @Given("^the following product releases added to the SDC Catalog")
    public void addEnvironmentToCatalog(Table table)
    throws Exception {
    	ProductUtils manager = new ProductUtils();
    	String name = "";
    	for (List<String> row: table.rows()) {
    		ProductReleaseDto productReleaseDto = new ProductReleaseDto();
    		productReleaseDto.setProductName(row.get(0));
    		productReleaseDto.setVersion(row.get(1));
    		productReleaseDto.setProductDescription("description");
    		productReleaseDto.setPrivateAttributes(attributes);
    		productReleaseDto.setSupportedOS(supportedOS);
    		productReleaseDto.setTransitableReleases(transitableReleases);
    		
    		ProductInstanceDto productInstanceDto = new ProductInstanceDto();
    		productInstanceDto.setProduct(productReleaseDto);
    		
    		addedProduct = manager.add(
    				productReleaseDto.getProductName(),
    				productReleaseDto.getVersion(),
    				productReleaseDto.getProductDescription(),
    				"",
    				productReleaseDto.getPrivateAttributes(),
    				productReleaseDto.getSupportedOS(),
    				productReleaseDto.getTransitableReleases());
    		
    		productReleaseDtos.add(productReleaseDto);
    		
    		productInstanceDtos.add(productInstanceDto);
    	}
    	
		environmentDto = new EnvironmentDto();
		environmentDto.setProducts(productReleaseDtos);
		environmentDto.setDescription("description");
		
    	EnvironmentUtils envManager = new EnvironmentUtils();
        environmentInserted = envManager.insert(
        		environmentDto.getName(),
        		environmentDto.getDescription(),
        		environmentDto.getProducts());

    }

    @Given(" And following product instances are installed$")
    public void iInsertEnvironmentInstance(Table table) throws Exception {
        String vdc = getProperty(VDC);
        String ip = getProperty(VM);

        ProductInstanceUtils manager = new ProductInstanceUtils();
        for (List<String> row: table.rows()) {
        createdProduct =
                manager.installIfNotInstalled(row.get(0), row.get(1), ip, vdc);
         productInstances.add(createdProduct);
        }
    }
    
    @Given("an environment instance present in the Catalog with products$")
    public void iDeleteEnvironmentInstance(Table table) throws Exception {
        /*
         *
         */
    }
    
    //INSTALLATION

    @When("^I install an environment Instance on products:$")
    public void iInsertEnvironment(Table table)
    throws Exception {
        EnvironmentInstanceUtils envInstanceManager = new EnvironmentInstanceUtils();
        environmentInstanceInserted = envInstanceManager.insert(environmentDto, productInstanceDtos);

    }


    @Then("^I get an environment Instance installed in vm$")
    public void iGetTheApplicationInstalledInVm() throws Exception {
        EnvironmentInstanceUtils manager = new EnvironmentInstanceUtils();
        Assert.assertEquals(environmentInstanceInserted,
                manager.load(environmentInstanceInserted.getId()));
        Assert.assertEquals(environmentInstanceInserted.getProductInstances().get(0).getStatus(), Status.INSTALLED);
        //TODO Sergio Arroyo: make assertions in the node
    }

    //UPDATE
    @When("I update an environment Instance with description \"([^\"]*)\"  products:$")
    public void iUpdateEnvironment(String envDescription, Table table)
    throws Exception {
        EnvironmentInstanceUtils envInstanceManager = new EnvironmentInstanceUtils();
        environmentDto.setDescription(envDescription);
        environmentInstanceInserted = envInstanceManager.insert(environmentDto, productInstanceDtos);

    }

    @Then("^I get an environment Instance with new description$")
    public void iGetTheApplicationUpdated() throws Exception {
         EnvironmentInstanceUtils manager = new EnvironmentInstanceUtils();
         Assert.assertEquals(environmentInstanceInserted.getEnvironment().getDescription(),
                 manager.load(environmentInstanceInserted.getId())
                 .getEnvironment().getDescription());
         Assert.assertEquals(environmentInstanceInserted.getProductInstances().get(0).getStatus(), Status.INSTALLED);
    }

    //Delete
    @When("^I delete the environment instance$")
    public void uninstallApplication(String applicationName, String version)
            throws Exception  {
        EnvironmentInstanceUtils manager = new EnvironmentInstanceUtils();
        manager.delete(environmentInstanceInserted.getId());
    }

    @Then("^Then there is not \"([^\"]*)\" \"([^\"]*)\" in the catalog$")
    public void assertApplicationUninsatlled(String appName, String version)
            throws Exception {
    	EnvironmentInstanceUtils manager = new EnvironmentInstanceUtils();
    	Assert.assertNull(manager.load(environmentInstanceInserted.getId()));

    }
}
