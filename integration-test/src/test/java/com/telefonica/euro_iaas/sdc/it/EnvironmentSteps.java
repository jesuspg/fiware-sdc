package com.telefonica.euro_iaas.sdc.it;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.it.util.EnvironmentUtils;
import com.telefonica.euro_iaas.sdc.it.util.ProductUtils;
import com.telefonica.euro_iaas.sdc.model.Environment;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

import cuke4duke.Table;
import cuke4duke.annotation.Pending;
import cuke4duke.annotation.I18n.EN.Given;
import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;


public class EnvironmentSteps {

	private List<ProductReleaseDto> productReleaseDtoInCatalog;
	private Environment environmentInserted;
	private Environment existedEnvironment;
	private Environment updatedEnvironment;

	private ProductRelease existedProduct;

	// ADD ENVIRONMENT
	@Given("^Given the following product release in the Catalog:$")
	public void getEnvironment(Table table) {
		productReleaseDtoInCatalog = new ArrayList<ProductReleaseDto>();
		for (List<String> row : table.rows()) {
			ProductReleaseDto productReleaseDto = new ProductReleaseDto();
			productReleaseDto.setProductName(row.get(0));
			productReleaseDto.setVersion(row.get(1));

			productReleaseDtoInCatalog.add(productReleaseDto);
		}
	}

	@When("^I insert \"([^\"]*)\" Environment  with description \"([^\"]*)\"$")
	public void addEnvironmentToCatalog(String envName, String envDesc)
			throws Exception {
		EnvironmentUtils manager = new EnvironmentUtils();
		environmentInserted = manager.insert(envName, envDesc,
				productReleaseDtoInCatalog);

	}

	@Then("^I get product \"([^\"]*)\" \"([^\"]*)\" in the catalog$")
	public void assertAddedProduct(String productName, String version)
			throws ResourceNotFoundException {
		ProductUtils manager = new ProductUtils();
		existedProduct = manager.load(productName, version);
		Assert.assertNotNull(existedProduct);
	}

	// UPDATE ENVIRONMENT
	@Given("^the environment \"([^\"]*)\"  in the Catalog$")
	public void getEnvironment(String envName, String envDesc) throws Exception {
		EnvironmentUtils manager = new EnvironmentUtils();
		existedEnvironment = manager.load(envName);
		System.out.println("existed Environment: "
				+ existedEnvironment.getName() + " wth description: "
				+ existedEnvironment.getDescription());
	}

	@When("^I update \"([^\"]*)\" Environment with the new description \"([^\"]*)\"$")
	public void updateProductToCatalog(String envName, String envDescription)
			throws Exception {

		List<ProductReleaseDto> productReleaseDtos = new ArrayList<ProductReleaseDto>();

		for (int i = 0; i < existedEnvironment.getProductReleases().size(); i++) {
			ProductReleaseDto productReleaseDto = new ProductReleaseDto();
			ProductRelease productRelease = existedEnvironment
					.getProductReleases().get(i);
			productReleaseDto.setPrivateAttributes(productRelease
					.getPrivateAttributes());
			productReleaseDto.setProductDescription(productRelease.getProduct()
					.getDescription());
			productReleaseDto.setProductName(productRelease.getProduct()
					.getName());
			productReleaseDto.setReleaseNotes(productRelease.getReleaseNotes());
			productReleaseDto.setSupportedOS(productRelease.getSupportedOOSS());
			productReleaseDto.setTransitableReleases(productRelease
					.getTransitableReleases());
			productReleaseDto.setVersion(productRelease.getVersion());

			productReleaseDtos.add(productReleaseDto);

		}
		EnvironmentUtils manager = new EnvironmentUtils();
		updatedEnvironment = manager.update(existedEnvironment.getName(),
				existedEnvironment.getDescription(), productReleaseDtos);

		System.out.println("Updted Environment: "
				+ updatedEnvironment.getName() + " wth description: "
				+ updatedEnvironment.getDescription());
	}

	@Then("^I get product \"([^\"]*)\" \"([^\"]*)\" updated in the catalog$")
	public void assertUpdatedEnvironment(String envName) {
		Assert.assertNotSame(updatedEnvironment, existedEnvironment);
		Assert.assertEquals(updatedEnvironment.getName(), existedEnvironment
				.getName());
		Assert.assertEquals(updatedEnvironment.getProductReleases(),
				existedEnvironment.getProductReleases());
		Assert.assertNotSame(updatedEnvironment.getDescription(),
				existedEnvironment.getDescription());
	}

	// DELETE PRODUCT
	@Given("^a environment \"([^\"]*)\" present in the Catalog$")
	public void getProductFromCatalog(String envName) throws Exception {
		System.out.println("envName: " + envName);
		EnvironmentUtils manager = new EnvironmentUtils();
		existedEnvironment = manager.load(envName);
		System.out.println("existed environment: "
				+ existedEnvironment.getName());
	}

	@When("^I delete environment \"([^\"]*)\"$")
	public void deleteProductToCatalog(String envName) throws Exception {
		EnvironmentUtils manager = new EnvironmentUtils();
		manager.delete(envName);
	}

	@Then("^there is not environment  \"([^\"]*)\" in the catalog$")
	public void assertDeletedProduct(String envName)
			throws ResourceNotFoundException {
		EnvironmentUtils manager = new EnvironmentUtils();
		Assert.assertNull(manager.load(envName));
	}
}
