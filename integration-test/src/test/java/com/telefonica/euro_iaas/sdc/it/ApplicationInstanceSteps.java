package com.telefonica.euro_iaas.sdc.it;

import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VDC;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VM;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.telefonica.euro_iaas.sdc.it.util.ApplicationInstanceUtils;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;

import cuke4duke.Table;
import cuke4duke.annotation.Pending;
import cuke4duke.annotation.I18n.EN.Given;
import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;

/**
 * Contains the necessary steps to install an application using the SDC.
 *
 * @author Sergio Arroyo
 *
 */
public class ApplicationInstanceSteps {

    private ApplicationInstance createdApplication;

    @Given("^\"([^\"]*)\" \"([^\"]*)\" added to the SDC Applications Catalog$")
    public void addApplicationToCatalog(
            String applicationName, String version)
    throws Exception {
        //TODO Sergio Arroyo, implement when the behavior is implemented on SDC
    }

    //INSTALLATION

    @Given("^application \"([^\"]*)\" \"([^\"]*)\" installed on products:$")
    @When("^I install the application \"([^\"]*)\" \"([^\"]*)\" on products:$")
    public void iInstallTheApplicatioOnProducts(
            String applicationName, String version, Table table)
    throws Exception {
     /*   List<ReleaseDto> products = new ArrayList<ReleaseDto>();
        for (List<String> product : table.rows()) {
            products.add(new ReleaseDto(product.get(0), product.get(1), null));
        }
        ApplicationInstanceUtils manager = new ApplicationInstanceUtils();
        String vdc = getProperty(VDC);
        String ip = getProperty(VM);
        createdApplication = manager.installIfNotInstalled(
                applicationName, version, ip, vdc, products);*/
    }


    @Then("^I get the application \"([^\"]*)\" \"([^\"]*)\" installed in vm$")
    public void iGetTheApplicationInstalledInVm(String applicationName,
            String version) throws Exception {
        String vdc = getProperty(VDC);

        ApplicationInstanceUtils manager = new ApplicationInstanceUtils();
        Assert.assertEquals(createdApplication,
                manager.load(vdc, createdApplication.getId()));
        Assert.assertEquals(createdApplication.getApplication().getVersion(),(version));
        Assert.assertEquals(
                createdApplication.getApplication().getApplication().getName(),
                (applicationName));
        Assert.assertEquals(createdApplication.getStatus(), Status.INSTALLED);
        //TODO Sergio Arroyo: make assertions in the node
    }

    //CONFIGURATION

    @When("^I configure the application \"([^\"]*)\" \"([^\"]*)\"" +
            " with \"([^\"]*)\" \"([^\"]*)\"$")
    public void iConfigureTheApplication(String applicationName, String version,
            String key, String value) throws Exception {
        ApplicationInstanceUtils manager = new ApplicationInstanceUtils();
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute(key, value));
        manager.configure(createdApplication.getVdc(),
                createdApplication.getId(), attributes);
    }

    @Then("^I get the application \"([^\"]*)\" \"([^\"]*)\" configured with \"([^\"]*)\" \"([^\"]*)\"$")
    @Pending
    public void iGetTheApplicationConfigured(String applicationName, String version,
            String key, String value) throws Exception {
        //TODO Sergio Arroyo: Make the assertions
    }

    // UPGRADE

    @When("^I upgrade to application \"([^\"]*)\" \"([^\"]*)\"$")
    public void updateApplication(String appName, String newVersion)
            throws Exception {
        ApplicationInstanceUtils manager = new ApplicationInstanceUtils();
        manager.upgrade(createdApplication.getVdc(),
                createdApplication.getId(), newVersion);
    }

    @Then("^I get application \"([^\"]*)\" upgraded form version \"([^\"]*)\" to \"([^\"]*)\" in VM$")
    @Pending
    public void assertApplicationUpgraded() throws Exception {
        //TODO Sergio Arroyo: Make the assertions
    }

    // UNINSATLL

    @When("^I uninstall the application \"([^\"]*)\" \"([^\"]*)\" from vdc$")
    public void uninstallApplication(String applicationName, String version)
            throws Exception  {
        ApplicationInstanceUtils manager = new ApplicationInstanceUtils();
        manager.uninstall(createdApplication.getVdc(), createdApplication.getId());
    }

    @Then("^I get the application \"([^\"]*)\" \"([^\"]*)\" uninstalled$")
    @Pending
    public void assertApplicationUninsatlled(String appName, String version)
            throws Exception {
        //TODO Sergio Arroyo: Make the assertions
    }
}
