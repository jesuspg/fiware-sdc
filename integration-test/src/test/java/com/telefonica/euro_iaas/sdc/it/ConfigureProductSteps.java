package com.telefonica.euro_iaas.sdc.it;

import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VDC;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VM;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;

import java.util.ArrayList;

import com.telefonica.euro_iaas.sdc.it.util.ProductInstanceUtils;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;

import cuke4duke.annotation.I18n.EN.Given;
import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;
/**
 * Contains the necessary steps to perform Configuration Product action.
 * @author Sergio Arroyo
 *
 */
public class ConfigureProductSteps {

    private ProductInstance createdProduct;

    @Given("^\"([^\"]*)\" \"([^\"]*)\" installed$")
    public void productInstalled(String productName, String version)
            throws Exception {
        String vdc = getProperty(VDC);
        String ip = getProperty(VM);
        ProductInstanceUtils manager = new ProductInstanceUtils();
        createdProduct = manager.installIfNotInstalled(
                productName, version, ip, vdc);
    }

    @When("^I configure the product with \"([^\"]*)\" \"([^\"]*)\"$")
    public void iConfigureProductAtVdc(String key, String value)
    throws Exception {
        ProductInstanceUtils manager = new ProductInstanceUtils();
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute(key, value));
        createdProduct = manager.configure(createdProduct.getVdc(),
                createdProduct.getId(), attributes);

    }

    @Then("^I get the product configured with \"([^\"]*)\" \"([^\"]*)\"$")
    public void iGetProductAtVdcConfigured(String key, String value) {
        //TODO Sergio Arroyo: make assertions in the node
    }
}
