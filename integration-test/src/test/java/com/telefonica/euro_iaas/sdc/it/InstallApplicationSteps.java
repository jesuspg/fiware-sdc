package com.telefonica.euro_iaas.sdc.it;

import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VDC;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.VM;
import static com.telefonica.euro_iaas.sdc.it.util.QAProperties.getProperty;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.telefonica.euro_iaas.sdc.it.util.ApplicationInstanceUtils;
import com.telefonica.euro_iaas.sdc.model.ApplicationInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;

import cuke4duke.annotation.I18n.EN.Then;
import cuke4duke.annotation.I18n.EN.When;

/**
 * Contains the necessary steps to install an application using the SDC.
 *
 * @author Sergio Arroyo
 *
 */
public class InstallApplicationSteps {

    private ApplicationInstance createdApplication;

    @When("^I install the application \"([^\"]*)\" \"([^\"]*)\" on products:$")
    public void iInstallTheApplicatioOnProducts(
            String applicationName, String version, cuke4duke.Table table)
    throws Exception {
        List<ReleaseDto> products = new ArrayList<ReleaseDto>();
        for (List<String> product : table.rows()) {
            products.add(new ReleaseDto(product.get(0), product.get(1), null));
        }
        ApplicationInstanceUtils manager = new ApplicationInstanceUtils();
        String vdc = getProperty(VDC);
        String ip = getProperty(VM);
        createdApplication = manager.install(applicationName, version, ip, vdc, products);
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
}
