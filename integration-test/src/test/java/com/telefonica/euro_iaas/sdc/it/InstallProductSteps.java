package com.telefonica.euro_iaas.sdc.it;

import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VDC;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VM;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;
import junit.framework.Assert;

import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.it.util.ProductInstanceUtils;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;

import cuke4duke.annotation.I18n.EN.Given;
import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;
/**
 * Test for product installation
 *
 * @author Sergio Arroyo
 *
 */
public class InstallProductSteps {
    private ProductInstance createdProduct;

    @Given("^\"([^\"]*)\" \"([^\"]*)\" added to the SDC Catalog$")
    public void productAddedToTheSDCCatalog(String arg1, String arg2) {
        //TODO Sergio Arroyo: add tomcat to catalog when is implemented
    }

    /**
     * Install the product in the given node.
     * @param productName the product name
     * @param version the product version
     * @throws MaxTimeWaitingExceedException
     */
    @When("^I install \"([^\"]*)\" \"([^\"]*)\" in vm$")
    public void iInstallProductInVM(
            String productName, String version)
            throws Exception {
        String vdc = getProperty(VDC);
        String ip = getProperty(VM);

        ProductInstanceUtils manager = new ProductInstanceUtils();
        createdProduct =
                manager.installIfNotInstalled(productName, version, ip, vdc);
    }

    /**
     * Make the assertions and uninstall the product (to get the system the
     * previous state before this test execution.
     * @param productName the product name
     * @param version the product version
     */
    @Then("^I get \"([^\"]*)\" \"([^\"]*)\" installed in vm$")
    public void iGetProductInstalledInVm(
            String productName, String version) throws Exception {

        String vdc = getProperty(VDC);

        ProductInstanceUtils manager = new ProductInstanceUtils();
        Assert.assertEquals(createdProduct,
                manager.load(vdc, createdProduct.getId()));
        Assert.assertEquals(createdProduct.getProduct().getVersion(),(version));
        Assert.assertEquals(
                createdProduct.getProduct().getProduct().getName(), (productName));
        Assert.assertEquals(createdProduct.getStatus(), Status.INSTALLED);
        //TODO Sergio Arroyo: make assertions in the node
    }
}
