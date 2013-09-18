package com.telefonica.euro_iaas.sdc.manager;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_ATTRIBUTES_LEFT_LIMITER;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_ATTRIBUTES_RIGHT_LIMITER;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_ATTRIBUTES_SEPARATOR;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_ATTRIBUTES_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_ROLE_TEMPLATE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.manager.impl.BaseInstallableInstanceManager;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 *
 * @author Sergio Arroyo
 *
 */
public class BaseInstallableManagerTest {

    private SystemPropertiesProvider propertiesProvider;

    private final static String ROLE_TEMPLATE = "name \"{3}\" \n"
            + " description \" This role aims to overwrite cookbook  attributes of"
            + " {1} cookbook associated to the node {0} by defining override"
            + " attributes \" \n run_list \"recipe[{1}]\" \n override_attributes"
            + " \"{1}\" => {2}";

    private final static String ATTRIBUTES_TEMPLATE = "\"{0}\" => {1}";
    private final static String SEPARATOR_TEMPLATE = ",";


    private final static String POPULATED = "name \"role-apache13081440333414299476184313710034\" "
            + "\n description \" This role aims to overwrite cookbook  attributes"
            + " of productName cookbook associated to the node vm.hostName by"
            + " defining override attributes \" \n run_list \"recipe[productName]\""
            + " \n override_attributes \"productName\" => {\"key1\" => \"value1\","
            + "\"key2\" => [\"value2.1\", \"value2.2\"]}";

    @Before
    public void prepare() {
        propertiesProvider = mock(SystemPropertiesProvider.class);
        when(propertiesProvider.getProperty(CHEF_ROLE_TEMPLATE)).thenReturn(
                ROLE_TEMPLATE);
        when(propertiesProvider.getProperty(CHEF_ATTRIBUTES_SEPARATOR))
                .thenReturn(SEPARATOR_TEMPLATE);
        when(propertiesProvider.getProperty(CHEF_ATTRIBUTES_TEMPLATE))
                .thenReturn(ATTRIBUTES_TEMPLATE);
        when(propertiesProvider.getProperty(CHEF_ATTRIBUTES_LEFT_LIMITER))
                .thenReturn("{");
        when(propertiesProvider.getProperty(CHEF_ATTRIBUTES_RIGHT_LIMITER))
                .thenReturn("}");
    }

    @Test
    public void testPopulateRoleTemplate() {
        List<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute("key1", "value1"));
        attributes.add(new Attribute("key2", "[\"value2.1\", \"value2.2\"]"));
        VM vm = new VM("vm.hostName", "vm.domain");

        BaseInstallableInstanceManager manager = new BaseInstallableInstanceManager();
        manager.setPropertiesProvider(propertiesProvider);
        String populatedTemplate = manager.populateRoleTemplate(vm,
                "productName", attributes,
                "role-apache13081440333414299476184313710034", "productName");

        Assert.assertEquals(POPULATED, populatedTemplate);

        verify(propertiesProvider, times(1)).getProperty(CHEF_ROLE_TEMPLATE);
        verify(propertiesProvider, times(1)).getProperty(CHEF_ATTRIBUTES_SEPARATOR);
        verify(propertiesProvider, times(1)).getProperty(CHEF_ATTRIBUTES_TEMPLATE);
        verify(propertiesProvider, times(1)).getProperty(CHEF_ATTRIBUTES_LEFT_LIMITER);
        verify(propertiesProvider, times(1)).getProperty(CHEF_ATTRIBUTES_RIGHT_LIMITER);
    }

    @Test
    public void testCreateRoleFile() throws Exception {
        List<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute("key1", "value1"));
        attributes.add(new Attribute("key2", "[\"value2.1\", \"value2.2\"]"));
        VM vm = new VM("vm.hostName", "vm.domain");

        BaseInstallableInstanceManager manager = new BaseInstallableInstanceManager();
        manager.setPropertiesProvider(propertiesProvider);

        File tmpFile = manager.createRoleFile(manager.populateRoleTemplate(vm,
                "productName", attributes,
                "role-apache13081440333414299476184313710034", "productName"),
                "role-apache13081440333414299476184313710034");

        Assert.assertEquals(POPULATED, FileUtils.readFileToString(tmpFile));
        tmpFile.deleteOnExit();
    }
}
