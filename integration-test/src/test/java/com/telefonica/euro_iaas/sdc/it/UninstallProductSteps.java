package com.telefonica.euro_iaas.sdc.it;

import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VDC;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VM;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;
import junit.framework.Assert;

import com.telefonica.euro_iaas.sdc.it.util.ProductInstanceUtils;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;

import cuke4duke.annotation.I18n.EN.Given;
import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;

/**
 * Contains the necessary steps to uninstall a product.
 *
 * @author Sergio Arroyo
 *
 */
public class UninstallProductSteps {

    private ProductInstance createdProduct;

    @Given("^\"([^\"]*)\" \"([^\"]*)\" installed at vdc$")
    public void productInstalledAtVdc(String productName, String version)
            throws Exception{
        String vdc = getProperty(VDC);
        String ip = getProperty(VM);

        ProductInstanceUtils manager = new ProductInstanceUtils();
        createdProduct = manager.installIfNotInstalled(
                productName, version, ip, vdc);

    }

    @When("^I uninstall \"([^\"]*)\" \"([^\"]*)\" from vdc$")
    public void iUninstallProductFromVdc(String productName, String version)
        throws Exception {
        ProductInstanceUtils manager = new ProductInstanceUtils();
        createdProduct = manager.uninstall(
                createdProduct.getVdc(), createdProduct.getId());
    }

    @Then("^I get \"([^\"]*)\" \"([^\"]*)\" uninstalled$")
    public void iGetProductUninstalled(String productName, String version) {
        Assert.assertEquals(Status.UNINSTALLED, createdProduct.getStatus());
        //TODO Sergio Arroyo: make assertions in the node
    }
}
