package com.telefonica.euro_iaas.sdc.it;

import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VDC;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VM;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;
import junit.framework.Assert;

import com.telefonica.euro_iaas.sdc.it.util.ProductInstanceUtils;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;

import cuke4duke.annotation.I18n.EN.Given;
import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;
/**
 * Provides the methods to perform UpgradeProduct test.
 * @author Sergio Arroyo
 *
 */
public class UpgradeProductSteps {

    private ProductInstance createdProduct;
    private ProductInstance updatedProduct;
    @Given("^\"([^\"]*)\" \"([^\"]*)\" installed in vm and vdc$")
    public void productInstalledInVmAtVdcTestVDC_(
            String productName, String version)
            throws Exception {
        ProductInstanceUtils manager = new ProductInstanceUtils();
        String vdc = getProperty(VDC);
        String ip = getProperty(VM);

        createdProduct =
                manager.installIfNotInstalled(productName, version, ip, vdc);
    }

    @When("^I updrade to \"([^\"]*)\" \"([^\"]*)\"$")
    public void iUpdradeToNewProductVersion(
            String productName, String newVersion) throws Exception {
        String vdc = getProperty(VDC);
        ProductInstanceUtils manager = new ProductInstanceUtils();
        updatedProduct = manager.upgrade(vdc, createdProduct.getId(), newVersion);
    }

    @Then("^I get \"([^\"]*)\" upgraded from version \"([^\"]*)\" to \"([^\"]*)\" in VM$")
    public void iGetProductUpgradedInVm(
            String productName, String oldVersion, String newVersion)
                    throws Exception {
        Assert.assertEquals(createdProduct, updatedProduct); //same id
        Assert.assertTrue(!createdProduct.getProduct().getVersion()
                .equals(updatedProduct.getProduct().getVersion()));
        Assert.assertEquals(newVersion, updatedProduct.getProduct().getVersion());
        Assert.assertEquals(updatedProduct.getStatus(), Status.INSTALLED);
        //TODO Sergio Arroyo: check in the node
    }

}
