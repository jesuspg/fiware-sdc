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

import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.it.util.ProductInstanceUtils;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import cuke4duke.annotation.I18n.EN.Given;
import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;
import cuke4duke.annotation.Pending;
import junit.framework.Assert;


import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VDC;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VM;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;

/**
 * Contains the necessary steps to perform Configuration Product action.
 * 
 * @author Sergio Arroyo
 */
public class ProductInstanceSteps {

    private ProductInstance createdProduct;
    private ProductInstance updatedProduct;

    @Given("^\"([^\"]*)\" \"([^\"]*)\" added to the SDC Catalog$")
    public void productAddedToTheSDCCatalog(String arg1, String arg2) {
        // TODO Sergio Arroyo: add tomcat to catalog when is implemented
    }

    // INSTALL
    /**
     * Install the product in the given node.
     * 
     * @param productName
     *            the product name
     * @param version
     *            the product version
     * @throws MaxTimeWaitingExceedException
     */
    @Given("^\"([^\"]*)\" \"([^\"]*)\" installed$")
    @When("^I install \"([^\"]*)\" \"([^\"]*)\" in vm$")
    public void iInstallProductInVM(String productName, String version) throws Exception {
        String vdc = getProperty(VDC);
        String ip = getProperty(VM);

        ProductInstanceUtils manager = new ProductInstanceUtils();
        createdProduct = manager.installIfNotInstalled(productName, version, ip, vdc);
    }

    /**
     * Make the assertions and uninstall the product (to get the system the previous state before this test execution.
     * 
     * @param productName
     *            the product name
     * @param version
     *            the product version
     */
    @Then("^I get \"([^\"]*)\" \"([^\"]*)\" installed in vm$")
    public void iGetProductInstalledInVm(String productName, String version) throws Exception {

        String vdc = getProperty(VDC);

        ProductInstanceUtils manager = new ProductInstanceUtils();
        Assert.assertEquals(createdProduct, manager.load(vdc, createdProduct.getName()));
        Assert.assertEquals(createdProduct.getProductRelease().getVersion(), (version));
        Assert.assertEquals(createdProduct.getProductRelease().getProduct().getName(), (productName));
        Assert.assertEquals(createdProduct.getStatus(), Status.INSTALLED);
        // TODO Sergio Arroyo: make assertions in the node
    }

    // UNINSTALL

    @When("^I uninstall \"([^\"]*)\" \"([^\"]*)\" from vdc$")
    public void iUninstallProductFromVdc(String productName, String version) throws Exception {
        ProductInstanceUtils manager = new ProductInstanceUtils();
        createdProduct = manager.uninstall(createdProduct.getVdc(), createdProduct.getName());
    }

    @Then("^I get \"([^\"]*)\" \"([^\"]*)\" uninstalled$")
    public void iGetProductUninstalled(String productName, String version) {
        Assert.assertEquals(Status.UNINSTALLED, createdProduct.getStatus());
        // TODO Sergio Arroyo: make assertions in the node
    }

    // UPGRADE

    @When("^I updrade to \"([^\"]*)\" \"([^\"]*)\"$")
    public void iUpdradeToNewProductVersion(String productName, String newVersion) throws Exception {
        String vdc = getProperty(VDC);
        ProductInstanceUtils manager = new ProductInstanceUtils();
        updatedProduct = manager.upgrade(vdc, createdProduct.getName(), newVersion);
    }

    @Then("^I get \"([^\"]*)\" upgraded from version \"([^\"]*)\" to \"([^\"]*)\" in VM$")
    public void iGetProductUpgradedInVm(String productName, String oldVersion, String newVersion) throws Exception {
        Assert.assertEquals(createdProduct, updatedProduct); // same id
        Assert.assertTrue(!createdProduct.getProductRelease().getVersion()
                .equals(updatedProduct.getProductRelease().getVersion()));
        Assert.assertEquals(newVersion, updatedProduct.getProductRelease().getVersion());
        Assert.assertEquals(updatedProduct.getStatus(), Status.INSTALLED);
        // TODO Sergio Arroyo: check in the node
    }

    // CONFIGURE

    @When("^I configure the product with \"([^\"]*)\" \"([^\"]*)\"$")
    public void iConfigureProductAtVdc(String key, String value) throws Exception {
        ProductInstanceUtils manager = new ProductInstanceUtils();
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute(key, value));
        createdProduct = manager.configure(createdProduct.getVdc(), createdProduct.getName(), attributes);
    }

    @Then("^I get the product configured with \"([^\"]*)\" \"([^\"]*)\"$")
    @Pending
    public void iGetProductAtVdcConfigured(String key, String value) {
        // TODO Sergio Arroyo: make assertions in the node
    }

}
