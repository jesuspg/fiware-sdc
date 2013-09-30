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

package com.telefonica.euro_iaas.sdc.it;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.it.util.ProductUtils;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import cuke4duke.Table;
import cuke4duke.annotation.I18n.EN.Given;
import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;
import junit.framework.Assert;

public class ProductSteps {

    private List<OS> supportedOS;
    private List<Attribute> attributes;
    private List<ProductRelease> transitableReleases;
    private ProductRelease addedProduct;
    private ProductRelease updatedProduct;

    private ProductRelease existedProduct;
    private ProductInstance existedProductInstance;

    private List<ProductRelease> productReleases;

    // ADD PRODUCT
    @Given("^OS:$")
    public void getOOSS(Table table) {
        supportedOS = new ArrayList<OS>();
        for (List<String> row : table.rows()) {
            supportedOS.add(new OS(row.get(0), row.get(1), row.get(2), "description"));
        }
    }

    @Given("^default attributes:$")
    public void getAttributes(Table table) {
        attributes = new ArrayList<Attribute>();
        for (List<String> row : table.rows()) {
            attributes.add(new Attribute(row.get(0), row.get(1), row.get(2)));
        }
    }

    @Given("^the transitable releases:$")
    public void getTransitableReleases(Table table) throws Exception {
        transitableReleases = new ArrayList<ProductRelease>();
        ProductUtils manager = new ProductUtils();
        for (List<String> row : table.rows()) {
            transitableReleases.add(manager.load(row.get(0), row.get(1)));
        }
    }

    @When("^I add product \"([^\"]*)\" \"([^\"]*)\" \\(\"([^\"]*)\"\\)$")
    public void addProductToCatalog(String productName, String version, String description) throws Exception {
        ProductUtils manager = new ProductUtils();
        addedProduct = manager.add(productName, version, description, "", attributes, supportedOS, transitableReleases);
    }

    @Then("^I get product \"([^\"]*)\" \"([^\"]*)\" in the catalog$")
    public void assertAddedProduct(String productName, String version) throws ResourceNotFoundException {
        ProductUtils manager = new ProductUtils();
        existedProduct = manager.load(productName, version);
        Assert.assertNotNull(existedProduct);
    }

    // UPDATE PRODUCT
    @Given("^a product \"([^\"]*)\" \"([^\"]*)\" in the Catalog$")
    public void getProduct(String productName, String version) throws Exception {
        System.out.println("product: " + "version: " + version);
        ProductUtils manager = new ProductUtils();
        existedProduct = manager.load(productName, version);
        System.out.println("existed product: " + existedProduct.getProduct().getName() + "version: "
                + existedProduct.getVersion());
    }

    @When("^I update \"([^\"]*)\" \"([^\"]*)\" \\(\"([^\"]*)\"\\)$")
    public void updateProductToCatalog(String productName, String version, String description) throws Exception {
        ProductUtils manager = new ProductUtils();
        updatedProduct = manager.update(productName, version, description, "", attributes, supportedOS,
                transitableReleases);
    }

    @Then("^I get product \"([^\"]*)\" \"([^\"]*)\" updated in the catalog$")
    public void assertUpdatedProduct(String productName, String version) {
        Assert.assertNotSame(updatedProduct, existedProduct);
        Assert.assertEquals(updatedProduct.getProduct().getName(), existedProduct.getProduct().getName());
        Assert.assertEquals(updatedProduct.getVersion(), existedProduct.getVersion());
        Assert.assertEquals(updatedProduct.getReleaseNotes(), existedProduct.getReleaseNotes());
        Assert.assertEquals(updatedProduct.getAttributes(), existedProduct.getAttributes());
        Assert.assertEquals(updatedProduct.getSupportedOOSS(), existedProduct.getSupportedOOSS());
        Assert.assertNotSame(updatedProduct.getProduct().getDescription(), existedProduct.getProduct().getDescription());
    }

    // DELETE PRODUCT
    @Given("^a product \"([^\"]*)\" \"([^\"]*)\" present in the Catalog$")
    public void getProductFromCatalog(String productName, String version) throws Exception {
        System.out.println("product: " + "version: " + version);
        ProductUtils manager = new ProductUtils();
        existedProduct = manager.load(productName, version);
        System.out.println("existed product: " + existedProduct.getProduct().getName() + "version: "
                + existedProduct.getVersion());
    }

    @When("^I delete \"([^\"]*)\" \"([^\"]*)\"$")
    public void deleteProductToCatalog(String productName, String version) throws Exception {
        ProductUtils manager = new ProductUtils();
        manager.delete(productName, version);
    }

    @Then("^there is not \"([^\"]*)\" \"([^\"]*)\" in the catalog$")
    public void assertDeletedProduct(String productName, String version) throws ResourceNotFoundException {
        // ProductUtils manager = new ProductUtils();
        // Assert.assertNull(manager.load(productName, version));
    }

    // DELETE COOKBOOK
    /*
     * @Given("^default attributes  Product Releases:$") public void getProductReleasesAttributes(Table table) {
     * attributes = new ArrayList<Attribute>(); for (List<String> row: table.rows()) { attributes.add(new
     * Attribute(row.get(0), row.get(1), row.get(2))); } }
     */
    @Given("^following Products Releases added to the SDC Catalog:$")
    public void addProductReleases(Table table) {
        productReleases = new ArrayList<ProductRelease>();
        ProductUtils manager = new ProductUtils();

        for (List<String> row : table.rows()) {
            addedProduct = manager.add(row.get(0), row.get(1), row.get(2), "", attributes, supportedOS,
                    transitableReleases);
        }

    }

    @When("^I delete product release \"([^\"]*)\" \"([^\"]*)\" and \"([^\"]*)\" from the catalog$")
    public void deleteProductReleaseCookbook(String productName, String version1, String version2) throws Exception {
        ProductUtils manager = new ProductUtils();
        manager.delete(productName, version1);
        manager.delete(productName, version2);
    }

    @Then("^There is not any product release cookbook \"([^\"]*)\" in the chef-server$")
    public void assertDeletedProductReleaseCookbook(String productName) throws ResourceNotFoundException {
        // ProductUtils manager = new ProductUtils();
        // Assert.assertNull(manager.load(productName, version));
    }
}
